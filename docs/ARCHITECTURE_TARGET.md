ARQUITETURA ALVO E PADRÃO DE ORGANIZAÇÃO
Objetivo

Definir o padrão arquitetural e organizacional utilizado durante a migração do projeto legado para a nova arquitetura.

Este documento é o contrato arquitetural da migração.

As tarefas de migração de features devem seguir estas regras e não devem criar padrões arquiteturais diferentes sem autorização explícita.

1. PRINCÍPIOS GERAIS

A migração deve priorizar:

simplicidade;
baixo acoplamento;
preservação do comportamento existente;
baixo risco;
alterações pequenas e isoladas;
reutilização da infraestrutura já existente;
ausência de abstrações desnecessárias.

Não criar camadas, classes, interfaces, helpers ou abstrações apenas por preferência arquitetural.

Não antecipar reutilização futura.

O código deve permanecer simples e próximo da necessidade real da feature.

2. ESTRUTURA ALVO

Estrutura geral:

com.<empresa>.<app>
├── data
│   └── models
├── domain
│   ├── services
│   └── repositories
├── ui
│   ├── activities
│   │   └── <feature>
│   │       ├── FeatureActivity.kt
│   │       ├── FeatureViewModel.kt
│   │       ├── FeatureAdapter.kt
│   │       └── componentes específicos
│   ├── fragments
│   ├── dialogs
│   └── views
├── util
│   ├── extensions
│   └── helpers
└── <Application>


A estrutura acima representa o padrão geral.

Não é obrigatório que todas as features possuam todos esses arquivos ou diretórios.

3. ORGANIZAÇÃO POR FEATURE

Arquivos específicos de uma feature devem permanecer agrupados dentro da própria feature.

Exemplo:

ui/activities/login/
├── LoginActivity.kt
├── LoginViewModel.kt
├── LoginAdapter.kt
└── componentes específicos


Não mover arquivos específicos para diretórios compartilhados sem necessidade real.

Por padrão, uma classe é considerada específica da feature.

4. CÓDIGO COMPARTILHADO

Uma classe deve ser considerada compartilhada quando for suficientemente abstrata para ser reutilizável em mais de um caso do aplicativo.

Não mover uma classe para uma área compartilhada apenas porque existe possibilidade futura de reutilização.

A reutilização deve ser real ou claramente necessária.

Exemplos:

componente visual utilizado por múltiplas features → ui/views;
dialog utilizado por múltiplas features → ui/dialogs;
helper genérico → util/helpers;
extension genérica → util/extensions.

Adapters específicos permanecem dentro da feature.

Adapters compartilhados somente devem existir quando realmente forem utilizados por múltiplas features.

5. DATA E DOMAIN

A infraestrutura de data e domain já foi migrada e deve ser considerada infraestrutura estabilizada.

Durante a migração de uma feature:

reutilizar os Models existentes;
reutilizar os Repositories existentes;
reutilizar os Services existentes;
reutilizar os contratos existentes;
não recriar infraestrutura que já existe;
não duplicar Repository, Service ou Model;
não reorganizar essas camadas sem necessidade.

Não criar data ou domain específicos para cada feature apenas para seguir uma arquitetura em camadas.

As implementações existentes de Repository e Services devem ser utilizadas conforme já definido no projeto.

6. MVVM

O padrão utilizado para novas features será MVVM.

Fluxo principal:

UI
 ↓
ViewModel
 ↓
Repository / Service
 ↓
Data

Activity / Fragment

Responsabilidades:

renderizar a UI;
capturar eventos do usuário;
observar dados do ViewModel;
atualizar Views;
exibir loading, erro, empty state e feedback visual;
realizar navegação;
manipular componentes que dependem diretamente de Android View/Context/Binding.

Activity/Fragment não deve conter regras de negócio complexas.

ViewModel

Responsabilidades:

coordenar o estado da tela;
executar chamadas aos Repositories/Services;
coordenar operações da tela;
manter dados necessários para a UI;
expor observables para a Activity/Fragment;
executar lógica que não depende diretamente de View, Activity, Fragment ou Binding.

O ViewModel:

não deve referenciar Activity;
não deve referenciar Fragment;
não deve referenciar View;
não deve manipular Views diretamente;
não deve realizar navegação diretamente;
não deve utilizar Binding;
não deve conter referência a Navigator concreto;
não deve receber Context sem justificativa técnica clara.

O ViewModel não deve se tornar simplesmente um novo Presenter contendo toda a lógica do sistema.

Regras de negócio reutilizáveis ou independentes da tela devem permanecer nas camadas compartilhadas apropriadas.

7. ESTADO DA TELA

Não é necessário migrar LiveData existente para StateFlow.

O projeto pode utilizar:

LiveData;
MutableLiveData;
SingleLiveEvent;

quando esses padrões já forem utilizados pelo projeto ou forem adequados à feature.

A prioridade é manter consistência com o projeto existente e evitar migrações tecnológicas paralelas.

Quando necessário, o ViewModel deve expor observables para que a UI reaja às alterações.

Exemplo conceitual:

ViewModel
    ↓
MutableLiveData / SingleLiveEvent
    ↓
Activity / Fragment
    ↓
renderização / feedback / navegação


Loading, sucesso, erro e empty state devem ser tratados de acordo com o padrão já utilizado pela tela/projeto.

Não criar um novo framework de estado.

Não adicionar bibliotecas externas para implementar estado.

8. EVENTOS ÚNICOS

Para eventos que não representam estado persistente da tela, utilizar o padrão já existente no projeto, incluindo SingleLiveEvent quando apropriado.

Exemplos:

Toast;
Dialog;
navegação;
mensagens temporárias;
eventos de ação única.

Se for necessário criar uma função auxiliar para esse comportamento, utilizar os Helpers/Extensions existentes ou criar uma solução simples e nativa.

Não adicionar bibliotecas externas apenas para tratamento de eventos.

9. NAVEGAÇÃO

A navegação pertence à UI.

A Activity/Fragment é responsável por realizar a navegação.

A navegação pode ocorrer:

A. Por ação direta do usuário

Exemplo:

Clique do botão
    ↓
Activity/Fragment
    ↓
navegação


Quando a navegação já é consequência direta do clique, ela deve permanecer na UI.

B. Após sucesso de uma operação

Exemplo:

Activity/Fragment
    ↓
ViewModel
    ↓
Repository / Service
    ↓
API
    ↓
ViewModel atualiza Observable
    ↓
Activity/Fragment observa
    ↓
navegação


Nesse caso, o ViewModel não executa a navegação.

O ViewModel apenas atualiza o observable/evento necessário para a UI reagir.

Não utilizar startActivity, Intent, Navigator concreto ou APIs de navegação diretamente no ViewModel.

10. NAVIGATOR LEGADO

Se a feature possuir um Navigator legado:

identificar todas as responsabilidades dele;
verificar se outras features ainda dependem dele;
migrar a responsabilidade necessária para a UI;
remover o Navigator somente quando não houver mais necessidade dele.

Se uma funcionalidade de navegação depender de outra feature ainda não migrada, não realizar a migração dessa outra feature.

Nesse caso, utilizar temporariamente o comportamento:

Toast: "Em construção"


A implementação temporária deve ficar restrita ao fluxo da feature atualmente migrada.

11. REGRAS DE NEGÓCIO

Não mover automaticamente toda lógica do Presenter para o ViewModel.

Classificar a lógica existente antes da migração:

Lógica de UI

Deve permanecer na Activity/Fragment.

Exemplos:

alterar View;
abrir Dialog;
exibir Toast;
manipular Binding;
navegação;
comportamento visual.
Coordenação da tela

Deve ir para o ViewModel.

Exemplos:

iniciar uma operação;
coordenar chamadas;
atualizar estado;
reagir ao resultado de Repository/Service.
Regra de negócio reutilizável

Deve permanecer ou ser colocada na camada compartilhada apropriada.

Não duplicar regras de negócio dentro do ViewModel.

12. DI / KOIN

O projeto utiliza Koin como mecanismo de DI.

Durante a migração:

manter Koin;
não introduzir outro framework de DI;
utilizar o padrão de ViewModel do Koin já adotado pelo projeto;
registrar o ViewModel no módulo Koin apropriado;
alterar DI somente quando necessário para a feature;
não reorganizar módulos de DI sem necessidade.

Para ViewModels, utilizar o mecanismo nativo de integração do Koin com ViewModel, seguindo a versão e o padrão já presentes no projeto.

Não criar módulos específicos apenas para satisfazer uma separação artificial.

13. LIFECYCLE

O ViewModel deve ser utilizado para preservar o estado e coordenar operações que pertencem à tela e precisam sobreviver à recriação da Activity/Fragment.

A Activity/Fragment deve observar os dados do ViewModel respeitando seu lifecycle.

Não manter referências de View, Activity, Fragment ou Binding dentro do ViewModel.

Não mover para o ViewModel lógica que dependa diretamente do lifecycle ou da View.

Não criar mecanismos próprios de retenção de estado se o ViewModel e os mecanismos existentes do Android já forem suficientes.

A migração deve preservar o comportamento atual da tela durante:

recriação da Activity/Fragment;
rotação;
retorno para a tela;
mudanças de lifecycle.

Não introduzir alterações de comportamento relacionadas ao lifecycle sem necessidade.

14. RXJAVA / COROUTINES

Não migrar RxJava para Coroutines durante a migração de uma feature apenas por preferência arquitetural.

Se a feature já utiliza RxJava:

manter o padrão existente;
adaptar apenas o necessário para integração com o ViewModel;
registrar a necessidade de uma futura migração separada, caso seja relevante.

A migração arquitetural e a migração de tecnologia assíncrona devem permanecer separadas.

15. RENOMEAÇÕES

Renomeações não devem ser realizadas automaticamente.

Se a migração indicar que uma classe, método, propriedade ou arquivo deveria ser renomeado:

interromper a execução antes da alteração e solicitar decisão.

Isso inclui, entre outros:

Activities;
Fragments;
Presenters;
ViewModels;
Models;
métodos;
propriedades;
pacotes;
arquivos.

Não realizar renomeações por preferência estética ou arquitetural.

16. PRESERVAÇÃO DE COMPORTAMENTO

A migração deve preservar o comportamento funcional existente.

Não alterar sem necessidade:

endpoints;
contratos de API;
Models;
parâmetros;
regras existentes;
mensagens;
validações;
loading;
tratamento de erros;
navegação;
ordem das operações;
comportamento de sessão;
comportamento de lifecycle.

Se uma alteração funcional for necessária para concluir a migração, informar antes de realizá-la.

17. BIBLIOTECAS

Não adicionar bibliotecas externas para solucionar problemas que podem ser resolvidos utilizando:

recursos nativos do Android;
recursos já existentes no projeto;
Helpers;
Extensions;
Koin já existente;
padrões já utilizados pelo projeto.

Se uma biblioteca externa parecer necessária:

interromper e solicitar autorização antes de adicioná-la.

18. ESCOPO

A migração deve ser isolada por feature.

Arquivos permitidos por padrão:

Activity da feature;
Fragment da feature;
ViewModel;
Adapter específico;
componentes específicos da feature;
arquivos diretamente relacionados à feature;
configuração de DI estritamente necessária.

Não alterar outras features.

Não reorganizar código global.

Não refatorar infraestrutura compartilhada.

Não migrar outra feature para resolver uma dependência.

Se uma dependência externa à feature for necessária:

se for impeditiva → interromper e informar;
se não for impeditiva → registrar em pendências e continuar.
19. MIGRATION_MAP

O arquivo MIGRATION_MAP contém o mapeamento das features, seus arquivos e dependências.

Ele deve ser utilizado como fonte de contexto para a migração.

Não é necessário analisar novamente todo o projeto a cada feature.

A IA deve utilizar:

MIGRATION_MAP;
este documento;
os arquivos específicos da feature;
os arquivos diretamente necessários para suas dependências.

Não reanalisar indiscriminadamente o projeto inteiro.

20. CRITÉRIO DE COMPARTILHAMENTO

Antes de mover uma classe para uma área compartilhada, verificar se:

ela já é utilizada por mais de uma feature; ou
sua responsabilidade é genuinamente genérica e independente da feature.

Na dúvida, manter dentro da feature.

O padrão é:

feature-specific → dentro da feature
shared → estrutura compartilhada


Nunca o contrário.

21. PRINCÍPIO FINAL

Durante a migração:

Migrar somente o necessário para transformar a feature de MVP/legado para MVVM, preservando comportamento e evitando qualquer refatoração não relacionada.

A arquitetura deve evoluir de forma incremental.

Não transformar a migração em uma reescrita do projeto.
