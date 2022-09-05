package com.testDataBuilder.ui.role;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;
import com.testDataBuilder.core.baseType.JavaTypes;

public class JavaTypeComboBox extends JComboBox {
    
    public JavaTypeComboBox(List<Class> javaTypes){
        super(new JavaTypeComboBoxModel(javaTypes));
    }
    
    @Override
    public JavaTypeComboBoxModel getModel() {
        return (JavaTypeComboBoxModel)super.getModel();
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if(anObject instanceof Class){
            Class javaType = (Class)anObject;
            String name = JavaTypes.getInstance().getTypeName(javaType);
            super.setSelectedItem(name);
        }else{
            super.setSelectedItem(anObject);
        }
        this.repaint();
    }

    public Class getSelectedClass() {    
        int index = this.getSelectedIndex();
        Class selObj = null;
        if(index != -1){
            selObj= (Class) this.getModel().getData().get(this.getSelectedIndex());
        }
        return selObj;
    }
    
    
    
}

class JavaTypeComboBoxModel implements MutableComboBoxModel{

    List data = null;
    
    private Object selItem = null;
    
    public JavaTypeComboBoxModel(List data){
        this.data = data;
        
    }
    
    public void addElement(Object obj) {
        if(!data.contains(obj)){
            this.data.add(obj);
        }
    }

    public void insertElementAt(Object obj, int index) {
        if(!data.contains(obj)){
            this.data.add(index, obj);
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
        Class c = (Class) data.get(index); 
        return JavaTypes.getInstance().getTypeName(c);
    }

    public int getSize() {
    	if(data == null){
    		return 0;
    	}
    	
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
