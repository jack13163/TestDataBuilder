package com.testDataBuilder.ui.main.queryConsole;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

public class ConnectionComboBox extends JComboBox {
    
    public ConnectionComboBox(List<String> connNames){
        super(new ConnectionComboBoxModel(connNames));
        if(connNames.size() > 0){
            this.setSelectedIndex(0);
        }
    }
    
    public ConnectionComboBox(){
        super(new ConnectionComboBoxModel(new ArrayList<String>(0)));
    }
    
    public void setConnNames(List<String> connNames){
        this.getModel().setData(connNames);
    }
    @Override
    public ConnectionComboBoxModel getModel() {
        return (ConnectionComboBoxModel)super.getModel();
    }

   
    public void initConnectionCombox(List<String> allConnNames, Object selObject){
        if(allConnNames != null){           
           this.setData(allConnNames);
            if(selObject != null){
               this.setSelectedItem(selObject);
            }else if(this.getItemCount() > 0){
                this.setSelectedIndex(0);
            }
        }else{
            this.removeAllItems();
        }
        this.updateUI();
    }
    
    public void initConnectionCombox(List<String> allConnNames){
        Object selObject = this.getSelectedItem();
        initConnectionCombox(allConnNames, selObject);
    }
    
    public List getData() {
        return getModel().getData();
    }

    public void setData(List data) {
        getModel().setData(data);
    }   
    
}

class ConnectionComboBoxModel implements MutableComboBoxModel{

    List<String> data = null;
    
    private Object selItem = null;
    
    public ConnectionComboBoxModel(List<String> data){
        this.data = data;
    }
    
    public void addElement(Object obj) {
        if(!data.contains(obj)){
            this.data.add((String)obj);
        }
    }

    public void insertElementAt(Object obj, int index) {
        if(!data.contains(obj)){
            this.data.add(index, (String)obj);
        }
    }

    public void removeElement(Object obj) {
        this.data.remove(obj);
    }

    public void removeElementAt(int index) {
        this.data.remove(index);        
    }

    public Object getSelectedItem() {
        return selItem;
    }

    public void setSelectedItem(Object anItem) {
        this.selItem = anItem;
    }

    public void addListDataListener(ListDataListener l) {
        
    }

    public Object getElementAt(int index) {
        return data.get(index);
    }

    public int getSize() {
        return data.size();
    }

    public void removeListDataListener(ListDataListener l) {
        
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }   
}
