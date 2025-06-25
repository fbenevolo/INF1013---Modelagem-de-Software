package DBConnection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DBConnection {

    /* --------------------------------------------------------
     * Caminhos portáveis (SEM barras “\” fixas)
     * -------------------------------------------------------- */
    private static final Path BASE_DIR   = Paths.get("src", "main", "resources", "data");
    private static final Path DB_FILE    = BASE_DIR.resolve("banco.db");
    private static final Path SCHEMA_SQL = BASE_DIR.resolve("schema.sql");

    private static final String URL = "jdbc:sqlite:" + DB_FILE.toAbsolutePath();

    private static Connection connection = null;

    /* --------------------------------------------------------
     * Conexão única (Singleton simples)
     * -------------------------------------------------------- */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Garante que o driver esteja carregado (opcional a partir do JDBC 4, mas seguro)
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return connection;
    }

    /* --------------------------------------------------------
     * Execução de múltiplos comandos SQL
     * -------------------------------------------------------- */
    public static void executeSqlScript(String sqlScript) {
        try (Statement stmt = getConnection().createStatement()) {
            for (String command : sqlScript.split(";")) {
                if (!command.isBlank()) stmt.execute(command.trim() + ";");
            }
            System.out.println("Schema criado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao executar schema: " + e.getMessage());
        }
    }

    /* --------------------------------------------------------
     * Cria o banco/tabelas se necessário
     * -------------------------------------------------------- */
    public static void initializeDatabaseIfNeeded() {
        try (Connection conn = getConnection();
             Statement  stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("""
                     SELECT name
                       FROM sqlite_master
                      WHERE type='table' AND name='Usuarios'
                    """);

            if (!rs.next()) {                 // Banco está “vazio”
                String schema = Files.readString(SCHEMA_SQL);
                executeSqlScript(schema);
            }
        } catch (Exception e) {
            System.out.println("Erro ao inicializar banco: " + e.getMessage());
        }
    }

    /* -------------------------------------------------------- */
    public static void closeConnection() {
        try { if (connection != null) connection.close(); }
        catch (SQLException e) { System.out.println("Erro ao fechar conexão: " + e.getMessage()); }
    }

    /* -------------------------------------------------------- */
    public static void executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
            stmt.executeUpdate();
        } catch (SQLException e) { System.out.println("Erro ao executar update: " + e.getMessage()); }
    }

    public static ResultSet executeQuery(String sql, Object... params) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Erro ao executar query: " + e.getMessage());
            return null;
        }
    }

    public static void executeQuery(String sql, ResultSetHandler handler, Object... params) {
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
            try (ResultSet rs = stmt.executeQuery()) { handler.handle(rs); }
        } catch (SQLException e) { System.out.println("Erro ao executar query: " + e.getMessage()); }
    }

    /* -------------------------------------------------------- */
    @FunctionalInterface
    public interface ResultSetHandler { void handle(ResultSet rs) throws SQLException; }
}
