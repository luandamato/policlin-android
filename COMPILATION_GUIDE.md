# 🔨 Guia de Compilação e Execução

## Pré-requisitos

- **Android Studio** 2024.1+
- **JDK 17+**
- **Android SDK 34+**
- **Build Tools 34.0.0**

## Passo 1: Compilação

### Usando Gradle Wrapper (Recomendado)
```bash
cd /Users/luandamato/Documents/Projetos/policlin-saude-smith

# Limpar builds anteriores
./gradlew clean

# Compilar
./gradlew build

# Ou para debug
./gradlew assembleDebug
```

### Resolver Dependências
```bash
# Se houver erro de dependências
./gradlew build --refresh-dependencies

# Sincronizar AndroidStudio
./gradlew sync
```

## Passo 2: Configurações Necessárias

### local.properties
Certifique-se que existe e aponta para o SDK:
```properties
sdk.dir=/Users/luandamato/Library/Android/sdk
ndk.dir=/Users/luandamato/Library/Android/sdk/ndk/25.1.8937393
```

### gradle.properties
```properties
org.gradle.jvmargs=-Xmx4096m
android.useAndroidX=true
android.enableJetifier=true
```

## Passo 3: Executar no Emulador

### Opção 1: Usando Gradle
```bash
./gradlew installDebug
```

### Opção 2: Usando Android Studio
1. Abrir projeto
2. Esperar indexação
3. Clicar em "Run" ou ▶️
4. Selecionar emulador/dispositivo

### Opção 3: Usando adb
```bash
# Instalar APK
adb install -r presentation/build/outputs/apk/debug/presentation-debug.apk

# Executar
adb shell am start -n br.com.policlinsaude/br.com.policlinsaude.ui.auth.login.LoginActivity
```

## Passo 4: Testar Login

### Fluxo 1: Novo Usuário
1. App abre em LoginActivity
2. Preencher campos:
   - **Matrícula:** (seu número de matrícula)
   - **Ordem:** (0-99)
   - **Senha:** (sua senha)
3. Clicar "ENTRAR"
4. Sistema obtém Firebase token
5. Faz POST para `/auth/login`
6. Se sucesso, navega para Home

### Fluxo 2: Usuário com Token
1. App abre em LoginActivity
2. Sistema checa token em SharedPreferences
3. Se válido, faz GET `/auth/me`
4. Se sucesso, navega direto para Home

### Fluxo 3: Welcome Screen
1. Clicar "I am a client" → Go to Login
2. Clicar "I am not a client" → Go to Home (guest)

## Erros Comuns e Soluções

### ❌ Erro: "Unresolved dependency kotlin-stdlib-jdk8:1.5.1"
**Solução:**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### ❌ Erro: "Cannot find symbol NetworkingService"
**Solução:**
- Verificar que imports estão corretos
- Fazer rebuild do projeto
- Limpar cache: `./gradlew clean`

### ❌ Erro: "Could not find Hilt"
**Solução:**
- Verificar que `classpath "com.google.dagger:hilt-android-gradle-plugin:2.50"` está em `build.gradle`
- Fazer rebuild

### ❌ Erro: "Cannot resolve symbol ActivityLoginBinding"
**Solução:**
- Limpar projeto: `./gradlew clean`
- Reconstruir: `./gradlew build`
- ViewBinding é gerado automaticamente

### ❌ Erro: "Android resource linking failed"
**Solução:**
```bash
./gradlew clean
rm -rf presentation/build
./gradlew build
```

### ❌ Erro: "API 31+ requires exported Activities"
**Solução:**
- Já foi adicionado `android:exported="true"` no manifest
- Se persiste, fazer rebuild

## Validar Estrutura

### Verificar Pastas Criadas
```bash
# Deve existir:
find presentation/src/main/java/br/com/policlinsaude/ui -type d | head -20

# Output esperado:
# ui/
# ui/auth/
# ui/auth/login/
# ui/auth/welcome/
# ui/legacy/
# ui/legacy/login/
# ui/insurance/
# ui/financial/
# ... etc
```

### Verificar Arquivos Criados
```bash
# Login MVVM
ls -la presentation/src/main/java/br/com/policlinsaude/ui/auth/login/
# Esperado: LoginActivity.kt, LoginViewModel.kt

# Models
ls -la presentation/src/main/java/br/com/policlinsaude/domain/model/
# Esperado: AuthModels.kt

# Repository
ls -la presentation/src/main/java/br/com/policlinsaude/data/repository/
# Esperado: AuthRepositoryImpl.kt
```

## Build Variants

### Debug
```bash
./gradlew assembleDebug
# Output: presentation/build/outputs/apk/debug/presentation-debug.apk
```

### Release
```bash
./gradlew assembleRelease
# Output: presentation/build/outputs/apk/release/presentation-release.apk
```

### Bundle (Google Play)
```bash
./gradlew bundleRelease
# Output: presentation/build/outputs/bundle/release/presentation-release.aab
```

## Verificação de Compilação

### Verificar Classes Geradas
```bash
# ViewBinding
find presentation/build/generated -name "*Binding.java"

# Hilt
find presentation/build/generated -path "*/hilt/*" -name "*.java"
```

### Verificar APK Contents
```bash
# Listar classes compiladas
unzip -l presentation/build/outputs/apk/debug/presentation-debug.apk | grep "classes.dex"

# Listar arquivos
unzip -l presentation/build/outputs/apk/debug/presentation-debug.apk | head -50
```

## Debug

### Ativar Logs
```bash
# Build com verbose
./gradlew build --debug

# Gradle logs
./gradlew build --info

# Stack trace completo
./gradlew build --stacktrace
```

### Debuggar no Android Studio
1. Abrir Android Studio
2. Colocar breakpoint (clique na linha)
3. Clicar "Debug" (🐞)
4. App para no breakpoint
5. Inspeccionar variáveis

### Logcat
```bash
# Ver logs do app
adb logcat | grep policlinsaude

# Ver apenas errors
adb logcat *:E | grep policlinsaude

# Com filtro
adb logcat -s policlinsaude
```

## Performance

### Otimizar Build
```bash
# Parallelizar
./gradlew build --parallel

# Daemon
./gradlew --daemon build

# Incremental
./gradlew build --build-cache
```

### Análise de Build
```bash
# Ver tempo por task
./gradlew build --profile

# Gera um relatório HTML em build/reports/profile/
```

## Testes

### Executar Testes Locais
```bash
./gradlew testDebugUnitTest
```

### Executar Testes Instrumentalizados
```bash
./gradlew connectedAndroidTest
```

### Cobertura de Testes
```bash
./gradlew testDebugUnitTest --info
```

## Troubleshooting

### Projeto não sincroniza
```bash
# Invalidar cache
./gradlew --stop

# Limpar
rm -rf .gradle
rm -rf build
rm -rf presentation/build

# Reconstruir
./gradlew build
```

### Android Studio não reconhece classes
```bash
# File → Invalidate Caches
# Ou via terminal:
rm -rf ~/.gradle/caches/
./gradlew clean
./gradlew build
```

### Problema com kotlin-stdlib
```bash
# Atualizar dependências
./gradlew build --refresh-dependencies

# Se persistir:
rm -rf ~/.gradle/caches/modules-2/
./gradlew clean build
```

## Verificação Final

### Antes de Commitar
```bash
# 1. Limpar
./gradlew clean

# 2. Compilar
./gradlew build

# 3. Verificar erros
./gradlew lint

# 4. Testar
./gradlew testDebugUnitTest

# Se tudo passou ✅
```

## Environment Variables (Opcional)

```bash
# ~/.zshrc ou ~/.bash_profile

# Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Android SDK
export ANDROID_SDK_ROOT=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools

# Gradle
export GRADLE_USER_HOME=$HOME/.gradle
export GRADLE_OPTS="-Xmx4096m"

# Kotlin
export KOTLINC_JVM_TARGET=17
```

## CI/CD (Para Futuro)

### GitHub Actions
```yaml
name: Build
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: ./gradlew build
```

### Local Pre-commit Hook
```bash
#!/bin/bash
# .git/hooks/pre-commit

./gradlew clean build
if [ $? -ne 0 ]; then
  echo "Build failed!"
  exit 1
fi
```

## Resources Úteis

- [Google Architecture Components](https://developer.android.com/topic/architecture)
- [Hilt Documentation](https://dagger.dev/hilt/)
- [Retrofit Guide](https://square.github.io/retrofit/)
- [RxJava2 Guide](http://reactivex.io/)
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding)

---

**Dúvidas?** Verificar documentação principal: [README_IMPLEMENTATION.md](README_IMPLEMENTATION.md)

**Status:** ✅ Pronto para compilação
