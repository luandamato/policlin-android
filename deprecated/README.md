# Código Deprecated

Esta pasta contém código legado que foi removido da build ativa. Os arquivos aqui servem como **referência** para reescrever funcionalidades usando a nova arquitetura do projeto.

## Pastas Aqui

### /core
Contém código antigo relacionado a:
- Aplicação (PoliclinSaudeApplication.kt - substituído por AppDelegate.kt)
- Modelos Hilt e Dagger (DI framework removido)
- BaseActivity/BaseFragment legados (substituído por novos em ui/activities/ e ui/fragments/)
- Helpers e utilitários legados

### /domain
Contém modelos de domínio do padrão Clean Architecture (removido). 
- Use `/data/models/Models.kt` no lugar

### /units
Telas e funcionalidades relacionadas a unidades (legado MVP)

## Como Reescrever

1. Use os arquivos aqui como referência para entender a lógica original
2. Implemente as funcionalidades seguindo o novo padrão em `/ui/activities/{feature}/`
3. Use `ServiceLocator` para DI em vez de Hilt/Dagger
4. Delete os arquivos legados quando a reimplementação estiver completa

## Nova Arquitetura

```
br.com.policlinsaude/
├── data/
│   ├── models/        ← Modelos de dados
│   ├── services/      ← API e ServiceLocator
│   └── values/        ← Constantes
├── ui/
│   ├── activities/    ← Uma pasta por feature
│   ├── fragments/
│   └── ...
└── util/
    └── extensions/    ← Extensões Kotlin
```

## Nota Build

Esta pasta é automaticamente excluída da build pelo sourceSets em `presentation/build.gradle`.
