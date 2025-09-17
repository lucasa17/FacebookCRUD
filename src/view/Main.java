package view;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Main {

	// Driver
	private static final String DRIVER_NAME = 
			"com.mysql.cj.jdbc.Driver";

	// URL de conexão
	private static final String DB_URL = 
			"jdbc:mysql://127.0.0.1/facebook";

	// Credenciais
	private static final String USER = "root";
	private static final String PW = "";

	public static void main(String[] args) 
			throws Exception {

		// update("Olá do Noé atualizado via código", 4);
		// insert("Olá do Emerson Via Código", 2);
		// read();
		// delete(2);
	}

	public static void update (String content, int postId) 
			throws Exception {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		// Carga do Driver do Mysql
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver JDBC não encontrado.");
			throw cnfe;
		}
		
		try {
			connection = DriverManager.getConnection(DB_URL, USER, PW);
			
			String sqlUpdate = " UPDATE posts "
					+ " SET "
					+ " content = ?, "
					+ " post_date = CURDATE() "
					+ " WHERE id = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, content);
			preparedStatement.setInt(2, postId);
			
			int rowsAfected = preparedStatement.executeUpdate();
			
			System.out.println(rowsAfected > 0 ? 
					"Post atualizado com sucesso" : 
						"Erro ao atualizar post");
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();
			
			if (connection != null)
				connection.close();
		}
	}
	
	public static void insert(String content, int userId)
			throws Exception{
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		// Carga do Driver do Mysql
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver JDBC não encontrado.");
			throw cnfe;
		}
		
		try {
			connection = DriverManager.getConnection(DB_URL, USER, PW);
			
			String sqlIsert = " INSERT INTO posts "
					+ " VALUES (DEFAULT, ?, CURDATE(), ?); ";
			
			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, content);
			preparedStatement.setInt(2, userId);
			
			int rowsAfected = preparedStatement.executeUpdate();
			
			System.out.println(rowsAfected > 0 ? 
					"Post inserido com sucesso" : "Erro ao inserir post");
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();
			
			if (connection != null)
				connection.close();
		}
	}

	public static void delete(int postId) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		// Carga do Driver do Mysql
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver JDBC não encontrado.");
			throw cnfe;
		}

		try {
			// Estabelecimento da conexão com o BD
			connection = DriverManager.getConnection(
					DB_URL, USER, PW);

			String sqlDelete = "delete from posts where id = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, postId);

			int rowsAfected = preparedStatement.executeUpdate();

			String message = rowsAfected > 0 ? "Deletado com sucesso" 
					: "Post não existe";
			System.out.println(message);

		} catch (SQLException sqle) {
			System.out.println("Erro ao conectar no Banco de Dados.");

			if (sqle.getCause() != null)
				if (sqle.getCause().getCause() != null)
					if (sqle.getCause().getCause() 
							instanceof ConnectException) {
						System.err.println("Banco de dados fora do ar.");
						throw sqle;
					}

			System.err.println(sqle.getMessage());
			throw sqle;
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();

			if (connection != null)
				connection.close();
		}

		System.out.println("Execução sem erros.");
	}

	public static void read() throws Exception {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		// Carga do Driver do Mysql
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver JDBC não encontrado.");
			throw cnfe;
		}

		try {
			// Estabelecimento da conexão com o BD
			connection = DriverManager.getConnection(
					DB_URL, USER, PW);

			statement = connection.createStatement();
			String sqlSeletc = " select u.nome, p.content, p.post_date "
					+ " from users u "
					+ " inner join posts p "
					+ " on u.id = p.user_id; ";

			rs = statement.executeQuery(sqlSeletc);

			while (rs.next()) {
				String userName = rs.getString("nome");
				String postContent = rs.getString("content");
				Date postDate = rs.getDate("post_date");

				System.out.println("Nome: " + userName);
				System.out.println("Post: " + postContent);
				System.out.println("Data do post: " + postDate.toString());
				System.out.println("----------");
			}

		} catch (SQLException sqle) {
			System.out.println("Erro ao conectar no Banco de Dados.");

			if (sqle.getCause() != null)
				if (sqle.getCause().getCause() != null)
					if (sqle.getCause().getCause() 
							instanceof ConnectException) {
						System.err.println("Banco de dados fora do ar.");
						throw sqle;
					}

			System.err.println(sqle.getMessage());
			throw sqle;
		} finally {
			if (rs != null)
				rs.close();

			if (statement != null)
				statement.close();

			if (connection != null)
				connection.close();
		}

		System.out.println("Execução sem erros.");
	}
}