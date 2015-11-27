/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.Controller;
import controller.ProcessingCtrl;
import controller.UICtrl;
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
public class PlList extends JList{
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
					PhotoList pl = (PhotoList) UICtrl.getPll().getSelectedValue();
					if(!(pl == null)) {
							UICtrl.getPlp().refresh(pl);
							UICtrl.setCurrent(pl.getId());
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
	public void remove(PhotoList data)
	{
         lmpl.removeElement(data);
	 this.setModel(lmpl);
	}
	
}
