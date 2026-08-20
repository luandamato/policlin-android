# Excluir cadastro

## Descrição resumida
Fluxo de exclusão de cadastro/conta do cliente com confirmação e validações de segurança.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `com.policlinsaude.newfeature.features.deleteUser`
- `com.policlinsaude.newfeature.features.deleteUser.model.DeleteUserRequest.kt`
- `com.policlinsaude.newfeature.features.deleteUser.ui.Activity.DeleteUserActivity.kt`
- `com.policlinsaude.newfeature.features.deleteUser.ui.Fragment.DeleteUserFragment.kt`
- `com.policlinsaude.newfeature.features.deleteUser.ui.ViewModel.DeleteUserViewModel.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `newfeature/src/main/res/layout/activity_delete_user.xml`, `newfeature/src/main/res/layout/fragment_delete_user.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com mensagens de confirmação e avisos

## Dependências com outras features
- `shared`
- `perfil`
- `login`

## Status
pendente
