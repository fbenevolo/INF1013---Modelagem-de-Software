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
import model.Estudante;
import model.Professor;
import model.Turma;

public class TurmaRepository {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;

    public TurmaRepository() {
        this.disciplinaRepository = new DisciplinaRepository();
        this.professorRepository = new ProfessorRepository();
    }

    public void inserir(Turma turma) {
        String sql = """
                INSERT INTO Turmas(sala, horario, codigo, disciplina_id, professor_id)
                VALUES(?,?,?,?,?)
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, turma.getSala());
            stmt.setString(2, turma.getHorario());
            stmt.setString(3, turma.getCodigo());
            stmt.setLong(4, turma.getDisciplina().getId());
            stmt.setLong(5, turma.getProfessor().getId());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    turma.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Turma buscarPorId(long id) {
        String sql = "SELECT * FROM Turmas WHERE id = ?";
        long turmaId = 0;
        String sala = null;
        String horario = null;
        String codigo = null;
        long disciplinaId = 0;
        long professorId = 0;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    turmaId = rs.getLong("id");
                    sala = rs.getString("sala");
                    horario = rs.getString("horario");
                    codigo = rs.getString("codigo");
                    disciplinaId = rs.getLong("disciplina_id");
                    professorId = rs.getLong("professor_id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Disciplina disciplina = disciplinaRepository.buscarPorId(disciplinaId);
        Professor professor = professorRepository.buscarPorId(professorId);

        return new Turma(
                turmaId,
                sala,
                horario,
                codigo,
                disciplina,
                professor);
    }

    public List<Turma> listar() {
        List<Turma> turmas = new ArrayList<>();
        String sql = "SELECT * FROM Turmas";
        class TurmaRaw {
            long id;
            String sala;
            String horario;
            String codigo;
            long disciplinaId;
            long professorId;
        }
        List<TurmaRaw> raws = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TurmaRaw raw = new TurmaRaw();
                raw.id = rs.getLong("id");
                raw.sala = rs.getString("sala");
                raw.horario = rs.getString("horario");
                raw.codigo = rs.getString("codigo");
                raw.disciplinaId = rs.getLong("disciplina_id");
                raw.professorId = rs.getLong("professor_id");
                raws.add(raw);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Agora, fora do bloco try, busque as entidades relacionadas
        for (TurmaRaw raw : raws) {
            Disciplina disciplina = disciplinaRepository.buscarPorId(raw.disciplinaId);
            Professor professor = professorRepository.buscarPorId(raw.professorId);
            Turma turma = new Turma(
                    raw.id,
                    raw.sala,
                    raw.horario,
                    raw.codigo,
                    disciplina,
                    professor);
            turmas.add(turma);
        }
        return turmas;
    }

    public void matricularEstudante(long idEstudante, long idTurma) throws IllegalArgumentException {
        Turma turma = buscarPorId(idTurma);
        if (turma == null) {
            throw new IllegalArgumentException("Turma não encontrada com ID: " + idTurma);
        }
        Estudante estudante = new EstudanteRepository().buscarPorId(idEstudante);
        if (estudante == null) {
            throw new IllegalArgumentException("Estudante não encontrado com ID: " + idEstudante);
        }
        

        String sql = """
                INSERT INTO Turma_Estudantes(turma_id, estudante_id)
                VALUES(?,?)
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idTurma);
            stmt.setLong(2, idEstudante);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}