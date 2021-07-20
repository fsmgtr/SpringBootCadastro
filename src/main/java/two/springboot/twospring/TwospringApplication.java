package two.springboot.twospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
 
 
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "two.springboot.model")
@ComponentScan(basePackages = { "two.*" })
@EnableJpaRepositories(basePackages = { "two.springboot.repository" })
@EnableTransactionManagement
@EnableWebMvc
public class TwospringApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(TwospringApplication.class, args);
		
		 /*
		BCryptPasswordEncoder  enconder =  new BCryptPasswordEncoder ();
		String resul =  enconder.encode("123");
		System.out.println(resul);
		$2a$10$nRcJSVwrE4n.G9LZEQZ0oO8kczbIihL71zmdZ017f150edqadPW2G
		*/
		 
		
		
		

	}

	/*
	 * re-implementação deste método. pegar o objeto de registro para que na hora
	 * que acesse o /login ele redirecione para o /login.
	 * 
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// mapeando e redirecionando
		registry.addViewController("/login").setViewName("/login");
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
	}

};