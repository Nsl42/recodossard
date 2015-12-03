package controller;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import model.ImgModel;
import model.PhotoList;
import review.DataPanelV2;
import review.GUIV2;

/**
 * Manages and refreshes the view
 * @author Anas Alaoui M'Darhri
 */
public class UICtrlV2 extends Controller{

	public static GUIV2 gui;
	public static HashMap<UUID, ArrayList<JLabel>> labels = new HashMap<>();
	static private ImgModel selected;
	static private PhotoList current;
	static private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Gallery list :");
	static private DefaultTreeModel treeModel =
		new DefaultTreeModel(rootNode);

	// Getters and Setters

	/** 
	 * @author Anas Alaoui M'Darhri
	 * @return currently selected object
	 */
	public static ImgModel getSelected() {
		return selected;
	}
	
	/**
	 * @author Anas ALaoui M'Darhri
	 * @param selected id of the newly selected photo
	 */
	public static void setSelected(UUID selected) {
		if(selected == null)
			UICtrlV2.selected = null;
		else
			for(ImgModel im : ProcessingCtrl.loadedPhotoLists.get(
				ProcessingCtrl.getPlidFromImgid(selected)).getPhotolist())
				if(im.getId().equals(selected))
					UICtrlV2.selected = im;
	}
	
	/**
	 * @author Anas Alaoui M'Darhri
	 * @param toCurrent the new PhotoList you want to display on the 
	 * main panel
	 */
	static public void setCurrent(UUID toCurrent) {
		UICtrlV2.current = ProcessingCtrl.loadedPhotoLists.get(toCurrent);
		System.out.println("curr " + current.toString());
		UICtrlV2.gui.getMp().getPlp().refresh(toCurrent);
		UICtrlV2.refreshDataDP();
		UICtrlV2.gui.repaint();
	}
	
	/**
	 * @author Anas Alaoui M'Darhri
	 * @return the currently displayed photolist
	 */

	static public PhotoList getCurrent() {
		return current;
	}

	// VIEW REFRESHING METHODS
	
	/**
	 * Refreshes the view when the first photolist has been added
	 * @author Anas Alaoui M'Darhri
	 */
	public static void closeNoPl()
	{
		if(!UICtrlV2.loadedPhotoLists.isEmpty())
		{
		gui.remove(gui.getNplp());
		gui.getNplp().setVisible(false);
		gui.getMp().setVisible(true);
		System.out.println("NpLp  " + gui.getMp().isVisible());
		System.out.println("Mp  " + gui.getMp().isVisible());
		System.out.println("closeNoPl");
		gui.validate();
		gui.repaint();
		}
	}
	
	/**
	 * Refreshes the Img-related data on the datapanel
	 * @author Anas Alaoui M'Darhri
	 * @return the updated and refreshed DataPanel
	 */
	static public DataPanelV2 refreshImgDP(){
		return UICtrlV2.gui.getMp().getDp().refreshImg();
	}

	/**
	 * Refreshes the PhotoList-related data on the datapanel
	 * @author Anas Alaoui M'Darhri
	 * @return the updated and refreshed DataPanel
	 */
	static public DataPanelV2 refreshDataDP(){
		return UICtrlV2.gui.getMp().getDp().refreshData();
	}
	
	
	// OBJECT INSTANCIATING METHODS
	
	/**
	 * Given a photoList, builds for each picture a label to display  in the 
	 * main view
	 * @author Anas Alaoui M'Darhri
	 * @param pl a existing PhotoList
	 */
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
	// GENERIC LISTENERS
	public static void addGalleryAction(JButton btn){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		if(jfc.showOpenDialog(btn) == JFileChooser.APPROVE_OPTION){
			if(UICtrlV2.loadedPhotoLists.isEmpty())
				UICtrlV2.gui.getMp().getPll().removeAll();
			UUID uid = ProcessingCtrl.
				acknowledge(jfc.getSelectedFile());
			UICtrlV2.closeNoPl();
			UICtrlV2.buildLabels(UICtrlV2.loadedPhotoLists.get(uid));
			UICtrlV2.setCurrent(uid);
			UICtrlV2.gui.getMp().getPll().add(UICtrlV2.loadedPhotoLists.get(uid));
			UICtrlV2.gui.repaint();
			System.out.println("HELLO REPAINT");
		}
	
	}
}
