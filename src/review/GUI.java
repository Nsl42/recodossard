/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.ProcessingCtrl;
import controller.UICtrl;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

/**
 *
 * @author nsl
 */
public class GUI extends JFrame{
	
	public GUI(String s)
	{
		super(s);

	}
	public static void main(String args[])
	{
		 final ProcessingCtrl processCtrl = new ProcessingCtrl();
		GUI gui = new GUI("RecoDossard");
		gui.setLayout(new BorderLayout());
		// Lecture du JSON, IF ELSE

		NoPlPanel nplp = new NoPlPanel();
		while(true)
		{
			while(processCtrl.loadedPhotoLists.isEmpty())
			{
				gui.add(nplp, BorderLayout.CENTER);
				gui.setVisible(true);
			}
			gui.remove(nplp);
			gui.add(UICtrl.getMp(), BorderLayout.CENTER);
			gui.invalidate();		
			gui.repaint();
		}
	}
}
