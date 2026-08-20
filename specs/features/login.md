# Login

## Descrição resumida
Fluxo de autenticação do cliente, entrada no app e acesso às áreas protegidas.

## Categoria
prioritária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.login`
- `br.com.policlinsaude.login.view.LoginActivity.kt`
- `br.com.policlinsaude.login.view.LoginView.kt`
- `br.com.policlinsaude.notHasPassword`
- `br.com.policlinsaude.forgotPassword`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_login.xml`, `res/layout/fragment_create_password.xml`, `res/drawable/login_header.png`, `res/drawable/login_header_new.png`, `res/drawable/ic_email.xml`, `res/drawable/ic_senha.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` (textos de login, termos, autenticação), itens específicos de layout que precisarem ser refatorados para nova navegação

## Dependências com outras features
- `shared`
- `forgot-password`
- `not-has-password`
- `home`

## Status
pendente
