package es.prog2425.calclog.data

import es.prog2425.calclog.model.Calculo

class RepoLogH2(private val dbManager: LogDatabaseManager) : IRepoLog {
    override var ruta: String? = null
    override var logActual: String? = null

    override fun crearRutaLog(): Boolean {
        // No se necesita en H2
        return true
    }

    override fun crearNuevoLog(): String {
        return "H2 Database - Nuevo log creado"
    }

    override fun getContenidoUltimoLog(): List<String> {
        return dbManager.getLogs().map { it.toString() }
    }

    override fun registrarEntrada(mensaje: String) {
        dbManager.insertLog("INFO", mensaje)
    }

    override fun registrarEntrada(calculo: Calculo) {
        dbManager.insertLog("CALC", calculo.toString())
    }
}