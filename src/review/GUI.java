/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.ProcessingCtrl;
import javax.swing.JFrame;

/**
 *
 * @author nsl
 */
public class GUI extends JFrame{
	
	public static void main(String args[])
	{
		 final ProcessingCtrl processCtrl = new ProcessingCtrl();
		GUI gui = new GUI();
		MainPanel mp = new MainPanel();
		NoPlPanel nplp = new NoPlPanel();
		PlPanel plp = new PlPanel();
		
		// Lecture du JSON, IF ELSE

		while(processCtrl.loadedPhotoLists.isEmpty())
		{
		gui.add(nplp);
		gui.setVisible(true);
		}
		gui.remove(nplp);
		gui.add(plp.getscrollPane(), plp.getGbc());
		gui.validate();
		gui.repaint();
		
		
	}
}
