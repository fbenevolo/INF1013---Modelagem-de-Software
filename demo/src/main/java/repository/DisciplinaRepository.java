package repository;

import DBConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Disciplina;

public class DisciplinaRepository {

    // CREATE
    public void inserir(Disciplina disciplina) {
        String sql = "INSERT INTO Disciplinas (codigo, nome, numeroDeCreditos) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getNumeroDeCreditos());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    disciplina.setId(keys.getLong(1));
                }
            }

            // Insere os pré-requisitos após obter o ID da disciplina
            if (disciplina.getPreRequisitos() != null && !disciplina.getPreRequisitos().isEmpty()) {
                inserirPreRequisitos(disciplina, conn);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao inserir disciplina: " + e.getMessage(), e);
        }
    }

    private void inserirPreRequisitos(Disciplina disciplina, Connection conn) throws SQLException {
        String sql = "INSERT INTO Disciplina_PreRequisitos (disciplina_id, prerequisito_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Disciplina pre : disciplina.getPreRequisitos()) {
                stmt.setLong(1, disciplina.getId());
                stmt.setLong(2, pre.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public Disciplina buscarPorId(long id) {
        String sql = "SELECT * FROM Disciplinas WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Disciplina disciplina = new Disciplina(
                            rs.getLong("id"),
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("numeroDeCreditos"));
                    // Carrega os pré-requisitos usando a mesma conexão
                    disciplina.setPreRequisitos(buscarPreRequisitos(disciplina.getId(), conn));
                    return disciplina;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Disciplina buscarPorNome(String nome) {
        String sql = "SELECT * FROM Disciplinas WHERE nome = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Disciplina disciplina = new Disciplina(
                            rs.getLong("id"),
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("numeroDeCreditos"));
                    // Carrega os pré-requisitos usando a mesma conexão
                    disciplina.setPreRequisitos(buscarPreRequisitos(disciplina.getId(), conn));
                    return disciplina;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Set<Disciplina> buscarPreRequisitos(long disciplinaId, Connection conn) throws SQLException {
        Set<Disciplina> preRequisitos = new HashSet<>();
        String sql = """
                SELECT d.* FROM Disciplinas d
                JOIN Disciplina_PreRequisitos pr ON d.id = pr.prerequisito_id
                WHERE pr.disciplina_id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, disciplinaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Disciplina pre = new Disciplina(
                            rs.getLong("id"),
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("numeroDeCreditos"));
                    preRequisitos.add(pre);
                }
            }
        }
        return preRequisitos;
    }

    // READ (listar todas)
    public List<Disciplina> listar() {
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT * FROM Disciplinas";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Disciplina d = new Disciplina(
                        rs.getLong("id"),
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getInt("numeroDeCreditos"));
                d.setPreRequisitos(buscarPreRequisitos(d.getId(), conn));
                lista.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // UPDATE
    public void atualizar(Disciplina disciplina) {
        String sql = "UPDATE Disciplinas SET codigo = ?, nome = ?, numeroDeCreditos = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            try {
                stmt.setString(1, disciplina.getCodigo());
                stmt.setString(2, disciplina.getNome());
                stmt.setInt(3, disciplina.getNumeroDeCreditos());
                stmt.setLong(4, disciplina.getId());
                stmt.executeUpdate();

                // Limpa e reinsere os pré-requisitos para garantir consistência
                limparPreRequisitos(disciplina.getId(), conn);
                if (disciplina.getPreRequisitos() != null && !disciplina.getPreRequisitos().isEmpty()) {
                    inserirPreRequisitos(disciplina, conn);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e; // Lança a exceção para quem chamou o método
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void limparPreRequisitos(long disciplinaId, Connection conn) throws SQLException {
        String sql = "DELETE FROM Disciplina_PreRequisitos WHERE disciplina_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, disciplinaId);
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deletar(long id) {
        String sql = "DELETE FROM Disciplinas WHERE id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Primeiro remove as dependências
                limparPreRequisitos(id, conn);
                // Depois remove a disciplina
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, id);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}