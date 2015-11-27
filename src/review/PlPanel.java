/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.Controller;
import controller.ProcessingCtrl;
import controller.UICtrl;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Observer;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.PhotoList;

/**
 *
 * @author nsl
 */
public class PlPanel {
	private JPanel galPicture;
	private JScrollPane scrollPane;
	private GridLayout gl ;
	public PlPanel()
	{
		gl = new GridLayout(0,7);
		UICtrl.gbc.gridx = 1;
		UICtrl.gbc.gridwidth = 6;
		UICtrl.gbc.gridheight = 2;
		UICtrl.gbc.gridy = 1;
		this.galPicture = new JPanel();
		//Getting the labels of existing imgmodels

	}
	
	public void refresh(PhotoList pl)
	{
		galPicture.removeAll();
		System.out.println(UICtrl.getlabels(pl.getId()));
		for(JLabel l : UICtrl.getlabels(pl.getId()))
		{
			galPicture.add(l);
		}
		galPicture.validate();
		galPicture.repaint();
		UICtrl.getMp().add(this.getscrollPane());
	}
	public void refresh()
	{
		for(UUID id : ProcessingCtrl.loadedPhotoLists.keySet())
			this.refresh(ProcessingCtrl.loadedPhotoLists.get(id));
	}
	public JScrollPane getscrollPane()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 3*screenSize.width/4;
		int height = 3*screenSize.height/4;
		gl.setVgap(10);
		this.scrollPane = new JScrollPane(galPicture);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//this.scrollPane.setPreferredSize(new Dimension(36*width/100, 70*height/100));
		this.galPicture.setLayout(gl);
		return this.scrollPane;
	}

	
}
