# Perfil

## Descrição resumida
Área do cliente com dados cadastrais, edição de telefone, alteração de senha e gerenciamento do perfil.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.perfil`
- `br.com.policlinsaude.perfil.view.PerfilFragment.kt`
- `br.com.policlinsaude.perfil.view.PerfilView.kt`
- `br.com.policlinsaude.editPassword`
- `br.com.policlinsaude.editPhone`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/fragment_perfil.xml`, `res/layout/fragment_personal_data.xml`, `res/layout/fragment_plan_data.xml`, `res/drawable/ic_profile.xml`, `res/drawable/ic_senha.xml`, `res/drawable/ic_trash.xml`
- Para `_legacy/res/` se reescrita: `res/layout/activity_edit_password.xml`, `res/layout/activity_edit_phone.xml`, `res/values/strings.xml` com textos de alteração de dados, confirmação e validações

## Dependências com outras features
- `shared`
- `login`
- `home`
- `edit-password`
- `edit-phone`

## Status
pendente
