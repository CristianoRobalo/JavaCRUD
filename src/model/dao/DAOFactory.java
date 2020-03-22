package model.dao;

import db.DB;
import model.dao.impl.ProductDAOJDBC;

public class DAOFactory {
	
	public static ProductDAO createProductDAO() {
		return new ProductDAOJDBC(DB.getConnection());
	}

}
