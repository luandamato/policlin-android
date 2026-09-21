# Migração da feature `notifications` para MVVM

## Arquivos criados
- `app/src/main/java/br/com/policlinsaude/ui/activities/notifications/NotificationActivity.kt`
  (toolbar + host do fragment; sem NavHostFragment/Navigation Component)
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notifications/NotificationViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notifications/NotificationsFragment.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/notifications/NotificationsAdapter.kt`

## Arquivos modificados
- `app/src/main/AndroidManifest.xml` — declara `NotificationActivity`.
- `app/src/main/java/br/com/policlinsaude/di/AppModules.kt` — registra `NotificationViewModel` no Koin.
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomeFragment.kt` — clique no sino
  (`appBarContainer.alertMenu`) agora abre `NotificationActivity` (antes não tinha handler).
- `app/src/main/res/values/strings.xml` — add `action_edit_notifications` ("Editar"),
  `action_delete_notifications` (`Apagar Notificação`), `msg_notifications_empty`.

## Layouts migrados
- `activity_notification.xml` (reescrito para container `FrameLayout` simples; no legado usava
  `NavHostFragment` + `notification_nav_graph.xml`), `fragment_notifications.xml`,
  `adapter_notifications.xml` e `toolbar.xml` (compartilhado) movidos de `_legacy/res` → `app/src/main/res`.

## Fluxo antigo (legado — stack `com.policlinsaude.newfeature`)
```
NotificationActivity (NavHost) → NotificationsFragment
  ↓
NotificationViewModel (LiveData<ViewModelResponse<...>>)
  ↓
NotificationRepositoryImpl → NotificationService (Retrofit) — MAPP_Notificacoes
```
DI: Koin (módulo `otherFeatures`).

## Fluxo novo (MVVM)
```
UI (NotificationActivity + NotificationsFragment)
  ↓ eventos
NotificationViewModel (LiveData + SingleLiveEvent)
  ↓ suspend
AppRepository.onGetNotifications / onDeleteNotifications
  ↓
AppService (MAPP_Notificacoes) → RetrofitProvider
```

- Fragment usado direto pela Activity via `FragmentManager` (`R.id.fragment_container`).
- Seleção/edição/deleção com o mesmo comportamento do legado (checkbox "selecionar tudo",
  botões Editar/Cancelar na toolbar, apagar selecionadas).
- Diferença: o antigo `ViewModelResponse`/`ViewModelResponseStatus` foram convertidos para
  `MutableLiveData` + `SingleLiveEvent` (padrão do novo app).

## Validação
- `:app:compileDebugKotlin` → **BUILD SUCCESSFUL**
- `:app:assembleDebug` → **BUILD SUCCESSFUL**
- `:app:clean :app:assembleDebug` → **BUILD SUCCESSFUL**

## Dúvidas / divergências (registradas, sem assumir)
1. **Base URL das notificações**: no legado, `NotificationRepositoryImpl` usava
   `RetrofitInstance.API_NOTIFICATION = "http://policlinsaude.com.br/mapp/api/"`.
   No `AppRepository` novo, `onGetNotifications`/`onDeleteNotifications` usam
   `notificationService` → `BASE_URL_APIAPP = "http://policlinsaude.com.br/apiapp/"`.
   ✅ Mantida a configuração já existente no `AppRepository` (não alterada);
   registrar no doc/MIGRATION_MAP como decisão a confirmar.
2. **Toolbar**: movido `toolbar.xml` (layout compartilhado do legado) para `app/src/main/res/layout`
   para suportar a tela de notificações.
3. Validação manual no device ainda não executada.