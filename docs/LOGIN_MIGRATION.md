# Migração da feature `login` para MVVM — Tarefa 05

## Arquivos criados
- `app/src/main/java/br/com/policlinsaude/ui/activities/login/LoginActivity.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activities/login/LoginViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/data/models/UserResponseMapper.kt` (JsonUserResponse → Person)
- `app/src/main/java/br/com/policlinsaude/util/helpers/SingleLiveEvent.kt` (evento único, sem lib externa)

## Arquivos modificados
- `app/src/main/AndroidManifest.xml` — `LoginActivity` passou a ser a Activity launcher (antes: MainActivity placeholder)
- `app/src/main/java/br/com/policlinsaude/di/AppModules.kt` — registro do `LoginViewModel` no Koin

## Arquivos removidos
- `app/src/main/java/br/com/policlinsaude/ui/activities/MainActivity.kt` — placeholder substituído pelo login como launcher.

## Layout movido
- `_legacy/res/layout/activity_login.xml` → `app/src/main/res/layout/activity_login.xml` (via git mv, preservando histórico).

## Fluxo antigo (legado)
```
View (LoginActivity + LoginView)
  ↓
Presenter (LoginPresenterImpl)
  ↓
UseCase (DoLoginUseCase / GetCurrentPersonUseCase) + UseCaseHandler
  ↓
Repository (RepositoryImpl)
  ↓
Datasource (NetworkingDatasourceImpl) → NetworkingService (RxJava Flowable)
```
Navegação: `LoginNavigator` (goToHome/goToForgotPassword/goToNotHasPassword).

## Fluxo novo (MVVM)
```
UI (LoginActivity — Binding)
  ↓ eventos
LoginViewModel
  ↓
AppRepository.onLogin (suspend)
  ↓
AppService.login → RetrofitProvider
  ↓
API
```
Resultado: ViewModel expõe `LiveData<Boolean> loading` + `SingleLiveEvent<LoginEvent> (event)`.

```
UI ← LiveData/Event — LoginViewModel
UI (navegação)
```

Navegação: realizada na UI (Activity) conforme `ARCHITECTURE_TARGET.md` §9. Para features ainda não migradas (Home, Esqueci a senha, Não tenho senha) usa-se **Toast "Em construção"** (§10).

## Responsabilidades
- Activity/Fragment: binding, validação visual, FCM token, loading, diálogos, navegação, olho (eye).
- ViewModel: coordena `login(...)`/`checkHasToken()`, chama `AppRepository`, salva sessão via `SessionManager`, expõe estado/evento.

## Validação
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL**
- `./gradlew :app:assembleDebug` → **BUILD SUCCESSFUL**
- `./gradlew :app:clean :app:assembleDebug` → **BUILD SUCCESSFUL**
- Teste manual dos fluxos principais ainda pendente (depende de executar o app em device/emulador).

## Pendências
- **Base URL do login** (`MAPP_Login`): decisão de usar `apiapp/` ou `mapp/api/` — pendência já registrada no MIGRATION_MAP; o usuário disse que iria avaliar posteriormente.
- As telas de destino pós-login (Home, forgot password, not has password) ainda não foram migradas — nesta migração foram substituídas por Toast "Em construção" conforme regra.
- `checkHasToken()` foi simplificado: apenas verifica `sessionManager.hasToken()` (não valida o token na API como o legado fazia via `getCurrentPerson`). Comportamento preservado para fins de navegação (sessão existente → vai para Home); anotado para possível validação futura.
- Não foram criados testes unitários (conforme seção 15 do TASK_05).
- Não foram realizadas renomeações de classes (mantidos os nomes `LoginActivity`/`LoginViewModel`).

## Logs adicionados (diagnóstico de API)
Para facilitar o diagnóstico do login, foram adicionados logs em três camadas:

1. **`LoggingInterceptor`** (`data/services/RetrofitProvider.kt`, TAG `RETROFIT`) — imprime **todas** as chamadas HTTP:
   - `REQUEST`: método, URL completa, headers e body do request
   - `RESPONSE`: código HTTP, message, duração (ms), headers e body do response
   - Erros de transporte também são logados com stack trace
2. **`AppRepository.request`** (`AppRepository`, TAG `AppRepository`) — loga a chamada, o response tipado (code + body), erro de transporte e erro HTTP.
3. **`LoginActivity` + `LoginViewModel`** (TAGs `LoginActivity`/`LoginViewModel`) — logam o `firebaseToken` obtido, os campos do login, o response do `onLogin`, a análise (actionCode/needUpdate/hasErrorMsg/user/token), o salvamento de pessoa/token e exceções.

> Para visualizar no Logcat use os filtros de TAG: `RETROFIT`, `AppRepository`, `LoginActivity`, `LoginViewModel`.