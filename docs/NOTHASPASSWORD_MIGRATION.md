# Migração da feature `notHasPassword` (cadastro de senha) — MVVM

## Objetivo
Migrar o fluxo de cadastro de primeiro acesso ("não tenho senha") do legado para
a nova arquitetura MVVM, sem Presenter/Navigator/Dagger/RxJava.

## Arquivos criados
- `app/src/main/java/br/com/policlinsaude/ui/activities/notHasPassword/NotHasPasswordActivity.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activities/notHasPassword/NotHasPasswordViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notHasPassword/NotHasPasswordStepperEnum.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notHasPassword/NotHasPasswordFragmentPagerAdapter.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notHasPassword/PersonalDataFragment.kt` (passo 1)
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notHasPassword/PlanDataFragment.kt` (passo 2)
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notHasPassword/CreatePasswordFragment.kt` (passo 3)
- `app/src/main/java/br/com/policlinsaude/data/models/RegisterPasswordMapper.kt`

## Arquivos modificados
- `app/src/main/AndroidManifest.xml` — declara `NotHasPasswordActivity`.
- `app/src/main/java/br/com/policlinsaude/di/AppModules.kt` — registra `NotHasPasswordViewModel`.
- `app/src/main/java/br/com/policlinsaude/ui/activities/login/LoginActivity.kt` — o botão "Não tenho senha"
  agora abre `NotHasPasswordActivity` (antes: Toast "Em construção").

## Layouts migrados
- `activity_not_has_password.xml`, `fragment_personal_data.xml`, `fragment_plan_data.xml`,
  `fragment_create_password.xml` movidos de `_legacy/res/layout` → `app/src/main/res/layout`.

## Fluxo antigo (legado)
```
View (NotHasPasswordActivity + 3 steppers)
  ↓
Presenter (NotHasPasswordPresenterImpl)  ← guardava PresentationPerson/PresentationPlan
  ↓
UseCases (CheckPlanUseCase, RegisterPasswordUseCase)
  ↓
RepositoryImpl → NetworkingDatasourceImpl → NetworkingService (MAPP_ManutencaoBeneficiario)
```
Navegação: `NotHasPasswordNavigator.finishScreen()` após sucesso.
DI: `NotHasPasswordModule` (Dagger).

## Fluxo novo (MVVM)
```
UI (NotHasPasswordActivity + ViewPager2 + 3 fragments)
  ↓ eventos / validação por fragment
NotHasPasswordViewModel  ← estado compartilhado (Person/Plan) entre os steps
  ↓ suspend
AppRepository.onCheckPlan / AppRepository.onRegisterPassword
  ↓
AppService (MAPP_ManutencaoBeneficiario) → RetrofitProvider
```

- ViewModel compartilhado: todos os fragments obtêm uma única instância via
  `NotHasPasswordActivity.viewModel` (sem Dagger; registro no Koin).
- Stepper: ViewPager2 + `FragmentStateAdapter` (mesma sequência do legado:
  Dados Pessoais → Dados do Plano → Criar Senha).
- Passo 2 executa o `checkPlan` **antes** de avançar (mesma semântica do legado:
  apenas com resposta OK avança).
- Passo 3 envia `RegisterPasswordBody` montado pelo novo `RegisterPasswordMapper`.

## Validação
- `:app:compileDebugKotlin` → **BUILD SUCCESSFUL**
- `:app:assembleDebug` → **BUILD SUCCESSFUL**
- `:app:clean :app:assembleDebug` → **BUILD SUCCESSFUL**

## Observações / divergências (registradas, sem assumir)
- **Bug corrigido no legado**: em `PersonalDataFragment`, o listener de máscara do CPF gravava
  o valor em `phone` em vez de `cpf` (linha `phone = extractedValue` dentro do bloco do CPF).
  Na migração, o CPF é gravado na variável `cpf` (comportamento claramente pretendido).
- O `fragment_personal_data.xml` do legado trazia **valores de teste** (`android:text="Teste"` e
  `01/01/1980`); os layouts foram movidos **sem** esses textos (preenchimento agora vem do ViewModel).
- Mantidas as mesmas string-names do legado (`title_personal_data`, `title_plan_data`,
  `title_create_password`, `url_terms`, `text_min_and_max_password`, etc.) — todos já existentes
  em `app/src/main/res/values/strings.xml`.

## Pendências
- Validação manual no device (fluxo completo) ainda não executada.
- Confirmar a base URL correta (`mapp/api/` vs `apiapp/`) — pendência geral já registrada em
  `docs/MIGRATION_MAP.md` (afeta `checkPlan`/`registerPassword`).