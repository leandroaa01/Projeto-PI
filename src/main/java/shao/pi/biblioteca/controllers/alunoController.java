package shao.pi.biblioteca.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class alunoController {
    @Autowired
    private EmprestimoRepository emp;
     @Autowired
    private LivroRepository liv;
     @Autowired
    private AlunoRepository alu;
       @GetMapping("/emprestimos")
    public ModelAndView emprestimo(){
        List<Emprestimo> emprestimos = emp.findAll();
		ModelAndView mv = new ModelAndView("emprestimos/listaE");
		mv.addObject("emprestimos", emprestimos);
		return mv;
    }
       @GetMapping("/livros")
    public ModelAndView livros(){
        List<Livro> livros = liv.findAll();
		ModelAndView mv = new ModelAndView("livros/listaL");
		mv.addObject("livros", livros);
		return mv;
    }
    	@GetMapping("/formA")
	public String formA(Aluno aluno) {
		return "alunos/formAlunos";
	}
@PostMapping("/aluno")
public String salvarAluno(@Valid Aluno aluno, BindingResult result, RedirectAttributes attributes) {
    if (result.hasErrors()) {
        return formA(aluno);
    }
    Aluno alunoExistente = alu.findByMatricula(aluno.getMatricula());
    if (alunoExistente != null && !alunoExistente.getId().equals(aluno.getId())) {
        result.rejectValue("matricula", "error.matricula", "Matrícula já está em uso.");
        return formA(aluno);
    }
    alu.save(aluno);
    attributes.addFlashAttribute("mensagem", "Aluno salvo com sucesso!");
    return "redirect:/ShaoBiblioteca/alunos";
}

 @GetMapping("/alunos")
	public ModelAndView listarAlunos() {
		List<Aluno> alunos = alu.findAll();
		ModelAndView mv = new ModelAndView("alunos/listaDa");
		mv.addObject("alunos", alunos);
		return mv;
	}
  
    @GetMapping("/{id}/edit")
	public ModelAndView editAluno(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Aluno> opt = alu.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/ShaoBiblioteca/alunos");
			return md;
		}
		
		Aluno aluno = opt.get();
		md.setViewName("alunos/formAlunos");
		md.addObject("aluno", aluno);
		
		return md;
	}
    @GetMapping("/{id}/delete")
    public String deleteAluno(@PathVariable Long id, RedirectAttributes attributes) {
         try {
        Optional<Aluno> opt = alu.findById(id);
        if (!opt.isEmpty()) {
            Aluno aluno = opt.get();
            alu.delete(aluno);
            attributes.addFlashAttribute("mensagem", "Aluno removido com sucesso!");
        }
    } catch (DataIntegrityViolationException ex) {
        attributes.addFlashAttribute("erro", "Não é possível excluir este aluno devido a registros relacionados.");
    }
    return "redirect:/ShaoBiblioteca/alunos";
}
}
