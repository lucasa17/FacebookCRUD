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
import model.data.UserDAO;
import model.data.mysql.utils.DAOUtils;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLUserDAO implements UserDAO{

	@Override
	public void save(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlIsert = "insert into users values (DEFAULT, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getGender().toString());
			preparedStatement.setString(3, user.getEmail());
			
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
	public void update(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = " UPDATE users "
					+ " SET "
					+ " nome = ?, "
					+ " sexo = ?, "
					+ " email = ? "
					+ " WHERE id = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getGender().toString());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setInt(4, user.getId());
			
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
	public void delete(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlDelete = " delete from users "
					+ " WHERE id = ?; ";
			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, user.getId());
			
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
	public List<User> findAll() throws ModelException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<User> usersList = new ArrayList<>();
	
		try {
			connection = MySQLConnectionFactory.getConnection();

	
			statement = connection.createStatement();
			String sqlSeletc = " select * from users ";
	
			rs = statement.executeQuery(sqlSeletc);
	
			while (rs.next()) {
				int userId = rs.getInt("id");
				String userName = rs.getString("nome");
				String userGender = rs.getString("sexo");
				String userEmail = rs.getString("email");
	
				User user = new User(userId);
				user.setName(userName);
				user.setGender(userGender.charAt(0));
				user.setEmail(userEmail);
				
				List<Post> posts = new MySQLPostDAO().findByUserId(userId);
				user.setPosts(posts);
				
				usersList.add(user);
			}
	
		} catch (SQLException sqle) {	
			DAOUtils.sqlExceptionTreatment("Erro ao selecionar users" ,sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}
		return usersList;
	}

}
