/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import model.ImgModel;
import model.PhotoList;
import review.DataPanelV2;
import review.GUIV2;

/**
 *
 * @author nsl
 */
public class UICtrlV2 extends Controller{
	public static GUIV2 gui;
	static private ImgModel selected;
	public static HashMap<UUID, ArrayList<JLabel>> labels = new HashMap<>();

	static private PhotoList current;

	public static ImgModel getSelected() {
		return selected;
	}
	
	public static void setSelected(UUID selected) {
		if(selected == null)
			UICtrlV2.selected = null;
		else
			for(ImgModel im : ProcessingCtrl.loadedPhotoLists.get(
				ProcessingCtrl.getPlidFromImgid(selected)).getPhotolist())
				if(im.getId().equals(selected))
					UICtrlV2.selected = im;
	}
	
	public static void closeNoPl()
	{
		if(!UICtrlV2.loadedPhotoLists.isEmpty())
		{
		gui.remove(gui.getNplp());
		gui.getNplp().setVisible(false);
		System.out.println("NpLp  " + gui.getMp().isVisible());
		System.out.println("Mp  " + gui.getMp().isVisible());
		gui.getMp().setVisible(true);
		System.out.println("closeNoPl");
		gui.validate();
		gui.repaint();
		}
	}
	
	static public void setCurrent(UUID toCurrent) {
		UICtrlV2.current = ProcessingCtrl.loadedPhotoLists.get(toCurrent);
		System.out.println("curr " + current.toString());
		UICtrlV2.gui.getMp().getPlp().refresh(toCurrent);
		UICtrlV2.refreshDataDP();
		UICtrlV2.gui.repaint();
	}
	
	static public PhotoList getCurrent() {
		return current;
	}
	
	static public DataPanelV2 refreshImgDP(){
		return UICtrlV2.gui.getMp().getDp().refreshImg();
	}
	static public DataPanelV2 refreshDataDP(){
		return UICtrlV2.gui.getMp().getDp().refreshData();
	}
	
	static public void buildLabels(PhotoList pl) {
		ArrayList<JLabel> ret = new ArrayList<>();
		for(final ImgModel im : pl.getPhotolist()) {
			JLabel labPic = new JLabel();
			labPic.setIcon(new ImageIcon(
				new ImageIcon(im.getFile().toString()).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
			labPic.setText("Toto " + im.getPath());
			labPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			labPic.setBorder(BorderFactory.createRaisedBevelBorder());
			labPic.setVisible(true);
			labPic.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if(im.equals(UICtrlV2.getSelected())) {
						UICtrlV2.setSelected(null);
						UICtrlV2.refreshImgDP();
					}
					else {
						UICtrlV2.setSelected(im.getId());
						UICtrlV2.refreshImgDP();

					}
				}
			});
			ret.add(labPic);
		}

		labels.put(pl.getId(), ret);
	}
	
}
