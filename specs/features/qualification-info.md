# Qualificações / Informações de qualificação

## Descrição resumida
Legenda e informações sobre qualificações, serviços e atributos dos prestadores de saúde.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.qualificationInfo`
- `br.com.policlinsaude.qualificationInfo.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/list_item_info_qualification.xml`, `res/layout/list_item_qualification_icon.xml`, `res/drawable/ic_*` relacionados a qualificações
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com legendas e textos de orientação sobre qualificações

## Dependências com outras features
- `shared`
- `medical-guide-list`
- `medical-guide-details`
- `own-network`

## Status
pendente
