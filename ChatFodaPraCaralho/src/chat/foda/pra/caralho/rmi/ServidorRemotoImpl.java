package chat.foda.pra.caralho.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalTime;

import chat.foda.pra.caralho.dao.AmigosDAO;
import chat.foda.pra.caralho.dao.UsuarioDAO;
import chat.foda.pra.caralho.dao.factory.DaoFactory;
import chat.foda.pra.caralho.models.Chat;
import chat.foda.pra.caralho.models.Usuario;
import chat.foda.pra.caralho.models.UsuarioLogado;
import chat.foda.pra.caralho.telas.TelaServidor;

public class ServidorRemotoImpl extends UnicastRemoteObject implements ServidorRemoto {
	
	/**
	 * Classe que implementa as a��es do Servidor
	 * 
	 * @author luiznazari
	 */
	private static final long serialVersionUID = -8382898850011577230L;
	
	private TelaServidor telaServidor;
	
	private Map<Long, ClienteRemoto> clientesConectados = new HashMap<>();
	
	private Map<Long, ArrayList<ClienteRemoto>> chatsAbertos = new HashMap<>();
	
	private Long autoIncrementChatId = 0l;
	
	private UsuarioDAO usuarioDAO;
	
	private AmigosDAO amigosDAO;
	
	public ServidorRemotoImpl() throws RemoteException {
		super();
		
		usuarioDAO = DaoFactory.get().usuarioDao();
		amigosDAO = DaoFactory.get().amigosDao();
	}
	
	public void setTelaServidor(TelaServidor telaServidor) {
		this.telaServidor = telaServidor;
	}
	
	public Integer getNumeroUsuariosLogados() {
		return clientesConectados.size();
	}
	
	public void enviarMensagemParaTodosClientes(String mensagem) throws RemoteException {
		for (ClienteRemoto cliente : clientesConectados.values()) {
			cliente.enviarParaTodos(mensagem);
		}
	}
	
	/**
	 * Envia mensagem para todos os clientes que possuem o chat com o c�digo especificado
	 * 
	 * @param codChat
	 * @param mensagem
	 */
	@Override
	public void enviarMensagemParaAmigos(Long codChat, String mensagem) throws RemoteException {
		for (ClienteRemoto cliente : chatsAbertos.get(codChat)) {
			cliente.enviarMensagem(codChat, mensagem);
		}
	}
	
	public void encerrarServicos() throws RemoteException {
		for (ClienteRemoto cliente : clientesConectados.values()) {
			cliente.desativarTodosChats();
		}
	}
	
	@Override
	public UsuarioLogado login(ClienteRemoto cliente, String email, String senha) throws RemoteException {
		Usuario usuario = usuarioDAO.findOneByEmail(email);
		if (usuario != null && !clientesConectados.containsKey(usuario.getCodigo())) {
			if (usuario.getSenha().equals(senha)) {
				UsuarioLogado usuarioLogado = new UsuarioLogado(usuario);
				clientesConectados.put(usuario.getCodigo(), cliente);
				telaServidor.escreverNoConsole("[" + LocalTime.now() + "] O usu�rio '"
				        + usuarioLogado.getUsuario().getPessoa().getNomeCompleto() + "' se conectou.");
				telaServidor.atualizaContador(this.getNumeroUsuariosLogados().toString());
				return usuarioLogado;
			}
		}
		return null;
	}
	
	@Override
	public void logout(Set<Long> codigos, ClienteRemoto cliente, String nome) throws RemoteException {
		// Fecha todos os chats do usu�rio
		try {
			for (Long codigo : codigos) {
				fecharChat(codigo, cliente, nome);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		clientesConectados.remove(nome);
		telaServidor.escreverNoConsole("[" + LocalTime.now() + "] O usu�rio '" + nome + "' se desconectou.");
		telaServidor.atualizaContador(this.getNumeroUsuariosLogados().toString());
	}
	
	@Override
	public boolean cadastrarUsuario(Usuario usuario) throws RemoteException {
		if (usuarioDAO.findOneByEmail(usuario.getEmail()) == null) {
			usuarioDAO.save(usuario);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void removerUsuario(Usuario usuario) throws RemoteException {
		usuarioDAO.delete(usuario);
	}
	
	@Override
	public void adicionaAmigo(Long codUsuario, Long codAmigo) throws RemoteException {
		amigosDAO.save(codUsuario, codAmigo);
	}
	
	@Override
	public void removerAmigo(Long codUsuario, Long codAmigo) throws RemoteException {
		amigosDAO.deleteOne(codUsuario, codAmigo);
	}
	
	@Override
	public Set<Usuario> getUsuariosDesconhecidos(Long codUsuario) throws RemoteException {
		return amigosDAO.findAllDesconhecidosByCodUsiario(codUsuario);
	}
	
	@Override
	public Chat criarChat(Usuario solicitante, Usuario amigo) {
		try {
			ClienteRemoto amigoCliente = clientesConectados.get(amigo.getCodigo());
			
			Chat chat = new Chat(autoIncrementChatId++);
			chat.adicionaUsuario(solicitante);
			chat.adicionaUsuario(amigo);
			
			ArrayList<ClienteRemoto> participantes = new ArrayList<>();
			participantes.add(clientesConectados.get(solicitante.getCodigo()));
			participantes.add(amigoCliente);
			
			amigoCliente.abrirChat(chat);
			chatsAbertos.put(chat.getCodigo(), participantes);
			return chat;
		} catch (RemoteException e) {
			telaServidor.escreverNoConsole("Ocorreu um erro ao abrir o char para os usu�rios: "
			        + solicitante.getPessoa().getNomeCompleto() + ", " + amigo.getPessoa().getNomeCompleto() + ".");
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public void fecharChat(Long codigo, ClienteRemoto cliente, String nome) throws RemoteException {
		ArrayList<ClienteRemoto> clientesNoChat = chatsAbertos.get(codigo);
		
		if (clientesNoChat.size() < 3) {
			for (ClienteRemoto cliente2 : clientesNoChat) {
				cliente2.desativarChat(codigo);
			}
			chatsAbertos.remove(codigo);
		} else {
			clientesNoChat.remove(cliente);
			for (ClienteRemoto cliente2 : clientesNoChat) {
				cliente2.enviarMensagem(codigo, "O usu�rio " + nome + " saiu da conversa.");
			}
		}
		
	}
	
	@Override
	public void trocarNickname(Long codigo, String novoNickname) throws RemoteException {
		Usuario usuario = usuarioDAO.findOne(codigo);
		usuario.setNickName(novoNickname);
		usuarioDAO.update(usuario);
	}
	
	@Override
	public void trocarSenha(Long codigo, String novaSenha) throws RemoteException {
		Usuario usuario = usuarioDAO.findOne(codigo);
		usuario.setSenha(novaSenha);
		usuarioDAO.update(usuario);
	}
	
}
