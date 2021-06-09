package sspinja.ui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class ListAction {
	public JComboBox<String> listAction;
	DefaultComboBoxModel<String> model;
	int selectedAction;
	
	
	
	ListAction() {
		model = new DefaultComboBoxModel<String>();
			    
		listAction = new JComboBox<String>();
		listAction.setModel(model);		
		listAction.setForeground(Color.BLACK);
	}
		
	public void addListAction(ArrayList<String> action){
		listAction.removeAllItems();
		model.removeAllElements();
		if (action != null && action.size() > 0) {
			for (String act : action)
				model.addElement(act);
		} 
	}
	
	public int size() {
		return model.getSize();
	}
	
	public void addAction(String act){
		model.addElement(act);
	}
	
	public String getSelectedAction() {		
		return (String) listAction.getSelectedItem();
	}
	
	public JComboBox<String> getListActionComboBox() {
		return listAction;
	}
	
	public int getSelectedItem(){
		return listAction.getSelectedIndex();
	}
}
