# Imposto de renda

## Descrição resumida
Fluxo de apuração/consulta do imposto de renda relacionado ao benefício ou contexto do cliente.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.incometax`
- `com.policlinsaude.newfeature.features.incometax.data.models.*`
- `com.policlinsaude.newfeature.features.incometax.ui.activities.IncomeTaxActivity.kt`
- `com.policlinsaude.newfeature.features.incometax.ui.fragments.IncomeTaxFragment.kt`
- `com.policlinsaude.newfeature.features.incometax.ui.viewmodels.IncomeTaxViewModel.kt`
- `com.policlinsaude.newfeature.features.incometax.ui.adapters.IncomeTaxItemAdapter.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_income_tax.xml`, `newfeature/src/main/res/layout/fragment_income_tax.xml`, `newfeature/src/main/res/layout/adapter_income_tax_item.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de explicação e validação de dados

## Dependências com outras features
- `shared`
- `perfil`
- `home`

## Status
pendente
