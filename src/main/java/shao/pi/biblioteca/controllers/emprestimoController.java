package shao.pi.biblioteca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import shao.pi.biblioteca.models.Emprestimo;
import shao.pi.biblioteca.repositories.EmprestimoRepository;

@Controller
@RequestMapping("/ShaoBibli")
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
		emprestimo.setSituacao("Empréstimo Ativa");
		System.out.println(emprestimo);
		emp.save(emprestimo);
		attributes.addFlashAttribute("mensagem", "Empréstimo salvo com sucesso!");
		return  "redirect:/ShaoBibli";
	}
    @GetMapping
	public ModelAndView listarEmprestimo() {
		List<Emprestimo> emprestimos = emp.findAll();
		ModelAndView mv = new ModelAndView("emprestimos/listaE");
		mv.addObject("emprestimos", emprestimos);

		return mv;
	}

}
