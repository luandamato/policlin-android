# Central de agendamento

## Descrição resumida
Fluxo de agendamento e consulta de horários/serviços do cliente em central de atendimento.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.ScheduleCentral`
- `com.policlinsaude.newfeature.features.ScheduleCentral.ui.activities.ScheduleCentralActivity.kt`
- `com.policlinsaude.newfeature.features.ScheduleCentral.ui.fragments.ScheduleCentralFragment.kt`
- `com.policlinsaude.newfeature.features.ScheduleCentral.ui.viewmodels.ScheduleCentralViewModel.kt`
- `com.policlinsaude.newfeature.features.ScheduleCentral.models.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_schedule_central.xml`, `newfeature/src/main/res/layout/fragment_schedule_central.xml`, `newfeature/src/main/res/values/*.xml` se compatíveis
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` e layouts de apoio específicos do fluxo se exigirem redesign ou compatibilidade visual

## Dependências com outras features
- `shared`
- `home`
- `perfil`

## Status
pendente
