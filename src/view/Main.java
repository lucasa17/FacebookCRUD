package view;

import java.util.List;

import model.Post;
import model.User;
import model.data.PostDAO;
import model.data.UserDAO;
import model.data.mysql.MySQLPostDAO;
import model.data.mysql.MySQLUserDAO;

public class Main {

	public static void main(String[] args) 
			throws Exception {

		PostDAO postDAO = new MySQLPostDAO();
		UserDAO userDAO = new MySQLUserDAO();
		
		//Novo post
		//Post newPost = new Post(0);
		//newPost.setContent("Post via DAO");
		//newPost.setUser(new User(1));
		//postDAO.save(newPost);
		
		//Atualizar um post
		//Post existingPost = new Post(8);
		//existingPost.setContent("Post via DAO atualizado");
		//postDAO.update(existingPost);
		
		//Post postToDelete = new Post(6);
		//postDAO.delete(postToDelete);
		
		//Listagem de posts		
		//List<Post> posts = postDAO.findAll();
		
		//for(Post p : posts) {
			//System.out.println(p.getContent() + " " + p.getUser().getName());
		//}
		
		
		
		
		//User maria = new User(0);
		//maria.setName("Maria via DAO");
		//maria.setGender('M');
		//maria.setEmail("maria@gmail.com");
		//userDAO.save(maria);
		
		//User maria = new User(6);
		//maria.setName("Maria Isabel");
		//maria.setGender('F');
		//maria.setEmail("maria@gmail.com");
		//userDAO.update(maria);
		
		//User userToDelete = new User(6);
		//userDAO.delete(userToDelete);
		
		List<User> users = userDAO.findAll();
		
		for(User user : users) {
			System.out.println("Usuário: " + user.getName());
			
			if(user.getPosts().size() > 0) {
				System.out.println("Posts: ");
				
				for(Post p : user.getPosts()) {
					System.out.println(p.getContent());
				}
			}
		}

	}
}