package shao.pi.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import shao.pi.biblioteca.models.Aluno;
import shao.pi.biblioteca.models.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    int countByMatriculaAlunoAndSituacao(Aluno aluno, String situacao);
}
  