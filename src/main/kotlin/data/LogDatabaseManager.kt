package es.prog2425.calclog.data

import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Esta clase la he pensado con la idea de permitir que en la base de datos
 * el log se pueda hacer con un CRUD(create,read,update,delete)
 * es decir, que se puedan actualizar, borrar,insertar
 * y consultar lo datos del log.
 * Se pone la ruta relativa y luego se transforma en absoluta porque te lo trata
 * como dos bases de datos distintas.
 */
class LogDatabaseManager {
    private val connection: Connection

    init {
        // Configurar la conexión con H2
        val rutaRelativa = "./logsDb"
        val rutaAbsoluta = Paths.get(rutaRelativa).toAbsolutePath().toString()
        connection = DriverManager.getConnection("jdbc:h2:$rutaAbsoluta", "sa", "")
        // Crear tabla si no existe
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS Logs (
                id INT AUTO_INCREMENT PRIMARY KEY,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                level VARCHAR(255),
                message TEXT
            );
        """.trimIndent()
        connection.createStatement().execute(createTableSQL)
    }

    // Crear un nuevo log (create)
    fun insertLog(level: String, message: String) {
        val sql = "INSERT INTO Logs (level, message) VALUES (?, ?)"
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        preparedStatement.setString(1, level)
        preparedStatement.setString(2, message)
        preparedStatement.executeUpdate()
    }

    // Leer todos los logs (read)
    fun getLogs(): List<Map<String, Any>> {
        val logs = mutableListOf<Map<String, Any>>()
        val sql = "SELECT * FROM Logs"
        val resultSet: ResultSet = connection.createStatement().executeQuery(sql)
        while (resultSet.next()) {
            logs.add(
                mapOf(
                    "id" to resultSet.getInt("id"),
                    "timestamp" to resultSet.getTimestamp("timestamp"),
                    "level" to resultSet.getString("level"),
                    "message" to resultSet.getString("message")
                )
            )
        }
        return logs
    }

    //Como realmente no vamos a usar toodo del CRUD, comento
    //las de actualizar y eliminar ya que lo único que se hace
    //es consultar al iniciar el programa y añadir al ir haciendo operaciones
    /**
    // Actualizar un log por ID (update)
    fun updateLog(id: Int, level: String, message: String) {
        val sql = "UPDATE Logs SET level = ?, message = ? WHERE id = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        preparedStatement.setString(1, level)
        preparedStatement.setString(2, message)
        preparedStatement.setInt(3, id)
        preparedStatement.executeUpdate()
    }

    // Eliminar un log por ID (delete)
    fun deleteLog(id: Int) {
        val sql = "DELETE FROM Logs WHERE id = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        preparedStatement.setInt(1, id)
        preparedStatement.executeUpdate()
    }*/
}