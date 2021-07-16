package two.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import two.springboot.model.Pessoa;
import two.springboot.model.Telefone;
import two.springboot.repository.PessoaRepository;
import two.springboot.repository.ProfissaoRepository;
import two.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private TelefoneRepository telefoneRepository;
	@Autowired
	private ReportUtil reposrtUtil;
	@Autowired
	private ProfissaoRepository profissaoRepository; 

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("profissoes", profissaoRepository.findAll());
		return andView;

	}

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa",
			consumes =  {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindigResult, final MultipartFile file) throws IOException {

		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));

		if (bindigResult.hasErrors()) {
			ModelAndView mandView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			mandView.addObject("pessoas", pessoaIt);
			mandView.addObject("pessoaobj", pessoa);

			// mostrar as validações
			List<String> msg = new ArrayList<>();

			for (ObjectError objecterror : bindigResult.getAllErrors()) {
				msg.add(objecterror.getDefaultMessage());// vem das anotações das entidades Nullemp...
			}
			mandView.addObject("msg", msg);
			mandView.addObject("profissoes", profissaoRepository.findAll());
			return mandView;
		}
		
		if(file.getSize() > 0) {
			pessoa.setCurriculo(file.getBytes());
			
		} else {
			if (pessoa.getId() != null && pessoa.getId() > 0) {//editando
				@SuppressWarnings("unused")
				byte [] curriculoTemp = pessoaRepository.findById(pessoa.getId()).get().getCurriculo();
				pessoa.setCurriculo(curriculoTemp);
			}
		}

		pessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		 
		return andView;
	}

	@GetMapping("editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		andView.addObject("pessoaobj", pessoa.get());
		andView.addObject("profissoes", profissaoRepository.findAll());
		return andView;

	}

	@GetMapping("excluirpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

		pessoaRepository.deleteById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoas", pessoaRepository.findAll());
		andView.addObject("pessoaobj", new Pessoa());

		return andView;

	}

	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa,
			@RequestParam("pesqsexo") String pesqsexo) {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();

		if (pesqsexo != null && !pesqsexo.isEmpty()) {
			pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa, pesqsexo);
		} else {
			pessoas = pessoaRepository.findPessoaByName(nomepesquisa);

		}

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoas", pessoas);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;

	}

	@GetMapping("**/pesquisarpessoa")
	public void imprimePdf(@RequestParam("nomepesquisa") String nomepesquisa, @RequestParam("pesqsexo") String pesqsexo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();

		if (pesqsexo != null && !pesqsexo.isEmpty()) {// nome sexo
			pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa, pesqsexo);
			
		} else if (nomepesquisa != null && !nomepesquisa.isEmpty()) {// nome
			pessoas = pessoaRepository.findPessoaByName(nomepesquisa);

		}else if (pesqsexo != null && !pesqsexo.isEmpty()) {// sexo
			pessoas = pessoaRepository.findPessoaBySexo(pesqsexo);

		}  
		else {// todos

			Iterable<Pessoa> iterator = pessoaRepository.findAll();
			for (Pessoa pessoa : iterator) {
				pessoas.add(pessoa);
			}
		}
		
		
		//chamando o serviço que faz a geração do relatório!
		
		byte[] pdf = reposrtUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
		
		//tamanho da resposta para o navegador
		response.setContentLength(pdf.length);
		
		//definir na resposta o tipo de arquivo
		response.setContentType("application/octet-stream");
		
		//definir o cabeçalho da resposta
		String headerKey= "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\" ", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		//finalizando a resposta pro navegador
		response.getOutputStream().write(pdf);
	}

	@GetMapping("telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastro/telefones");

		andView.addObject("pessoaobj", pessoa.get());
		andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		return andView;

	}

	@PostMapping("**addfonepessoa/{pessoaid}")
	public ModelAndView addfonepessoa(@Valid Telefone telefone, @PathVariable("pessoaid") Long pessoaid,
			BindingResult bindigResult) {

		// retorna o objeto pessoa
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();

		if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {

			ModelAndView andView = new ModelAndView("cadastro/telefones");
			andView.addObject("pessoaobj", pessoa);
			andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));

			List<String> msg = new ArrayList<>();
			if (telefone.getNumero().isEmpty()) {
				msg.add("Numero deve ser Informado");
			}
			if (telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser Informado");
			}
			andView.addObject("msg", msg);

			return andView;

		}

		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);

		ModelAndView andView = new ModelAndView("cadastro/telefones");
		andView.addObject("pessoaobj", pessoa);
		andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return andView;

	}

	/*
	 * Interceptou a url enviou para o método com o id
	 * th:href="@{/excluirtelefone/{idtelefone}(idtelefone=${fone.id})}">Excluir</a>
	 * </td>
	 * 
	 * vai chegar aqui já com o id public ModelAndView
	 * excluirtelefone(@PathVariable("idtelefone") Long idtelefone)
	 * 
	 * Carrega o objeto telefone e retorno com o pai Pessoa pessoa =
	 * telefoneRepository.findById(idtelefone).get().getPessoa();
	 * 
	 * deletei o telefone selecionado telefoneRepository.deleteById(idtelefone);
	 * 
	 * fico na mesma tela ModelAndView andView = new
	 * ModelAndView("cadastro/telefones");
	 * 
	 * Passo o objeto pai para mostrar na tela andView.addObject("pessoaobj",
	 * pessoa);
	 * 
	 * CArrego os telefones conforme o id andView.addObject("telefones",
	 * telefoneRepository.getTelefones(pessoa.getId()));
	 * 
	 * 
	 */

	@GetMapping("excluirtelefone/{idtelefone}")
	public ModelAndView excluirtelefone(@PathVariable("idtelefone") Long idtelefone) {
		// retornando o objeto pessoa.
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		telefoneRepository.deleteById(idtelefone);

		ModelAndView andView = new ModelAndView("cadastro/telefones");
		andView.addObject("pessoaobj", pessoa);
		andView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));

		return andView;

	}

}
