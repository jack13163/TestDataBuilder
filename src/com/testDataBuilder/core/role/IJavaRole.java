package com.testDataBuilder.core.role;

import java.sql.SQLException;

import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.exception.BaseException;

public interface IJavaRole {

	/**
	 * 获取其中一个字段的值（当前生成的值）。
	 * @param columnName 列名。
	 * @return 返回列值。
	 * @throws BaseException
	 * @throws SQLException
	 */
	public abstract Object getValue(String field) throws BaseException,
			SQLException;

	/**
	 * 根据给定的字段，获取参照表。
	 * @param fields 字段（这些字段必须是一个外键中的多个字段）
	 * @return 方法会根据fields，获取对应的值，并在参照表中找到对应的行。
	 * @throws BaseException
	 * @throws SQLException
	 */
	public abstract TableConfig getReferencer(String... fields)
			throws BaseException, SQLException;

	public abstract TableConfig getTableConfig();

	public abstract void setTableConfig(TableConfig tableConfig);

	public abstract Object getValueByJava() throws BaseException,
			SQLException;

}