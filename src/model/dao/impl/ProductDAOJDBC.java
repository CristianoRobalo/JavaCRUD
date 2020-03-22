package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.ProductDAO;
import model.entities.Product;

public class ProductDAOJDBC implements ProductDAO{
	
	private Connection connection;
	
	public ProductDAOJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Product product) {
		PreparedStatement statement = null;
		
		try {
			connection = DB.getConnection();
			statement = connection.prepareStatement("insert into product(name, "
													+ "description, price) "
													+ "values(?, ?, ?)"
													, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, product.getName());
			statement.setString(2, product.getDescription());
			statement.setDouble(3, product.getPrice());
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					int productId = resultSet.getInt(1);
					product.setProductId(productId);
				}
				
				DB.closeResultSet(resultSet);
			}
			else {
				throw new DBException("Erro ao inserir dados. Nenhum dado foi inserido.");
			}
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
	}

	@Override
	public Product findById(Integer identifier) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			statement = connection.prepareStatement("select productId as id, "
													+ "name, description, price "
													+ "from product "
													+ "where productId = ?");
			statement.setInt(1, identifier);
			
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				Product product = instantiateProduct(resultSet);
				return product;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Product> findByName(String name) {		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			statement = connection.prepareStatement("select productId as id, "
													+ "name, description, price "
													+ "from product "
													+ "where name = ? "
													+ "order by name");
			statement.setString(1, name);
			
			List<Product> products = new ArrayList<Product>();
			resultSet = statement.executeQuery();			
			while (resultSet.next()) {
				products.add(instantiateProduct(resultSet));
			}
			return products;
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Product> findAll() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			statement = connection.prepareStatement("select productId as id, name, "
													+ "description, price " 
													+ "from product order by id");
			
			List<Product> products = new ArrayList<Product>();
			resultSet = statement.executeQuery();			
			while (resultSet.next()) {
				products.add(instantiateProduct(resultSet));
			}
			return products;
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public void update(Product product) {
		PreparedStatement statement = null;
		
		try {
			connection = DB.getConnection();
			statement = connection.prepareStatement("update product " 
													+ "set name = ?, "
													+ "description = ?, "
													+ "price = ? "
													+ "where productId = ?");
			
			statement.setString(1, product.getName());
			statement.setString(2, product.getDescription());
			statement.setDouble(3, product.getPrice());
			statement.setDouble(4, product.getProductId());
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
	}

	@Override
	public void deleteById(Integer identifier) {
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from product "
													+ "where productId = ?;");
			statement.setInt(1, identifier);
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
	}

	private Product instantiateProduct(ResultSet resultSet) throws SQLException {
		return new Product(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("description"),
						resultSet.getDouble("price")
				   );
	}

}
