package com.cogent.fooddeliveryapp.utils;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * CustomNamingStrategy
 *
 * @author bryan
 * @date Feb 18, 2022-4:56:39 PM
 */
public class CustomNamingStrategy extends PhysicalNamingStrategyStandardImpl {
	private static final long serialVersionUID = 1L;

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		String newTableName = name.getText().concat("_tbl");
		return Identifier.toIdentifier(newTableName);
	}
}
