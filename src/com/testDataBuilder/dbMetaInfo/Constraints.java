package com.testDataBuilder.dbMetaInfo;

import java.util.List;

import com.testDataBuilder.resources.RM;

public class Constraints {

	private PrimaryKey primaryKey;
	private List<ForeignKey> foreignKeys;
	
	public Constraints(PrimaryKey primaryKey, List<ForeignKey> foreignKeys) {
		super();
		this.primaryKey = primaryKey;
		this.foreignKeys = foreignKeys;
	}

	public String toString(){
		return RM.R("label.Constraints.info.constraint");
	}


	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(List<ForeignKey> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
}
