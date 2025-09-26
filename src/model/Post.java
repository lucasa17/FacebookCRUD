package model;

import java.util.Date;

public class Post {
	private int id;
	private String content;
	private Date date;
	private User user;
	
	public Post(int id) {
		this.id = id; 
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void validate() {;
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("Conteúdo do Post não pode ser vazio");
		}
		
		if (user == null || user.getId() > 0){
			throw new IllegalArgumentException("Usuário inválido");
		}
	}
}
