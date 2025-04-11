package es.prog2425.calclog

import es.prog2425.calclog.service.ServicioCalc
import es.prog2425.calclog.app.Controlador
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

    val repoLog = RepoLogTxt(GestorFichTxt())
    Controlador(Consola(), ServicioCalc(), ServicioLog(repoLog)).iniciar(args)

    /*
    O también instanciando en variables locales... es lo mismo al fin y al cabo.

    val consola = Consola()
    val gestorFicheros = GestorFichText()
    val repoLog = RepoLogTxt(gestorFicheros)
    val servicioLog = ServicioLog(repoLog)
    val calculadora = ServicioCalc()
    val controlador = Controlador(consola, calculadora, servicioLog)

    controlador.iniciar(args)
    */
}
