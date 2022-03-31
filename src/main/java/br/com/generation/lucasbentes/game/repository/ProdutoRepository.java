package br.com.generation.lucasbentes.game.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.generation.lucasbentes.game.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	public List<Produto> findAllByNomeContainingIgnoreCase(String nome);
	public List<Produto> findAllByValor(BigDecimal valor);
	public List<Produto> findAllByCodigo(int codigo);
	
}
