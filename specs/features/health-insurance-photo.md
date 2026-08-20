# Carteirinha / Foto do convênio

## Descrição resumida
Fluxo de visualização e atualização da foto/carteirinha do cliente, com zoom e upload/edição.

## Categoria
secundária

## Pacotes/arquivos envolvidos no policlin atual
- `br.com.policlinsaude.healthInsurancePhoto`
- `br.com.policlinsaude.healthInsurancePhoto.HealthInsurancePhotoActivity.kt`
- `br.com.policlinsaude.healthInsurancePhoto.HealthInsurancePhotoView.kt`
- `br.com.policlinsaude.healthInsurancePhoto.ZoomImage.kt`

## Resources a migrar para `_legacy/res/` vs. reaproveitados diretamente
- Reaproveitados diretamente: `res/layout/activity_health_insurance_photo.xml`, `res/drawable/health_insurance.png`, `res/drawable/carteirinha_verso.png`, `res/drawable/ic_share.xml`
- Para `_legacy/res/` se reescrita: `res/values/strings.xml` com textos de instrução e erro de carregamento; layouts auxiliares de zoom e seleção de imagem

## Dependências com outras features
- `shared`
- `perfil`
- `home`

## Status
pendente
