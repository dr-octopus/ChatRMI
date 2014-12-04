package chat.foda.pra.caralho.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import chat.foda.pra.caralho.models.Chat;
import chat.foda.pra.caralho.models.Usuario;

/**
 * Declara��o de todos os m�todos executados no Cliente, vis�veis para o Servidor
 * 
 * @author luiznazari
 */
public interface ClienteRemoto extends Remote {
	
	/**
	 * Envia a mensagem para o cliente, a mesma ser� entregue para o chat que possuir o c�digo especificado.
	 * 
	 * @param codChat
	 *            Chat destino
	 * @param mensagem
	 * @throws RemoteException
	 */
	public void enviarMensagem(Long codChat, String mensagem) throws RemoteException;
	
	/**
	 * Envia a mensagem para todos os chats abertos que o cliente possui.
	 * 
	 * @param mensagem
	 * @throws RemoteException
	 */
	public void enviarParaTodos(String mensagem) throws RemoteException;
	
	/**
	 * Abre um novo chat para o cliente.
	 * 
	 * @param chat
	 * @throws RemoteException
	 */
	public void abrirChat(Chat chat) throws RemoteException;
	
	/**
	 * Inativa um chat do cliente que possua o c�digo especificado. N�o ser� mais poss�vel enviar mensagens atrav�s do
	 * mesmo, a menos que seja reativado.
	 * 
	 * @param codChat
	 * @param mensagem
	 * @throws RemoteException
	 */
	public void desativarChat(Long codChat, String mensagem) throws RemoteException;
	
	/**
	 * Inativa todos os chats que o cliente possui.
	 * 
	 * @throws RemoteException
	 */
	public void desativarTodosChats() throws RemoteException;
	
	/**
	 * Adiciona um participante para o chat, enviando uma mensagem para notificar e atualiza o chat e a respectiva tela
	 * do cliente.
	 * 
	 * @param chat
	 * @throws RemoteException
	 */
	public void atualizaChat(Chat chat, String mensagem) throws RemoteException;
	
	/**
	 * Remove um participante do chat, enviando uma mensagem para notificar e atualiza o chat e a respectiva tela do
	 * cliente.
	 * 
	 * @param codChat
	 * @param usuario
	 * @throws RemoteException
	 */
	public void removeAmigoDoChat(Long codChat, Usuario usuario, String mensagem) throws RemoteException;
	
}
