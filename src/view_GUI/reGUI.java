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
		super("RecoDossard");
		final ProcessingCtrl processCtrl = new ProcessingCtrl();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JLabel noGallery = new JLabel("No gallery");
		noGallery.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		JLabel addGalToStart = new JLabel("To start : Add a gallery");
		addGalToStart.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		final JButton add = new JButton("Add Gallery");
		add.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		add.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				System.out.println("ADDED");
				if(jfc.showOpenDialog(add) == JFileChooser.APPROVE_OPTION){
//				System.out.println(processCtrl.processing(processCtrl.acknowledge(jfc.getSelectedFiles()[1])));	
				System.out.println("ADDED");
		}
			}
		});
		mainPanel.add(noGallery);
		mainPanel.add(addGalToStart);
		mainPanel.add(add);
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
		while(true)
		{
			frame.add(mainPanel);
			frame.show();

		}
	}
}
