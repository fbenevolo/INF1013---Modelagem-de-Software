package repository;

import DBConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Estudante;

public class EstudanteRepository {

    // CREATE
    public void inserir(Estudante estudante) {
        String sqlUsuario = "INSERT INTO Usuarios (nome, email, senha) VALUES (?, ?, ?)";
        String sqlEstudante = "INSERT INTO Estudantes (id, matriculaEstudante) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmtUsuario.setString(1, estudante.getNome());
                stmtUsuario.setString(2, estudante.getEmail());
                stmtUsuario.setString(3, estudante.getSenha());
                stmtUsuario.executeUpdate();

                try (ResultSet keys = stmtUsuario.getGeneratedKeys()) {
                    if (keys.next()) {
                        long idUsuario = keys.getLong(1);
                        estudante.setId(idUsuario);

                        try (PreparedStatement stmtEstudante = conn.prepareStatement(sqlEstudante)) {
                            stmtEstudante.setLong(1, idUsuario);
                            stmtEstudante.setString(2, estudante.getMatriculaEstudante());
                            stmtEstudante.executeUpdate();
                        }
                    }
                }
                conn.commit(); // Confirma a transação
            } catch (SQLException e) {
                conn.rollback(); // Desfaz a transação em caso de erro
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (por ID)
    public Estudante buscarPorId(long id) {
        String sql = """
                SELECT u.id, u.nome, u.email, u.senha, e.matriculaEstudante
                FROM Usuarios u JOIN Estudantes e ON u.id = e.id
                WHERE u.id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estudante(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("matriculaEstudante"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ (listar todos)
    public List<Estudante> listar() {
        List<Estudante> lista = new ArrayList<>();
        String sql = """
                SELECT u.id, u.nome, u.email, u.senha, e.matriculaEstudante
                FROM Usuarios u JOIN Estudantes e ON u.id = e.id
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estudante e = new Estudante(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("matriculaEstudante"));
                lista.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Estudante buscarPorEmailESenha(String email, String senha) {
        String sql = """
                SELECT u.id, u.nome, u.email, u.senha, e.matriculaEstudante
                FROM Usuarios u JOIN Estudantes e ON u.id = e.id
                WHERE u.email = ? AND u.senha = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estudante(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("matriculaEstudante"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void atualizar(Estudante estudante) {
        String sqlUsuario = "UPDATE Usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        String sqlEstudante = "UPDATE Estudantes SET matriculaEstudante = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                    PreparedStatement stmtEstudante = conn.prepareStatement(sqlEstudante)) {

                stmtUsuario.setString(1, estudante.getNome());
                stmtUsuario.setString(2, estudante.getEmail());
                stmtUsuario.setString(3, estudante.getSenha());
                stmtUsuario.setLong(4, estudante.getId());
                stmtUsuario.executeUpdate();

                stmtEstudante.setString(1, estudante.getMatriculaEstudante());
                stmtEstudante.setLong(2, estudante.getId());
                stmtEstudante.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletar(long id) {
        // A ordem é importante devido às chaves estrangeiras (FK)
        String sqlEstudante = "DELETE FROM Estudantes WHERE id = ?";
        String sqlUsuario = "DELETE FROM Usuarios WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtEstudante = conn.prepareStatement(sqlEstudante);
                    PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {

                stmtEstudante.setLong(1, id);
                stmtEstudante.executeUpdate();

                stmtUsuario.setLong(1, id);
                stmtUsuario.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}