package shao.pi.biblioteca.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import shao.pi.biblioteca.models.Aluno;
import shao.pi.biblioteca.models.Emprestimo;
import shao.pi.biblioteca.models.Livro;
import shao.pi.biblioteca.repositories.AlunoRepository;
import shao.pi.biblioteca.repositories.EmprestimoRepository;
import shao.pi.biblioteca.repositories.LivroRepository;

@Controller
@RequestMapping("/ShaoBiblioteca")
public class emprestimoController {
    @Autowired
    private EmprestimoRepository emp;
     @Autowired
    private LivroRepository liv;
     @Autowired
    private AlunoRepository alu;

    
    	@GetMapping("/form")
	public String form(Emprestimo emprestimo) {
		return "emprestimos/formEmprestimos";
	}
@PostMapping
public String salvarEmprestimo(@Valid Emprestimo emprestimo, BindingResult result, RedirectAttributes attributes) {

    if (result.hasErrors()) {
        return form(emprestimo);
    }

    Aluno aluno = alu.findByMatricula(emprestimo.getMatriculaAluno().getMatricula());
    Livro livro = liv.findByTitulo(emprestimo.getTituloLivro().getTitulo());
    if (livro == null) {
        result.rejectValue("tituloLivro.titulo", "error.tituloLivro", "Erro: Livro não encontrado");
        return form(emprestimo);
    }
    if (aluno == null) {
        result.rejectValue("matriculaAluno.matricula", "error.matriculaAluno", "Erro: Aluno não encontrado");
        return form(emprestimo);
    }

   
    int emprestimosAtivos = emp.countByMatriculaAlunoAndSituacao(aluno, "Empréstimo Ativo");
    if (emprestimosAtivos >= 3) {
        result.rejectValue("matriculaAluno.matricula", "error.matriculaAluno", "Erro: O aluno já possui 3 empréstimos ativos.");
        return form(emprestimo);
    }
    System.out.println(livro);
    System.out.println(aluno);
    emprestimo.setMatriculaAluno(aluno);
    emprestimo.setTituloLivro(livro);
    emprestimo.setSituacao("Empréstimo Ativo");

    emp.save(emprestimo);
    attributes.addFlashAttribute("mensagem", "Empréstimo salvo com sucesso!");
    return "redirect:/ShaoBiblioteca";
}

    @GetMapping
	public ModelAndView listarEmprestimo() {
		List<Emprestimo> emprestimos = emp.findAll();
        LocalDate dataAtual = LocalDate.now();
    
    // Calculando a diferença em dias e adicionando ao objeto Emprestimo
    for (Emprestimo emprestimo : emprestimos) {
        long diferencaEmDias = ChronoUnit.DAYS.between(emprestimo.getdEmprestimo(), dataAtual);
        emprestimo.setDiferencaDias(diferencaEmDias);
         if(diferencaEmDias > 14 && emprestimo.getdDevolucao() == null) {
            emprestimo.setSituacao("Empréstimo Atrasado");
        }if(diferencaEmDias <= 14 && !emprestimo.getSituacao().equals("Empréstimo Finalizado")){
           emprestimo.setSituacao("Empréstimo Ativo");  
        }
        emp.save(emprestimo);
    }
		ModelAndView mv = new ModelAndView("emprestimos/listaE");
		mv.addObject("emprestimos", emprestimos);

		return mv;
	}
    @GetMapping("/{id}/finalizar")
    public String finalizarEmprestimo(@PathVariable Long id, RedirectAttributes attributes) {
        Optional<Emprestimo> opt = emp.findById(id);
        if (opt.isEmpty()) {
            attributes.addFlashAttribute("mensagem", "Empréstimo não encontrado.");
            return "redirect:/ShaoBiblioteca";
        }
         LocalDate dataAtual = LocalDate.now();
        Emprestimo emprestimo = opt.get();
           emprestimo.setdDevolucao(dataAtual);
            emprestimo.setSituacao("Empréstimo Finalizado");
            emp.save(emprestimo);
            attributes.addFlashAttribute("mensagem", "Empréstimo finalizado com sucesso!");
        return "redirect:/ShaoBiblioteca";
    }

   

}
