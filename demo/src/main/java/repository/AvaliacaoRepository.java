package repository;

import DBConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Avaliacao;
import model.Estudante;
import model.Tag;
import model.Turma;

public class AvaliacaoRepository {

    // Dependências para carregar objetos aninhados
    private final EstudanteRepository estudanteRepository;
    private final TurmaRepository turmaRepository;

    public AvaliacaoRepository() {
        this.estudanteRepository = new EstudanteRepository();
        this.turmaRepository = new TurmaRepository();
    }

    // CREATE
    public void inserir(Avaliacao avaliacao) {
        String sql = """
                INSERT INTO Avaliacoes (nota, comentario, data, titulo, estudante_id, turma_id, tag)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setFloat(1, avaliacao.getNota());
            stmt.setString(2, avaliacao.getComentario());
            stmt.setString(3, avaliacao.getData().toString()); // ISO 8601
            stmt.setString(4, avaliacao.getTitulo());
            stmt.setLong(5, avaliacao.getEstudante().getId());
            stmt.setLong(6, avaliacao.getTurma().getId());
            stmt.setString(7, avaliacao.getTags().name());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    avaliacao.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (por ID)
    public Avaliacao buscarPorId(long id) {
        String sql = "SELECT * FROM Avaliacoes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long estudanteId = rs.getLong("estudante_id");
                    long turmaId = rs.getLong("turma_id");

                    // CORREÇÃO: Busca os objetos reais usando seus repositórios
                    Estudante estudante = estudanteRepository.buscarPorId(estudanteId);
                    Turma turma = turmaRepository.buscarPorId(turmaId);
                    Tag tag = Tag.valueOf(rs.getString("tag"));

                    return new Avaliacao(
                            rs.getLong("id"),
                            rs.getFloat("nota"),
                            rs.getString("comentario"),
                            LocalDate.parse(rs.getString("data")),
                            rs.getString("titulo"),
                            estudante,
                            turma,
                            tag);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Avaliacao> listar() {
        List<Avaliacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Avaliacoes";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long estudanteId = rs.getLong("estudante_id");
                long turmaId = rs.getLong("turma_id");

                // CORREÇÃO: Busca os objetos reais usando seus repositórios
                Estudante estudante = estudanteRepository.buscarPorId(estudanteId);
                Turma turma = turmaRepository.buscarPorId(turmaId);
                Tag tag = Tag.valueOf(rs.getString("tag"));

                Avaliacao a = new Avaliacao(
                        rs.getLong("id"),
                        rs.getFloat("nota"),
                        rs.getString("comentario"),
                        LocalDate.parse(rs.getString("data")),
                        rs.getString("titulo"),
                        estudante,
                        turma,
                        tag);
                lista.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Avaliacao> buscarPorEstudante(long estudanteId) {
        List<Avaliacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Avaliacoes WHERE estudante_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, estudanteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long turmaId = rs.getLong("turma_id");

                    // CORREÇÃO: Busca os objetos reais usando seus repositórios
                    Estudante estudante = estudanteRepository.buscarPorId(estudanteId);
                    Turma turma = turmaRepository.buscarPorId(turmaId);
                    Tag tag = Tag.valueOf(rs.getString("tag"));

                    Avaliacao a = new Avaliacao(
                            rs.getLong("id"),
                            rs.getFloat("nota"),
                            rs.getString("comentario"),
                            LocalDate.parse(rs.getString("data")),
                            rs.getString("titulo"),
                            estudante,
                            turma,
                            tag);
                    lista.add(a);
                }
                return lista;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Avaliacao> buscarPorDisciplina(Optional<Long> disciplinaId, Optional<String> nomeDisciplina) {
        List<Avaliacao> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT a.* FROM Avaliacoes a INNER JOIN Turmas t ON a.turma_id = t.id");
        sql.append(" INNER JOIN Disciplinas d ON t.disciplina_id = d.id");

        if (disciplinaId.isPresent()) {
            sql.append(" WHERE d.id = ?");
        } else if (nomeDisciplina.isPresent()) {
            sql.append(" WHERE d.nome = ?");
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (disciplinaId.isPresent()) {
                stmt.setLong(1, disciplinaId.get());
            } else if (nomeDisciplina.isPresent()) {
                stmt.setString(1, nomeDisciplina.get());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long estudanteId = rs.getLong("estudante_id");
                    long turmaId = rs.getLong("turma_id");

                    // CORREÇÃO: Busca os objetos reais usando seus repositórios
                    Estudante estudante = estudanteRepository.buscarPorId(estudanteId);
                    Turma turma = turmaRepository.buscarPorId(turmaId);
                    Tag tag = Tag.valueOf(rs.getString("tag"));

                    Avaliacao a = new Avaliacao(
                            rs.getLong("id"),
                            rs.getFloat("nota"),
                            rs.getString("comentario"),
                            LocalDate.parse(rs.getString("data")),
                            rs.getString("titulo"),
                            estudante,
                            turma,
                            tag);
                    lista.add(a);
                }
                return lista;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // UPDATE
    public void atualizar(Avaliacao avaliacao) {
        String sql = """
                UPDATE Avaliacoes
                SET nota = ?, comentario = ?, data = ?, titulo = ?, estudante_id = ?, turma_id = ?, tag = ?
                WHERE id = ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, avaliacao.getNota());
            stmt.setString(2, avaliacao.getComentario());
            stmt.setString(3, avaliacao.getData().toString());
            stmt.setString(4, avaliacao.getTitulo());
            stmt.setLong(5, avaliacao.getEstudante().getId());
            stmt.setLong(6, avaliacao.getTurma().getId());
            stmt.setString(7, avaliacao.getTags().name());
            stmt.setLong(8, avaliacao.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletar(long id) {
        String sql = "DELETE FROM Avaliacoes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}