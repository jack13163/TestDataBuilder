package com.testDataBuilder.ui.core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

public class JComboBoxEx extends JComboBox {

	
	public JComboBoxEx() {
		super(new FieldCheckBoxModel());
		init();
	}
	
	public JComboBoxEx(List data) {		
		super(new FieldCheckBoxModel());
		getModel().setData(data);
		init();
	}

	public void init(){
		
	}
	
	public FieldCheckBoxModel getModel(){
		return (FieldCheckBoxModel) super.getModel();
	}
	
	public void addElement(Object obj) {
		getModel().addElement(obj);
		this.updateUI();
	}

	public void insertElementAt(Object obj, int index) {
		getModel().insertElementAt(obj, index);
		this.updateUI();
	}

	public void removeElement(Object obj) {
		getModel().removeElement(obj);
		this.updateUI();
	}

	public void removeElementAt(int index) {
		getModel().removeElementAt(index);		
		this.updateUI();
	}

	public void setData(List data) {
		this.getModel().setData(data);
		this.updateUI();
	}
}


class FieldCheckBoxModel implements MutableComboBoxModel{

	private List data = new ArrayList();
	
	private Object selItem = null;
	
	public FieldCheckBoxModel(){
		super();
	}
	
	public FieldCheckBoxModel(List data){
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
