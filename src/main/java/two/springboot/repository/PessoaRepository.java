package two.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import two.springboot.model.Pessoa;

@Repository
@org.springframework.transaction.annotation.Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query(value = " SELECT p FROM Pessoa p WHERE p.nome like %?1% ")
	List<Pessoa> findPessoaByName(String nome);

	@Query(value = " SELECT p FROM Pessoa p WHERE p.nome like %?1% and p.sexo = ?2 ")
	List<Pessoa> findPessoaByNameSexo(String nome, String sexo);

	@Query(value = " SELECT p FROM Pessoa p WHERE p.sexo = ?1 ")
	List<Pessoa> findPessoaBySexo(String sexo);

	default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {

		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);

		// Configurando a pesquisa para consultar por partes do nome no banco de Dados,
		// igual ao Like com sql
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		//une o objeto com o valor e a configuração para consultar
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

		Page<Pessoa> pessoas = findAll(example, pageable);
		return pessoas;
	}
	
	
	default Page<Pessoa> findPessoaBySexoPage(String nome, String sexo, Pageable pageable) {

		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexo(sexo);

		// Configurando a pesquisa para consultar por partes do nome no banco de Dados,
		// igual ao Like com sql
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexo",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		//une o objeto com o valor e a configuração para consultar
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

		Page<Pessoa> pessoas = findAll(example, pageable);
		return pessoas;
	}
	
	

}
