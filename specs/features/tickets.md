# Tickets

## Descrição resumida
Fluxo de tickets/solicitações do cliente com listagem, detalhes e ações relacionadas a chamados e atendimento.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.tickets`
- `com.policlinsaude.newfeature.features.tickets.data.models.*`
- `com.policlinsaude.newfeature.features.tickets.ui.activities.TicketsActivity.kt`
- `com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment.kt`
- `com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketDetailFragment.kt`
- `com.policlinsaude.newfeature.features.tickets.ui.viewmodels.TicketViewModel.kt`
- `com.policlinsaude.newfeature.features.tickets.ui.adapters.TicketAdapter.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_tickets.xml`, `newfeature/src/main/res/layout/fragment_tickets.xml`, `newfeature/src/main/res/layout/fragment_ticket_detail.xml`, `newfeature/src/main/res/layout/adapter_tickets.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de chamados, filtros e detalhes

## Dependências com outras features
- `shared`
- `home`
- `perfil`

## Status
pendente
