package com.testDataBuilder.ui.main;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import com.testDataBuilder.config.AutoConfigEngine;
import com.testDataBuilder.config.DatabaseConfig;
import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.dbMetaInfo.Column;
import com.testDataBuilder.dbMetaInfo.Constraints;
import com.testDataBuilder.dbMetaInfo.Database;
import com.testDataBuilder.dbMetaInfo.ForeignKey;
import com.testDataBuilder.dbMetaInfo.PrimaryKey;
import com.testDataBuilder.dbMetaInfo.Table;
import com.testDataBuilder.util.Global;

public class JDBTree extends JTree {

	public static int LEVEL_DB = 0;
	public static int LEVEL_TABLE = 1;
	public static int LEVEL_COLUMNS = 2;
	public static int LEVEL_COLUMN = 3;
	
    static Logger logger = Logger.getLogger(JDBTree.class);
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    
	public JDBTree(MainFrame parent){
		super();
        this.parent = parent;
        this.setModel(new DBTreeModel());
		this.setCellRenderer(new TDBTreeRenderer(this));
        this.setSelectionRow(0);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
    
    private Database database = null;  //  @jve:decl-index=0:
    private DatabaseConfig curSelDatabaseConfig = null;  //  @jve:decl-index=0:
    
    public void autoConfigAll() throws IOException{
        List<Table> tables = this.database.getTables();
        try{
            for(Table table : tables){
                if(!tableIsConfiged(table)){
                    AutoConfigEngine autoConfigEngine = new AutoConfigEngine();
                    TableConfig tableConfig = autoConfigEngine.reverseTable(table);
                    tableConfig.saveConfig();
                    tableConfig.deleteBankupFile();
                }
            }
        }finally{
            this.updateUI();
        }
    }
	
    public Database getData() {
        return this.getModel().getData();
    }

    public void setData(Database database) {
        this.getModel().setData(database);
    }
    
    public Map<String, DatabaseConfig> getDatabaseConfigs() {
        return this.getModel().getDatabaseConfigs();
    }

    public void setDatabaseConfigs(Map<String, DatabaseConfig> databaseConfigs) {
        this.getModel().setDatabaseConfigs(databaseConfigs);
    }
    
    public boolean connIsOpend(String configName){
        return this.parent.getWorkspaceDataCache().getTestDataConfig()
                        .getConnFromCache(configName) != null;
    }
    
    @Override
    public DBTreeModel getModel() {
        return (DBTreeModel)super.getModel();
    }

    public boolean tableIsConfiged(Table table){
    	File configFile = WorkspaceDataCache.getInstance().getTableConfigFile(table.getTableName());
        return configFile.exists();
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database curSelDB) {
        this.database = curSelDB;
    }
//
//    public Table getCurSelTable() {
//        return curSelTable;
//    }
//
//    public void setCurSelTable(Table curSelTable) {
//        this.curSelTable = curSelTable;
//    }

    public DatabaseConfig getCurSelDatabaseConfig() {
        return curSelDatabaseConfig;
    }

    public void setCurSelDatabaseConfig(DatabaseConfig curSelDatabaseConfig) {
        this.curSelDatabaseConfig = curSelDatabaseConfig;
    }

}

class DBTreeModel implements TreeModel {

    private Database data = null;
	
    Map<String, DatabaseConfig> databaseConfigs = null;
    
	public DBTreeModel(){
        data = new Database();
	}
	
	public DBTreeModel(Database data, Map<String, DatabaseConfig> databaseConfigs){
		this.data = data;
        this.databaseConfigs = databaseConfigs;
	}

    public void addTreeModelListener(TreeModelListener l) {
		
	}

	public Object getChild(Object parent, int index) {
        
		Object obj = null;
		//Database,column.
        if(parent instanceof List){
            List list = (List)parent;
            if(index >= list.size()){
                obj = databaseConfigs;
            }else{
                obj = list.get(index);
            }
        } else if(parent instanceof Map){
            obj = databaseConfigs.values().toArray()[index];
        }else if(parent instanceof Database){
            Database database = (Database)parent;
            obj = database.getTables().get(index);
        }else if(parent instanceof Table){
            Table table = (Table)parent;
        	switch(index){
        	case 0:
        		obj = table.getColumns();
        		break;
        	case 1:
        		obj = new Constraints(table.getPrimaryKey(), table.getForeignKeys());
        		break;
        	default:
        		System.out.println("error in getChild.......");
        	}
        }else if(parent instanceof Constraints){
        	Constraints constraints = (Constraints)parent;
        	if(constraints.getPrimaryKey() == null){
        		obj = constraints.getForeignKeys().get(index);
        	}else{
        		if(index == 0){
        			obj =  constraints.getPrimaryKey();
        		}else{
        			obj =  constraints.getForeignKeys().get(index -1);
        		}
        	}
        }else if(parent instanceof PrimaryKey){
        	PrimaryKey primaryKey = (PrimaryKey)parent;
        	obj = primaryKey.getFields().get(index);
        }else if(parent instanceof ForeignKey){
        	ForeignKey foreignKey = (ForeignKey)parent;
        	String field = foreignKey.getFKFileds().get(index);
        	String refField = foreignKey.getRefFields().get(field);
        	obj = field + " => " + refField;
        }
        
		return obj;
	}

	public int getChildCount(Object parent) {
        
		int count = 0;
		//
		if(parent instanceof List){
            List list = (List)parent;            
            count = list.size();
            if(count > 0 && list.get(0) instanceof Database){
                count = count + 1;
            }
        }else if(parent instanceof Map){
            Map map = (Map)parent;
            count = map.size();
        }else if(parent instanceof Database){
            Database database = (Database)parent;
            if(database.getTables() == null){
            	count = 0;
            }else{
            	count = database.getTables().size();
            }
        }else if(parent instanceof Table){
            //Table table = (Table)parent;
            count = 2;
        }else if(parent instanceof Column){
           count = 0;
        }else if(parent instanceof Constraints){
        	Constraints constraints = (Constraints)parent;
        	if(constraints.getPrimaryKey()!= null){
        		++count;
        	}
        	count += constraints.getForeignKeys().size();
        }else if(parent instanceof PrimaryKey){
        	PrimaryKey primaryKey = (PrimaryKey)parent;
        	count = primaryKey.getFields().size();
        }else if(parent instanceof ForeignKey){
        	ForeignKey foreignKey = (ForeignKey)parent;
        	count = foreignKey.getFKFileds().size();
        }
       
		return count;
	}

	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}

	public Object getRoot() {
		return data;
	}

	public boolean isLeaf(Object node) {
        if(node instanceof Column
          || node instanceof String
          || node instanceof DatabaseConfig){
            return true;
        }
		return false;
	}

	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

    public Database getData() {
        return data;
    }

    public void setData(Database data) {
        this.data = data;       
    }

    public Map<String, DatabaseConfig> getDatabaseConfigs() {
        return databaseConfigs;
    }

    public void setDatabaseConfigs(Map<String, DatabaseConfig> databaseConfigs) {
        this.databaseConfigs =databaseConfigs;
    }
	
}



class TDBTreeRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;

    JDBTree parent = null;
    public TDBTreeRenderer(JDBTree parent){
        this.parent = parent;
    }
    
    public Component getTreeCellRendererComponent(
            JTree tree, Object value,
            boolean sel, boolean expanded, 
            boolean leaf, int row,
            boolean hasFocus) {
       String strIcon = "Host.gif";
        
       if(value instanceof List){
			List list = (List)value;
			if(list.size() > 0){
				if(list.get(0) instanceof Database){
					strIcon = "Host.gif";
				}else if(list.get(0) instanceof Column){
					strIcon = "Columns.gif";
				}
			}
        }else if(value instanceof Database){
        	strIcon = "Database.gif";
        }else if(value instanceof Table){
            if(parent.tableIsConfiged((Table)value)){
                strIcon = "TableSel.gif";
            }else{
                strIcon = "Table.gif";
            }
        	
        }else if(value instanceof Column){
			strIcon = "Field.gif";
		}else if(value instanceof Constraints){
        	strIcon = "Constraints.gif";
        }else if(value instanceof PrimaryKey){
        	strIcon = "pk.gif";
        }else if(value instanceof ForeignKey){
        	strIcon = "pk.gif";
        }else if(value instanceof String){
        	strIcon = "cField.gif";
        }else if(value instanceof DatabaseConfig){
            DatabaseConfig databaseConfig = (DatabaseConfig)value;
            if(parent.connIsOpend(databaseConfig.getName())){
                strIcon = "DatabaseConnected.gif";
            }else{
                strIcon = "Database.gif";
            }
        }
       
        URL url = this.getClass().getResource(Global.ICON_TREE_DIR + strIcon);
        Icon icon = new ImageIcon(url);
        setLeafIcon(icon);
        setOpenIcon(icon);
        setClosedIcon(icon);
       
        return super.getTreeCellRendererComponent(tree, value, sel, expanded,leaf, row, hasFocus);

    }

}

