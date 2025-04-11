package es.prog2425.calclog.app

import es.prog2425.calclog.model.Calculo
import es.prog2425.calclog.model.Operadores
import es.prog2425.calclog.ui.IEntradaSalida

/**
 * Clase responsable de gestionar las operaciones aritméticas
 * y la interacción con el usuario para recoger los datos del cálculo.
 */
class Calculadora(private val ui: IEntradaSalida) {

    /**
     * Solicita un número decimal al usuario y lanza una excepción si la entrada no es válida.
     */
    private fun pedirNumero(msj: String, msjError: String = "Número no válido!"): Double {
        return ui.pedirDouble(msj) ?: throw InfoCalcException(msjError)
    }

    /**
     * Solicita al usuario un operador aritmético válido y lo convierte en una instancia de [Operadores].
     */
    private fun pedirOperador(): Operadores {
        val simbolo = ui.pedirInfo("Introduce el operador (+, -, x, /): ").firstOrNull()
        return Operadores.getOperador(simbolo) ?: throw InfoCalcException("El operador no es válido!")
    }

    /**
     * Realiza la operación matemática según el operador recibido.
     */
    private fun realizarOperacion(num1: Double, operador: Operadores, num2: Double): Double {
        return when (operador) {
            Operadores.SUMA -> num1 + num2
            Operadores.RESTA -> num1 - num2
            Operadores.MULTIPLICACION -> num1 * num2
            Operadores.DIVISION -> num1 / num2
        }
    }

    /**
     * Solicita al usuario los datos necesarios para realizar un cálculo y retorna un objeto [Calculo].
     */
    fun pedirCalculo(): Calculo {
        val numero1 = pedirNumero("Introduce el primer número: ", "El primer número no es válido!")
        val operador = pedirOperador()
        val numero2 = pedirNumero("Introduce el segundo número: ", "El segundo número no es válido!")
        val resultado = realizarOperacion(numero1, operador, numero2)
        return Calculo(numero1, numero2, operador, resultado)
    }

    /**
     * Recibe los datos de entrada de un cálculo y retorna el objeto [Calculo] con el resultado.
     */
    fun realizarCalculo(num1: Double, operador: Operadores, num2: Double): Calculo {
        return Calculo(num1, num2, operador, realizarOperacion(num1, operador, num2))
    }

}