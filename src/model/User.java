package model;

import java.util.List;

public class User {
	private int id;
	private String name;
	private UserGender gender;
	private String email;
	private List<Post> posts;
	
	public User(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserGender getGender() {
		return gender;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public int getId() {
		return id;
	}
	
	public void validate() {
		if(name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome do usuário não pode ser vazio");
		}
		
		if(gender == null) {
			throw new IllegalArgumentException("O gênero do usuário não pode ser vazio");
		}
		
		if(email == null || email.isBlank()) {
			throw new IllegalArgumentException("O email do usuário não pode ser vazio");	
		}
	}
}
