package repository;

import DBConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Disciplina;
import model.Professor;

public class ProfessorRepository {

    // CREATE
    public void inserir(Professor professor) {
        String sqlUsuario = "INSERT INTO Usuarios (nome, email, senha) VALUES (?, ?, ?)";
        String sqlProfessor = "INSERT INTO Professores (id, matriculaProfessor) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmtUsuario.setString(1, professor.getNome());
                stmtUsuario.setString(2, professor.getEmail());
                stmtUsuario.setString(3, professor.getSenha());
                stmtUsuario.executeUpdate();

                ResultSet keys = stmtUsuario.getGeneratedKeys();
                if (keys.next()) {
                    long idUsuario = keys.getLong(1);
                    professor.setId(idUsuario);

                    try (PreparedStatement stmtProfessor = conn.prepareStatement(sqlProfessor)) {
                        stmtProfessor.setLong(1, idUsuario);
                        stmtProfessor.setString(2, professor.getMatriculaProfessor());
                        stmtProfessor.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (por ID)
    public Professor buscarPorId(long id) {
        String sql = """
                SELECT u.id, u.nome, u.email, u.senha, p.matriculaProfessor
                FROM Usuarios u JOIN Professores p ON u.id = p.id
                WHERE u.id = ?
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Professor(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("matriculaProfessor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Professor buscarPorEmailESenha(String email, String senha) {
        String sql = """
                SELECT u.id, u.nome, u.email, u.senha, p.matriculaProfessor
                FROM Usuarios u JOIN Professores p ON u.id = p.id
                WHERE u.email = ? AND u.senha = ?
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Professor(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("matriculaProfessor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ (listar todos) - Adicionado para consistência
    public List<Professor> listar() {
        List<Professor> lista = new ArrayList<>();
        String sql = """
                SELECT u.id, u.nome, u.email, u.senha, p.matriculaProfessor
                FROM Usuarios u JOIN Professores p ON u.id = p.id
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Professor p = new Professor(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("matriculaProfessor"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Professor buscarPorNome(String nome) {
        String sql = """
                SELECT * FROM Usuarios u JOIN Professores p ON u.id = p.id
                WHERE u.nome = ?
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Professor(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("matriculaProfessor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Disciplina> buscarDisciplinasProfessor(String nomeProfessor) {
        List<Disciplina> disciplinas = new ArrayList<>();

        String sql = """
            SELECT DISTINCT d.id, d.codigo, d.nome, d.numeroDeCreditos
            FROM Disciplinas d
            JOIN Turmas t ON t.disciplina_id = d.id
            JOIN Professores p ON t.professor_id = p.id
            JOIN Usuarios u ON p.id = u.id
            WHERE u.nome = ?
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina(
                        rs.getLong("id"),
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getInt("numeroDeCreditos")
                    );
                    disciplinas.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disciplinas;
    }

}