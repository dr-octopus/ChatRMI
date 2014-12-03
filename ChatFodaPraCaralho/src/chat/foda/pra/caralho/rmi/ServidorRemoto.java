package chat.foda.pra.caralho.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import chat.foda.pra.caralho.models.Chat;
import chat.foda.pra.caralho.models.Usuario;
import chat.foda.pra.caralho.models.UsuarioLogado;

/**
 * Declara��o de todos os m�todos executados no Servidor, vis�veis para o Cliente
 * 
 * @author luiznazari
 */
public interface ServidorRemoto extends Remote {
	
	/**
	 * Realiza o login de um usu�rio, com base no e-mail e senha e grava a conex�o com o host do usu�rio
	 * (ClienteRemoto), nos registros do servidor.
	 * 
	 * @param cliente
	 * @param email
	 * @param senha
	 * @return UsuarioLogado contendo o usu�rio
	 * @throws RemoteException
	 */
	public UsuarioLogado login(ClienteRemoto cliente, String email, String senha) throws RemoteException;
	
	/**
	 * Cria um novo registro de usu�rio no sistema.
	 * 
	 * @param usuario
	 * @return true se foi poss�vel cadastrar, false caso contr�rio
	 * @throws RemoteException
	 */
	public boolean cadastrarUsuario(Usuario usuario) throws RemoteException;
	
	/**
	 * Remove o cadastro de um usu�rio no sistema.
	 * 
	 * @param usuario
	 * @throws RemoteException
	 */
	public void removerUsuario(Usuario usuario) throws RemoteException;
	
	/**
	 * Utilizado pelos usu�rios. Envia uma mensagem para todos os clientes que possuem um chat com o c�digo espeficiado.
	 * 
	 * @param chatCodigo
	 * @param mensagem
	 * @throws RemoteException
	 */
	public void enviarMensagemParaAmigos(Long chatCodigo, String mensagem) throws RemoteException;
	
	/**
	 * Fecha e/ou inativa os chats em que o usu�rio faz parte, baseado na quantidade de usu�rios presentes no chat.
	 * Remove os registros da conex�o do usu�rio nos registros do servidor.
	 * 
	 * @param codigosChats
	 * @param cliente
	 * @param usuario
	 * @throws RemoteException
	 */
	public void logout(Set<Long> codigosChats, ClienteRemoto cliente, Usuario usuario) throws RemoteException;
	
	/**
	 * Adiciona um novo registro de amigos para os usu�rios especificados. Um usu�rio � amigo de outro, mas este pode
	 * n�o t�-lo como amigo.
	 * 
	 * @param codUsuario
	 * @param codAmigo
	 * @throws RemoteException
	 */
	public void adicionaAmigo(Long codUsuario, Long codAmigo) throws RemoteException;
	
	/**
	 * Remove o registro de amigos para os usu�rios especificados.
	 * 
	 * @param codUsuario
	 * @param codAmigo
	 * @throws RemoteException
	 */
	public void removerAmigo(Long codUsuario, Long codAmigo) throws RemoteException;
	
	/**
	 * Cria um novo chat entre os usu�rios especificados. O pedido de abertura de chat � enviado pelo usu�rio
	 * solicitante para o usu�rio amigo. Grava os registros do novo chat no servidor.
	 * 
	 * @param solicitante
	 * @param amigo
	 * @return Novo chat caso ambos usu�rios estiverem logados. Null caso o usu�rio amigo n�o estiver logado ou ocorrer
	 *         um erro na conex�o.
	 * @throws RemoteException
	 */
	public Chat criarChat(Usuario solicitante, Usuario amigo) throws RemoteException;
	
	/**
	 * Adiciona um novo participante para um chat e atualiza os chats dos participantes atuais.
	 * 
	 * @param chat
	 * @param userToInvite
	 * @return Verdadeiro caso o usu�rio estiver conectado, falso caso contr�rio
	 * @throws RemoteException
	 */
	public boolean convidarParaChat(Chat chat, Usuario userToInvite) throws RemoteException;
	
	/**
	 * Fecha e/ou desativa os chats onde o usu�rio que est� deslogando participa.
	 * Se h� mais de dois usu�rios no chat, o mesmo n�o � exclu�do, apenas retira o usu�rio deslogado.
	 * 
	 * @param codChat
	 * @param cliente
	 *            ClienteRemoto do usu�rio que deslogou
	 * @param nome
	 *            Nome do usu�rio que deslogou
	 * @throws RemoteException
	 */
	public void fecharChat(Long codChat, ClienteRemoto cliente, Usuario usuario) throws RemoteException;
	
	/**
	 * Troca a senha para o usu�rio especificado.
	 * 
	 * @param codUsuario
	 * @param novaSenha
	 * @throws RemoteException
	 */
	public void trocarSenha(Long codUsuario, String novaSenha) throws RemoteException;
	
	/**
	 * Troca o nickname para o usu�rio especificado.
	 * 
	 * @param codUsuario
	 * @param novoNickname
	 * @throws RemoteException
	 */
	public void trocarNickname(Long codUsuario, String novoNickname) throws RemoteException;
	
	/**
	 * Procura todos os usu�rios que n�o possuem amizade com o usu�rio solicitante.
	 * 
	 * @param codUsuario
	 * @return Lista de usu�rios que n�o s�o amigos do usu�rio soliciante.
	 * @throws RemoteException
	 */
	public Set<Usuario> getUsuariosDesconhecidos(Long codUsuario) throws RemoteException;
}
