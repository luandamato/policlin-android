# Guia Médico - Lista

## Descrição resumida
Busca e listagem de prestadores, especialidades e planos atendidos por região e filtro.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.medicalGuideList`
- `br.com.policlinsaude.medicalGuideList.view.MedicalGuideListActivity.kt`
- `br.com.policlinsaude.medicalGuideList.view.MedicalGuideListView.kt`
- `br.com.policlinsaude.medicalGuideOptions`
- `br.com.policlinsaude.medicalGuideDetails`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_medical_guide_list.xml`, `res/layout/activity_medical_guide_options.xml`, `res/layout/list_item_medical_guide.xml`, `res/layout/list_item_medical_guide_andre.xml`, `res/layout/view_filters.xml`, `res/drawable/medical_guide.png`, `res/drawable/ic_info.xml`
- Para `_legacy/res/` se reescrita: `res/layout/custom_view_info_medical_guide_list.xml`, `res/layout/custom_view_info_medical_guide_list_units.xml`, `res/values/strings.xml` com filtros, títulos e mensagens de vazio

## Dependências com outras features
- `shared`
- `home`
- `medical-guide-details`
- `units`
- `map`
- `qualification-info`

## Status
pendente
