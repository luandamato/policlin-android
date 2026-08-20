# Mapa

## Descrição resumida
Fluxo de visualização geográfica, localização e mapas de unidades e rede própria.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.map`
- `br.com.policlinsaude.map.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_maps.xml`, `res/layout/activity_maps_teste.xml`, `res/drawable/ic_maps.xml`, `res/drawable/ic_maps_white.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com títulos e avisos de mapa e localização; recursos de mapa específicos da integração do Google Maps

## Dependências com outras features
- `shared`
- `own-network`
- `units`
- `medical-guide-list`

## Status
pendente
