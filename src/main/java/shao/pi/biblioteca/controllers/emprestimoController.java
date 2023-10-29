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
import shao.pi.biblioteca.models.Emprestimo;
import shao.pi.biblioteca.repositories.EmprestimoRepository;

@Controller
@RequestMapping("/ShaoBiblioteca")
public class emprestimoController {
    @Autowired
    private EmprestimoRepository emp;
    
    	@GetMapping("/form")
	public String form(Emprestimo emprestimo) {
		return "emprestimos/formEmprestimos";
	}
	@PostMapping
	public String salvar(@Valid Emprestimo emprestimo, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return form(emprestimo);
		}
		emprestimo.setSituacao("Empréstimo Ativo"); 
		System.out.println(emprestimo);
		emp.save(emprestimo);
		attributes.addFlashAttribute("mensagem", "Empréstimo salvo com sucesso!");
		return  "redirect:/ShaoBiblioteca";
	}
    @GetMapping
	public ModelAndView listarEmprestimo() {
		List<Emprestimo> emprestimos = emp.findAll();
        LocalDate dataAtual = LocalDate.now();
    
    // Calculando a diferença em dias e adicionando ao objeto Emprestimo
    for (Emprestimo emprestimo : emprestimos) {
        long diferencaEmDias = ChronoUnit.DAYS.between(emprestimo.getdEmprestimo(), dataAtual);
        emprestimo.setDiferencaDias(diferencaEmDias);
    }
		ModelAndView mv = new ModelAndView("emprestimos/listaE");
		mv.addObject("emprestimos", emprestimos);

		return mv;
	}
    
    @PostMapping("/atualizarSituacao")
    public String atualizarSituacao() {
    List<Emprestimo> emprestimos = emp.findAll();
    for (Emprestimo e : emprestimos) {
        LocalDate dataAtual = LocalDate.now();
        long diferencaDias = ChronoUnit.DAYS.between(e.getdEmprestimo(), dataAtual);
        if(diferencaDias >= 14 && !e.getSituacao().equals("Empréstimo Finalizado")) {
            e.setSituacao("Empréstimo Atrasado");
        }
        emp.save(e);
    }
    return "redirect:/ShaoBiblioteca";
}
    @GetMapping("/{id}/finalizar")
    public String finalizarEmprestimo(@PathVariable Long id, RedirectAttributes attributes) {
        Optional<Emprestimo> opt = emp.findById(id);
        if (opt.isEmpty()) {
            attributes.addFlashAttribute("mensagem", "Empréstimo não encontrado.");
            return "redirect:/ShaoBiblioteca";
        }
        Emprestimo emprestimo = opt.get();
            emprestimo.setSituacao("Empréstimo Finalizado");
            emp.save(emprestimo);
            attributes.addFlashAttribute("mensagem", "Empréstimo finalizado com sucesso!");
        return "redirect:/ShaoBiblioteca";
    }

    @GetMapping("/{id}/cancelar")
    public String cancelarEmprestimo(@PathVariable Long id, RedirectAttributes attributes) {
        Optional<Emprestimo> opt = emp.findById(id);
        if(!opt.isEmpty()){
            Emprestimo emprestimo = opt.get();
            emp.delete(emprestimo);
            attributes.addFlashAttribute("mensagem", "Empréstimo cancelado com sucesso!");
        }
         return "redirect:/ShaoBiblioteca";
    }

}
