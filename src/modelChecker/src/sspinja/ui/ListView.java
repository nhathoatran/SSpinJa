package sspinja.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class ListView {
	JList<String> listLog;
	DefaultListModel<String> model;
	int logCount = 0;
	
	ListView(boolean isSetBgColor, Color bgColor, boolean isDisplayIcon) {
		model = new DefaultListModel<String>();
			    
		listLog = new JList<String>();
		listLog.setModel(model);
		listLog.setVisibleRowCount(5);
		
		listLog.setValueIsAdjusting(true);
		listLog.setSelectionBackground(new Color(240, 240, 255));
		listLog.setSelectionForeground(Color.BLACK);
				
		listLog.setCellRenderer(new DefaultListCellRenderer() {
	        @Override
	        public Component getListCellRendererComponent(
	                JList list, Object value, int index,
	                boolean isSelected, boolean cellHasFocus) {

	            JLabel label = (JLabel) super.getListCellRendererComponent(
	                    list, value, index, isSelected, cellHasFocus);
	            BufferedImage image;
				try {
					if (isDisplayIcon) {
						image = ImageIO.read(ResourceLoader.load("state.gif"));
					} else {
						image = ImageIO.read(ResourceLoader.load("trans.gif"));
					}
					if (image != null)
		            	label.setIcon(new ImageIcon(image));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	            if (isSetBgColor && index % 2 == 0)
	            	label.setBackground(bgColor);
	            return label;
	        }
		});
	}
	
	public JList<String> getList() {
		return listLog;
	}
	
	public int getSelectedIndex() {
		return listLog.getSelectedIndex();
	}
	
	public void setSelectionModel(int mod) {

	}
	
	public String getSelectedText(){
		return listLog.getSelectedValue();
	}
	
	public void setBackgroundColor(Color color){
		listLog.setBackground(color);
	}
	
	public void setTextColor(Color color){
		listLog.setForeground(color);
	}
	
	
	public int getLogCount(){
		return logCount;
	}
	
	public void addItem(String item){
		model.addElement(item);
		listLog.ensureIndexIsVisible(logCount);
		logCount++;
	}
	
	public void addTransition(String fromState, String act, String toState) {
		model.addElement((++logCount) + ":\t" + fromState + "\t->" + act + "->\t" + toState);
	}
	
	public String removeLastItem(){
		if (logCount > 0) {
			logCount--;
			return model.remove(logCount);
		} else
			return "";
	}
	
}

