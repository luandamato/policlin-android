# Autorização de guia

## Descrição resumida
Fluxo de autorização e validação de guias, com dados do beneficiário, respostas, prazos e upload de documentos/imagens.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.guidAuthorizer`
- `com.policlinsaude.newfeature.features.guidAuthorizer.components.Accordion.kt`
- `com.policlinsaude.newfeature.features.guidAuthorizer.data.models.*`
- `com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity.kt`
- `com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments.*`
- `com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel.kt`
- `com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_guide_authorizer.xml`, `newfeature/src/main/res/layout/fragment_guide_authorizer.xml`, `newfeature/src/main/res/layout/fragment_guide_authorizer_details.xml`, `newfeature/src/main/res/layout/fragment_beneficiary_data.xml`, `newfeature/src/main/res/layout/fragment_process_request.xml`, `newfeature/src/main/res/layout/fragment_request_data.xml`, `newfeature/src/main/res/layout/fragment_request_detail.xml`, `newfeature/src/main/res/layout/fragment_answer_questions.xml`, `newfeature/src/main/res/layout/adapter_guide_authorizer.xml`, `newfeature/src/main/res/layout/adapter_picture.xml`, `newfeature/src/main/res/layout/adapter_question_and_answer.xml`, `newfeature/src/main/res/layout/component_accordion.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` e layouts de componentes compartilhados que precisem ser reestilizados

## Dependências com outras features
- `shared`
- `home`
- `perfil`
- `medical-guide-list`

## Status
pendente
