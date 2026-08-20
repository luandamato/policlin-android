# Migração: Login & Welcome para MVVM

## Status da Migração

✅ **COMPLETADO** - Login e Welcome foram migrados para MVVM com Hilt

## O Que Foi Feito

### 1. Criação de Models (Domain Layer)
**Arquivo:** [domain/model/AuthModels.kt](domain/model/AuthModels.kt)
- `LoginRequest` - Dados enviados para API (register, order, password, firebaseToken, osVersion)
- `LoginResponse` - Resposta da API com token e dados do usuário
- `UserProfile` - Modelo de dados do usuário
- `InsurancePlan` - Planos de seguro associados ao usuário
- `ForgotPasswordRequest` - Request para esqueci a senha
- `PasswordResetRequest` - Request para resetar senha
- `ApiResponse<T>` - Wrapper genérico para respostas da API

### 2. Criação de Repository Interface (Domain Layer)
**Arquivo:** [domain/repository/AuthRepository.kt](domain/repository/AuthRepository.kt)
```kotlin
interface AuthRepository {
    fun login(loginRequest: LoginRequest): Single<LoginResponse>
    fun getCurrentUser(): Single<UserProfile>
    fun hasValidToken(): Single<Boolean>
    fun logout(): Completable
    fun requestPasswordReset(email: String): Single<String>
    fun resetPassword(token: String, newPassword: String): Single<String>
    fun saveToken(token: String): Completable
    fun getToken(): Single<String>
    fun clearAuthData(): Completable
}
```

### 3. Criação de Use Cases (Domain Layer)
**Arquivo:** [domain/usecase/AuthUseCases.kt](domain/usecase/AuthUseCases.kt)
- `LoginUseCase` - Executa login
- `GetCurrentUserUseCase` - Obtém dados do usuário logado
- `CheckValidTokenUseCase` - Verifica se token existe e é válido
- `LogoutUseCase` - Realiza logout
- `ForgotPasswordUseCase` - Solicita reset de senha
- `ResetPasswordUseCase` - Completa reset de senha

### 4. Atualização de NetworkingService (Data Layer)
**Arquivo:** [core/network/NetworkingService.kt](core/network/NetworkingService.kt)

**Endpoints adicionados:**
```kotlin
@POST("auth/login")
fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>

@GET("auth/me")
fun getCurrentUser(@Header("Authorization") token: String): Single<UserProfile>

@POST("auth/logout")
fun logout(@Header("Authorization") token: String): Single<String>

@POST("auth/forgot-password")
fun forgotPassword(@Body request: ForgotPasswordBody): Single<String>

@POST("auth/reset-password")
fun resetPassword(@Body request: ResetPasswordBody): Single<String>
```

### 5. Implementação da Repository (Data Layer)
**Arquivo:** [data/repository/AuthRepositoryImpl.kt](data/repository/AuthRepositoryImpl.kt)

**Características:**
- Comunica com NetworkingService (API)
- Salva/recupera token em SharedPreferences
- Gerencia autenticação local
- Transforma respostas da API em modelos de domínio
- Usa RxJava para operações assíncronas

### 6. Atualização do RepositoryModule (DI)
**Arquivo:** [core/di/RepositoryModule.kt](core/di/RepositoryModule.kt)
```kotlin
@Binds
@Singleton
abstract fun bindAuthRepository(
    impl: AuthRepositoryImpl
): AuthRepository
```

### 7. Implementação do WelcomeActivity
**Arquivo:** [ui/auth/welcome/WelcomeActivity.kt](ui/auth/welcome/WelcomeActivity.kt)

**Features:**
- ViewBinding automático
- Escolha entre "I am a client" ou "I am not a client"
- Navegação para LoginActivity ou Home
- Responde a clicks em links de termos/privacidade

### 8. Implementação do WelcomeViewModel
**Arquivo:** [ui/auth/welcome/WelcomeViewModel.kt](ui/auth/welcome/WelcomeViewModel.kt)

**Responsabilidades:**
- Gerenciar ações de navegação
- Encapsular lógica de welcome
- Fornecer LiveData para observação

### 9. Refactor da LoginActivity para MVVM
**Arquivo:** [ui/auth/login/LoginActivity.kt](ui/auth/login/LoginActivity.kt)

**Mudanças em relação à versão MVP antiga:**
- ❌ Removido: `@Inject lateinit var presenter: LoginPresenter`
- ❌ Removido: `AndroidInjection.inject(this)`
- ❌ Removido: Implementação da interface `LoginView`
- ✅ Adicionado: `private val viewModel: LoginViewModel by viewModels()`
- ✅ Adicionado: `@AndroidEntryPoint` anotação
- ✅ Adicionado: ViewBinding (`ActivityLoginBinding`)
- ✅ Adicionado: `observe()` em LiveData
- ✅ Adicionado: Validação com `AwesomeValidation`
- ✅ Adicionado: Obtenção de Firebase token antes do login

**Features mantidas:**
- Mesmo layout (activity_login.xml) - 100% compatível
- Mesma validação de campos
- Mesmo botão de esconder/mostrar senha
- Mesma lógica de login com Firebase token
- Mesmos links de "Esqueceu senha" e "Não tem senha"

### 10. Implementação do LoginViewModel
**Arquivo:** [ui/auth/login/LoginViewModel.kt](ui/auth/login/LoginViewModel.kt)

**Estados de Login:**
```kotlin
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object CheckingToken : LoginState()
    data class Success(val user: UserProfile) : LoginState()
    data class Error(val message: String) : LoginState()
    object InvalidCredentials : LoginState()
}
```

**LiveData:**
- `loginState` - Estado do login (Idle, Loading, Success, Error)
- `passwordVisibility` - Visibilidade da senha (Hidden, Visible)
- `navigateToHome` - Sinal para navegar após sucesso

**Métodos principais:**
- `checkValidToken()` - Verifica se existe token válido
- `login(register, order, password, firebaseToken)` - Executa login
- `togglePasswordVisibility()` - Alterna visibilidade
- `clearError()` - Limpa mensagens de erro
- `navigationHandled()` - Reseta flag de navegação

### 11. Cópia da LoginActivity antiga para legacy
**Local:** [ui/legacy/login/](ui/legacy/login/)

**Motivo:** Preservar código MVP antigo como referência
**Status:** Funcional mas não será mais usado

## Fluxos de Operação

### Flow 1: Usuário não logado (Novo)
```
App Launch
  ↓
LoginActivity.checkValidToken()
  ↓
CheckValidTokenUseCase (SharedPreferences)
  ↓
Token não encontrado
  ↓
LoginState.Idle (Formulário visível)
  ↓
Usuário entra dados
  ↓
LoginActivity.getFirebaseTokenAndLogin()
  ↓
LoginViewModel.login(register, order, password, token)
  ↓
LoginUseCase.execute()
  ↓
AuthRepository.login(request)
  ↓
NetworkingService.login() [POST /auth/login]
  ↓
API retorna token + user data
  ↓
Repository salva token em SharedPreferences
  ↓
LoginViewModel obtém dados do usuário
  ↓
LoginState.Success(user)
  ↓
Activity observa e navega para Home
```

### Flow 2: Usuário já logado (Sessão válida)
```
App Launch
  ↓
LoginActivity.checkValidToken()
  ↓
CheckValidTokenUseCase (SharedPreferences)
  ↓
Token encontrado e válido
  ↓
GetCurrentUserUseCase.execute()
  ↓
AuthRepository.getCurrentUser() [GET /auth/me + token]
  ↓
API retorna dados do usuário
  ↓
LoginState.Success(user)
  ↓
Activity observa e navega para Home
```

### Flow 3: Alternar visibilidade de senha
```
User clicks eye icon
  ↓
LoginActivity.imgEye.setOnClickListener()
  ↓
LoginViewModel.togglePasswordVisibility()
  ↓
passwordVisibility.value = Visible / Hidden
  ↓
Activity observa mudança
  ↓
Atualiza transformationMethod do EditText
  ↓
Senha fica visível ou escondida
```

## Integração com Aplicação Existente

### AndroidManifest.xml
```xml
<!-- Launcher Activity agora aponta para novo LoginActivity -->
<activity
    android:name=".ui.auth.login.LoginActivity"
    android:screenOrientation="portrait"
    android:windowSoftInputMode="stateHidden"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<!-- Welcome Activity adicionada -->
<activity
    android:name=".ui.auth.welcome.WelcomeActivity"
    android:screenOrientation="portrait"
    android:exported="true" />
```

### Próximas navegações
- LoginActivity → MenuActivity (home) após sucesso
- WelcomeActivity → LoginActivity (se "I am a client")
- WelcomeActivity → MenuActivity (se "I am not a client")

**TODO:** Implementar Navigation Component para navegação segura

## Padrões de Código Utilizados

### 1. Clean Architecture (3 layers)
- **Domain:** Models, Repositories (interfaces), Use Cases
- **Data:** Repository implementations, API/Database access
- **UI:** Activities, ViewModels, Views

### 2. MVVM Pattern
- **Model:** Data classes (LoginRequest, LoginResponse, UserProfile)
- **View:** Activity com ViewBinding
- **ViewModel:** Lógica de apresentação, gerenciamento de state

### 3. Dependency Injection (Hilt)
- `@HiltViewModel` para ViewModels
- `@AndroidEntryPoint` para Activities
- `@Binds` para repository bindings
- `@Provides` para singleton objects (AppModule)

### 4. Reactive Programming (RxJava2)
- `Single<T>` para operações que retornam um único valor
- `Flowable<T>` para streams
- `Completable` para operações sem retorno
- `flatMap` para encadeamento de observables
- `subscribeOn` / `observeOn` para threading
- `CompositeDisposable` para limpeza automática

### 5. ViewBinding
- Binding automático de layouts
- Tipagem segura para views
- Sem mais `findViewById()`
- Sem mais `kotlinx.android.synthetic`

## Vantagens da Nova Arquitetura

✅ **Testabilidade:** ViewModel sem dependência de Context
✅ **Reusabilidade:** Mesmos use cases em diferentes features
✅ **Manutenibilidade:** Separação clara de responsabilidades
✅ **Type Safety:** ViewBinding evita casting errado
✅ **Memory Leaks:** CompositeDisposable no onCleared()
✅ **Reactive:** RxJava para operações assíncronas
✅ **Clean Code:** Sem boilerplate de Presenter/Interface
✅ **Google Recommended:** MVVM é o padrão recomendado pelo Google

## Diferenças em Relação ao MVP Antigo

| Aspecto | MVP (Antigo) | MVVM (Novo) |
|---------|--------------|------------|
| **Injeção** | Presenter injetado | ViewModel via viewModels() |
| **Callbacks** | Interface (LoginView) | LiveData observers |
| **Threading** | Handler + Presenter | RxJava + Schedulers |
| **Ciclo de vida** | Manual em Presenter | Automático via ViewModel |
| **Views** | findViewById() | ViewBinding |
| **Testabilidade** | Difícil (Context) | Fácil (sem Context) |
| **Sintaxe** | Verbose (interface) | Concisa (sealed class) |

## Arquivos Modificados

1. ✅ [domain/model/AuthModels.kt](domain/model/AuthModels.kt) - CRIADO
2. ✅ [domain/repository/AuthRepository.kt](domain/repository/AuthRepository.kt) - CRIADO
3. ✅ [domain/usecase/AuthUseCases.kt](domain/usecase/AuthUseCases.kt) - CRIADO
4. ✅ [data/repository/AuthRepositoryImpl.kt](data/repository/AuthRepositoryImpl.kt) - CRIADO
5. ✅ [core/network/NetworkingService.kt](core/network/NetworkingService.kt) - ATUALIZADO
6. ✅ [core/di/RepositoryModule.kt](core/di/RepositoryModule.kt) - ATUALIZADO
7. ✅ [ui/auth/login/LoginViewModel.kt](ui/auth/login/LoginViewModel.kt) - CRIADO
8. ✅ [ui/auth/login/LoginActivity.kt](ui/auth/login/LoginActivity.kt) - CRIADO
9. ✅ [ui/auth/welcome/WelcomeViewModel.kt](ui/auth/welcome/WelcomeViewModel.kt) - CRIADO
10. ✅ [ui/auth/welcome/WelcomeActivity.kt](ui/auth/welcome/WelcomeActivity.kt) - CRIADO
11. ✅ [ui/legacy/login/](ui/legacy/login/) - COPIADO (backup)
12. ✅ [presentation/src/main/AndroidManifest.xml](presentation/src/main/AndroidManifest.xml) - ATUALIZADO

## Próximos Passos

1. **Implementar outros flows de auth:**
   - Forgot Password Activity/ViewModel
   - Password Reset Activity/ViewModel
   - Biometric login (futura feature)

2. **Implementar outros features mantendo o padrão:**
   - Insurance/Carteirinha (Insurance feature)
   - Financial (Financeiro feature)
   - Medical Guide (Guia Médico feature)
   - Authorization (Autorização feature)

3. **Melhorar navegação:**
   - Integrar Navigation Component
   - Implementar deep links
   - Guardar estado entre navegação

4. **Adicionar interceptadores:**
   - Adicionar Authorization header automaticamente
   - Refresh token automaticamente
   - Tratar erro 401 (token expirado)

5. **Adicionar testes:**
   - Unit tests para ViewModels
   - Integration tests para Repository
   - UI tests para Activities

## Documentação Adicional

Ver [ui/auth/LOGIN_MVVM_README.md](ui/auth/LOGIN_MVVM_README.md) para:
- Explicação detalhada de cada componente
- Exemplos de código
- Como usar como referência para outras features
- Padrões de teste
