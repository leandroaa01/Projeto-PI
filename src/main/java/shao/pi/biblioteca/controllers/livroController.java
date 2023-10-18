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
import shao.pi.biblioteca.models.Livro;
import shao.pi.biblioteca.repositories.LivroRepository;

@Controller
@RequestMapping("/ShaoBiblioteca")
public class livroController {
    @Autowired
    private LivroRepository liv;

    
    	@GetMapping("/formL")
	public String formL(Livro livro) {
		return "livros/formLivros";
	}
@PostMapping("/livro")
public String salvarLivro(@Valid Livro livro, BindingResult result, RedirectAttributes attributes) {
    if (result.hasErrors()) {
        return formL(livro);
    }
    Livro livroExistente = liv.findByTitulo(livro.getTitulo());
    if (livroExistente != null && !livroExistente.getId().equals(livro.getId())) {
        result.rejectValue("titulo", "error.titulo", "Livro já exitir.");
        return formL(livro);
    }
    liv.save(livro);
    attributes.addFlashAttribute("mensagem", "Livro salvo com sucesso!");
    return "redirect:/ShaoBiblioteca/livros";
}

 @GetMapping("/livro")
	public ModelAndView listarAlunos() {
		List<Livro> livros = liv.findAll();
		ModelAndView mv = new ModelAndView("livros/listaL");
		mv.addObject("livros", livros);
		return mv;
	}
  
    @GetMapping("/{id}/editLivro")
	public ModelAndView editLivro(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Livro> opt = liv.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/ShaoBiblioteca/livros");
			return md;
		}
		
		Livro livro = opt.get();
		md.setViewName("livros/formLivros");
		md.addObject("livro", livro);
		
		return md;
	}
    @GetMapping("/{id}/deleteLivro")
    public String deleteLivro(@PathVariable Long id, RedirectAttributes attributes) {
         try {
        Optional<Livro> opt = liv.findById(id);
        if (!opt.isEmpty()) {
            Livro livro = opt.get();
            liv.delete(livro);
            attributes.addFlashAttribute("mensagem", "Livro removido com sucesso!");
        }
    } catch (DataIntegrityViolationException ex) {
        attributes.addFlashAttribute("erro", "Não é possível excluir este Livro devido a registros relacionados.");
    }
    return "redirect:/ShaoBiblioteca/livros";
}
}
