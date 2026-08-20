# Token

## Descrição resumida
Fluxo de geração e visualização de token/beneficiário para acesso a serviços e autorização do cliente.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.Token`
- `com.policlinsaude.newfeature.features.Token.ui.TokenActivity.kt`
- `com.policlinsaude.newfeature.features.Token.ui.TokenViewModel.kt`
- `com.policlinsaude.newfeature.features.Token.ui.ui.freagments.TokenFragment.kt`
- `com.policlinsaude.newfeature.features.Token.ui.ui.freagments.TokenBeneficiarioFragment.kt`
- `com.policlinsaude.newfeature.features.Token.models.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_token.xml`, `newfeature/src/main/res/layout/fragment_token.xml`, `newfeature/src/main/res/layout/fragment_token_beneficiario.xml`, `newfeature/src/main/res/layout/adapter_dependente.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de autorização, beneficiários e confirmação de operação

## Dependências com outras features
- `shared`
- `perfil`
- `home`

## Status
pendente
