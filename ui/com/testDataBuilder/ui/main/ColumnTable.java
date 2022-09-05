package com.testDataBuilder.ui.main;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.testDataBuilder.config.ColumnConfig;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.JComboBoxEx;
import com.testDataBuilder.ui.util.ColumnInfo;

public class ColumnTable extends JTable {


	private List<ColumnInfo> columnNames = null; 
	
	public ColumnTable(){
		super();
		init();
	}
	
	protected void init(){		
		setModel(new ColumnTableModel(getColumnNames()));
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		for(int i=0; i < this.getColumnModel().getColumnCount(); i++){
			TableColumn fieldColumn =this.getColumnModel().getColumn(i);
			switch(i){
			case 0://字段名列.
				fieldColumn.setCellEditor(new ColumnCellEditor(getFieldsComboBox()));
				break;
			case 1://ref参照
				fieldColumn.setCellEditor(new ColumnCellEditor(getRefComboBox()));
                break;
			default:
				
			}
			fieldColumn.setPreferredWidth(this.getColumnNames().get(i).getWidth());
		}
	}
	
	private List<ColumnInfo> getColumnNames() {
		if(columnNames == null){
			columnNames = new ArrayList<ColumnInfo>();
			columnNames.add(new ColumnInfo(RM.R("label.ColumnTable.columnName.columnName"), 100)); //$NON-NLS-1$
			columnNames.add(new ColumnInfo(RM.R("label.ColumnTable.columnName.refRole"), 150)); //$NON-NLS-1$
			columnNames.add(new ColumnInfo(RM.R("label.ColumnTable.columnName.isPK"), 5));			 //$NON-NLS-1$
		}
		return columnNames;
	}

	private JComboBoxEx fieldComboBox =  null;
	
	public JComboBoxEx getFieldsComboBox(){
		if(fieldComboBox == null){
			fieldComboBox = new JComboBoxEx();
		}
		return fieldComboBox;
	}
	
	public void setFields(List fields){
		this.getFieldsComboBox().setData(fields);
	}

	private JComboBoxEx refComboBox = null;
	
	public JComboBoxEx getRefComboBox(){
		if(refComboBox == null){
			refComboBox = new JComboBoxEx();	
			refComboBox.setToolTipText(RM.R("label.ColumnTable.toolTip")); //$NON-NLS-1$
            refComboBox.addFocusListener(new FocusListener(){

                public void focusGained(FocusEvent e) {
                    
                }

                public void focusLost(FocusEvent e) {
                    
                }
               
            });
           
		}
		return refComboBox;
	}
	
	public void setRefs(List ref){
		if(ref != null){
			ref.add(0, ""); //$NON-NLS-1$
		}
		this.getRefComboBox().setData(ref);
	}
	
	public ColumnTableModel getDataModel() {
		return (ColumnTableModel) dataModel;
	}


	public List<ColumnConfig> getData(){
		return this.getDataModel().getData();
	}
	
	public void setData(List<ColumnConfig> columnData) {
		this.getDataModel().setData(columnData);
        this.clearSelection();
		this.updateUI();
	}
	
	public void addRow(){
		int size = this.getData().size();
		this.getData().add(new ColumnConfig("column" + size)); //$NON-NLS-1$
		this.updateUI();
		
	}

	public Class<?> getColumnClass(int column) {
		Object value = this.getValueAt(0, column);
		if(value == null){
			return Object.class;
		}
		return value.getClass();
	}
	
	public void deleteCurrentItem(){
		this.getDataModel().deleteItem(this.getSelectedRow());
		this.updateUI();
	}
}

class ColumnCellEditor extends DefaultCellEditor{

    public ColumnCellEditor(JComboBox comboBox) {
        super(comboBox);
        getComponent().addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e) {
               ColumnCellEditor.this.stopCellEditing();
            }
            
        });
    }
    
    @Override
    public JComboBox getComponent() {
        return (JComboBox)super.editorComponent;
    }
}

class ColumnTableModel extends AbstractTableModel{

	List<ColumnConfig> data = new ArrayList<ColumnConfig>();
	List<ColumnInfo> columnInfos = null;
	
	
	public ColumnTableModel(List<ColumnInfo> columnInfos){
		this.columnInfos = columnInfos;
	}
	
	public ColumnTableModel(List<ColumnConfig> data, List<ColumnInfo> columnInfos){
		this.data = data;
		this.columnInfos = columnInfos;
	}
	
	public int getColumnCount() {		
		return columnInfos.size();
	}

	public int getRowCount() {
		if(data == null){
			return 0;
		}
		return data.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ColumnConfig columnConfig = data.get(rowIndex);
		Object value = null;
		switch(columnIndex){
		case 0:
			value = columnConfig.getName();
			break;
		case 1:
			value = columnConfig.getRef();
			break;
		case 2:
			value = columnConfig.isPK();
			break;
		default:
			value = ""; //$NON-NLS-1$
		}
		return value;
	}

	@Override
	public int findColumn(String columnName) {
		return this.columnInfos.indexOf(columnName);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Object value = this.getValueAt(0, columnIndex);
		if(value == null){
			return Object.class;
		}
		return value.getClass();
	}

	@Override
	public String getColumnName(int column) {
		ColumnInfo columnInfo =  columnInfos.get(column);
		return columnInfo.getName();
	}

	public int getColumnWidth(int column){
		ColumnInfo columnInfo =  columnInfos.get(column);
		return columnInfo.getWidth();
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		ColumnConfig columnConfig = this.getData().get(rowIndex);
		switch(columnIndex){
		case 0:
			columnConfig.setName((String) aValue);
			break;
		case 1:
			columnConfig.setRef((String)aValue);
			break;
		case 2:
			columnConfig.setPK((Boolean) aValue);
			break;
		default:
			
		}
	}

	public List<ColumnConfig> getData() {
		return data;
	}

	public void setData(List<ColumnConfig> data) {
        if(data == null){
            data = new LinkedList<ColumnConfig>();
        }
		this.data = data;
	}

	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}

	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}
	
	public void deleteItem(int index){
		this.getData().remove(index);
	}
	
}
