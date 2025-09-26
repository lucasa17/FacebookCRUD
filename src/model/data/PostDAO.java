package model.data;

import java.util.List;

import model.ModelException;
import model.Post;

public interface PostDAO {
	void save(Post post) throws ModelException;
	void update(Post post) throws ModelException;
	void delete(Post post) throws ModelException;
	List<Post> findAll() throws ModelException;
	List<Post> findByUserId(int userId) throws ModelException;
}
