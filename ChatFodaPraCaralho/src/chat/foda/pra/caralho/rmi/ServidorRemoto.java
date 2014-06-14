package chat.foda.pra.caralho.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import chat.foda.pra.caralho.modelo.Usuario;
import chat.foda.pra.caralho.modelo.UsuarioLogado;

/**
 * Declara��o de todos os m�todos executados no servidor vis�veis para o Cliente
 * 
 * @author luiznazari
 */
public interface ServidorRemoto extends Remote {
	
	public UsuarioLogado login(ClienteRemoto cliente, String nome, String senha) throws RemoteException;
	
	public boolean cadastrarUsuario(String nome, String senha) throws RemoteException;
	
	public void removerUsuario(String nome) throws RemoteException;
	
	public void enviarMensagemParaServidor(Integer chatCodigo, String mensagem) throws RemoteException;
	
	public void logout(ClienteRemoto cliente, String nome) throws RemoteException;
	
	public boolean adicionaAmigo(Usuario usuario, String nomeAmigo) throws RemoteException;
	
	public void removerAmigo(Usuario usuario, String nomeAmigo) throws RemoteException;
}
