# Esqueci minha senha

## Descrição resumida
Fluxo de recuperação de senha por e-mail e acompanhamento do processo de recuperação.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.forgotPassword`
- `br.com.policlinsaude.forgotPassword.view.ForgotPasswordActivity.kt`
- `br.com.policlinsaude.forgotPassword.view.ForgotPasswordView.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_forgot_password.xml`, `res/drawable/ic_email.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com instruções de recuperação, mensagens de confirmação e valiações

## Dependências com outras features
- `shared`
- `login`

## Status
pendente
