package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Post;
import model.User;
import model.UserGender;
import model.data.DAOUtils;
import model.data.UserDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLUserDAO implements UserDAO {

	@Override
	public void save(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlIsert = " INSERT INTO "
					        + " users VALUES "
					        + " (DEFAULT, ?, ?, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getGender().toString());
			preparedStatement.setString(3, user.getEmail());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir user do BD.", sqle);
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
					         + " set "
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
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar user do BD.", sqle);
		} catch (ModelException me) {
			throw me;
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

			String sqlUpdate = " DELETE FROM users WHERE id = ?; ";

			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setInt(1, user.getId());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao deletar user do BD.", sqle);
		} catch (ModelException me) {
			throw me;
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
			String sqlSelect = " SELECT * FROM users; ";

			rs = statement.executeQuery(sqlSelect);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("nome");
				String genderStr = rs.getString("sexo");
				UserGender gender = 
					    genderStr.equals("M") ? UserGender.M :
					    genderStr.equals("F") ? UserGender.F : 
					    genderStr.equals("T") ? UserGender.T ;

				
				String email = rs.getString("email");
				
				User user = new User(id);
				user.setName(name);
				user.setGender(gender);
				user.setEmail(email);
				
				List<Post> posts = new MySQLPostDAO().findByUserId(id);
				user.setPosts(posts);
				
				usersList.add(user);
			}

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar posts do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}

		return usersList;
	}
}
