# Guia Médico - Detalhes

## Descrição resumida
Tela de detalhes do prestador/guia médico, com informações institucionais, filtros e dados complementares.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.medicalGuideDetails`
- `br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity.kt`
- `br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsView.kt`
- `br.com.policlinsaude.medicalGuideDetails.view.adapter`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_medical_guide_details.xml`, `res/layout/list_item_medical_guide_details.xml`, `res/drawable/ic_information.xml`, `res/drawable/ic_informacoes.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de detalhes, filtros e legendas; layouts auxiliares de detalhe e legendas

## Dependências com outras features
- `shared`
- `medical-guide-list`
- `qualification-info`
- `map`

## Status
pendente
