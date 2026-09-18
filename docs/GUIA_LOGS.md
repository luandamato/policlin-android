# 📋 Guia: Reduzindo Logs no Logcat

**Data:** 18 de setembro de 2026

---

## 🎯 O Problema

O aplicativo estava gerando **muitos logs** no Logcat:
- Todos os requests/responses de API (completos com headers e body)
- Logs de debug de várias partes do código
- Logs de diferentes bibliotecas
- Dificultava visualizar apenas o que importa

---

## ✅ Solução Implementada

Implementei um **sistema de logs condicional** que:

1. **Em DESENVOLVIMENTO** (`BuildConfig.DEBUG = true`)
   - Logs detalhados de API (request/response completo)
   - Logs de debug habilitados
   - Permite rastrear problemas

2. **Em PRODUÇÃO** (`BuildConfig.DEBUG = false`)
   - **Nenhum log de network** (sem request/response)
   - **Apenas erros são impressos**
   - Reduz drasticamente o ruído no logcat

---

## 🔧 Como Usar

### 1️⃣ Usar LogManager para Logs Manuais

Em vez de:
```kotlin
Log.d("TAG", "Minha mensagem")  // Sempre aparece
```

Use:
```kotlin
import br.com.policlinsaude.utils.LogManager

// Apenas em desenvolvimento
LogManager.d("TAG", "Minha mensagem")

// Sempre aparece (mesmo em produção)
LogManager.e("TAG", "Erro crítico", exception)
```

### 2️⃣ Exemplos Completos

```kotlin
import br.com.policlinsaude.utils.LogManager

class MeuViewModel {
    fun carregarDados() {
        // ✅ Aparece apenas em DEBUG
        LogManager.d("MeuViewModel", "Iniciando carregamento de dados")
        
        try {
            val dados = repository.fetchDados()
            
            // ✅ Usar para debug de estruturas
            LogManager.data("MeuViewModel", "Dados recebidos", dados)
            
            // ✅ Usar seções para organizar logs
            LogManager.section("MeuViewModel", "PROCESSAMENTO INICIADO")
            
            processar(dados)
            
        } catch (e: Exception) {
            // ❌ SEMPRE aparece (mesmo em produção) - use para erros
            LogManager.e("MeuViewModel", "Erro ao carregar dados", e)
        }
    }
}
```

### 3️⃣ Substituir Logs Antigos

**Antes:**
```kotlin
Log.d("PRESENTATION", "Valor de medicalGuideList: ${domain.toString()}")
Log.d("Andre", "valor de cityOptions: ${presentation.cityOptions.toString()}")
Log.d("LOGIN", "ESTAVA HIDED")
```

**Depois:**
```kotlin
LogManager.d("PRESENTATION", "Valor de medicalGuideList: ${domain.toString()}")
LogManager.d("ANDRE", "Valor de cityOptions: ${presentation.cityOptions.toString()}")
LogManager.d("LOGIN", "Status: ESTAVA HIDED")
```

---

## 📊 Métodos Disponíveis

| Método | Descrição | Produção? | Desenvolvimento? |
|--------|-----------|-----------|-----------------|
| `LogManager.d()` | Debug | ❌ Não | ✅ Sim |
| `LogManager.e()` | Erro | ✅ **Sim** | ✅ Sim |
| `LogManager.i()` | Info | ❌ Não | ✅ Sim |
| `LogManager.w()` | Warning | ❌ Não | ✅ Sim |
| `LogManager.v()` | Verbose | ❌ Não | ✅ Sim |
| `LogManager.section()` | Separador visual | ❌ Não | ✅ Sim |
| `LogManager.data()` | Debug de dados | ❌ Não | ✅ Sim |

---

## 🔍 Como Funciona

### Em Desenvolvimento (Build Debug)

```
BuildConfig.DEBUG = true
        ↓
LogManager.d("TAG", "mensagem") → Log.d() → 📱 Aparece no logcat
LogManager.e("TAG", "erro")     → Log.e() → 📱 Aparece no logcat
Retrofit logs                    → Log.d() → 📱 Aparece no logcat
```

### Em Produção (Build Release)

```
BuildConfig.DEBUG = false
        ↓
LogManager.d("TAG", "mensagem") → Ignorado → 📵 NÃO aparece
LogManager.e("TAG", "erro")     → Log.e() → 📱 Aparece no logcat
Retrofit logs                    → Ignorado → 📵 NÃO aparece
```

---

## 📁 Arquivos Modificados

### 1. **RetrofitProvider.kt** (Interceptor de Network)
- Adicionado check de `BuildConfig.DEBUG`
- Em produção: Sem logs de network, apenas erros
- Em desenvolvimento: Logs completos de request/response

### 2. **LogManager.kt** (NOVO - Utilitário Central)
- Classe singleton para gerenciar logs
- Métodos convenientes (d, e, i, w, v)
- Métodos especiais (section, data)
- Todos respeitam `BuildConfig.DEBUG`

---

## 🚀 Próximos Passos

### 1. Substituir Logs Antigos (Opcional mas Recomendado)

Arquivos com muitos `Log.d()` que podem ser substituídos:
- `_legacy/src/main/java/br/com/policlinsaude/mapper/PresentationMedicalGuideListMapper.kt`
- `_legacy/src/main/java/br/com/policlinsaude/mapper/PresentationHealthInsurancePhotoMapper.kt`
- `_legacy/src/main/java/br/com/policlinsaude/login/view/LoginActivity.kt`

Comando para encontrá-los:
```bash
grep -r "Log\.d\(" app/src/main --include="*.kt" | wc -l
```

### 2. Usar LogManager em Novo Código

Ao escrever novo código, sempre use:
```kotlin
LogManager.d("TAG", "mensagem")  // Em vez de Log.d()
LogManager.e("TAG", "erro")      // Em vez de Log.e()
```

---

## ✅ Verificação

Para testar, basta:

1. **Build Debug** (Desenvolvimento)
   ```bash
   ./gradlew :app:installDebug
   # Logcat mostra todos os logs
   ```

2. **Build Release** (Produção)
   ```bash
   ./gradlew :app:assembleRelease
   # Logcat mostra apenas erros
   ```

---

## 💡 Dicas Importantes

### Para Sempre Logar (mesmo em Produção)
```kotlin
// Erros e exceções
LogManager.e("TAG", "Erro crítico", exception)

// ✅ Use apenas para:
// - Exceções não tratadas
// - Erros de rede críticos
// - Falhas de segurança
// - Analytics/Tracking
```

### Para Debug Visual em Desenvolvimento
```kotlin
// Separar seções
LogManager.section("MyTag", "SEÇÃO IMPORTANTE")
LogManager.d("MyTag", "dados 1")
LogManager.d("MyTag", "dados 2")
LogManager.section("MyTag", "FIM DA SEÇÃO")

// Estruturas complexas
LogManager.data("MyTag", "Usuario", usuario)
LogManager.data("MyTag", "Lista itens", items)
```

### NÃO Logar em Produção
```kotlin
// ❌ NUNCA faça isso
if (BuildConfig.DEBUG) {
    Log.d("TAG", "mensagem")  // Redundante! Use LogManager
}

// ✅ Faça assim
LogManager.d("TAG", "mensagem")  // Ele já verifica DEBUG internamente
```

---

## 📈 Benefícios

✅ **Menos Ruído**
- Logcat mais limpo e legível
- Foco apenas no que importa

✅ **Melhor Performance**
- Menos I/O em produção
- Menos consumo de bateria

✅ **Mais Segurança**
- Não expõe dados sensíveis em produção
- Headers, tokens, etc. não são logados

✅ **Debugging Eficiente**
- Em desenvolvimento: todos os logs detalhados
- Em produção: apenas erros críticos

---

## 🔄 Migração Gradual

Não é necessário migrar tudo de uma vez:

1. ✅ Novos códigos usam `LogManager`
2. ✅ Erros críticos existentes foram atualizados
3. ⏳ Código legado pode ser migrado gradualmente
4. ✅ Retrofit já está otimizado

---

## 📝 Resumo

| Situação | Use | Comportamento |
|----------|-----|---------------|
| Log de debug temporário | `LogManager.d()` | Só debug |
| Erro que precisa saber em produção | `LogManager.e()` | Sempre |
| Info de fluxo | `LogManager.i()` | Só debug |
| Aviso importante | `LogManager.w()` | Só debug |
| Debug super detalhado | `LogManager.v()` | Só debug |
| Organizar logs visualmente | `LogManager.section()` | Só debug |
| Logar objeto/estrutura | `LogManager.data()` | Só debug |

---

**Solução Implementada:** 18 de setembro de 2026
