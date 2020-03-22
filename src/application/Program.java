package application;

import java.util.List;

import db.DB;
import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.entities.Product;

public class Program {

	public static void main(String[] args) {
		ProductDAO productDAO = DAOFactory.createProductDAO();
		
		/* test insert */
		Product product = new Product("Mouse", "Wireless mouse", 59.90);
		productDAO.insert(product);
		
		/* test findById */
		product = productDAO.findById(3);
		System.out.println(product);
		
		/* test findByName */
		List<Product> products = productDAO.findByName("Mouse");
		for (Product p : products) {
			System.out.println(p);
		}
		
		/* test findAll */
		products = productDAO.findAll();
		for (Product p : products) {
			System.out.println(p);
		}
		
		/* test update */
		product.setPrice(45.00);
		productDAO.update(product);
		System.out.println(product);
		
		/* test deleteById */
		productDAO.deleteById(3);
		products = productDAO.findAll();
		for (Product p : products) {
			System.out.println(p);
		}
		
		
		DB.closeConnection();
	}
	
}
