package two.springboot.repository;

import javax.transaction.Transactional;

 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import two.springboot.model.Profissao;


@Repository
@Transactional
public interface ProfissaoRepository extends CrudRepository<Profissao, Long> {
	
	 
}
