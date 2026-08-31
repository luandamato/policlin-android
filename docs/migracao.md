DEFINIR A ARQUITETURA ALVO E PADRÃO DE ORGANIZAÇÃO

Objetivo:
Definir um padrão arquitetural simples antes de migrar features.

REFERÊNCIA:
Queremos uma organização semelhante a um projeto em que:
- data concentra models/services/repositories, sem armazenamento em banco de dados locais;
- ui/activities/<feature> concentra os arquivos da feature;
- ViewModel fica junto da feature;
- adapters específicos ficam junto da feature;
- ui/views contém componentes visuais realmente compartilhados;
- ui/dialogs contém dialogs compartilhados;
- util contém helpers/extensions genéricos;
- não existem dezenas de camadas artificiais.

ESTRUTURA ALVO:

com.<empresa>.<app>
├── data
│   ├── models
├── domain
│   ├── services
│   ├── repositories
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

IMPORTANTE:
Não obrigar cada feature a ter data/domain próprios. Use camadas compartilhadas quando os dados forem compartilhados.

MVVM:
Activity/Fragment:
- renderiza UI;
- recebe eventos do usuário;
- observa estado;
- navega.

ViewModel:
- coordena estado da tela;
- executa casos de uso/repository;
- não referencia View/Activity/Fragment;
- não manipula views diretamente.

State:
Preferir StateFlow para novo código quando apropriado.
Não migrar todo LiveData/RxJava existente apenas por preferência.

NAVEGAÇÃO:
Não colocar startActivity diretamente no ViewModel.
Se houver Navigator legado, substituí-lo gradualmente por navegação na UI ou uma abstração simples de navegação.

ADAPTERS:
Adapter específico de uma feature fica na pasta da feature.
Adapter compartilhado só deve ir para uma pasta comum quando realmente for usado por múltiplas features.

OBJETIVO:
Produzir um documento/plano antes de editar código:
- arquitetura atual;
- arquitetura alvo;
- mapeamento MVP → MVVM;
- padrão de nomes;
- estratégia de estado;
- estratégia de navegação;
- estratégia de DI;
- convenções de pacotes.

Não realizar a migração de todas as features nesta tarefa.
