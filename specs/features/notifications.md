# Notificações

## Descrição resumida
Tela e fluxo de notificações do cliente, com listagem, detalhes e ações de remoção/atualização.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.notifications`
- `com.policlinsaude.newfeature.features.notifications.data.models.*`
- `com.policlinsaude.newfeature.features.notifications.ui.activities.NotificationActivity.kt`
- `com.policlinsaude.newfeature.features.notifications.ui.fragments.NotificationsFragment.kt`
- `com.policlinsaude.newfeature.features.notifications.ui.viewmodels.NotificationViewModel.kt`
- `com.policlinsaude.newfeature.features.notifications.ui.adapters.NotificationsAdapter.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_notification.xml`, `newfeature/src/main/res/layout/fragment_notifications.xml`, `newfeature/src/main/res/layout/adapter_notifications.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de notificação, remoção e mensagens vazias

## Dependências com outras features
- `shared`
- `home`
- `perfil`

## Status
pendente
