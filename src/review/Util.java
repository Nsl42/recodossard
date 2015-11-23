/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.ProcessingCtrl;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import model.ImgModel;
import model.PhotoList;

/**
 *
 * @author nsl
 */
public class Util {
	static public  ArrayList<JLabel> createlabels()
	{
		ArrayList<JLabel> ret = new ArrayList<JLabel>();
		for(UUID plid : ProcessingCtrl.loadedPhotoLists.keySet())
			for(ImgModel im : ProcessingCtrl.loadedPhotoLists.get(plid).getPhotolist())
			{
				JLabel labPic = new JLabel();
			labPic.setIcon(new ImageIcon(
					new ImageIcon(im.getFile().toString()).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
			labPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			labPic.setBorder(BorderFactory.createRaisedBevelBorder());
			labPic.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
				}	
			});
			ret.add(labPic);

			}
		return ret;
	}
	
}
