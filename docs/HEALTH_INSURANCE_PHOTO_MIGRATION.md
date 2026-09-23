# Migração da feature `healthInsurancePhoto` (carteirinha) para MVVM

## Arquivos criados
- `app/src/main/java/br/com/policlinsaude/ui/activity_healthInsurancePhoto/HealthInsurancePhotoActivity.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activity_healthInsurancePhoto/HealthInsurancePhotoViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activity_healthInsurancePhoto/PhotoPageAdapter.kt`
- `app/src/main/java/br/com/policlinsaude/ui/views/ZoomClass.kt` (componente de zoom do legado,
  movido para `ui/views` pois é visual e usado pela carteirinha)

## Arquivos modificados
- `app/src/main/AndroidManifest.xml` — declara `HealthInsurancePhotoActivity`.
- `app/src/main/java/br/com/policlinsaude/di/AppModules.kt` — registra `HealthInsurancePhotoViewModel` no Koin.
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomeFragment.kt` — tile `HEALTH_INSURANCE`
  agora abre a carteirinha real (antes: Toast "Em construção").

## Layouts migrados
- `activity_health_insurance_photo.xml` (reescrito: ViewPager1 + `com.rd.PageIndicatorView` →
  **ViewPager2** + indicador nativo de pontos; mantidas Toolbar/verso),
  `list_item_page_photos.xml` (referência `ZoomClass` atualizada para `ui.views.ZoomClass`) e
  `menu/activity_health_insurance_photo.xml` (item `action_verso`) movidos de `_legacy/res` → `app/src/main/res`.

## Fluxo antigo (legado — MVP)
```
View (HealthInsurancePhotoActivity)
  ↓
Presenter (HealthInsurancePhotoPresenterImpl)
  ↓
GetHealthInsurancePhotoUseCase → RepositoryImpl → NetworkingDatasourceImpl → NetworkingService
  ↓ (cache: SharedPreferences photosPref / photoVersoPref)
```
- `getImage(context)` checava `CheckInternetConnection.check(context)`.
- `showDialogError` / `showWithoutNetworkDialog` usavam as fotos salvas em SharedPreferences.
- ViewPager1 + Glide; `ZoomClass` (pinch/drag/double-tap).

## Fluxo novo (MVVM)
```
UI (HealthInsurancePhotoActivity)
  ↓ eventos
HealthInsurancePhotoViewModel (LiveData + SingleLiveEvent)
  ↓ suspend
AppRepository.onGetHealthInsurancePhoto(token, checkUpdated = 0)
  ↓
AppService (MAPP_CarterinhaVirtualv2)
```
- Cache mantido via `SessionManager.saveCarteirinha/clearCarteirinha` (mesmas chaves legadas
  `photosPref`/`photoVersoPref`).
- Conectividade avaliada na UI (`ConnectivityHelper.isOnline`) e passada ao ViewModel
  (evita referência a Context dentro do ViewModel).
- Frente em ViewPager2 + indicador de pontos nativo; verso via toolbar (`action_verso`).
- `ZoomClass` (zoom/drag) preservado em `ui/views/ZoomClass.kt`.

## Validação
- `:app:compileDebugKotlin` → **BUILD SUCCESSFUL**
- `:app:assembleDebug` → **BUILD SUCCESSFUL**
- `:app:clean :app:assembleDebug` → **BUILD SUCCESSFUL**

## Pendências / divergências (registradas, sem assumir)
- O campo `verificaAlteracao` do endpoint segue `0` (mesmo do legado).
- A rotação `rotate(90f)` das fotos da frente foi mantida do legado (verificar no device
  se as imagens chegam corretas — pode variar conforme o backend).
- Validação manual no device ainda não executada.