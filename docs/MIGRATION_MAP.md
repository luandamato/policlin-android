# MAPA DE MIGRAÇÃO DO LEGADO — `docs/MIGRATION_MAP.md`

> Documento de **análise** produzido a partir do legado (`_legacy`) e da estrutura nova.
> **Não altera código-fonte.** Base: 24/08/2026, ramo `chore/updates`.

---

## 1. Visão geral

O projeto original é **híbrido**:

- **MVP legado** (Dagger + RxJava + `Navigator`) — ~19 features em `br.com.policlinsaude.<feature>`.
- **MVVM novo** (Koin + LiveData + Coroutines/`viewModelScope`) — 9 features em
  `otherFeatures/features/<Feature>`.

O `_legacy` reúne todo o código anterior (classes + `res/layout`, `res/navigation`, `res/menu`).
A **arquitetura alvo** é MVVM (ViewModels com `StateFlow` para código novo) com camadas
compartilhadas `data` / `domain` / `ui` / `util` e navegação na UI (sem `Navigator`).

Já migrados para o app novo: `data/models` (81), `domain/models` (25), `data/services`
(`AppService` com 47 endpoints + `RetrofitProvider` + `NetworkConstants` + `ServerErrorResponse`),
`data/repositories` (`AppRepository`), `data/local` (`SessionManager`) e DI (Koin no `MainApplication`).

---

## 2. Mapa de features

### 2.1 Features MVP (Dagger + RxJava + Navigator)

| Feature | Entrada | Arquitetura atual | Tecnologia |
|---|---|---|---|
| `login` | `LoginActivity` (launcher) | MVP (Presenter/View/Navigator) | RxJava |
| `home` | `MenuActivity` (shell) + `HomeFragment` | MVP + shell (drawer) | RxJava |
| `perfil` | `PerfilFragment` (no shell) | MVP | RxJava |
| `preferences` | `PreferencesFragment` (no shell) | MVP | RxJava |
| `informations` | `InformationsFragment` (no shell) | MVP (sem presenter) | Coroutines/live |
| `links` | `LinksFragment` (no shell) | MVP (sem presenter) | Coroutines/live |
| `favorites` | `FavoritesActivity` | MVP | RxJava |
| `editPassword` | `EditPasswordActivity` | MVP | RxJava |
| `editPhone` | `EditPhoneActivity` | MVP | RxJava |
| `forgotPassword` | `ForgotPasswordActivity` | MVP | RxJava |
| `notHasPassword` | `NotHasPasswordActivity` (3 Fragments internos) | MVP | RxJava |
| `healthInsurancePhoto` | `HealthInsurancePhotoActivity` | MVP | RxJava |
| `medicalGuideOptions` | `MedicalGuideOptionsActivity` | MVP | RxJava |
| `medicalGuideList` | `MedicalGuideListActivity` | MVP | RxJava |
| `medicalGuideDetails` | `MedicalGuideDetailsActivity` | MVP | RxJava |
| `ownNetwork` | `OwnNetworkFragment`/`Activity` | MVP | RxJava |
| `units` | `UnitsActivity`/`UnitsFragment` | MVP | RxJava |
| `map` | `MapsActivity` | MVP | RxJava |

### 2.2 Features MVVM novas (`otherFeatures/features`)

| Feature | Entrada | Arquitetura atual | Tecnologia |
|---|---|---|---|
| `Token` | `TokenActivity` | MVVM (LiveData + `viewModelScope`) | Coroutines |
| `ScheduleCentral` | `ScheduleCentralActivity` | MVVM | Coroutines |
| `incometax` | `IncomeTaxActivity` | MVVM | Coroutines |
| `tickets` | `TicketsActivity` | MVVM | Coroutines |
| `coparticipation` | `ResearchCoParticipationActivity` | MVVM | Coroutines |
| `extractor` | `FactorExtractorActivity` | MVVM | Coroutines |
| `notifications` | `NotificationActivity` | MVVM | Coroutines |
| `guidAuthorizer` | `GuideAuthorizerActivity` (8 Fragments) | MVVM | Coroutines |
| `deleteUser` | `DeleteUserActivity` | MVVM | Coroutines |

### 2.3 Camadas / utilitários (não são features)

- `core/` — `BaseActivity`, `BaseFragment`, `BaseFragmentWithInject`, DI (Dagger), helpers.
- `data/` — datasources (networking/preferences/realm), DI, mappers, DTOs.
- `domain/` — models, repository (interface única `Repository`), usecases (27), exceções.
- `model/` + `mapper/` — modelos de apresentação `Presentation*` e mapeadores.
- `otherFeatures/` — `data/{networking,services,repositories}` e `utils/`, `components/`.

---

## 3. Dependências entre features

### 3.1 Fluxo principal (shell)

```text
Login
  └── MenuActivity (shell/Home)
        ├── HomeFragment
        │     ├── MedicalGuideOptions ─► MedicalGuideList ─► MedicalGuideDetails ─► Favoritos
        │     ├── HealthInsurancePhoto (carteirinha)
        │     ├── OwnNetwork ─► MedicalGuideDetails
        │     ├── Favorites ─► MedicalGuideDetails
        │     ├── Units ─► MedicalGuideDetails / Map
        │     ├── Token
        │     ├── Tickets
        │     ├── Notification
        │     ├── ScheduleCentral
        │     ├── FactorExtractor (extractor)
        │     ├── ResearchCoParticipation (coparticipation)
        │     ├── IncomeTax
        │     └── GuideAuthorizer
        ├── PerfilFragment ─► EditPhone / EditPassword / DeleteUser
        ├── PreferencesFragment
        ├── InformationsFragment
        └── LinksFragment
```

### 3.2 Dependências por destino (quem chama quem)

| Activity/Fragment de destino | Originado em |
|---|---|
| `MenuActivity` | `home/di`, `home/navigator`, `home/view`, `informations`, `links`, `login/navigator`, `perfil/view`, `preferences/view` |
| `LoginActivity` | `home/navigator`, `login/*`, `medicalGuideDetails/navigator`, `preferences/navigator` |
| `FavoritesActivity` | `favorites/*`, `home/navigator` |
| `ForgotPasswordActivity` | `login/navigator`, `forgotPassword/*` |
| `NotHasPasswordActivity` | `login/navigator` |
| `OwnNetworkActivity` | `home/navigator`, `units/navigator` |
| `UnitsActivity` | `home/navigator`, `units/*` |
| `MedicalGuideOptionsActivity` | `home/navigator`, `medicalGuideOptions/*` |
| `MedicalGuideListActivity` | `medicalGuideOptions/navigator` |
| `MedicalGuideDetailsActivity` | `favorites/navigator`, `medicalGuideList/navigator`, `ownNetwork/navigator`, `units/navigator` |
| `HealthInsurancePhotoActivity` | `home/navigator`, `healthInsurancePhoto/*` |
| `EditPasswordActivity` / `EditPhoneActivity` | `perfil/navigator` |
| `MapsActivity` | `medicalGuideList/navigator`, `ownNetwork/navigator`, `units/navigator` |
| `TokenActivity` | `home/navigator`, `home/view` |
| `TicketsActivity`, `NotificationActivity`, `ScheduleCentralActivity`, `FactorExtractorActivity`, `ResearchCoParticipationActivity`, `IncomeTaxActivity`, `GuideAuthorizerActivity` | `home/view` (e internos da feature) |
| `DeleteUserActivity` | `perfil/navigator` |

**Resumo:** `login` → `home` → (todas as demais). O shell `home` é o hub central;
`medicalGuideDetails` é alvo comum de 4 features independentes. `perfil` abre
`editPhone`/`editPassword`/`deleteUser`.

---

## 4. Endpoints por feature

Todos os endpoints estão concentrados no `AppService` (47). Mapeamento feature ─ endpoints:

| Feature | Endpoints (path relativo) |
|---|---|
| `login` | `MAPP_Login`, `MAPP_ManutencaoBeneficiario` (register), `MAPP_RecuperarSenha` |
| `home` | `MAPP_RetornaBeneficiario`, `apiAcessoBotoes` (validateButtons), Banner (`getBanners` @Url) |
| `perfil` | `MAPP_AtualizaAvatar`, `MAPP_RetornaBeneficiario` |
| `preferences` | (preferências locais) |
| `favorites` | `MAPP_ManutencaoFavoritos` (add/remove), `MAPP_RetornaFavoritos` |
| `editPassword` | `MAPP_AtualizaSenha` |
| `editPhone` | `MAPP_AtualizaTelefone` |
| `forgotPassword` | `MAPP_RecuperarSenha` |
| `notHasPassword` | `MAPP_ManutencaoBeneficiario` (checkPlan) |
| `healthInsurancePhoto` | `MAPP_CarterinhaVirtualv2` |
| `medicalGuideOptions` | `MAPP_RetornaGuiaMedicoFiltrov2` |
| `medicalGuideList` | `MAPP_Retorna_GuiaMedicov4` |
| `medicalGuideDetails` | `MAPP_RetornaGuiaMedicoDetalhes` |
| `ownNetwork` | `MAPP_RetornaRedePropria` |
| `units` | `MAPP_RetornaRedeUnidades` |
| `map` | (usa dados de listagem; sem endpoint próprio) |
| `Token` | `rest/apiTokenAtendimento`, `rest/apiListaBeneficiario` |
| `ScheduleCentral` | `rest/apiGetDadosCentral` |
| `incometax` | `rest/apiIR` |
| `tickets` | `rest/apiBoletos` |
| `coparticipation` | `rest/APIValoresCopartCombos`, `rest/apiValoresCopart` |
| `extractor` | `rest/apiExtratoCoparticipacao`, `rest/apiExtratoCopartComboAno`, `rest/apiExtratoCopartComboMes` |
| `notifications` | `MAPP_Notificacoes` (GET/POST) |
| `guidAuthorizer` | `rest/apiAutorizador*` (Pesquisa, Combo, GravarCabecalho, Anexo, Cancelar, PrazoResposta, Busca*, GravarResposta) |
| `deleteUser` | `rest/apiExcluirCadastroApp` |

**Sessão/token:** compartilhado via `SessionManager` (novo) / `SharedPreferences` (legado: chaves
`token`, `person`, `photosPref`, `photoVersoPref`, `selecaoBeneficiarioAutorizador`).

---

## 5. Arquitetura atual

- **DI:** Dagger (legado) + Koin (`otherFeatures`). Novo usa Koin.
- **Rede:** legado usa Retrofit+RxJava `NetworkingService`; novo usa `AppService`+`AppRepository`.
- **Estado:** legado RxJava (`Flowable`/`Completable` + `UseCaseHandler`); MVVM novo `LiveData` +
  `ViewModelResponse`. Novo alvo `StateFlow`.
- **Persistência:** legado `PreferencesDatasource` (SharedPreferences) + `RealmDatasource` (DB local,
  **removido**); novo `SessionManager`.

---

## 6. Arquitetura alvo

```
br.com.policlinsaude
├── data/{models,services,repositories,local}
├── domain/{models,repositories,usecases}
├── ui/{activities, fragments, dialogs, views}
├── util/{extensions,helpers}
└── MainApplication + di (Koin)
```

- Uma feature = `ui/activities/<feature>` (`FeatureActivity` + `FeatureViewModel` + `FeatureAdapter`)
  ou `ui/fragments/<feature>`.
- `ViewModel` junto à feature; adaptadores específicos junto à feature; componentes compartilhados
  em `ui/views`/`ui/dialogs`.

---

## 7. Ordem recomendada de migração

Prioridade (minimizar retrabalho): fundação → autenticação/sessão → shell/home →
features independentes → features dependentes → limpeza do legado.

### Fase 0 — Fundação (parcialmente feita)
- [x] `data/services` (`AppService`/`RetrofitProvider`/`NetworkConstants`/`ServerErrorResponse`)
- [x] `data/repositories` (`AppRepository`)
- [x] `data/models` + `domain/models`
- [x] `data/local` (`SessionManager`)
- [x] DI (Koin + `MainApplication`)
- [ ] Padrão `UiState` + `BaseViewModel` (StateFlow) — criar junto da primeira feature
- [ ] Dependência `koin-androidx-viewmodel` no Gradle (para resolver `viewModel { }`)
- [ ] Definir base URL correta do login (legado `mapp/api` vs novo `apiapp`)

### Fase 1 — Autenticação/sessão
1. `login` (entrada; valida fatia vertical: UI → VM → repo → service → rede → `SessionManager`)

### Fase 2 — Shell/home
2. `home` (`MenuActivity` shell + `HomeFragment`) — hub de navegação

### Fase 3 — Features independentes (MVVM já)
3. `notifications` → 4. `tickets` → 5. `token` → 6. `ScheduleCentral` → 7. `incometax`
8. `extractor` → 9. `coparticipation` → 10. `deleteUser`

### Fase 4 — Features independentes (MVP)
11. `preferences` (usa `SessionManager` get/putBoolean)
12. `perfil` (depende de sessão; abre editPhone/editPassword/deleteUser)
13. `editPhone` → 14. `editPassword` → 15. `forgotPassword` → 16. `notHasPassword`
17. `favorites` → 18. `healthInsurancePhoto` (carteirinha)
19. `units` → 20. `ownNetwork` → 21. `map`

### Fase 5 — Features dependentes / complexas
22. `medicalGuideOptions` → 23. `medicalGuideList` → 24. `medicalGuideDetails`
25. `guidAuthorizer` (8 fragments; maior complexidade agregada)

### Fase 6 — Limpeza do legado
- Remover `_legacy`, `NetworkingService`/datasources RxJava, `Navigator`s, Dagger, usecases não usados.


---

## 8. Riscos

1. **Base URL divergente** — endpoints legados (`MAPP_Login` etc.) apontavam para `mapp/api/`,
   enquanto os modernos (`rest/api*`) usam `apiapp/`. Se misturados, chamadas falham.
2. **Dois stacks de rede** — legado RxJava vs novo coroutines; garantir conversão correta dos bodies.
3. **Dual DI (Dagger + Koin)** — remover Dagger gradualmente para evitar injeção duplicada.
4. **`guidAuthorizer` e `medicalGuide*`** — maior complexidade (8 fragments / cadeia entre 4 Activities);
   migrar por último.
5. **Carteirinha (`healthInsurancePhoto`)** — depende de fotos (Bitmap/cache/permissões) e do
   `SessionManager` (frente/verso). Exigirá atenção com UI de imagens.
6. **Token FCM (`firebaseToken`)** — o `login` envia token de push; precisa mecanismo de obtenção na
   feature nova (helper próprio).
7. **Recursos (layouts/nav-graphs)** — ainda em `_legacy/res`; precisam migrar com cada feature.

---

## 9. Dúvidas / Decisões pendentes (requer confirmação humana)

> Sem assumir qual está correto quando há divergência legado vs novo.

1. **Base URL do login**: endpoints legados usam `mapp/api/` (`MAPP_Login`, `MAPP_RecuperarSenha`,
   `MAPP_CarterinhaVirtualv2`, `MAPP_ValidaBeneficiario`, `MAPP_RetornaBeneficiario`, `MAPP_Atualiza*`,
   `MAPP_ManutencaoFavoritos`, `MAPP_RetornaFavoritos`, `MAPP_RetornaRede*`) e os novos usam `apiapp/`
   (`rest/api*`). **Confirmar a base real de produção** — o `NetworkingService` legado usava `mapp/api/`
   e o `RetrofitInstance` novo `apiapp/`. O `AppRepository` hoje usa `BASE_URL=apiapp/` por padrão e
   `BASE_URL_NOTIFICATION=mapp/api/` apenas para notificações/perfil. **Decidir** se todos os endpoints
   de conta/sessão (login, senha, favoritos, rede, carteirinha) devem usar `apiapp/` ou `mapp/api/`.
2. **Token/pessoa no repository**: alguns endpoints pedem `token` como `@Field`/`@Header` e hoje os
   métodos do `AppRepository` recebem `token` por parâmetro. Confirmar se o `AppRepository` deve ler o
   token do `SessionManager` internamente (em vez de receber por parâmetro) na nova arquitetura.
3. **`ViewModelResponse` (LiveData)** vs `StateFlow`: as features MVVM novas usam `LiveData` +
   `ViewModelResponse`. A diretriz diz "StateFlow para código novo"; **decidir** se reconstruímos as
   features MVVM novas com StateFlow na migração ou mantemos LiveData.
4. **`getBanners` (Banner)**: usa `@Url` dinâmico no legado; validar se a URL do banner vem de
   configuração ou de outro endpoint.
5. **`map`/`MapsActivity`**: depende das listagens (units/ownNetwork/guia); confirmar se terá
   `MapsActivity` própria ou fragmento embutido na arquitetura alvo.
6. **`notHasPassword`**: possui sub-fragments internos (steppers); confirmar escopo (fragmentos vs activity única).
7. **`informations`/`links`**: fragments "sem presenter" no legado; confirmar se são apenas telas
   estáticas ou se devem ganhar ViewModel na migração.

