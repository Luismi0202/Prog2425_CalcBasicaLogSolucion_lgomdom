package es.prog2425.calclog.data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class LogDatabaseManager {
    private val connection: Connection

    init {
        // Configurar la conexión con H2
        connection = DriverManager.getConnection("jdbc:h2:./logsDb", "sa", "")
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

    // Crear un nuevo log
    fun insertLog(level: String, message: String) {
        val sql = "INSERT INTO Logs (level, message) VALUES (?, ?)"
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        preparedStatement.setString(1, level)
        preparedStatement.setString(2, message)
        preparedStatement.executeUpdate()
    }

    // Leer todos los logs
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

    // Actualizar un log por ID
    fun updateLog(id: Int, level: String, message: String) {
        val sql = "UPDATE Logs SET level = ?, message = ? WHERE id = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        preparedStatement.setString(1, level)
        preparedStatement.setString(2, message)
        preparedStatement.setInt(3, id)
        preparedStatement.executeUpdate()
    }

    // Eliminar un log por ID
    fun deleteLog(id: Int) {
        val sql = "DELETE FROM Logs WHERE id = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        preparedStatement.setInt(1, id)
        preparedStatement.executeUpdate()
    }
}