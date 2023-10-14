package shao.pi.biblioteca.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Emprestimo {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String tituloLivro;

    @NotBlank
    private Long matriculaAluno;
    
    @NotBlank
    private String situacao;
    

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate dEmprestimo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate dDevolucao;
   
    @PrePersist
    public void prePersist() {
    this.dEmprestimo = LocalDate.now();
    this.dDevolucao = this.dEmprestimo.plusDays(14);
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public Long getMatriculaAluno() {
        return matriculaAluno;
    }

    public void setMatriculaAluno(Long matriculaAluno) {
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


