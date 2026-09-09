# Prompt reutilizable — Migración de una feature a MVVM

> Cómo usar: copia el bloque de abajo, reemplaza `[FEATURE]` por el nombre de la
> feature (ej: `home`, `favorites`, `perfil`) y colócalo como prompt de la tarea.
> Es un resumen ejecutivo; las reglas detalladas viven en los `.md` que se
> referencian y deben ser respetadas.

---

## PROMPT

```
Migra la feature "[FEATURE]" del legado a la nueva arquitectura MVVM,
siguiendo estrictamente los documentos del proyecto:

1. docs/MIGRATION_MAP.md      -> mapa de la feature, endpoints, dependencias, orden
2. docs/ARCHITECTURE_TARGET.md-> contrato arquitectural (MVVM, Koin, LiveData/events, navegación)
3. docs/TASK_05_MIGRATE_FEATURE.md -> procedimiento detallado (análisis, flujo, validación, saída)
4. docs/LOGIN_MIGRATION.md    -> EJEMPLO ya hecho (login) para seguir como referencia
5. _legacy/                   -> código fuente antiguo (leer, NO editar)

REGLAS ESENCIALES:
- Scope lock: tocar solo archivos de la feature + DI estrictamente necesario.
- Reutilizar infraestrutura ya migrada (data/models, data/services/AppService,
  data/repositories/AppRepository, data/local/SessionManager, util/*, ui/dialogs,
  ui/views) — NO duplicar ni reorganizar.
- No migrar RxJava->Coroutines solo por preferencia; no añadir librerías.
- No renombrar clases por preferencia estética; si hace falta, interrumpir y preguntar.
- ViewModel: coordina estado y llama a Repository/Service; NO referencia View/Activity/
  Fragment/Binding; NO navega; no usa Navigator concreto.
- Activity/Fragment: renderiza, captura eventos, observa el estado, muestra
  loading/error/empty, y realiza la navegación.
- Estado: usar LiveData/MutableLiveData/SingleLiveEvent (consistente con el proyecto).
- Navegación: en la UI. Si el destino no está migrado: Toast "Em construção".
- DI: Koin, registrar el ViewModel en di/AppModules.kt con viewModel { ... }.
- Preservar comportamiento funcional: endpoints, mensajes, validaciones, loading,
  errores, orden de operaciones, sesión, lifecycle.
- Si algo queda fuera de estas reglas o hay duda arquitectural: DETENER y preguntar.

ANTES de editar (análisis, sin código):
- FLUXO ACTUAL: View -> Presenter -> Repository/Service -> Data
- NAVEGAÇÃO ATUAL: View -> Navigator -> outra tela
- MAPA ANTES: fluxo e dependências da feature (según MIGRATION_MAP + código em _legacy)

DESPUÉS (verificación):
- Compilar: ./gradlew :app:assembleDebug
- Confirmar que Presenter/Navigator/interface View de la feature desaparecen.
- Reportar: arquivos creados/modificados/removidos, fluxo antigo vs novo, build,
  validación manual, pendências.
```

---

## Notas
- La primera migración de referencia fue `login` — ver `docs/LOGIN_MIGRATION.md`
  para ver el formato de saída y las tendências (base URL, checkHasToken, etc.).
- Al iniciar una feature, confirmar en `docs/MIGRATION_MAP.md` su **orden sugerido**
  y si depende de otra feature aún no migrada.