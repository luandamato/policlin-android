# Migração da feature `home` para MVVM — Tarefa 05

## Arquivos criados
- `app/src/main/java/br/com/policlinsaude/ui/activities/home/MenuActivity.kt` (shell + drawer)
- `app/src/main/java/br/com/policlinsaude/ui/activities/home/MenuViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activities/home/MenuAdapter.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activities/home/MenuOptionEnum.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomeFragment.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomeViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomeAdapter.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomePageAdapter.kt`
- `app/src/main/java/br/com/policlinsaude/ui/fragments/home/HomeOptionEnum.kt`
- `app/src/main/java/br/com/policlinsaude/data/models/BannerMapper.kt`

## Arquivos modificados
- `app/src/main/AndroidManifest.xml` — declara `MenuActivity`.
- `app/src/main/java/br/com/policlinsaude/di/AppModules.kt` — registra `MenuViewModel` + `HomeViewModel`.
- `app/src/main/java/br/com/policlinsaude/ui/activities/login/LoginActivity.kt` — navegação pós-login agora vai ao `MenuActivity` real (antes: Toast "Em construção").

## Layouts/menu migrados
- `activity_home.xml`, `app_bar_home.xml`, `fragment_home.xml` (ajustado a ViewPager2), `list_item_home.xml`,
  `list_item_menu.xml`, `list_item_page_home.xml`, `nav_header_home.xml` e `menu/activity_home_drawer.xml`,
  movidos de `_legacy/res` → `app/src/main/res`.

## Fluxo antigo (legado)
```
View (MenuActivity / HomeFragment)
  ↓
Presenter (MenuPresenterImpl / HomePresenterImpl)
  ↓
UseCases (GetCurrentPerson, GetBanners, GetValidationUserConnected, ValidateButtons, DoLogoff)
  ↓
Repository (RepositoryImpl) → Datasource → NetworkingService (RxJava)
```
Navegação: `MenuNavigator` + `HomeNavigator` (startActivity / fragment replace).

## Fluxo novo (MVVM)
```
UI (MenuActivity → shell; HomeFragment → conteúdo)
  ↓ eventos
MenuViewModel / HomeViewModel
  ↓ suspend
AppRepository (onGetProfile, onGetBanners, onValidateUserConnected, onValidateButtons, onDoLogoff)
  ↓
AppService → RetrofitProvider
```

- **MenuActivity** host do `HomeFragment` via `FragmentManager` (mesmo padrão do legado).
- **Navegação interna real** do drawer: HOME abre o `HomeFragment`; demais destinos ainda não migrados → Toast "Em construção".
- **Banners**: ViewPager2 + indicador nativo de pontos (o leía ViewPager1 + `com.rd.PageIndicatorView`).
- **ViewModel**: LiveData + SingleLiveEvent; sem View/Activity/Fragment/Binding; sem navegar.

## Responsabilidades
- UI: binding, banners (ViewPager2), grid, indicador, loading, navegação, diálogos.
- ViewModel: carrega pessoa, banners, validações (user/buttons), expõe estado/eventos.

## Validação
- `:app:compileDebugKotlin` → **BUILD SUCCESSFUL**
- `:app:assembleDebug` → **BUILD SUCCESSFUL**
- `:app:clean :app:assembleDebug` → a executar (pendente).

## Pendências
- Banners carregados via `http://mpoli.tangram.net.br/appplcsa/api/Banner` (mantido do legado).
- Destinos dos tiles (medical guide, carteirinha, rede, favoritos, boletos, coparticipação, extrato, IR,
  autorizador, central, token) e do drawer (perfil, preferências, informação, unidades, links, telefone CID)
  ainda não migrados → Toast "Em construção" (conforme ARCHITECTURE_TARGET §10).
- `com.rd.PageIndicatorView` (lib legada incompatível com ViewPager2) mantida no build.gradle mas não usada em home; possível remoção futura.
- Indiferença não eliminada: `activity_home_drawer.xml` do menu foi movido mas o drawer usa `MenuAdapter` com `list_item_menu.xml` (não a NavigationView padrão) — mesmo comportamento do legado.