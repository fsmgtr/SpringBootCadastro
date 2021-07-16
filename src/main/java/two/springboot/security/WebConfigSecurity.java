package two.springboot.security;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	@Override // configura as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {

		
		http.csrf() .disable() // desativa as configurações padrão de memória do spring 
		  .authorizeRequests() // permitir e restringir acessos
		  .antMatchers(HttpMethod.GET, "/").permitAll()// qualquer usuário acessa a página incial
		  .antMatchers("**/materialize/**\"").permitAll()
		  .antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("USER")
		  .anyRequest().authenticated() .and().formLogin().permitAll()// permite a  qualquer usuário
		  .loginPage("/login")// página login
		  .defaultSuccessUrl("/cadastropessoa")// se logar
		  .failureUrl("/login?error=true")// se falhar o login volta para tela de  autentic
		  .and().logout()// MApeia URL de logout e invalida usuário  autenticado 
		 .logoutSuccessUrl("/login").logoutRequestMatcher(new   AntPathRequestMatcher("/logout"));
		
		
	}

	/*
	 
	 * validação emmemória
	 * 
	 * @Override// Cria autenticação do usuário com banco de dados ou em memória
	 * protected void configure(AuthenticationManagerBuilder auth) throws Exception
	 * {
	 * auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance
	 * ())//Autenticando sem codificar .withUser("adm") .password("1")
	 * .roles("ADMIN"); }
	 * @Override // Cria autenticação do usuário com banco de dados ou em memória
	 * protected void configure(AuthenticationManagerBuilder auth) throws Exception
	 * {
	 * 
	 * auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new
	 * BCryptPasswordEncoder());
	 * 
	 * 
	 * }
	 * 
	 */

	@Override // Cria autenticação do usuário com banco de dados ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override // Ignora URL especificas
	/*
	 * ex: o materialize pois se o usuario estiver logado ou não terá acesso ao css
	 * 
	 */
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**").antMatchers(HttpMethod.GET, "/resources/**", "/static/**", "/**",
				"/materialize/**", "**/materialize/**");

	}
}
