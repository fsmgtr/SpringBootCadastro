package two.springboot.repository;

 

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import two.springboot.model.Pessoa;


@Repository
@org.springframework.transaction.annotation.Transactional
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {
	
	
	@Query(value=" SELECT p FROM Pessoa p WHERE p.nome like %?1% ") 
	List<Pessoa>findPessoaByName(String nome);
	
	@Query(value=" SELECT p FROM Pessoa p WHERE p.nome like %?1% and p.sexo = ?2 ") 
	List<Pessoa>findPessoaByNameSexo(String nome, String sexo);
	
	@Query(value=" SELECT p FROM Pessoa p WHERE p.sexo = ?1 ") 
	List<Pessoa>findPessoaBySexo(String sexo);
	
	

}
