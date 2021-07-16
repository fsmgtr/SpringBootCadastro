package two.springboot.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import two.springboot.model.Usuario;

@Repository
@Transactional
public interface UsuarioReppository extends CrudRepository<Usuario, Long> {

	@Query(value="SELECT u FROM Usuario u WHERE u.login = ?1")
	Usuario findUserByLogin(String login);
	
}
