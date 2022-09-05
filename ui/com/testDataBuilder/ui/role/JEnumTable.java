package com.testDataBuilder.ui.role;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import com.testDataBuilder.core.role.EnumList;
import com.testDataBuilder.core.role.EnumObj;
import com.testDataBuilder.resources.RM;

public class JEnumTable extends JTable {

    public JEnumTable(){
        super(new JEnumTableModel());
        init();
    }
    
    public JEnumTable(EnumList<EnumObj> enumList){
        super(new JEnumTableModel(enumList));
        init();
    }
    
    public void init(){
       
    }
    
    @Override
    public JEnumTableModel getModel(){
        return (JEnumTableModel) super.getModel();
    }
    
    
    public void setData(EnumList<EnumObj> enumList){
        this.getModel().setEnumList(enumList);
        this.updateUI();
    }
    
    public EnumList<EnumObj> getData(){
        return this.getModel().getEnumList();
    }
    
    public void addEnum(Object value, Integer percent){
        this.getData().add(new EnumObj(value, percent));
        this.updateUI();
    }
    
    public boolean deleteCurrentItem(){
         if(this.getSelectedRow() != -1){
             this.getData().remove(this.getSelectedRow());
             this.updateUI();
             return true;
         }
         return false;
    }
    
    public EnumObj getSelEnumObj(){
    	int index = this.getSelectedRow();
    	if(index != -1){
            return this.getData().get(index);            
        }
    	return null;
    }
}

class JEnumTableModel extends AbstractTableModel{

    private EnumList<EnumObj> enumList = new EnumList<EnumObj>();

    public JEnumTableModel(){
    }

    
    public JEnumTableModel(EnumList<EnumObj> enumList){
        this.enumList = enumList;
    }
    
    @Override
    public String getColumnName(int column) {
        String columnName = null;
        switch(column){
        case 0:
            columnName = RM.R("label.JEnumTable.columnName.value");
            break;
        case 1:
            columnName = RM.R("label.JEnumTable.columnName.percent");
            break;
        default:
            columnName = "unknow column";  
        }
        return columnName;
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    
    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        if(enumList == null){
            return 0;
        }
        
        return enumList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(enumList == null){
            return null;
        }
        Object obj = null;
        EnumObj e = enumList.get(rowIndex);
        switch(columnIndex){
        case 0:
            obj = e.getValue();
            break;
        case 1:
            obj = e.getPercent();
            break;
        default:
            
        }
        
        return obj;
    }

    public EnumList<EnumObj> getEnumList() {
        return enumList;
    }

    public void setEnumList(EnumList<EnumObj> enumList) {
        this.enumList = enumList;
    }
    
}