# Shared

## Descrição resumida
Infraestrutura compartilhada do app: camadas de base, utilitários, navegação, DTO/modelos, preferências e widgets reutilizáveis utilizados por praticamente todas as features.

## Categoria
shared

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.core`
- `br.com.policlinsaude.data`
- `br.com.policlinsaude.mapper`
- `br.com.policlinsaude.model`
- `br.com.policlinsaude.preferences`
- `br.com.policlinsaude.util`
- `br.com.policlinsaude.ui.adapters`
- `br.com.policlinsaude.ui.util`
- `br.com.policlinsaude.ui.views`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/values/colors.xml`, `res/values/dimens.xml`, `res/values/styles.xml`, `res/drawable/ic_*`, `res/drawable/*_selector*`, `res/drawable/side_nav_bar.xml`, `res/layout/nav_header_home.xml`, `res/layout/toolbar.xml`, `res/layout/view_loading.xml`
- Migração para `_legacy/res/` somente se houver reescrita de componente visual compartilhado: `res/layout/app_bar_home.xml`, `res/layout/list_item_menu.xml`, `res/layout/spinner_list_item.xml`, `res/values/strings.xml` quando o texto global precisar ser refeito em conjunto

## Dependências com outras features
- Base para todas as features do app
- Reaproveitado por login, home, perfil, guia médico, rede própria, favoritos, áreas de cadastro e informações

## Status
pendente
