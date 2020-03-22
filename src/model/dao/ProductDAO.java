package model.dao;

import java.util.List;

import model.entities.Product;

public interface ProductDAO {
	
	public void insert(Product product);
	public Product findById(Integer identifier);
	public List<Product> findByName(String name);
	public List<Product> findAll();
	public void update(Product product);
	public void deleteById(Integer identifier);

}
