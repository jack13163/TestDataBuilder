package com.testDataBuilder.ui.role;

import java.awt.Component;
import java.net.URL;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.util.Global;

public class JRoleTree extends JTree {
    
	public JRoleTree(){
		super();
		this.setCellRenderer(new RoleRenderer());
		this.setModel(new RoleTreeModel(null));
        this.setSelectionRow(0);
	}
	
	public WorkspaceDataCache getData() {
		return this.getModel().getData();
	}

	public void setData(WorkspaceDataCache data) {
        this.getModel().setData(data);
		this.updateUI();
	}
        
    @Override
    public RoleTreeModel getModel() {
        return (RoleTreeModel)super.getModel();
    }

    ///////////////////////////////////////////////////////////
    /**
	 * 添加一项到list中.
	 * <p><code>addItem</code></p>
	 * @param item
	 * @return
	 * @author LiuXiaojie 2007-7-7
	 */
	public Role addItem(Role item){
//		int size = this.model.addItem(item);	
//		this.setSelectedIndex(size -1);
//		this.updateUI();		
//		return item;
		return null;
	}
	
	/**
	 * 添加多项到list中.
	 * <p><code>addAllItem</code></p>
	 * @param items
	 * @return
	 * @author LiuXiaojie 2007-7-9
	 */
	public int addAllItem(List items){
//		int size = this.model.addAllItem(items);	
//		this.setSelectedIndex(size -1);
//		this.updateUI();		
//		return size;
		return 1;
	}
	
	/**
	 * 删掉一项.
	 * <p><code>removeItem</code></p>
	 * @param index
	 * @return 返回删掉的那一项.
	 * @author LiuXiaojie 2007-7-9
	 */
	public Role removeItem(int index){
//		int currentItem = this.getSelectedIndex(); 
//		if(index + 1 == this.getData().size()){
//			currentItem = index -1;
//		}
//
//		Role retObj =  this.model.removeItem(index);
//		this.setSelectedIndex(currentItem);
//		//this.updateUI();
//		return retObj;
		return null;
	}
	
    public void clear(){
        //this.getData().clear();        
    }
    
	public Role getItem(int index){
		return null;
	}

	/**
	 * 得到当前类型.
	 * <p><code>getCurrentItem</code></p>
	 * @param index
	 * @return
	 * @author LiuXiaojie 2007-7-7
	 */
	public Role getCurrentItem(){
		return null;
	}
	
	/**
	 * 统计数量.
	 * <p><code>getCount</code></p>
	 * @param name
	 * @return
	 * @author LiuXiaojie 2007-7-9
	 */
	public int getCount(String name){
		return 0;
	}
	
	/**
	 * 添加一个缺省项.
	 * <p><code>addDefaultItem</code></p>
	 * @return 返回list的size.
	 * @author LiuXiaojie 2007-7-9
	 */
	public Role addDefaultItem(){		
//		List data = this.getData();
//		String tempName = defaultTypeName;
//		for(int i=0;i < Integer.MAX_VALUE; i++){
//			if(data.contains(tempName)){
//				tempName = defaultTypeName + (i + 1);
//			}else{
//				break;
//			}
//		}
//		Role role = new Role();
//		role.setName(tempName);
//		return this.addItem(role);
		return null;
	}
	
	public Role deleteCurrentItem() {
	return null;
	}
}

class RoleTreeModel implements TreeModel {

    private WorkspaceDataCache data = null;

	public RoleTreeModel(WorkspaceDataCache data){
		this.data = data;
	}

    public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	public Object getChild(Object parent, int index) {
        
		Object obj = null;
		if(parent instanceof WorkspaceDataCache){
            WorkspaceDataCache workspaceDataCache = (WorkspaceDataCache)parent;
			if(index == 0){
				obj = workspaceDataCache.getGlobalRoleFactory();
			}else{
				obj = workspaceDataCache.getTableConfigs().get(index -1).getRoleFactory();
			}
		}else if(parent instanceof RoleFactory){
			RoleFactory roleFactory = (RoleFactory)parent;
			obj = roleFactory.getRoles().get(index);
		}
		return obj;
	}

	public int getChildCount(Object parent) {
        
		int count = 0;
		if(parent instanceof WorkspaceDataCache){
            WorkspaceDataCache workspaceDataCache = (WorkspaceDataCache)parent;
			count = 1 + workspaceDataCache.getTableConfigs().size();
		}else if(parent instanceof RoleFactory){
			RoleFactory roleFactory = (RoleFactory)parent;
			count = roleFactory.getRoles().size();
		}
		
		return count;
	}

	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getRoot() {
		return data;
	}

	public boolean isLeaf(Object node) {
        return (node instanceof Role);
	}

	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

    public WorkspaceDataCache getData() {
        return data;
    }

    public void setData(WorkspaceDataCache data) {
        this.data = data;    
    }
	
}



class RoleRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;

    public Component getTreeCellRendererComponent(
            JTree tree, Object value,
            boolean sel, boolean expanded, 
            boolean leaf, int row,
            boolean hasFocus) {
        String strIcon= "Database.gif";

        if(value instanceof RoleFactory){
            strIcon= "Table3.gif";
        }else if(value instanceof Role){
            strIcon= "role.gif";
        }
       
        
        URL url = this.getClass().getResource(Global.ICON_TREE_DIR + strIcon);
        
        Icon icon = new ImageIcon(url);
        setLeafIcon(icon);
        setOpenIcon(icon);
        setClosedIcon(icon);
       
        return super.getTreeCellRendererComponent(tree, value, sel, expanded,leaf, row, hasFocus);

    }

}

