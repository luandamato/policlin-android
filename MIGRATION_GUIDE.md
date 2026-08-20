# Guia de Migração para MVVM com Hilt

## Resumo das mudanças realizadas

### 1. **Atualização de Dependências** ✅
- Gradle: 6.7.1 → 8.5
- AGP (Android Gradle Plugin): 3.6.1 → 8.1.3
- Kotlin: 1.5.1 → 1.9.20
- AndroidX Libraries: Versões 2024
- **DI Migration**: Dagger2 → **Hilt** (simplifica muito!)

### 2. **Estrutura de Módulo Único** ✅
```
presentation/src/main/java/br/com/policlinsaude/
├── core/                    # Camada de infraestrutura
│   ├── di/                 # Módulos Hilt e Injeção de dependência
│   ├── network/            # Retrofit e NetworkingService
│   └── database/           # Room Database
├── data/                   # Camada de dados
│   ├── repository/         # Implementações dos repositórios
│   └── datasource/         # Fontes de dados (Local, Remote)
├── domain/                 # Camada de domínio
│   ├── model/             # Modelos de domínio (dados puros)
│   └── usecase/           # Casos de uso
├── ui/                     # Camada de apresentação
│   ├── base/              # Classes base (ViewModel, Fragment, Activity)
│   ├── feature/           # Funcionalidades (user, medical-guide, etc)
│   └── util/              # Utilitários e extensões
└── PoliclinApplication.kt  # Application com @HiltAndroidApp
```

### 3. **Padrão MVVM Implementado** ✅

#### Componentes Principais:

**ViewModel** (Gerencia estado da UI e dados):
```kotlin
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    
    val userProfile: LiveData<UserDomain> = MutableLiveData()
    val isLoading: LiveData<Boolean> = MutableLiveData()
    val errorMessage: LiveData<String> = MutableLiveData()
}
```

**Fragment/Activity** (Apenas apresentação):
```kotlin
@AndroidEntryPoint
class UserProfileFragment : BaseFragment() {
    private val viewModel: UserProfileViewModel by viewModels()
    
    override fun onViewCreated(...) {
        setupObservers()
    }
}
```

**Repository** (Abstrai fonte de dados):
```kotlin
interface UserRepository {
    fun getUserProfile(token: String): Flowable<UserDomain>
}

class UserRepositoryImpl @Inject constructor(
    private val networkingService: NetworkingService
) : UserRepository { ... }
```

### 4. **Injeção de Dependência com Hilt** ✅

Benefícios em relação ao Dagger2:
- ✨ **Mais simples**: Menos anotações e setup
- ⚡ **Mais rápido**: Hilt otimiza o tempo de compilação
- 🎯 **Android-first**: Integrado com ciclo de vida Android

```kotlin
// Antes (Dagger2):
@Component(modules = [AppModule::class])
interface AppComponent { ... }

// Agora (Hilt):
@HiltAndroidApp
class PoliclinApplication : Application()

// Em qualquer lugar:
@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private val viewModel: UserProfileViewModel by viewModels()
}
```

---

## Próximos Passos para Completar a Migração

### 1. **Migrar Código Existente**

Para cada Feature (módulo antigo):
- [ ] Transferir código da `presentation/` para `ui/feature/`
- [ ] Transferir código da `domain/` para `domain/`
- [ ] Transferir código da `data/` para `data/`

Exemplo:
```
Antigo:
presentation/ → br.com.policlinsaude.login.view.LoginActivity
              → br.com.policlinsaude.login.presenter.LoginPresenter

Novo:
ui/feature/auth/ → ui.feature.auth.LoginActivity
                 → ui.feature.auth.LoginViewModel
```

### 2. **Converter Presenters → ViewModels**

```kotlin
// Antes:
class LoginPresenter @Inject constructor(
    private val repository: AuthRepository
) {
    fun onLoginClicked(email: String, password: String) {
        // callback hell
    }
}

// Depois:
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    val loginSuccess: LiveData<Boolean> = MutableLiveData()
    
    fun login(email: String, password: String) {
        // Usar Coroutines ou Reactive (Flowable)
    }
}
```

### 3. **Substituir RxJava por Coroutines (Opcional)**

Para um código mais moderno, considere:

```kotlin
// Antigo (RxJava):
viewModel.loadUser()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(...)

// Novo (Coroutines):
viewModel.viewModelScope.launch {
    try {
        val user = withContext(Dispatchers.IO) {
            userRepository.getUser()
        }
        userProfile.value = user
    } catch (e: Exception) {
        errorMessage.value = e.message
    }
}
```

### 4. **Atualizar AndroidManifest.xml**

✅ Já feito! Alteramos para usar `PoliclinApplication`

### 5. **Criar Layouts com Data Binding** (Opcional)

Modernize seus layouts:

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="viewModel"
            type="br.com.policlinsaude.ui.feature.user.UserProfileViewModel" />
    </data>
    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        
        <TextView
            android:text="@{viewModel.userProfile.name}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
            
    </LinearLayout>
</layout>
```

### 6. **Remover Módulos Antigos**

Depois de migrar todo o código:
- [ ] Deletar `domain/`
- [ ] Deletar `data/`
- [ ] Deletar `newfeature/`
- [ ] Atualizar `settings.gradle` (já feito para `:presentation`)

---

## Exemplos de Implementação por Feature

### Feature de Login

```kotlin
// domain/usecase/LoginUseCase.kt
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginParams, TokenDomain> {
    override fun execute(params: LoginParams?): Flowable<TokenDomain> {
        return params?.let {
            authRepository.login(it.email, it.password)
        } ?: Flowable.error(Exception("Params required"))
    }
}

// ui/feature/auth/LoginViewModel.kt
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    
    fun login(email: String, password: String) {
        disposables.add(
            loginUseCase.execute(LoginParams(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(...)
        )
    }
}

// ui/feature/auth/LoginActivity.kt
@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            viewModel.login("email", "password")
        }
        
        viewModel.loginSuccess.observe(this) { success ->
            if (success) startActivity(Intent(...))
        }
    }
}
```

---

## Checklist Final

### Antes de fazer merge/commit:

- [ ] Projeto compila sem erros
- [ ] Gradle sync bem-sucedido
- [ ] Nenhuma dependência conflitante
- [ ] AndroidManifest.xml atualizado
- [ ] Hilt encontra todos os injetáveis
- [ ] Testes unitários passam
- [ ] Testes de integração passam
- [ ] BuildVariants compilam (debug e release)

---

## Recursos Úteis

- **Hilt Documentation**: https://developer.android.com/training/dependency-injection/hilt-android
- **MVVM Architecture**: https://developer.android.com/jetpack/guide
- **Lifecycle**: https://developer.android.com/jetpack/androidx/releases/lifecycle
- **Coroutines**: https://developer.android.com/kotlin/coroutines

---

## Suporte

Em caso de dúvidas durante a migração:
1. Verifique se a anotação `@HiltAndroidApp` está em `PoliclinApplication`
2. Certifique-se de que todas as classes têm `@AndroidEntryPoint` ou herdam de `BaseViewModel`
3. Limpe o build: `./gradlew clean`
4. Invalide caches: No Android Studio, vá em File → Invalidate Caches

---

**Data**: 6 de maio de 2026
**Versão**: 1.0
