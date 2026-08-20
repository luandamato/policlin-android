# Extrator de fatores

## Descrição resumida
Módulo de cálculo e extração de fatores, com múltiplos cards/itens e detalhamento de dados.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.extractor`
- `com.policlinsaude.newfeature.features.extractor.data.models.*`
- `com.policlinsaude.newfeature.features.extractor.ui.activities.FactorExtractorActivity.kt`
- `com.policlinsaude.newfeature.features.extractor.ui.fragments.FactorExtractorFragment.kt`
- `com.policlinsaude.newfeature.features.extractor.ui.viewmodels.FactorExtractorViewModel.kt`
- `com.policlinsaude.newfeature.features.extractor.ui.adapters.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_factor_extractor.xml`, `newfeature/src/main/res/layout/fragment_factor_extractor.xml`, `newfeature/src/main/res/layout/fragment_factor_extractor_detail.xml`, `newfeature/src/main/res/layout/adapter_factor_extractor_cards.xml`, `newfeature/src/main/res/layout/adapter_factor_extractor_detail.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` e textos de explicação/aviso do fluxo

## Dependências com outras features
- `shared`
- `home`
- `perfil`

## Status
pendente
