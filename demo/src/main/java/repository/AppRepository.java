package repository;

import DBConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Avaliacao;
import model.Disciplina;
import model.Estudante;
import model.Professor;
import model.Tag;
import model.Turma;

public class AppRepository {
    
    // CREATE
    public void inserirAvaliacao(Avaliacao avaliacao) {
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
    public Avaliacao buscarAvaliacaoPorId(long id) {
        String sql = "SELECT * FROM Avaliacoes WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Você vai precisar carregar estudante e turma de outro lugar
                Estudante estudante = new Estudante(0, "Ze das couves", "bla", "Bla", "Bla"); 
                Disciplina d = new Disciplina(0, "bla", "bla", 4);
                Professor prof = new Professor(0, "Ze das couves", "bla", "Bla", "Bla"); 
                estudante.setId(rs.getLong("estudante_id"));
                Turma turma = new Turma(0, "Bla", "Bla", "blA", d ,prof); 
                turma.setId(rs.getLong("turma_id"));

                Tag tag = Tag.valueOf(rs.getString("tag"));

                return new Avaliacao(
                        rs.getLong("id"),
                        rs.getFloat("nota"),
                        rs.getString("comentario"),
                        LocalDate.parse(rs.getString("data")),
                        rs.getString("titulo"),
                        estudante,
                        turma,
                        tag
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ (listar todos)
    public List<Avaliacao> listarAvaliacoes() {
        List<Avaliacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Avaliacoes";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estudante estudante = new Estudante(0, "Ze das couves", "bla", "Bla", "Bla"); 
                Disciplina d = new Disciplina(0, "bla", "bla", 4);
                Professor prof = new Professor(0, "Ze das couves", "bla", "Bla", "Bla"); 
                estudante.setId(rs.getLong("estudante_id"));
                Turma turma = new Turma(0, "Bla", "Bla", "blA", d ,prof); 
                turma.setId(rs.getLong("turma_id"));
                Tag tag = Tag.valueOf(rs.getString("tag"));

                Avaliacao a = new Avaliacao(
                        rs.getLong("id"),
                        rs.getFloat("nota"),
                        rs.getString("comentario"),
                        LocalDate.parse(rs.getString("data")),
                        rs.getString("titulo"),
                        estudante,
                        turma,
                        tag
                );

                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Avaliacao> listaAvaliacaoPorDisciplina(String nome){
        String sql = """
                    SELECT * FROM Avaliacoes as a
                    LEFT JOIN Turmas as t ON a.turma_id = t.id
                    LEFT JOIN Disciplinas as d ON d.id = t.disciplina_id
        """;
        List<Avaliacao> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estudante estudante = new Estudante(0, "Ze das couves", "bla", "Bla", "Bla"); 
                Disciplina d = new Disciplina(0, "bla", "bla", 4);
                Professor prof = new Professor(0, "Ze das couves", "bla", "Bla", "Bla"); 
                estudante.setId(rs.getLong("estudante_id"));
                Turma turma = new Turma(0, "Bla", "Bla", "blA", d ,prof); 
                turma.setId(rs.getLong("turma_id"));
                Tag tag = Tag.valueOf(rs.getString("tag"));

                Avaliacao a = new Avaliacao(
                        rs.getLong("id"),
                        rs.getFloat("nota"),
                        rs.getString("comentario"),
                        LocalDate.parse(rs.getString("data")),
                        rs.getString("titulo"),
                        estudante,
                        turma,
                        tag
                );

                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // UPDATE
    public void atualizarAvaliacao(Avaliacao avaliacao) {
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
    public void deletarAvaliacao(long id) {
        String sql = "DELETE FROM Avaliacoes WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE
    public void inserirDisciplina(Disciplina disciplina) {
        String sql = "INSERT INTO Disciplinas (codigo, nome, numeroDeCreditos) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getNumeroDeCreditos());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                disciplina.setId(keys.getLong(1));
            }

            inserirPreRequisitos(disciplina, conn);

        } catch (SQLException e) {
            e.printStackTrace();
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

    // READ (por ID)
    public Disciplina buscarDisciplinaPorId(long id) {
        String sql = "SELECT * FROM Disciplinas WHERE id = ?";
        Disciplina disciplina = null;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                disciplina = new Disciplina(
                    rs.getLong("id"),
                    rs.getString("codigo"),
                    rs.getString("nome"),
                    rs.getInt("numeroDeCreditos")
                );
                disciplina.setPreRequisitos(buscarPreRequisitos(disciplina.getId(), conn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disciplina;
    }

    public Disciplina buscarDisciplinaPorNome(String name) {
        String sql = "SELECT * FROM Disciplinas WHERE nome = ?";
        Disciplina disciplina = null;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                disciplina = new Disciplina(
                    rs.getLong("id"),
                    rs.getString("codigo"),
                    rs.getString("nome"),
                    rs.getInt("numeroDeCreditos")
                );
                disciplina.setPreRequisitos(buscarPreRequisitos(disciplina.getId(), conn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disciplina;
    }

    private Set<Disciplina> buscarPreRequisitos(long disciplinaId, Connection conn) throws SQLException {
        Set<Disciplina> preRequisitos = new HashSet<>();
        String sql = "SELECT d.* FROM Disciplinas d " +
                    "JOIN Disciplina_PreRequisitos pr ON d.id = pr.prerequisito_id " +
                    "WHERE pr.disciplina_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, disciplinaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Disciplina pre = new Disciplina(
                    rs.getLong("id"),
                    rs.getString("codigo"),
                    rs.getString("nome"),
                    rs.getInt("numeroDeCreditos")
                );
                preRequisitos.add(pre);
            }
        }

        return preRequisitos;
    }

    // READ (listar todas)
    public List<Disciplina> listarDisciplinas() {
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
                    rs.getInt("numeroDeCreditos")
                );
                d.setPreRequisitos(buscarPreRequisitos(d.getId(), conn));
                lista.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // UPDATE
    public void atualizarDisciplina(Disciplina disciplina) {
        String sql = "UPDATE Disciplinas SET codigo = ?, nome = ?, numeroDeCreditos = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getNumeroDeCreditos());
            stmt.setLong(4, disciplina.getId());
            stmt.executeUpdate();

            // Limpa e reinsere os pré-requisitos
            limparPreRequisitos(disciplina.getId(), conn);
            inserirPreRequisitos(disciplina, conn);

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
    public void deletarDisciplina(long id) {
        String sql = "DELETE FROM Disciplinas WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            limparPreRequisitos(id, conn);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE
    public void inserirEstudante(Estudante estudante) {
        String sqlUsuario = "INSERT INTO Usuarios (nome, email, senha) VALUES (?, ?, ?)";
        String sqlEstudante = "INSERT INTO Estudantes (id, matriculaEstudante) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmtUsuario.setString(1, estudante.getNome());
                stmtUsuario.setString(2, estudante.getEmail());
                stmtUsuario.setString(3, estudante.getSenha());
                stmtUsuario.executeUpdate();

                ResultSet keys = stmtUsuario.getGeneratedKeys();
                if (keys.next()) {
                    long idUsuario = keys.getLong(1);
                    estudante.setId(idUsuario);

                    try (PreparedStatement stmtEstudante = conn.prepareStatement(sqlEstudante)) {
                        stmtEstudante.setLong(1, idUsuario);
                        stmtEstudante.setString(2, estudante.getMatriculaEstudante());
                        stmtEstudante.executeUpdate();
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

    public void inserirProfessor(Professor professor) {
        String sqlUsuario = "INSERT INTO Usuarios (nome, email, senha) VALUES (?, ?, ?)";
        String sqlProf = "INSERT INTO Professores (id, matriculaProfessor) VALUES (?, ?)";

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

                    try (PreparedStatement stmtEstudante = conn.prepareStatement(sqlProf)) {
                        stmtEstudante.setLong(1, idUsuario);
                        stmtEstudante.setString(2, professor.getMatriculaProfessor());
                        stmtEstudante.executeUpdate();
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
    public Estudante buscarEstudantePorId(long id) {
        String sql = "SELECT u.id, u.nome, u.email, u.senha, e.matriculaEstudante " +
                    "FROM Usuarios u JOIN Estudantes e ON u.id = e.id WHERE u.id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Estudante(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("matriculaEstudante")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ (listar todos)
    public List<Estudante> listarEstudantes() {
        List<Estudante> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.email, u.senha, e.matriculaEstudante " +
                    "FROM Usuarios u JOIN Estudantes e ON u.id = e.id";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estudante e = new Estudante(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("matriculaEstudante")
                );
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // UPDATE
    public void atualizarEstudante(Estudante estudante) {
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
    public void deletarEstudante(long id) {
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
    
    public void inserirTurma(String sala, String horario, String codigo, String disciplinaNome, String professor){
        String sql = """
            INSERT INTO Turmas(sala, horario, codigo, disciplina_id, professor_id) 
            VALUES(?,?,?,?,?)
            """;
        String sqlDisciplina = """
            SELECT id FROM Disciplinas WHERE nome = ?
            """;
        String sqlProfessor = """
            SELECT id FROM Usuarios WHERE nome = ?
            """;
        
        try (Connection conn = DBConnection.getConnection()) {
            Long disciplinaId = null;
            Long professorId = null;
            try(PreparedStatement stmt = conn.prepareStatement(sqlDisciplina)){
                stmt.setString(1, disciplinaNome);
                ResultSet rsDisciplina = stmt.executeQuery();
                if (rsDisciplina.next()) {
                    disciplinaId = rsDisciplina.getLong(1);  // ou rs.getInt("COUNT(*)") dependendo do alias
                }
            }  catch (SQLException e) {
                e.printStackTrace();
            }  
            try (PreparedStatement stmt = conn.prepareStatement(sqlProfessor)) {

                stmt.setString(1, professor);
                ResultSet rsProfessor = stmt.executeQuery();
                if (rsProfessor.next()) {
                    professorId = rsProfessor.getLong(1);  // ou rs.getInt("COUNT(*)") dependendo do alias
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }  
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, sala);
                stmt.setString(2, horario);
                stmt.setString(3, codigo);
                stmt.setLong(4, disciplinaId);
                stmt.setLong(5, professorId);


                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                
                if (keys.next()) {
                    long turmaId = keys.getLong(1);
                    System.out.println("Turma inserida com ID: " + turmaId);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch  (SQLException e) {
            e.printStackTrace();
        }

    }

    





    
}
