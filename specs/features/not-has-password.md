# Não tenho senha

## Descrição resumida
Fluxo para usuários que ainda não possuem senha cadastrada ou que acessam o app sem cadastro completo.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.notHasPassword`
- `br.com.policlinsaude.notHasPassword.*`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_not_has_password.xml`, `res/layout/fragment_create_password.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de criação de senha, instruções e mensagens de validação

## Dependências com outras features
- `shared`
- `login`

## Status
pendente
