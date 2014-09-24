package chat.foda.pra.caralho.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import chat.foda.pra.caralho.telas.AutorizacaoBanco;

public abstract class ConexaoUtil {

	protected static Connection conexao;

	static {
		String url = "jdbc:mysql://localhost/chatFodaPraCaralho";

		if (!Log.existeLog()) {
			AutorizacaoBanco conf = new AutorizacaoBanco();
			Log.criarArqLog();
			Log.escrever(conf.getUsuario());
			Log.escrever(conf.getSenha());
		}

		String usuario = Log.ler()[0], senha = Log.ler()[1];
		
		try {
			conexao = DriverManager.getConnection(url, usuario, senha);
			conexao.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("Erro ao criar conex�o");
			e.printStackTrace();
		}
	}

	public static void fechaConexao() {
		try {
			conexao.close();
		} catch (SQLException e) {
			System.out.println("Erro ao fechar conex�o");
			e.printStackTrace();
		}
	}

	public static Connection getConexao() {
		return conexao;
	}
	
}
