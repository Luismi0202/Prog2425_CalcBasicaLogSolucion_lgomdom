package es.prog2425.calclog.app

import es.prog2425.calclog.model.Operadores
import es.prog2425.calclog.service.IServicioLog
import es.prog2425.calclog.ui.IEntradaSalida

/**
 * Controlador principal de la aplicación que gestiona el flujo de ejecución y coordina
 * la interacción entre la interfaz de usuario, la calculadora y el servicio de logs.
 */
class Controlador(
    private val ui: IEntradaSalida,
    private val calculadora: Calculadora,
    private val servicioLog: IServicioLog
) {

    companion object {
        private const val RUTA_POR_DEFECTO = "./log"
    }

    /**
     * Inicia el flujo principal de la aplicación gestionando argumentos, logs
     * y el ciclo de operaciones interactivas con el usuario.
     */
    fun iniciar(args: Array<String>) {
        if (!procesarArgumentos(args)) return

        mostrarInfo(servicioLog.getInfoUltimoLog())

        servicioLog.crearNuevoLog()

        if (args.size == 4) ejecutarCalculoConArgumentos(args)

        ui.pausar("Pulsa ENTER para iniciar la calculadora...")
        ui.limpiarPantalla()

        bucleCalculosUsuario()
    }

    /**
     * Procesa los argumentos recibidos desde la línea de comandos para establecer
     * la ruta del log y validar su formato.
     */
    private fun procesarArgumentos(args: Array<String>): Boolean {
        val ruta = when (args.size) {
            0 -> RUTA_POR_DEFECTO
            1, 4 -> args[0]
            else -> {
                ui.mostrarError("Número de argumentos inválido. Esperado: 0, 1 o 4.")
                return false
            }
        }

        if (servicioLog.crearRutaLog(ruta)) {
            ui.mostrar("Ruta $ruta creada")
        }

        return true
    }

    /**
     * Muestra el contenido del log más reciente o un mensaje si no existe.
     */
    private fun mostrarInfo(lineas: List<String>) {
        if (lineas.isEmpty()) {
            ui.mostrar("No existe información de un log previo!")
        } else {
            ui.mostrar("Contenido del log más reciente:")
            lineas.forEach { ui.mostrar(it) }
        }
    }

    /**
     * Realiza una operación matemática a partir de los argumentos recibidos.
     */
    private fun ejecutarCalculoConArgumentos(args: Array<String>) {
        val numero1 = args[1].replace(',', '.').toDoubleOrNull()
        val operador = Operadores.getOperador(args[2].firstOrNull())
        val numero2 = args[3].replace(',', '.').toDoubleOrNull()

        if (numero1 == null || operador == null || numero2 == null) {
            val msg = "Error en los argumentos: operación no válida."
            ui.mostrarError(msg)
            servicioLog.registrarEntradaLog(msg)
        } else {
            realizarCalculo(numero1, operador, numero2)
        }
    }

    /**
     * Ejecuta un bucle de cálculos interactivos con el usuario hasta que este decida salir.
     */
    private fun bucleCalculosUsuario() {
        do {
            try {
                realizarCalculo()
            } catch (e: InfoCalcException) {
                val mensaje = e.message ?: "Se ha producido un error!"
                ui.mostrarError(mensaje)
                servicioLog.registrarEntradaLog(mensaje)
            }
        } while (ui.preguntar())

        ui.limpiarPantalla()
    }

    /**
     * Ejecuta un cálculo utilizando parámetros explícitos o solicitándolos al usuario.
     * Registra el resultado en el log.
     */
    private fun realizarCalculo(num1: Double? = null, operador: Operadores? = null, num2: Double? = null) {
        val mensaje = if (num1 == null || operador == null || num2 == null) {
            calculadora.pedirCalculo().toString()
        } else {
            calculadora.realizarCalculo(num1, operador, num2).toString()
        }
        ui.mostrar(mensaje)
        servicioLog.registrarEntradaLog(mensaje)
    }
}