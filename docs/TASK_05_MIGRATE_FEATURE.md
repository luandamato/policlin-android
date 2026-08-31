TAREFA 05 — MIGRAR UMA FEATURE ESPECÍFICA PARA MVVM
FEATURE

[SUBSTITUA ESTE TEXTO PELO NOME DA FEATURE]

OBJETIVO

Migrar somente a feature indicada de MVP/arquitetura legada para MVVM, seguindo integralmente o padrão definido em ARCHITECTURE_TARGET.md.

A migração deve preservar o comportamento funcional existente e alterar somente o necessário.

A feature deve terminar a tarefa funcionando sem Presenter/Navigator legado desnecessário.

CONTEXTO

As tarefas anteriores já foram concluídas.

A infraestrutura global do projeto já foi migrada, incluindo, quando aplicável:

Models;
Services;
Repositories;
SessionManager;
infraestrutura compartilhada;
DI existente.

Não recriar ou migrar novamente essa infraestrutura.

O arquivo MIGRATION_MAP contém o mapeamento das features e suas dependências.

Utilize-o como fonte de contexto.

Não é necessário analisar todo o projeto novamente.

REGRA ABSOLUTA — SCOPE LOCK

Esta tarefa deve modificar somente:

arquivos específicos da feature;
Activity/Fragment da feature;
ViewModel da feature;
Adapter específico da feature;
componentes específicos da feature;
arquivos de DI estritamente necessários para registrar o ViewModel;
arquivos diretamente necessários para concluir a migração.

Não:

migrar outras features;
reorganizar outras features;
refatorar infraestrutura global;
alterar Services compartilhados;
alterar Repositories compartilhados;
alterar Models compartilhados;
migrar RxJava para Coroutines;
adicionar bibliotecas;
renomear classes;
reorganizar pacotes fora da feature.
ANTES DE EDITAR

Não editar código imediatamente.

Primeiro analisar a feature.

Consultar:

MIGRATION_MAP;
ARCHITECTURE_TARGET.md;
arquivos específicos da feature;
dependências diretamente utilizadas pela feature.

Identificar:

Activity/Fragment;
Presenter;
Navigator;
interface View;
Repository;
Service;
Models;
UseCases, se existirem;
DI/Koin;
Adapters;
navegação;
observables existentes;
chamadas para API;
regras de negócio;
regras de UI;
componentes compartilhados utilizados;
dependências com outras features;
possíveis referências externas ao Presenter/Navigator.
MAPA OBRIGATÓRIO

Antes da implementação, produzir:

FLUXO ATUAL

View
 ↓
Presenter
 ↓
Repository / Service
 ↓
Data


e:

NAVEGAÇÃO ATUAL

View
 ↓
Navigator
 ↓
Outra tela / outra feature


Também produzir:

DEPENDÊNCIAS

Feature
├── Repository
├── Service
├── Model
├── DI
├── Adapter
├── componentes compartilhados
└── outras features

CLASSIFICAÇÃO DA LÓGICA

Antes de mover código do Presenter, classificar cada responsabilidade.

UI

Permanece na Activity/Fragment:

manipulação de Views;
Binding;
Toast;
Dialog;
navegação;
alterações visuais;
comportamento específico de componentes Android.
COORDENAÇÃO DA TELA

Vai para o ViewModel:

iniciar operações;
chamar Repository/Service;
coordenar fluxo da tela;
armazenar/expor estado;
reagir aos resultados das operações;
atualizar observables.
REGRA DE NEGÓCIO REUTILIZÁVEL

Não mover automaticamente para o ViewModel.

Reutilizar a camada compartilhada existente.

Não criar uma nova camada somente para acomodar a migração.

PLANO DE IMPLEMENTAÇÃO

Depois da análise, definir o plano específico da feature.

O plano deve conter:

1. Activity/Fragment que será migrado
2. ViewModel que será criado
3. Estado/observables necessários
4. Responsabilidades que sairão do Presenter
5. Responsabilidades que permanecerão na UI
6. Responsabilidades que continuarão nas camadas compartilhadas
7. Tratamento de loading
8. Tratamento de sucesso
9. Tratamento de erro
10. Navegação
11. Alterações de DI
12. Presenter
13. Navigator
14. interface View
15. possíveis dependências externas


Não implementar mudanças que não estejam relacionadas a esse plano.

IMPLEMENTAÇÃO
1. Criar ViewModel

Criar:

FeatureViewModel.kt


seguindo o padrão do projeto.

Registrar no Koin utilizando o padrão de ViewModel já existente.

Não criar novo padrão de DI.

2. Definir observables

Utilizar o mecanismo existente no projeto.

Preferência:

MutableLiveData;
LiveData;
SingleLiveEvent;

conforme a necessidade da tela.

Não migrar a feature para StateFlow apenas por preferência.

Não adicionar biblioteca para implementar observables.

3. Migrar lógica do Presenter

Mover para o ViewModel somente:

coordenação da tela;
chamadas de Repository/Service;
processamento necessário para produzir o estado da UI;
lógica independente de View/Activity/Fragment.

Não mover:

acesso a View;
Binding;
Toast;
Dialog;
navegação;
Context sem necessidade;
manipulação direta de componentes Android.

O ViewModel não pode depender da Activity, Fragment ou View.

4. Integrar Activity/Fragment

A Activity/Fragment deve:

observar os observables;
renderizar o estado;
exibir loading;
exibir erro;
exibir empty state;
exibir feedback;
realizar navegação;
capturar eventos do usuário.

Preservar o comportamento existente da tela.

5. NAVEGAÇÃO
Clique do usuário

Se a navegação ocorre diretamente após clique de botão:

Clique
 ↓
Activity/Fragment
 ↓
Navegação


Manter a navegação na UI.

Não passar essa responsabilidade para o ViewModel.

Sucesso de API

Se a navegação ocorre após sucesso de uma requisição:

Activity/Fragment
 ↓
ViewModel
 ↓
Repository / Service
 ↓
API
 ↓
ViewModel atualiza Observable/Event
 ↓
Activity/Fragment observa
 ↓
Navegação


O ViewModel não realiza a navegação.

6. FEATURE AINDA NÃO MIGRADA

Se a feature depender exclusivamente de navegação para outra feature ainda não migrada:

não migrar a outra feature.

No ponto de navegação, utilizar temporariamente:

Toast: "Em construção"


Não alterar outras funcionalidades da feature.

7. PRESERVAÇÃO DE RXJAVA

Se a feature utilizar RxJava:

manter RxJava;
não converter para Coroutines;
não introduzir nova tecnologia assíncrona;
realizar somente adaptações necessárias para integração com o ViewModel.

Uma eventual migração para Coroutines deve ser tratada como tarefa separada.

8. LIFECYCLE

O ViewModel deve ser utilizado de forma compatível com o lifecycle da Activity/Fragment.

Não armazenar no ViewModel:

View;
Activity;
Fragment;
Binding;
referência direta ao contexto sem justificativa.

Preservar o comportamento existente em:

rotação;
recriação;
retorno para a tela;
mudanças de lifecycle.

Não criar mecanismos próprios de lifecycle/state management quando o ViewModel e os mecanismos existentes forem suficientes.

9. PRESERVAR APIs E CONTRATOS

Não alterar:

endpoints;
contratos de API;
parâmetros;
Models compartilhados;
Repository;
Service;
regras externas à feature.

Se alguma dessas alterações for necessária para concluir a migração:

interromper e informar a dependência antes de alterar.

10. RENOMEAÇÕES

Não renomear automaticamente nenhuma classe, método, propriedade, arquivo ou pacote.

Se identificar necessidade de renomeação:

INTERROMPER A EXECUÇÃO E PERGUNTAR QUAL OPÇÃO DEVE SER UTILIZADA.

Não prosseguir com a renomeação por conta própria.

11. REMOÇÃO DO LEGADO

Depois que a UI estiver funcionando através do ViewModel:

verificar se ainda existem referências ao Presenter;
verificar se ainda existem referências ao Navigator;
verificar se ainda existem referências à interface View antiga;
verificar se existem referências em Kotlin;
verificar se existem referências em Java;
verificar XML;
verificar Manifest;
verificar Navigation;
verificar DI/Koin;
verificar reflection ou referências indiretas identificáveis no código.

Somente depois dessas verificações remover classes que comprovadamente não possuem mais uso.

12. PRESERVAÇÃO DO LEGADO EXTERNO

Se Presenter, Navigator ou outra classe da feature ainda possuir dependências externas:

não apagar.

Identificar:

Classe
 ↓
Quem utiliza?
 ↓
Qual dependência?


Se essa dependência impedir a remoção:

interromper se for impeditiva;
caso não seja impeditiva, registrar em pendências.

Não migrar automaticamente a dependência externa.

13. DI / KOIN

Alterar Koin somente se necessário.

Adicionar o ViewModel ao módulo apropriado seguindo o padrão já utilizado no projeto.

Não:

criar novo framework;
criar novo padrão;
reorganizar módulos;
alterar dependências não relacionadas.
14. HELPERS / EXTENSIONS

Se for necessário algum comportamento genérico como:

Toast;
Dialog;
extensão;
helper simples;

utilizar o que já existe.

Se realmente não existir:

criar solução simples;
preferencialmente nativa;
dentro da estrutura util;
somente se o comportamento for suficientemente genérico para ser reutilizado.

Não criar helper específico da feature para uma única chamada sem necessidade.

Não adicionar biblioteca externa.

15. TESTES E VALIDAÇÃO

Não serão criados testes unitários nesta tarefa.

Executar:

build/compile do projeto;
validação da feature;
teste manual dos fluxos principais.

Validar no mínimo:

abertura da tela;
carregamento;
sucesso;
erro;
empty state, quando aplicável;
ações dos principais botões;
chamadas de API;
navegação;
comportamento das dependências ainda legadas;
comportamento após recriação da tela quando aplicável.
16. VERIFICAÇÃO FINAL

Antes de concluir:

Código

Verificar:

ViewModel não possui View/Activity/Fragment/Binding;
ViewModel não navega diretamente;
Activity/Fragment observa os observables;
loading está preservado;
erro está preservado;
sucesso está preservado;
navegação está na UI;
Presenter não é mais necessário;
Navigator não é mais necessário, quando aplicável.
Referências

Pesquisar referências de:

Presenter
Navigator
View interface


em:

.kt;
.java;
.xml;
AndroidManifest.xml;
Navigation;
Koin/DI;
reflection/referências indiretas identificáveis.
Escopo

Confirmar que não foram alteradas outras features ou infraestrutura sem necessidade.

17. CRITÉRIO DE CONCLUSÃO

A tarefa só pode ser considerada concluída quando:

a feature funciona como antes;
a feature está utilizando MVVM;
o ViewModel está integrado;
a UI observa o estado/eventos;
navegação está na UI;
Presenter foi removido quando não houver dependências;
Navigator foi removido quando não houver dependências;
interface View antiga foi removida quando não houver uso;
não existem referências conhecidas ao legado removido;
DI está funcionando;
build foi executado;
fluxos principais foram validados;
nenhuma outra feature foi migrada;
nenhuma dependência externa foi alterada indevidamente.
18. SAÍDA OBRIGATÓRIA

Ao finalizar, retornar:

Arquivos criados

Lista dos arquivos criados.

Arquivos modificados

Lista dos arquivos modificados.

Arquivos removidos

Lista dos arquivos removidos e justificativa.

Fluxo antigo
View
 ↓
Presenter
 ↓
Repository / Service
 ↓
Data


e, quando aplicável:

View
 ↓
Navigator
 ↓
Outra tela

Fluxo novo
UI
 ↓
ViewModel
 ↓
Repository / Service
 ↓
Data


e:

UI
 ↓
Navegação


ou:

ViewModel
 ↓
Observable/Event
 ↓
UI
 ↓
Navegação


conforme o caso.

Validação

Informar:

build executado;
fluxos testados;
resultado.
Pendências

Listar qualquer dependência, melhoria ou problema que não tenha impedido a conclusão.

Não esconder problemas encontrados.

19. REGRA FINAL

Esta tarefa é uma migração arquitetural de uma única feature, não uma refatoração geral.

Sempre preferir:

menor alteração necessária
+
preservação de comportamento
+
menor escopo possível
+
reutilização da infraestrutura existente


Se algo estiver fora dessas regras ou houver dúvida arquitetural:

interromper antes de modificar e solicitar decisão.
