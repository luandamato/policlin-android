# _legacy — Código histórico de referência

Esta pasta guarda **todo o código-fonte anterior** do módulo `app`, movido aqui para servir como
base de referência durante a reescritura da aplicação para a nova arquitetura.

> As próximas tarefas de migração devem usar esta pasta como base de consulta e **não** alterar
> a aplicação atual.

## Estrutura

```
_legacy/
├── src/
│   ├── main/        # Código de produção anterior (Dagger + RxJava + Koin + MVVM novo)
│   │   └── java/br/com/policlinsaude/...
│   ├── test/        # Testes anteriores
│   └── androidTest/ # Testes de integração anteriores
└── res/
    ├── layout/      # Layouts legados (restantes da migração)
    ├── navigation/  # Nav-graphs legados (safeargs)
    └── menu/        # Menus legados
```

## Já migrado para o app

- **`data/models`** + **`domain/models`**: todos os modelos (DTOs, apresentação, feature).
- **`data/services`**: `AppService` (serviço único, 47 endpoints), `RetrofitProvider`,
  `NetworkConstants`, `ServerErrorResponse`.
- **`data/repositories`**: `AppRepository` (repositório único, 1 método por endpoint).
- **`data/local`**: `SessionManager` (token, pessoa, carteirinha, preferências).
- **`di`** + `MainApplication`: Koin com `AppRepository`/`SessionManager`.
- **`util/extensions`**: `ViewExt` (animações/autoscroll/teclado), `BitmapExt`, `StringExt`,
  `DateExt`, `ContextExt` (DatePickerHelper/openBrowser).
- **`util/helpers`**: `InvalidData`, `Mask`, `Validations`, `LocationHelper` (coroutines),
  `PhotoPickerHelper`, `ConnectivityHelper`, `UiState`.
- **`ui/dialogs`**: `DialogHelper` (unificado dos 2 legados), `BottomSheetCommon` +
  `BottomSheetAdapter` (layouts migrados).
- **`ui/views`**: `BaseActivity`, `BaseFragment`, `BaseViewModel` (sem Dagger).

## Normalização para a migração

- **Namespace único**: `br.com.policlinsaude.*` (no histórico convivem `com.policlinsaude.newfeature.*`).
- **Capa de dados única**: `data/{models,services,repositories,local}`.
- Cada feature deve viver em `ui/activities/<feature>` / `ui/fragments/<feature>`.
- ViewModel junto à feature; adaptadores específicos junto à feature.
- **Estado**: `StateFlow` para código novo; não migrar todo LiveData/RxJava apenas por preferência.