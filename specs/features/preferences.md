# Preferências

## Descrição resumida
Configurações do usuário para notificações, localização e ajustes operacionais do aplicativo.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.preferences`
- `br.com.policlinsaude.preferences.view.PreferencesFragment.kt`
- `br.com.policlinsaude.preferences.view.PreferencesView.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/fragment_preferences.xml`, `res/drawable/ic_preferences.xml`, `res/drawable/ic_preferencias.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de preferências, permissão, localização e notificações

## Dependências com outras features
- `shared`
- `home`
- `perfil`

## Status
pendente
