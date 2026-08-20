# Unidades

## Descrição resumida
Listagem e detalhes de unidades da Policlin, com filtros de localização e tipo de estabelecimento.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.units`
- `br.com.policlinsaude.units.view.UnitsActivity.kt`
- `br.com.policlinsaude.units.view.UnitsFragment.kt`
- `br.com.policlinsaude.units.view.UnitsView.kt`
- `br.com.policlinsaude.units.view.adapter`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_units.xml`, `res/layout/fragment_units.xml`, `res/layout/list_item_units.xml`, `res/layout/custom_view_info_medical_guide_list_units.xml`, `res/drawable/ic_menu.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de unidades e filtros, layouts de filtros e menus de localização

## Dependências com outras features
- `shared`
- `home`
- `map`
- `medical-guide-list`

## Status
pendente
