package com.testDataBuilder.ui.role;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.util.ColumnInfo;

public class JTableTable extends JTable {


    private List<ColumnInfo> columnNames = null; 
    
    public JTableTable(){
        super();
        init();
    }
    
    protected void init(){
        setModel(new ColumnTableModel(getColumnNames()));
        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        for(int i=0; i < this.getColumnModel().getColumnCount(); i++){
            TableColumn fieldColumn =this.getColumnModel().getColumn(i);           
            fieldColumn.setPreferredWidth(this.getColumnNames().get(i).getWidth());
        }
        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == 3){
                    JTableTable.this.showPopupMenu(e);
                }
                super.mouseReleased(e);
            }
            
        });
    }
    
    public void showPopupMenu(MouseEvent e){
        JPopupMenu menu = getTablePopupMenu();
        menu.show(this, e.getX(), e.getY());
    }
    
    private List<ColumnInfo> getColumnNames() {
        if(columnNames == null){
            columnNames = new ArrayList<ColumnInfo>();
            columnNames.add(new ColumnInfo(RM.R("label.JTableTable.columnName.isGenerate"), 5)); //$NON-NLS-1$
            columnNames.add(new ColumnInfo(RM.R("label.JTableTable.columnName.tableName"), 150)); //$NON-NLS-1$
            columnNames.add(new ColumnInfo(RM.R("label.JTableTable.columnName.count"), 20));           //$NON-NLS-1$
        }
        return columnNames;
    }

    public ColumnTableModel getDataModel() {
        return (ColumnTableModel) dataModel;
    }


    public List<TableConfig> getData(){
        return this.getDataModel().getData();
    }
    
    public void setData(List<TableConfig> columnData) {
        this.getDataModel().setData(columnData);
        this.clearSelection();
        this.updateUI();
    }

    public Class<?> getColumnClass(int column) {
        Object value = this.getValueAt(0, column);
        if(value == null){
            return Object.class;
        }
        return value.getClass();
    }
    
    public void selectAll(){
        if(this.getData() != null){
            for(TableConfig tableConfig : this.getData()){
                tableConfig.setIsGenerate(Boolean.TRUE);
            }
            this.updateUI();
        }
    }
    
    public void diselectAll(){
        if(this.getData() != null){
            for(TableConfig tableConfig : this.getData()){
                tableConfig.setIsGenerate(Boolean.FALSE);
            }
            this.updateUI();
        }
    }
    
    public JPopupMenu getTablePopupMenu(){
        JPopupMenu root = new JPopupMenu("table");
        
        root.add(getSelectAllMenuItem());
        root.add(getDiselectAllMenuItem());
        
        return root;
    }
    
    //config
    JMenuItem selectAllAllMenuItem = null;
    public JMenuItem getSelectAllMenuItem(){
        if(selectAllAllMenuItem == null){
            selectAllAllMenuItem = new JMenuItem(RM.R("label.JTableTable.poput.selectAll"));
            selectAllAllMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTableTable.this.selectAll();
                }                
            });
        }
        return selectAllAllMenuItem;
    }
    
    //config
    JMenuItem dsselectAllAllMenuItem = null;
    public JMenuItem getDiselectAllMenuItem(){
        if(dsselectAllAllMenuItem == null){
            dsselectAllAllMenuItem = new JMenuItem(RM.R("label.JTableTable.poput.diselectAll"));
            dsselectAllAllMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTableTable.this.diselectAll();
                }                
            });
        }
        return dsselectAllAllMenuItem;
    }
    
}


class ColumnTableModel extends AbstractTableModel{

    private List<TableConfig> tableConfigs = new ArrayList<TableConfig>();
    
    List<ColumnInfo> columnInfos = null;
    
    
    public ColumnTableModel( List<ColumnInfo> columnInfos){
        this.columnInfos = columnInfos;
    }
    
    public ColumnTableModel(List<TableConfig> tableConfigs, List<ColumnInfo> columnInfos){
        this.tableConfigs = tableConfigs;
        this.columnInfos = columnInfos;
    }
    
    public int getColumnCount() {       
        return columnInfos.size();
    }

    public int getRowCount() {
        if(tableConfigs == null){
            return 0;
        }
        return tableConfigs.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TableConfig tableConfig = tableConfigs.get(rowIndex);
        Object value = null;
        switch(columnIndex){
        case 0:
            value = tableConfig.getIsGenerate();
            break;
        case 1:
            value = tableConfig.getTableName();
            break;
        case 2:
            value = tableConfig.getCount();
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
        return (columnIndex == 0);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TableConfig columnConfig = this.getData().get(rowIndex);
        switch(columnIndex){
        case 0:
            columnConfig.setIsGenerate((Boolean)aValue);
            break;
        case 1:            
            break;
        case 2:
            break;
        default:
            
        }
    }

    public List<TableConfig> getData() {
        return tableConfigs;
    }

    public void setData(List<TableConfig> data) {
        if(data == null){
            data = new LinkedList<TableConfig>();
        }
        this.tableConfigs = data;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }
}

