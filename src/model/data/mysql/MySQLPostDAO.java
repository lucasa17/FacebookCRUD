package model.data.mysql;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.ModelException;
import model.Post;
import model.User;
import model.data.PostDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLPostDAO implements PostDAO {

	@Override
	public void save(Post post) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlIsert = " INSERT INTO posts "
					+ " VALUES (DEFAULT, ?, CURDATE(), ?); ";
			
			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, post.getContent());
			preparedStatement.setInt(2, post.getUser().getId());
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException sqle) {
			sqlExceptionTreatment("Erro ao inserir post" ,sqle);
		} catch (ModelException me) {
			throw me;
		}
		finally {
			close(preparedStatement);
			close(connection);
		}
	}

	@Override
	public void update(Post post) throws ModelException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = " UPDATE posts "
					+ " SET "
					+ " content = ?, "
					+ " post_date = CURDATE() "
					+ " WHERE id = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, post.getContent());
			preparedStatement.setInt(2, post.getUser().getId());
			
			
		} catch (SQLException sqle) {
			sqlExceptionTreatment("Erro ao atualizar" ,sqle);
		}
		finally {
			close(preparedStatement);
			close(connection);
		}
	}

	@Override
	public void delete(Post post) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlDelete = "delete from posts where id = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, post.getUser().getId());
		} catch (SQLException sqle) {
			sqlExceptionTreatment("Erro ao deletar post" ,sqle);
		} finally {
			close(preparedStatement);
			close(connection);
		}
	}

	@Override
	public List<Post> findAll() throws ModelException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Post> postsList = new ArrayList<>();
	
		try {
			connection = MySQLConnectionFactory.getConnection();

	
			statement = connection.createStatement();
			String sqlSeletc = " select * from posts ";
	
			rs = statement.executeQuery(sqlSeletc);
	
			while (rs.next()) {
				int postId = rs.getInt("id");
				String postContent = rs.getString("content");
				Date postDate = rs.getDate("post_date");
				int userId = rs.getInt("user_id");
	
				Post newPost = new Post(postId);
				newPost.setContent(postContent);
				newPost.setDate(postDate);
				
				User postUser = new User(userId);
				newPost.setUser(postUser);
				
				postsList.add(newPost);
			}
	
		} catch (SQLException sqle) {	
			sqlExceptionTreatment("Erro ao selecionar posts" ,sqle);
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
		return postsList;
	}

	private void sqlExceptionTreatment( String errorMessage, SQLException sqle) throws ModelException {
		if (sqle.getCause() != null)
			if (sqle.getCause().getCause() != null)
				if (sqle.getCause().getCause() 
						instanceof ConnectException) {
					throw new ModelException("Banco de dados fora do ar.", sqle.getCause().getCause());
				}
		throw new ModelException("errorMessage", sqle);
	}
	
	private void close(AutoCloseable resource) throws ModelException{
		if (resource != null)
			try {
				resource.close();
			} catch (Exception e) {
				throw new ModelException("Erro ao fechar o recurso "+ resource);
			}
	}

}
