# Rede Própria

## Descrição resumida
Apresentação da rede própria de prestadores e navegação por mapa/localização.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.ownNetwork`
- `br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity.kt`
- `br.com.policlinsaude.ownNetwork.view.OwnNetworkFragment.kt`
- `br.com.policlinsaude.ownNetwork.view.OwnNetworkView.kt`
- `br.com.policlinsaude.ownNetwork.view.adapter`
- `br.com.policlinsaude.map`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_own_network.xml`, `res/layout/fragment_own_network.xml`, `res/layout/list_item_own_network.xml`, `res/layout/list_item_page_own_network.xml`, `res/drawable/own_network.png`, `res/drawable/ic_maps.xml`, `res/drawable/ic_maps_white.xml`
- Para `_legacy/res/` se reescrita: `res/layout/activity_maps.xml`, `res/layout/activity_maps_teste.xml`, `res/values/strings.xml` com textos de rede e mapa

## Dependências com outras features
- `shared`
- `home`
- `map`
- `units`

## Status
pendente
