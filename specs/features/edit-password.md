# Alterar senha

## Descrição resumida
Edição de senha do cliente em ambiente de perfil e área do usuário autenticado.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.editPassword`
- `br.com.policlinsaude.editPassword.*` (pacote próprio para alteração de senha)

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_edit_password.xml`, `res/drawable/ic_senha.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de validação and confirm., mensagens de sucesso e erro

## Dependências com outras features
- `shared`
- `perfil`
- `login`

## Status
pendente
