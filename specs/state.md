# Estado da migração

Fase atual: Fase 1 - mapeamento e geração de specs
Última feature migrada: nenhuma
Features migradas: (nenhuma)
Features pendentes:
1. shared — base comum para todo o app; deve migrar primeiro porque sustenta autenticação, navegação e comunicações entre módulos.
2. login — entrypoint principal do app e dependência da maioria dos fluxos autenticados.
3. home — hub principal de navegação e acesso às áreas do cliente.
4. perfil — contexto do usuário e módulos de dados/alteração de conta.
5. coparticipation — funcionalidade de valor do cliente e parte dos fluxos de benefício em `newfeature/`.
6. guide-authorizer — fluxo de autorização de guia com alta complexidade e muitos recursos/fragmentos.
7. medical-guide-list — consulta de prestadores e estrutura central para o guia médico legado.
8. medical-guide-details — depende da listagem do guia médico e do contexto de consulta.
9. medical-guide-options — apoio ao fluxo do guia médico.
10. own-network — área de geolocalização e rede própria, fortemente ligada a mapa e unidades.
11. units — módulo complementar de localização e lista institucional.
12. map — dependência transversal de localizações e unidades/atendimento.
13. token — fluxo de autenticação/beneficiário em módulo novo (`newfeature/`).
14. schedule-central — fluxo de agendamento com dependência do perfil e da navegação principal.
15. delete-user — ação sensível no perfil, mas com dependência funcional do login e da área de conta.
16. favorites — funcionalidade auxiliar de apoio e navegação do cliente.
17. factor-extractor — módulo secundário com múltiplos componentes visuais e dados processados.
18. notifications — módulo de avisos do usuário, com dependência do home e do perfil.
19. tickets — fluxo de atendimento/solicitação com dependência de navegação e contexto do cliente.
20. health-insurance-photo — fluxo de carteirinha e foto com interação do usuário e acesso ao perfil.
21. forgot-password — recuo do login, necessário para completar autenticação.
22. not-has-password — fluxo alternativo de acesso e criação de senha.
23. edit-password — ação do perfil.
24. edit-phone — ação do perfil.
25. preferences — configuração do usuário, vinculada ao home e ao perfil.
26. informations — conteúdo institucional de apoio.
27. links — conteúdo institucional e links externos.
28. qualification-info — suporte para legenda e contexto de qualificações.
29. income-tax — módulo funcional do pacote `newfeature/`, com dependências secundárias de usuário e home.
Ainda em _legacy/: (a mover na Fase 3)
Decisões técnicas registradas: (nenhuma)
MigrationActivity: não criada
