package chat.foda.pra.caralho.telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import chat.foda.pra.caralho.clienteServidor.ClienteRmi;
import chat.foda.pra.caralho.models.Chat;
import classes.Fodas.Pra.Caralho.GridConstraints;

public class TelaCliente extends JFrame {
	
	/**
	 * @author luiznazari
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenuBar jmbMenuBar;
	
	private JMenu jmnArquivo;
	
	private JMenuItem jmiSair;
	
	private JMenu jmnAmigo;
	
	private JMenuItem jmiAdicionarAmigo;
	
	private JMenuItem jmiRemoverAmigo;
	
	private JMenu jmnAjuda;
	
	private JMenuItem jmiComandos;
	
	private JPanel pnlMain;
	
	private JPanel pnlUsuario;
	
	private JLabel jlbBemVindo;
	
	private JLabel jlbNomeUsuario;
	
	private JSeparator jsepListaDeAmigos;
	
	private JList<String> jlstAmigos;
	
	private JSplitPane jspChat;
	
	private JScrollPane jspAmigos;
	
	private DefaultListModel<String> dlmAmigos;
	
	private ArrayList<TelaChat> chatList;
	
	private ClienteRmi cliente;
	
	private String nickName;
	
	private MyJTabbedPane jtpChat;
	
	private JPanel jpnAreaChat;
	
	private Dimension minDimensao;
	
	public TelaCliente(ClienteRmi cliente) {
		this.cliente = cliente;
		this.cliente.getClienteService().setTelaCliente(this);
		this.nickName = cliente.getUsuarioLogado().getUsuario().getNickName();
		this.chatList = new ArrayList<>();
		this.minDimensao = new Dimension(200, 400);
		
		setTitle(nickName);
		setContentPane(getMainPanel());
		setJMenuBar(getMenu());
		addWindowListener(new EventosTelaCliente(this));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(550, 400));
		setSize(550, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JMenuBar getMenu() {
		jmbMenuBar = new JMenuBar();
		
		jmnArquivo = new JMenu("Arquivo");
		jmbMenuBar.add(jmnArquivo);
		
		jmiSair = new JMenuItem("Sair");
		jmnArquivo.add(jmiSair);
		
		jmnAmigo = new JMenu("Amigos");
		jmbMenuBar.add(jmnAmigo);
		
		jmiAdicionarAmigo = new JMenuItem("Adicionar");
		jmnAmigo.add(jmiAdicionarAmigo);
		
		jmiRemoverAmigo = new JMenuItem("Remover");
		jmnAmigo.add(jmiRemoverAmigo);
		
		jmnAjuda = new JMenu("Ajuda");
		jmbMenuBar.add(jmnAjuda);
		
		jmiComandos = new JMenuItem("Comandos");
		jmnAjuda.add(jmiComandos);
		
		getAcoesMenu();
		
		return jmbMenuBar;
	}
	
	private void getAcoesMenu() {
		jmiSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		jmiAdicionarAmigo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nomeAmigo = JOptionPane.showInputDialog(null, "Digite o nome do amigo:");
				if (!cliente.getUsuarioLogado().getUsuario().getNomeCompleto().equals(nomeAmigo)
				        && !dlmAmigos.contains(nomeAmigo)) {
					try {
						if (cliente.getService().adicionaAmigo(cliente.getUsuarioLogado().getUsuario(), nomeAmigo)) {
							dlmAmigos.addElement(nomeAmigo);
						} else {
							JOptionPane.showMessageDialog(null, "Usu�rio n�o cadastrado.");
						}
					} catch (RemoteException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Conex�o - Erro ao adicionar novo amigo.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Nome digitado inv�lido ou amigo j� adicionado.");
				}
			}
		});
		
		jmiRemoverAmigo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nomeAmigo = JOptionPane.showInputDialog(null, "Digite o nome do amigo:");
				if (nomeAmigo != null && dlmAmigos.contains(nomeAmigo)) {
					try {
						cliente.getService().removerAmigo(cliente.getUsuarioLogado().getUsuario(), nomeAmigo);
						dlmAmigos.removeElement(nomeAmigo);
						JOptionPane.showMessageDialog(null, "Amigo removido com sucesso!");
					} catch (RemoteException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Conex�o - Erro ao remover amigo.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Amigo n�o encontrado");
				}
			}
		});
		
		jmiComandos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Enter = Enviar mensagem\n" + "Shift + Enter = Quebrar linha",
				        "Key Commands", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	private JPanel getMainPanel() {
		pnlMain = new JPanel(new BorderLayout());
		
		jspChat = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getPainelDoUsuario(), getChatPanel());
		jspChat.setContinuousLayout(true);
		jspChat.setOneTouchExpandable(true);
		jspChat.setDividerLocation(200);
		pnlMain.add(jspChat, BorderLayout.CENTER);
		
		acoes();
		return pnlMain;
	}
	
	private JPanel getPainelDoUsuario() {
		pnlUsuario = new JPanel(new GridBagLayout());
		pnlUsuario.setMinimumSize(minDimensao);
		
		jlbBemVindo = new JLabel("Bem Vindo,");
		pnlUsuario.add(jlbBemVindo,
		        new GridConstraints().setAnchor(GridConstraints.WEST).setInsets(5, 5, 0, 5).setFill(GridConstraints.BOTH)
		                .setOccupiedSize(GridConstraints.REMAINDER, 1));
		
		jlbNomeUsuario = new JLabel(nickName);
		pnlUsuario.add(jlbNomeUsuario,
		        new GridConstraints().setAnchor(GridConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridConstraints.BOTH)
		                .setOccupiedSize(GridConstraints.REMAINDER, 1));
		
		jsepListaDeAmigos = new JSeparator();
		pnlUsuario.add(jsepListaDeAmigos,
		        new GridConstraints().setAnchor(GridConstraints.CENTER).setInsets(5).setFill(GridConstraints.HORIZONTAL)
		                .setOccupiedSize(GridConstraints.REMAINDER, 1));
		
		dlmAmigos = new DefaultListModel<String>();
		jlstAmigos = new JList<String>(getListaAmigos());
		jspAmigos = new JScrollPane(jlstAmigos);
		
		pnlUsuario.add(jspAmigos,
		        new GridConstraints().setAnchor(GridConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridConstraints.BOTH)
		                .setOccupiedSize(GridConstraints.REMAINDER, GridConstraints.REMAINDER).setGridWeight(1, 1));
		
		return pnlUsuario;
	}
	
	private JPanel getChatPanel() {
		jpnAreaChat = new JPanel(new BorderLayout());
		jpnAreaChat.setMinimumSize(new Dimension(minDimensao));
		jpnAreaChat.setBorder(new EmptyBorder(5, 5, 5, 5));
		jtpChat = new MyJTabbedPane();
		jpnAreaChat.add(jtpChat, BorderLayout.CENTER);
		
		return jpnAreaChat;
	}
	
	private void acoes() {
		
		jlbNomeUsuario.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				jlbNomeUsuario.setForeground(Color.BLACK);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				jlbNomeUsuario.setForeground(Color.blue);
				jlbNomeUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					String novoNickname = JOptionPane.showInputDialog(null, "Digite o novo nickname:");
					if (!novoNickname.isEmpty() && !novoNickname.equals(" ")) {
						nickName = novoNickname;
						jlbNomeUsuario.setText(novoNickname);
					}
					cliente.getService().atualizarNickname(cliente.getUsuarioLogado().getUsuario().getNomeCompleto(),
					        novoNickname);
				} catch (RemoteException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Conex�o - Erro ao alterar nickname");
				} catch (NullPointerException e) {}
			}
		});
		
		jlstAmigos.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					String nomeAmigo = jlstAmigos.getSelectedValue();
					abrirChat(nomeAmigo);
				}
				
			}
		});
	}
	
	public void abrirChat(String nomeAmigo) {
		final TelaChat telaChat = new TelaChat(this);
		
		Chat chat = null;
		
		try {
			chat = cliente.getService().criarChat(cliente.getUsuarioLogado().getUsuario(), nomeAmigo);
		} catch (RemoteException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Conex�o - Erro ao abrir chat");
		}
		
		if (chat != null) {
			telaChat.setChat(chat);
			chatList.add(telaChat);
			jtpChat.addNewTab(nomeAmigo, telaChat.getChatPanel());
		} else {
			JOptionPane.showMessageDialog(this, "O usu�rio selecionado n�o est� logado", "Conex�o",
			        JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	public void iniciarChatExistente(Chat chat, String nomeAmigo) {
		final TelaChat telaChat = new TelaChat(this);
		
		telaChat.setChat(chat);
		
		chatList.add(telaChat);
		jtpChat.addNewTab(nomeAmigo, telaChat.getChatPanel());
	}
	
	public DefaultListModel<String> getListaAmigos() {
		try {
			for (String nomeDoAmigo : cliente.getUsuarioLogado().getUsuario().getAmigos()) {
				dlmAmigos.addElement(nomeDoAmigo);
			}
		} catch (NullPointerException e) {}
		return dlmAmigos;
	}
	
	public void enviarParaTodos(String mensagem) {
		for (TelaChat tc : chatList) {
			tc.recebeMensagem(mensagem);
		}
	}
	
	public void enviarParaChat(Integer chatCodigo, String mensagem) {
		for (TelaChat tc : chatList) {
			if (tc.getChat().getCodigo().equals(chatCodigo)) {
				tc.recebeMensagem(mensagem);
				break;
			}
		}
	}
	
	public void desativarChat(Integer chatCodigo) {
		for (TelaChat tc : chatList) {
			if (tc.getChat().getCodigo().equals(chatCodigo)) {
				tc.desativaChat("O amigo se desconectou.");
				break;
			}
		}
	}
	
	public void desativarTodosChats() {
		for (TelaChat tc : chatList) {
			tc.desativaChat("O servidor est� offline.");
		}
	}
	
	public ArrayList<Long> getCodigosChat() {
		ArrayList<Long> codigos = new ArrayList<>();
		for (TelaChat tc : chatList) {
			codigos.add(tc.getChat().getCodigo());
		}
		return codigos;
	}
	
	public ClienteRmi getCliente() {
		return this.cliente;
	}
	
	public String getNickName() {
		return nickName;
	}
	
}