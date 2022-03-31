package br.com.generation.lucasbentes.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.generation.lucasbentes.game.model.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

	public List<Categoria> findAllByTipoContainingIgnoreCase(String tipo);
	
}
