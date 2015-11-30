/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package controller;

import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import model.ImgModel;
import model.PhotoList;
import review.DataPanel;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import review.MainPanel;
import review.NoPlPanel;
import review.PlList;
import review.PlPanel;

/**
 *
 * @author nsl
 */
public class UICtrl {
	static private ImgModel selected;
	static public GridBagConstraints gbc = new GridBagConstraints();

	public static JButton getAddGalleryButton() {
		final JButton add = new JButton("Add Gallery");
		add.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		add.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("CallToPrintStackTrace")
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				if(jfc.showOpenDialog(add) == JFileChooser.APPROVE_OPTION){
						UUID uid = ProcessingCtrl.
							acknowledge(jfc.getSelectedFile());
//						UICtrl.labels.put(uid, UICtrl.getlabels(ProcessingCtrl.loadedPhotoLists.get(uid)));
//						UICtrl.setCurrent(uid);
//						UICtrl.pll.add(UICtrl.getCurrent());
				}
			}
		});
		return add;
	}
	private static MainPanel mp = new MainPanel();
	static private PlPanel plp = new PlPanel();
	static private DataPanel dp = new DataPanel();
	public static PlList pll = new PlList();
	

	
	public static PlPanel getPlp() {
		return UICtrl.plp;
		
	}
	public static PlList getPll() {
		return UICtrl.pll;
		
	}
	public static ImgModel getSelected() {
		return selected;
	}
	
	public static void setSelected(UUID selected) {
		if(selected == null)
			UICtrl.selected = null;
		else
			for(ImgModel im : ProcessingCtrl.loadedPhotoLists.get(
				ProcessingCtrl.getPlidFromImgid(selected)).getPhotolist())
				if(im.getId().equals(selected))
					UICtrl.selected = im;
	}
	
	public static DataPanel getDp() {
		return dp;
	}
	
	static public DataPanel refreshImgDP(){
		return UICtrl.dp.refreshImg().setGBC();
	}
	static public DataPanel refreshDataDP(){
		return UICtrl.dp.refreshData().setGBC();
	}
	
	static public  ArrayList<JLabel> getlabels(PhotoList pl){
		//System.out.println(pl.toString());
		ArrayList<JLabel> ret = new ArrayList<JLabel>();
		for (final ImgModel im : pl.getPhotolist()){
			System.out.println(im.getPath());
			JLabel labPic = new JLabel();
			labPic.setIcon(new ImageIcon(
				new ImageIcon(im.getFile().toString()).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
			labPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			labPic.setBorder(BorderFactory.createRaisedBevelBorder());
			labPic.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if(im.equals(UICtrl.getSelected())) {
						UICtrl.setSelected(null);
						UICtrl.refreshImgDP();
					}
					else {
						UICtrl.setSelected(im.getId());
						UICtrl.refreshImgDP();
					}
				}
			});
			ret.add(labPic);
		}
		return ret;
	}
	static public  ArrayList<JLabel> getlabels(UUID id)
	{
//		return UICtrl.labels.get(id);
return null;

	}

	public static MainPanel getMp() {
		return UICtrl.mp;
	}
}
