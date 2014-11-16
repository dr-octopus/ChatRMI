package chat.foda.pra.caralho.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import chat.foda.pra.caralho.models.Chat;
import chat.foda.pra.caralho.models.Usuario;
import chat.foda.pra.caralho.telas.TelaCliente;

/**
 * Classe que implementa as a��es do Cliente
 * 
 * @author luiznazari
 */
public class ClienteRemotoImpl extends UnicastRemoteObject implements ClienteRemoto {
	private static final long serialVersionUID = 6754107487405504158L;
	
	private TelaCliente telaCliente;
	
	public ClienteRemotoImpl() throws RemoteException {
		super();
	}
	
	@Override
	public void enviarMensagem(Long chatCodigo, String mensagem) throws RemoteException {
		telaCliente.enviarParaChat(chatCodigo, mensagem);
	}
	
	@Override
	public void enviarParaTodos(String mensagem) throws RemoteException {
		telaCliente.enviarParaTodos(mensagem);
	}
	
	@Override
	public void abrirChat(Chat chat) throws RemoteException {
		telaCliente.iniciarChatExistente(chat);
	}
	
	@Override
	public void desativarChat(Long chatCodigo) throws RemoteException {
		telaCliente.desativarChat(chatCodigo);
	}
	
	@Override
	public void desativarTodosChats() throws RemoteException {
		telaCliente.desativarTodosChats();
	}
	
	@Override
	public void atualizaChat(Chat chat, String mensagem) {
		this.telaCliente.enviarParaChat(chat.getCodigo(), mensagem);
		this.telaCliente.atualizaTelaChat(chat);
	}
	
	@Override
	public void atualizaChat(Long codChat, Usuario usuario, String mensagem) throws RemoteException {
		this.telaCliente.enviarParaChat(codChat, mensagem);
		this.telaCliente.atualizaTelaChatRemoveUsuario(codChat, usuario);
	}
	
	public TelaCliente getTelaCliente() {
		return telaCliente;
	}
	
	public void setTelaCliente(TelaCliente telaCliente) {
		this.telaCliente = telaCliente;
	}
	
}
