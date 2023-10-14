package shao.pi.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import shao.pi.biblioteca.models.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{
    
}
