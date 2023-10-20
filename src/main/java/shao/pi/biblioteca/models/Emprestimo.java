package shao.pi.biblioteca.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Emprestimo {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @ManyToOne
    private Livro tituloLivro;

    @ManyToOne
    private Aluno matriculaAluno;
    

    private String situacao;
    

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate dEmprestimo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate dDevolucao;
   
    @PrePersist
    public void prePersist() {
    this.dEmprestimo = LocalDate.now();
}
private long diferencaDias;


    public long getDiferencaDias() {
        return diferencaDias;
    }

    public void setDiferencaDias(long diferencaDias) {
        this.diferencaDias = diferencaDias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(Livro tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public Aluno getMatriculaAluno() {
        return matriculaAluno;
    }

    public void setMatriculaAluno(  Aluno matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public LocalDate getdEmprestimo() {
        return dEmprestimo;
    }

    public void setdEmprestimo(LocalDate dEmprestimo) {
        this.dEmprestimo = dEmprestimo;
    }

    public LocalDate getdDevolucao() {
        return dDevolucao;
    }

    public void setdDevolucao(LocalDate dDevolucao) {
        this.dDevolucao = dDevolucao;
    }

    @Override
    public String toString() {
        return "Emprestimo [id=" + id + ", tituloLivro=" + tituloLivro + ", matriculaAluno=" + matriculaAluno
                + ", situacao=" + situacao + ", dEmprestimo=" + dEmprestimo + ", dDevolucao=" + dDevolucao + "]";
    }
}


