package view;

import java.util.List;

import model.Post;
import model.User;
import model.data.DAOFactory;
import model.data.PostDAO;
import model.data.UserDAO;
import model.data.mysql.MySQLPostDAO;
import model.data.mysql.MySQLUserDAO;

public class Main {

	public static void main(String[] args) 
			throws Exception {
		
		PostDAO postDAO = DAOFactory.createPostDAO();
		UserDAO userDAO = DAOFactory.createUserDAO();
		
		
		// Listagem de posts
		List<Post> posts = postDAO.findAll();
		for(Post p : posts) {
			System.out.println("Post: " + p.getContent() 
				+ " User: " + p.getUser().getName());
			
		}
		
		// Listagem de usuários
		List<User> users = userDAO.findAll();
		for(User user : users) {
			System.out.println("Usuário: "+ user.getName());
			
			if (user.getPosts().size() > 0) {
				System.out.println("Posts");
			
				for (Post p : user.getPosts()) {
					System.out.println(p.getContent());
				}
			}
		}
	}
}
