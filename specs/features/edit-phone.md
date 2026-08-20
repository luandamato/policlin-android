# Alterar celular

## Descrição resumida
Fluxo de atualização do número de celular cadastrado pelo cliente.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.editPhone`
- `br.com.policlinsaude.editPhone.*` (pacote próprio para edição do celular)

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_edit_phone.xml`, `res/drawable/ic_phone`/recursos vinculados quando existirem
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de validação e sucesso

## Dependências com outras features
- `shared`
- `perfil`

## Status
pendente
