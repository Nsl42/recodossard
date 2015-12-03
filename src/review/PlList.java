/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.Controller;
import controller.ProcessingCtrl;
import controller.UICtrlV2;
import static java.lang.System.out;
import java.util.UUID;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;
import model.PhotoList;

/**
 *
 * @author nsl
 */
public class PlList<String> extends JList{
	DefaultListModel<PhotoList> lmpl;

	public PlList(){
		this.lmpl = new DefaultListModel<>();
		this.setLayoutOrientation(JList.VERTICAL);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(!lse.getValueIsAdjusting())
				{
					PhotoList pl;
					System.out.println("SOURCE " +UICtrlV2.gui.getMp().getPll().getSelectedValue());
					
					pl = (PhotoList)UICtrlV2.gui.getMp().getPll().getSelectedValue();
					if(!(pl == null)) {
							UICtrlV2.setCurrent(pl.getId());
							UICtrlV2.gui.getMp().getPlp().refresh(pl.getId());
						}
				}
				
			}
		});
		this.setVisible(true);
	}
	public PlList(PhotoList[] data){
		super(data);
		this.setVisible(true);
	}
	public void add(PhotoList data)
	{
         lmpl.addElement(data);
	 this.setModel(lmpl);
	}
	public void removeAll()
	{
		lmpl.removeAllElements();
		this.setModel(lmpl);
	}
	public void remove(PhotoList data)
	{
         lmpl.removeElement(data);
	 this.setModel(lmpl);
	}
	
}
