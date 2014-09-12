package chat.foda.pra.caralho.testes.jdbc;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chat.foda.pra.caralho.dao.UsuarioDAO;
import chat.foda.pra.caralho.dao.factory.DaoFactory;
import chat.foda.pra.caralho.models.Pessoa;
import chat.foda.pra.caralho.models.Usuario;

public class UsuarioCRUDTeste {
	
	private UsuarioDAO usuarioDAO;
	
	private Usuario usuario;
	
	private Pessoa pessoa;
	
	@BeforeClass
	public void criaConexaoBanco() {
		usuarioDAO = DaoFactory.get().usuarioDAO();
	}
	
	@Before
	public void criaAmbiente() {
		pessoa = new Pessoa();
		pessoa.setNomeCompleto("Pessoa Teste 1");
		pessoa.setCpf("123456");
		pessoa.setDataNascimento(LocalDate.now().minusYears(1));
		
		usuario = new Usuario();
		usuario.setSenha("123");
		usuario.setNickName("Nick Teste 1");
		usuario.setEmail("usuario1@teste.com");
		usuario.setPessoa(pessoa);
	}
	
	/**
	 * M�todo para testar a inser��o de um novo usu�rio no banco
	 * Se ocorrer erro no processo, n�o pode salvar os dados do Usu�rio nem da Pessoa relacionada ao usu�rio
	 */
	@Test
	public void testeSalvar() {
		assertEquals(0, 0);
	}
	
}
