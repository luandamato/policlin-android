# Login & Welcome Feature - MVVM Architecture

## Visão Geral

Esta é a implementação do fluxo de Login e Welcome usando MVVM com Hilt DI e RxJava.

## Estrutura de Pastas

```
ui/auth/
├── login/
│   ├── LoginActivity.kt          # UI - ViewBinding, sem Presenter
│   └── LoginViewModel.kt         # ViewModel - lógica de login
└── welcome/
    ├── WelcomeActivity.kt        # UI - ViewBinding
    └── WelcomeViewModel.kt       # ViewModel - lógica de welcome

domain/
├── model/
│   └── AuthModels.kt            # LoginRequest, LoginResponse, UserProfile, etc
├── repository/
│   └── AuthRepository.kt        # Interface para operações de auth
└── usecase/
    └── AuthUseCases.kt          # LoginUseCase, GetCurrentUserUseCase, etc

data/
└── repository/
    └── AuthRepositoryImpl.kt     # Implementação com API + SharedPreferences

core/
├── network/
│   └── NetworkingService.kt     # Endpoints Retrofit para auth
└── di/
    └── RepositoryModule.kt      # Binding de AuthRepository
```

## Fluxo de Dados (MVVM)

### Sem dados salvos (Novo usuário)
1. **App Launch** → WelcomeActivity é a launcher activity
2. **User clicks "I am a client"** → Navigate to LoginActivity
3. **User enters credentials** → LoginActivity coleta dados
4. **Click Login** → LoginViewModel executa LoginUseCase
5. **LoginUseCase** → Chama AuthRepository.login()
6. **AuthRepositoryImpl** → Faz POST request via NetworkingService
7. **API retorna LoginResponse** → Repository salva token em SharedPreferences
8. **Success** → ViewModel atualiza state → Activity observa e navega para Home

### Com dados salvos (Já logado)
1. **App Launch** → LoginActivity (launcher) chama `checkValidToken()`
2. **LoginViewModel.checkValidToken()** → Executa CheckValidTokenUseCase
3. **CheckValidTokenUseCase** → Verifica SharedPreferences
4. **Token válido** → Chama GetCurrentUserUseCase
5. **GetCurrentUserUseCase** → GET /auth/me com token
6. **Success** → LoginState muda para Success
7. **Activity observa** → Navega direto para Home

## Componentes Principais

### 1. LoginActivity (UI Layer)
```kotlin
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>()
```
- **Responsabilidades:**
  - Inflater layout usando ViewBinding
  - Coleta input do usuário (register, order, password)
  - Observa ViewModel state
  - Mostra loading/erro/sucesso
  - Valida formulário (AwesomeValidation)
  - Obtém Firebase token antes de fazer login

- **Diferenças da versão antiga (Presenter MVP):**
  - Sem Presenter injected
  - ViewModel injetado via `viewModels()`
  - Usa LiveData observers em vez de interface callback
  - Sem métodos `override fun showLoading()` - feito via state observation

### 2. LoginViewModel (Presentation Logic)
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkValidTokenUseCase: CheckValidTokenUseCase
) : BaseViewModel()
```
- **Responsabilidades:**
  - Gerenciar estado de login (Idle, Loading, Success, Error, InvalidCredentials)
  - Executar login via use case
  - Gerenciar estado de visibilidade de senha
  - Controlar navegação (via LiveData)
  - Gerenciar disposables RxJava

- **Estados:**
  - `LoginState.Idle` - Tela pronta para input
  - `LoginState.CheckingToken` - Verificando token existente
  - `LoginState.Loading` - Requisição em progresso
  - `LoginState.Success` - Login bem-sucedido
  - `LoginState.Error` - Erro na requisição
  - `LoginState.InvalidCredentials` - Validação local falhou

- **LiveData:**
  - `loginState` - Observado pela Activity para atualizar UI
  - `passwordVisibility` - Observado para mostrar/esconder senha
  - `navigateToHome` - Observado para navegar após sucesso

### 3. LoginRequest / LoginResponse (Models)
```kotlin
data class LoginRequest(
    val register: String,
    val order: String,
    val password: String,
    val firebaseToken: String,
    val osVersion: String
)

data class LoginResponse(
    val token: String,
    val refreshToken: String?,
    val user: UserProfile?
)
```
- **LoginRequest:** Enviado para API POST /auth/login
- **LoginResponse:** Recebido da API com token e dados do usuário

### 4. AuthRepository Interface (Domain Layer)
```kotlin
interface AuthRepository {
    fun login(loginRequest: LoginRequest): Single<LoginResponse>
    fun getCurrentUser(): Single<UserProfile>
    fun hasValidToken(): Single<Boolean>
    fun logout(): Completable
    fun saveToken(token: String): Completable
    fun getToken(): Single<String>
    fun clearAuthData(): Completable
    // ... mais métodos
}
```
- **Define "O QUE" precisa ser feito**, não "COMO"
- Implementação real em AuthRepositoryImpl

### 5. AuthRepositoryImpl (Data Layer)
```kotlin
class AuthRepositoryImpl @Inject constructor(
    private val networkingService: NetworkingService,
    private val context: Context
) : AuthRepository
```
- **Responsabilidades:**
  - Chamar NetworkingService (API)
  - Salvar/recuperar token em SharedPreferences
  - Transformar respostas da API em modelos do domain
  - Schedular com RxJava (subscribeOn, observeOn)

- **Token Management:**
  - Salva token após sucesso do login: `preferences.putString(KEY_TOKEN, token)`
  - Recupera token para requisições autenticadas: `getToken().flatMap { token -> ... }`
  - Limpa token no logout: `preferences.remove(KEY_TOKEN)`

### 6. Use Cases
```kotlin
// LoginUseCase
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(loginRequest: LoginRequest): Single<LoginResponse>
}

// GetCurrentUserUseCase
class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(): Single<UserProfile>
}

// CheckValidTokenUseCase
class CheckValidTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(): Single<Boolean>
}
```
- **Uma classe por ação/operação**
- **Injeta repository** e expõe via `execute()`
- **Encapsula lógica de negócio** complexa

### 7. NetworkingService (Network Layer)
```kotlin
interface NetworkingService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @GET("auth/me")
    fun getCurrentUser(@Header("Authorization") token: String): Single<UserProfile>

    @POST("auth/logout")
    fun logout(@Header("Authorization") token: String): Single<String>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordBody): Single<String>
}
```
- **Define endpoints Retrofit**
- Usa @Body para enviar JSON
- Usa @Header para adicionar Authorization Bearer token
- Retorna Single<T> (não Flowable) para operações únicas

## Injeção de Dependências (Hilt)

### Binding de AuthRepository
```kotlin
// core/di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
```
- Quando LoginViewModel pede `AuthRepository`, Hilt injeta `AuthRepositoryImpl`
- AuthRepositoryImpl recebe `NetworkingService` e `Context` via construtor
- NetworkingService e Context são providenciados por AppModule

### Injeção em LoginViewModel
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkValidTokenUseCase: CheckValidTokenUseCase
) : BaseViewModel()
```
- `@HiltViewModel` marca o ViewModel para injeção
- Use cases são injetados automaticamente
- Use cases injetam repositories que são bindados no RepositoryModule

## RxJava Management

### No BaseViewModel
```kotlin
open class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
```
- Todo ViewModel estende BaseViewModel
- Todas as subscriptions RxJava são adicionadas: `disposables.add(...)`
- Quando ViewModel é destruído, todas as subscriptions são limpas automaticamente
- **Previne memory leaks!**

### No LoginViewModel
```kotlin
disposables.add(
    loginUseCase.execute(loginRequest)
        .flatMap { _ -> getCurrentUserUseCase.execute() }
        .subscribeOn(Schedulers.io())           // Executa em thread background
        .observeOn(AndroidSchedulers.mainThread()) // Resultado volta pra UI thread
        .subscribeBy(
            onSuccess = { user ->
                _loginState.value = LoginState.Success(user)
            },
            onError = { error ->
                _loginState.value = LoginState.Error(error.message.orEmpty())
            }
        )
)
```
- `flatMap` encadeia duas operações
- `subscribeOn(Schedulers.io())` - executa requisição em thread de I/O
- `observeOn(AndroidSchedulers.mainThread())` - resultado volta na UI thread
- `subscribeBy` é alias para `subscribe` com named parameters

## ViewBinding

### Na Activity
```kotlin
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityLoginBinding.inflate(inflater)
}
```
- Estende `BaseActivity<ActivityLoginBinding>`
- Implementa `getViewBinding()` que retorna binding
- BaseActivity cuida de `setContentView(binding.root)`
- Acessar views: `binding.buttonEnter`, `binding.editTextRegister`, etc
- **Sem mais `findViewById()` ou `kotlinx.android.synthetic`!**

## Migração da versão MVP (antiga)

### Antes (MVP com Presenter)
```kotlin
class LoginActivity : BaseActivity(), LoginView {
    @Inject lateinit var presenter: LoginPresenter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        presenter.checkHasToken()
    }
    
    override fun showLoginLoading() {
        login_progressbar.visibility = View.VISIBLE
    }
}
```

### Agora (MVVM)
```kotlin
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkValidToken()
    }
    
    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when(state) {
                is LoginViewModel.LoginState.Loading -> {
                    binding.login_progressbar.visibility = View.VISIBLE
                }
            }
        }
    }
}
```

**Principais mudanças:**
- Sem `@Inject lateinit var presenter`
- Sem `AndroidInjection.inject(this)`
- Sem callbacks de interface (`LoginView`)
- Usa `@AndroidEntryPoint` + `by viewModels()`
- Usa `observe()` em vez de implementar interface
- Sem `findViewById()` - usa ViewBinding

## Testabilidade

### Unit Test do ViewModel
```kotlin
@Test
fun testLogin_Success() {
    val request = LoginRequest("123", "01", "pass", "token", "12.0")
    
    // Mock use case
    whenever(loginUseCase.execute(request))
        .thenReturn(Single.just(loginResponse))
    
    // Execute
    viewModel.login("123", "01", "pass", "token")
    
    // Assert
    assertEquals(LoginViewModel.LoginState.Success, viewModel.loginState.value)
    assertEquals(true, viewModel.navigateToHome.value)
}
```
- ViewModel não depende de Activity/Context
- Fácil de mockar dependências
- Usa LiveData::getOrAwaitValue() para assertions
- Sem Presenter, sem AsyncTask, sem Handler - tudo testável!

## Endpoints Configurados

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/login` | Login com credentials |
| GET | `/auth/me` | Obter dados do usuário |
| POST | `/auth/logout` | Logout (limpar sessão) |
| POST | `/auth/forgot-password` | Solicitar reset de senha |
| POST | `/auth/reset-password` | Completar reset de senha |

**Base URL:** `https://api.policlinsaude.com.br/`

**Headers:**
- `Authorization: Bearer {token}` - Para requisições autenticadas
- `Content-Type: application/json` - Automático (Gson + Retrofit)

## Como Usar como Referência

Esta feature é um exemplo completo para implementar outras features. Para adicionar nova feature (ex: Insurance/Carteirinha):

1. **Criar Models** em `domain/model/InsuranceModels.kt`
2. **Criar Interface Repository** em `domain/repository/InsuranceRepository.kt`
3. **Criar Use Cases** em `domain/usecase/InsuranceUseCases.kt`
4. **Criar RepositoryImpl** em `data/repository/InsuranceRepositoryImpl.kt`
5. **Criar ViewModel** em `ui/insurance/InsuranceViewModel.kt`
6. **Criar Activity/Fragment** em `ui/insurance/InsuranceActivity.kt` estendendo BaseActivity
7. **Adicionar endpoints** em `core/network/NetworkingService.kt`
8. **Adicionar binding** em `core/di/RepositoryModule.kt` com @Binds

Siga o mesmo padrão de clean architecture com 3 camadas:
- **Domain:** Models, Repositories (interfaces), Use Cases
- **Data:** Repository implementations
- **UI:** Activities, ViewModels, ViewBinding

## Próximos Passos

- [ ] Navegação entre Login e Welcome com Navigation Component
- [ ] Forgot Password flow completo
- [ ] Password reset com token
- [ ] Refresh token automaticamente
- [ ] Interceptor para adicionar Authorization header
- [ ] Tratamento de erro 401 (token expirado)
- [ ] Unit tests para ViewModel
- [ ] UI tests para Activities
