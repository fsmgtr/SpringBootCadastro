package two.springboot.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import two.springboot.model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long>{

	@Query("SELECT t FROM Telefone t WHERE t.pessoa.id = ?1")
	public List<Telefone> getTelefones(Long pessoaid);
	
}
