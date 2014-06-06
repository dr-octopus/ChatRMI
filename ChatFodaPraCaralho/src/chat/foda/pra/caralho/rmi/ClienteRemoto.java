package chat.foda.pra.caralho.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Declara��o de todos os m�todos executados no servidor vis�veis para o Cliente
 * 
 * @author luiznazari
 */
public interface ClienteRemoto extends Remote {

	public String escuta() throws RemoteException;
	
	public void enviaMensagem(String mensagem) throws RemoteException;
}
