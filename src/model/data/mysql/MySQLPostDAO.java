package model.data.mysql;

import java.sql.Connection;
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
import model.data.mysql.utils.DAOUtils;
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
			DAOUtils.sqlExceptionTreatment("Erro ao inserir post" ,sqle);
		} catch (ModelException me) {
			throw me;
		}
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
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
			preparedStatement.setInt(2, post.getId());
			preparedStatement.executeUpdate();
			
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatment("Erro ao atualizar" ,sqle);
		}
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
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
			preparedStatement.setInt(1, post.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatment("Erro ao deletar post" ,sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
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
	
			setUpUsers(rs, postsList);
	
		} catch (SQLException sqle) {	
			DAOUtils.sqlExceptionTreatment("Erro ao selecionar posts" ,sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}
		return postsList;
	}
	
	@Override
	public List<Post> findByUserId(int userId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Post> postsList = new ArrayList<>();
	
		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSeletc = " select * from posts where user_id = ?";
	
			preparedStatement = connection.prepareStatement(sqlSeletc);
			preparedStatement.setInt(1, userId);
			
			rs = preparedStatement.executeQuery();	
			
			setUpUsers(rs, postsList);
	
		} catch (SQLException sqle) {	
			DAOUtils.sqlExceptionTreatment("Erro ao selecionar posts" ,sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return postsList;
	}
	
	private void setUpUsers(ResultSet rs, List<Post> postsList) throws SQLException{
		while (rs.next()) {
			int postId = rs.getInt("id");
			String postContent = rs.getString("content");
			Date postDate = rs.getDate("post_date");
			int userIdRs = rs.getInt("user_id");

			Post newPost = new Post(postId);
			newPost.setContent(postContent);
			newPost.setDate(postDate);
			
			User postUser = new User(userIdRs);
			newPost.setUser(postUser);
			
			postsList.add(newPost);
		}
	}
}
