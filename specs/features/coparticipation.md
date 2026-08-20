# Coparticipação

## Descrição resumida
Consulta e pesquisa de coparticipação, com filtros e detalhes por item/serviço.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.coparticipation`
- `com.policlinsaude.newfeature.features.coparticipation.data.services.CoParticipationServices.kt`
- `com.policlinsaude.newfeature.features.coparticipation.data.repositories.CoParticipationRepository.kt`
- `com.policlinsaude.newfeature.features.coparticipation.data.repositories.CoParticipationRepositoryImpl.kt`
- `com.policlinsaude.newfeature.features.coparticipation.ui.activities.ResearchCoParticipationActivity.kt`
- `com.policlinsaude.newfeature.features.coparticipation.ui.fragments.*`
- `com.policlinsaude.newfeature.features.coparticipation.ui.viewmodels.CoParticipationViewModel.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_research_co_participation.xml`, `newfeature/src/main/res/layout/fragment_research_co_participation_filters.xml`, `newfeature/src/main/res/layout/adapter_co_participation_item_detail.xml`, `newfeature/src/main/res/layout/adapter_researsh_co_participation.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml`, filtros, mensagens de vazio e ações de busca/ordenação

## Dependências com outras features
- `shared`
- `home`
- `perfil`

## Status
pendente
