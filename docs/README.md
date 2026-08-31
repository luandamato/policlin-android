# _legacy — Código histórico de referência

Esta pasta guarda **todo o código-fonte anterior** do módulo `app`, movido aqui para servir como
base de referência durante a reescritura da aplicação para a nova arquitetura.

> Data: 24/08/2026.
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
    ├── layout/      # Layouts legados (97 arquivos)
    ├── navigation/  # Nav-graphs legados (safeargs)
    └── menu/        # Menus legados
```

## Já migrado para o app

- **`data/models`** (81): DTOs `Json*`, apresentação `Presentation*`, modelos por feature + `UserModel`.
- **`domain/models`** (25): modelos de domínio.
- **`data/services`**: **`AppService`** (serviço único com todos os 47 endpoints), `RetrofitProvider`,
  `NetworkConstants`, `ServerErrorResponse`.
- **`data/repositories`**: **`AppRepository`** (repositório único, 1 método por endpoint), sem `RequestHandler` (embutido).
- **`util/helpers/InvalidData`**: helper único (deixa de haver 3 cópias).

### Camada de rede (`data/services` + `data/repositories`)

A camada de rede segue o padrão do projeto de referência, mas **consolidada**:

```
data/services/
├── AppService.kt            # SERVICE ÚNICO — todos os endpoints (47)
├── RetrofitProvider.kt      # centralizador Retrofit (ServiceGenerator-like)
├── NetworkConstants.kt      # URLs/códigos de rede
└── ServerErrorResponse.kt
data/repositories/
└── AppRepository.kt         # REPOSITORY ÚNICO — 1 método por endpoint
```

- **1 arquivo de service** (`AppService`) contém todos os endpoints modernos (`rest/api*`) e legados
  (`MAPP_*`, `Banner`, `apiAcessoBotoes`, `apiExcluirCadastroApp`, etc.).
- **1 arquivo de repositório** (`AppRepository`) expõe um método para cada endpoint, com o tratamento
  de erro (`ServerErrorResponse`) embutido via `.request { ... }`.
- Base URL: usa `NetworkConstants.BASE_URL` (padrão) e `BASE_URL_NOTIFICATION` (para notificações/perfil).

## Como usar

1. **Não editar aqui**: é um snapshot de referência para consulta/adaptação feature a feature.
2. Ao migrar, **copiar/adaptar** arquivos desta pasta para a nova raiz:
   - código → `app/src/main/java/br/com/policlinsaude/...`
   - recursos → `app/src/main/res/...`

## Normalização para a migração

- **Namespace único**: `br.com.policlinsaude.*` (no histórico convivem `com.policlinsaude.newfeature.*`).
- **Capa de dados única**: `data/{models,services,repositories,local}`.
- Cada feature deve viver em `ui/activities/<feature>` / `ui/fragments/<feature>`.
- ViewModel junto à feature; adaptadores específicos junto à feature.
- **Estado**: `StateFlow` para código novo; não migrar todo LiveData/RxJava apenas por preferência.