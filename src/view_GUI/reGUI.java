/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_GUI;

import controller.ProcessingCtrl;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nsl
 */
public class reGUI extends JFrame{
	
	private ProcessingCtrl processCtrl;
	private static GridBagConstraints gbc;
	private static JPanel mainPanel;
	private static JPanel galPicture;
	/**
	 * Constructor
	 */
	public reGUI(){
		super("Rec(oDossard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
	/*
		add(mainPanel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(3*screenSize.width/4, 3*screenSize.height/4);
		setLocationRelativeTo(null);
		setResizable(false);
		
		setVisible(true);
		*/
	}


	/**
	 * Main which create a JFrame containing GUI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JFrame frame = new reGUI();
			frame.add(mainPanel);
			frame.setVisible(true);
	}
}
