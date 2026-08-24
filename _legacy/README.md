# _legacy — Código histórico de referência

Esta pasta guarda **todo o código-fonte anterior** do módulo `app`, movido aqui para servir como
base de referência durante a reescritura da aplicação para a nova arquitetura.

> Data: 24/08/2026.
> As próximas tarefas de migração (Tarefas 05+) devem usar esta pasta como base de consulta
> e **não** alterar a aplicação atual.

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

## Como usar

1. **Não editar aqui**: é um snapshot de referência para consulta/adaptação feature a feature.
2. Ao migrar, **copiar/adaptar** arquivos desta pasta para a nova raiz:
   - código → `app/src/main/java/br/com/policlinsaude/...`
   - recursos → `app/src/main/res/...`
3. O source set de `app/src` ficou **vazio** de arquivos `.kt`/`.java`; os assets comuns
   (`res/values`, `res/drawable`, `res/font`, `res/mipmap`, `AndroidManifest.xml`) permanecem,
   simplificados num manifest mínimo que executa a `MainActivity` placeholder.

## Normalização para a migração

- **Namespace único**: `br.com.policlinsaude.*` (no histórico convivem `com.policlinsaude.newfeature.*`).
- **Capa de dados única**: `data/{models,services,repositories,local}` (hoje há dois stacks).
- Cada feature deve viver em `ui/activities/<feature>` / `ui/fragments/<feature>`.
- ViewModel junto à feature; adaptadores específicos junto à feature.
- **Estado**: `StateFlow` para código novo; não migrar todo LiveData/RxJava apenas por preferência.
