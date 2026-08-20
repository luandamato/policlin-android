# Home

## Descrição resumida
Tela inicial e menu principal do app, com navegação por módulos e acesso às áreas do cliente.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.home`
- `br.com.policlinsaude.home.view.HomeFragment.kt`
- `br.com.policlinsaude.home.view.MenuActivity.kt`
- `br.com.policlinsaude.home.view.MenuView.kt`
- `br.com.policlinsaude.home.view.adapter`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_home.xml`, `res/layout/fragment_home.xml`, `res/layout/list_item_home.xml`, `res/layout/list_item_page_home.xml`, `res/drawable/ic_home.xml`, `res/drawable/ic_home_big.xml`, `res/drawable/navbar_home.png`, `res/drawable/navbar_home_sem_logo.png`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` para títulos do menu, `res/layout/app_bar_home.xml`, `res/layout/nav_header_home.xml`

## Dependências com outras features
- `shared`
- `login`
- `perfil`
- `preferences`
- `medical-guide-list`
- `own-network`
- `units`
- `favorites`

## Status
pendente
