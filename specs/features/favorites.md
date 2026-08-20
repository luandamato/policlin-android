# Favoritos

## Descrição resumida
Gestão de itens favoritos e lista de elementos salvos pelo usuário.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.favorites`
- `br.com.policlinsaude.favorites.*` (estrutura do módulo em pacote próprio)

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_favorites.xml`, `res/layout/list_item_favorite_header.xml`, `res/layout/list_item_favorite_item.xml`, `res/drawable/ic_favorite.xml`, `res/drawable/ic_favorite_full.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com mensagens de sucesso e estado vazio

## Dependências com outras features
- `shared`
- `home`
- `medical-guide-list`
- `own-network`

## Status
pendente
