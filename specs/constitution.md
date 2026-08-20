# Constitution — Migração Android Policlin

Este arquivo vive em `/specs/constitution.md` no repositório do policlin. Ele não muda entre sessões — é lido no início de cada prompt de fase, nunca colado de novo no chat.

## Projetos
- Referência (SCS): `/Users/luandamato/Documents/Projetos/santa-casa-backup`
- Alvo (policlin): `/Users/luandamato/Documents/Projetos/policlin-saude-smith`

## Estrutura de specs
- `/specs/constitution.md` — este arquivo
- `/specs/architecture.md` — arquitetura do SCS identificada na Fase 1
- `/specs/dependencies.md` — auditoria de dependências (Fase 2)
- `/specs/features/<nome>.md` — uma spec por feature (escopo, arquivos, status)
- `/specs/state.md` — estado atual da migração, atualizado a cada etapa concluída e a cada decisão técnica tomada

## Estratégia de organização durante a migração
Antes de qualquer alteração, mova o código fonte atual do policlin para `_legacy/` dentro do projeto, mantendo a estrutura original intacta, comentando se necessário para o projeto compilar. O novo código é construído do zero fora dessa pasta, seguindo `/specs/architecture.md`.

```
policlin-saude-smith/
├── _legacy/                  ← código original intacto, somente leitura
│   ├── java/
│   └── res/                  ← só o que precisa ser reescrito
├── specs/                    ← specs do SDD
└── app/src/main/
    ├── java/                 ← novo código
    └── res/                  ← resources ativos
```

### Regras da `_legacy/`
- NUNCA edite arquivos dentro de `_legacy/` — somente leitura, apenas para consulta
- Resources só vão para `_legacy/res/` se precisarem ser reescritos; senão permanecem em `res/` e são reaproveitados diretamente
- Apague arquivos de `_legacy/` SOMENTE após aprovação explícita
- Ao final da migração completa, `_legacy/` deve estar vazia e será removida

### Resources, XMLs e localização
- Layouts, drawables, strings identificados como necessários de reescrita seguem o mesmo processo: original para `_legacy/res/`, novo em `res/`, remoção de `_legacy/res/` após aprovação
- Quando um resource de string for reescrito, TODOS os seus qualifiers de idioma (`values/`, `values-en/`, `values-es/` etc.) migram e são reescritos juntos — nunca só o `values/` default
- Resources que não precisam de alteração permanecem em `res/`, reaproveitados diretamente

### Assets fora de `res/`
- Arquivos em `assets/`, `raw/` e outras pastas fora de `res/` seguem a mesma regra dos resources: só migram para `_legacy/` se precisarem ser reescritos; senão permanecem e são reaproveitados diretamente

### Ponto de entrada durante a migração
- Imediatamente após mover o código para `_legacy/`, crie uma `MigrationActivity` minimalista como launcher temporário no `AndroidManifest.xml`, apenas para garantir compilação desde a primeira alteração
- Conforme cada feature for migrada, substitua ou remova a `MigrationActivity` e declare a Activity correta como launcher
- Cada nova Activity é declarada no Manifest assim que sua feature for migrada

### AndroidManifest.xml e credenciais
- Permissões, credenciais, configurações de assinatura (keystore), `google-services.json` e chaves de API/SDK permanecem exatamente as mesmas — nunca regenere, normalize ou substitua
- Atualize apenas formato/sintaxe se necessário para compatibilidade com AGP mais recente
- `MigrationActivity` é removida do Manifest assim que a Activity launcher real for migrada

### Versionamento
- `versionCode` e `versionName` NUNCA são alterados durante a migração, mesmo que o formato do `build.gradle` mude

### Build variants / flavors
- Fora de escopo da análise da Fase 1. Se uma feature específica depender de um flavor, trate como decisão pontual dentro da migração dessa feature e registre em `/specs/state.md`

### ProGuard / R8
- Fora de escopo da IA. A validação de regras de ofuscação/minificação é feita manualmente pelo usuário — não gere, edite ou valide ProGuard/R8 em nenhuma fase

### Navegação entre código novo e legado
- Conforme cada feature for migrada, ela entra no fluxo de navegação do app
- Para fluxos ainda não migrados, ao navegar para eles exiba uma tela ou toast "Em construção" em vez de navegar para código legado

### Injeção de dependências
- O projeto não usa DI com conflito entre módulos, mas se durante a migração for identificado conflito ou ambiguidade, tome a decisão técnica mais adequada para manter o processo fluindo, sem necessidade de consulta — e registre a decisão (ver seção "Registro de decisões técnicas")

### Versão do Java
- Atualize de Java 11 para Java 17 (LTS, compatível com AGP moderno e compileSdk/targetSdk atuais)
- Se Java 17 for incompatível com alguma dependência crítica insubstituível, atualize para a LTS mais recente compatível e informe o motivo

### Erros e bloqueios — sem rollback automático
- Se ocorrer um erro em qualquer fase ou feature, tente corrigir dentro do escopo já aprovado
- Se não conseguir corrigir, PARE, alerte claramente no chat descrevendo o erro e o que já foi tentado, e aguarde instrução
- NUNCA reverta arquivos, desfaça commits ou decida sozinho como contornar um bloqueio — isso exige instrução explícita

### Aprovação
- "ok" é aprovação para prosseguir
- Se a mensagem de aprovação contiver apontamento ou correção, resolva antes de avançar
- Toda etapa que altera código ou apaga arquivos de `_legacy/` aguarda aprovação explícita antes da próxima etapa — incluindo a exclusão de `_legacy/` ao final de cada feature, sem exceção
- Em caso de dúvida se uma mensagem é aprovação, pergunte diretamente

### Registro de decisões técnicas
- Toda decisão técnica autônoma (conflito de DI, fallback de versão Java, resolução de ambiguidade, uso pontual de flavor) é registrada em `/specs/state.md` no momento em que é tomada, não apenas ao final da fase

### Commits
- A IA NÃO faz commits. Ao final de cada fase ou feature aprovada, liste os arquivos criados/alterados/removidos para o usuário revisar e commitar manualmente após os testes

## Restrições obrigatórias
- NUNCA altere lógica de negócio, layouts, strings, cores, ícones ou comportamento do app
- NUNCA remova funcionalidades existentes
- `_legacy/` é somente leitura — NUNCA edite arquivos dentro dela
- O projeto DEVE compilar (`assembleDebug`) a partir da primeira alteração e ao final de cada fase/feature
- Shared sempre antes de qualquer feature; uma feature por vez, nunca inicie a próxima sem aprovação explícita
- Apague arquivos de `_legacy/` SOMENTE após aprovação explícita
- Para fluxos ainda não migrados, exiba "Em construção"
- Features sem equivalente no SCS: migre por associação de padrões, sem consulta adicional
- DI: decisão técnica autônoma em caso de conflito, registrada em `/specs/state.md`
- Java 17 como alvo; fallback documentado se incompatível
- Substitua dependências apenas pelas alternativas aprovadas em `/specs/dependencies.md`
- Prefira Kotlin em todo código novo ou refatorado
- Ambiguidade ou risco alto não coberto por estas regras: pare e pergunte antes de agir
- Sem commits automáticos, sem branch automático, sem rollback automático — ver seções acima
