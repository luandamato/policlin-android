# Migração da tela de detalhe da unidade — MVVM

## Objetivo
Migrar a **tela de detalhe da unidade** (antes `MedicalGuideDetailsActivity` com
`caller = "Units"`), acessada a partir da **lista de unidades** (`UnitsFragment`,
já migrada). A tela exibia os dados do estabelecimento sem favorito/planos.

## Arquivos criados
- `app/src/main/java/br/com/policlinsaude/ui/activity_unitDetail/UnitDetailActivity.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activity_unitDetail/UnitDetailViewModel.kt`
- `app/src/main/java/br/com/policlinsaude/ui/activity_unitDetail/UnitDetailAdapter.kt`

## Arquivos modificados
- `app/src/main/AndroidManifest.xml` — declara `UnitDetailActivity`.
- `app/src/main/java/br/com/policlinsaude/di/AppModules.kt` — registra `UnitDetailViewModel` (Koin).
- `app/src/main/java/br/com/policlinsaude/ui/fragments/units/UnitsFragment.kt` — o clique num item
  agora abre `UnitDetailActivity` (antes: Toast "Detalhes em construção").

## Layouts migrados
- `activity_medical_guide_details.xml` → `app/src/main/res/layout/activity_unit_detail.xml`
- `list_item_medical_guide_details.xml` → `app/src/main/res/layout/list_item_unit_detail.xml`

## Fluxo antigo (legado — MVP)
```
UnitsActivity → onItemClick → UnitsNavigator.goToDetails(establishment)
  → MedicalGuideDetailsActivity.start(activity, establishment, "Units")
```
A Activity tinha Presenter (`MedicalGuideDetailsPresenterImpl`) + Navigator
(`MedicalGuideDetailsNavigatorImpl`) + Favoritos/Planos (desativados no modo "Units").

## Fluxo novo (MVVM)
```
UnitsFragment (lista)
  ↓ clique
UnitDetailActivity.start(activity, establishment)   ← serializa com Gson (sem Parcelize)
  ↓
UnitDetailViewModel (parseia JSON)
  ↓
render: foto/frente ou mapa estático, título, telefones, WhatsApp, mapa, compartilhar
```

## Decisões
- **Sem Parcelize**: `PresentationEstablishment` é trocado entre Activities via
  JSON string (Gson) — premissa do projeto (não usar Parcelize @Parcelize).
- **Sem Navigator concreto**: chamadas de telefone (`ACTION_DIAL`), WhatsApp
  (`api.whatsapp.com`), mapa (`geo:`) e compartilhar (`ACTION_SEND`) são feitas
  direto na UI (conforme ARCHITECTURE_TARGET §10).
- **Sem favoritos/planos** no modo "Units": os botões são ocultados (mesmo comportamento do legado).
- `UnitDetailAdapter` (específico da feature) replica o
  `MedicalGuideDetailsAdapter` para `caller="Units"` (sem razão social/CNPJ/tipo).
- Reutiliza `ListItemUnitDetailBinding` (nome novo) e `list_item_qualification_icon.xml`.

## Validação
- `:app:compileDebugKotlin` → **BUILD SUCCESSFUL**
- `:app:assembleDebug` → **BUILD SUCCESSFUL**
- `:app:clean :app:assembleDebug` → **BUILD SUCCESSFUL**

## Pendências / divergências (registradas, sem assumir)
- Validação manual no device ainda não executada.
- A `MedicalGuideDetailsActivity` legada também era usada pelo guia médico/rede própria;
  nesta tarefa migramos apenas o modo "Units". Quando o guia médico for migrado,
  decidir se a tela de detalhe será compartilhada ou específica da feature.