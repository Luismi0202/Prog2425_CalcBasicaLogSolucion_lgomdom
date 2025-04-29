package es.prog2425.calclog

import es.prog2425.calclog.service.ServicioCalc
import es.prog2425.calclog.app.Controlador
import es.prog2425.calclog.data.LogDatabaseManager
import es.prog2425.calclog.data.RepoLogH2
import es.prog2425.calclog.data.RepoLogTxt
import es.prog2425.calclog.service.ServicioLog
import es.prog2425.calclog.ui.Consola
import es.prog2425.calclog.utils.GestorFichTxt

/**
 * Punto de entrada de la aplicación.
 *
 * Inicializa los componentes necesarios de la arquitectura (UI, repositorio, servicio, lógica de negocio)
 * y delega el control al controlador principal de la aplicación.
 */
fun main(args: Array<String>) {
    val dbManager = LogDatabaseManager()
    val repoLog = RepoLogH2(dbManager)
    Controlador(Consola(), ServicioCalc(), ServicioLog(repoLog)).iniciar(args)
}
