package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String BASE_PATH = "demo\\src\\main\\resources\\data";
    private static final String URL = String.format("jdbc:sqlite:%s\\banco.db", BASE_PATH);
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return connection;
    }

    public static void executeSqlScript(String sqlScript) {
        try (Statement stmt = getConnection().createStatement()) {
            String[] commands = sqlScript.split(";");
            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command.trim() + ";");
                }
            }
            System.out.println("Schema criado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao executar schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeDatabaseIfNeeded() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Usuarios'");
            if (!rs.next()) {
                // Banco está vazio, executar schema.sql
                String schema = new String(java.nio.file.Files.readAllBytes(
                        java.nio.file.Paths.get(String.format("%s\\schema.sql", BASE_PATH))));
                executeSqlScript(schema);
            }
        } catch (Exception e) {
            System.out.println("Erro ao inicializar banco: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    public static void executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao executar update: " + e.getMessage());
        }
    }

    public static ResultSet executeQuery(String sql, Object... params) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Erro ao executar query: " + e.getMessage());
            return null;
        }
    }

    public static void executeQuery(String sql, ResultSetHandler handler, Object... params) {
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                handler.handle(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar query: " + e.getMessage());
        }
    }

    @FunctionalInterface
    public interface ResultSetHandler {
        void handle(ResultSet rs) throws SQLException;
    }
}
