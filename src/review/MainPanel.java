/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package review;

import controller.ProcessingCtrl;
import controller.UICtrl;
import java.util.ArrayList;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import view_GUI.ProgressBarAnalyze;

/**
 *
 * @author nsl
 */
public class MainPanel extends JPanel {
	
	public MainPanel(){
		super();
		this.setLayout(new GridBagLayout());
		// Useful tools instanciation
		Border lineborder; 
		lineborder = BorderFactory.createLineBorder(Color.black, 1);
		JPanel galPicture = new JPanel();
		GridLayout gl = new GridLayout(0,7);
		ProgressBarAnalyze pbaInit = new ProgressBarAnalyze(false, false);
		File[] listFiles;
		ArrayList<JLabel> listJLab = new ArrayList<JLabel>();
		ArrayList<JLabel> indexPicSelected = new ArrayList<JLabel>();
		ArrayList<File> list;
		JLabel labPic;
		JTextField search;
		
		
		//System.out.println(UICtrl.gbc.toString());
		UICtrl.gbc.gridx = 3;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 0;
		UICtrl.gbc.anchor = GridBagConstraints.PAGE_START;
		JButton analyseButton = new JButton("Analyze whole Gallery");
		analyseButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				System.out.println(ProcessingCtrl.listProcessing(UICtrl.getCurrent()));
				System.out.println("Processing...");
				
			} });
		this.add(analyseButton,UICtrl.gbc);

		UICtrl.gbc.weightx = 1;
		UICtrl.gbc.weighty = 1;
		UICtrl.gbc.gridx = 0;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 0;
		UICtrl.gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		UICtrl.gbc.fill = GridBagConstraints.HORIZONTAL;
		JLabel myGallery = new JLabel("GalleryName");
		myGallery.setPreferredSize(new Dimension(70,25));
		myGallery.setBorder(lineborder);
		this.add(myGallery, UICtrl.gbc);
		
		UICtrl.gbc.gridx = 0;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 2;
		UICtrl.gbc.gridy = 1;
		this.add(new JScrollPane(UICtrl.getPll()), UICtrl.gbc);

		UICtrl.gbc.gridx = 0;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 4;
		UICtrl.gbc.anchor = GridBagConstraints.LAST_LINE_START;
		this.add(UICtrl.getAddGalleryButton(), UICtrl.gbc);
				
		
		UICtrl.gbc.gridx = 1;
		UICtrl.gbc.gridwidth = 2;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 0;
		UICtrl.gbc.anchor = GridBagConstraints.PAGE_START;
		JLabel nameGal = new JLabel(" Name of gallery");
		nameGal.setPreferredSize(new Dimension(120,25));
		nameGal.setBorder(lineborder);
		this.add(nameGal, UICtrl.gbc);
		
		
		
		UICtrl.gbc.gridx = 4;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 0;
		UICtrl.gbc.anchor = GridBagConstraints.PAGE_START;
		JButton addPicture = new JButton(new ImageIcon(((new ImageIcon("res/add_image.png").getImage()
			.getScaledInstance(25, 25,java.awt.Image.SCALE_SMOOTH)))));
		addPicture.setPreferredSize(new Dimension(1,25));
		addPicture.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			}
		});
		this.add(addPicture,UICtrl.gbc);
		
		
		UICtrl.gbc.gridx = 5;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 0;
		UICtrl.gbc.anchor = GridBagConstraints.PAGE_START;
		JButton deletePicture = new JButton(new ImageIcon(((new ImageIcon("res/remove_image.png").getImage()
			.getScaledInstance(25, 25,java.awt.Image.SCALE_SMOOTH)))));
		deletePicture.setPreferredSize(new Dimension(1,25));
		deletePicture.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt){
			}
		});
		this.add(deletePicture,UICtrl.gbc);
		
		
		UICtrl.gbc.gridx = 6;
		UICtrl.gbc.gridwidth = 1;
		UICtrl.gbc.gridheight = 1;
		UICtrl.gbc.gridy = 0;
		UICtrl.gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		search = new JTextField("Search bib...");
		search.setPreferredSize(new Dimension(50,25));
		this.add(search,UICtrl.gbc);
		search.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		UICtrl.gbc.gridx =2;
		UICtrl.gbc.gridy =2;
		UICtrl.gbc.gridwidth =2;
		UICtrl.gbc.gridheight =2;
		UICtrl.gbc.anchor = GridBagConstraints.PAGE_START;
		UICtrl.getMp().add(UICtrl.getPlp().getscrollPane(), UICtrl.gbc);
		UICtrl.gbc.gridx =2;
		UICtrl.gbc.gridy =5;
		UICtrl.gbc.gridwidth =2;
		UICtrl.gbc.gridheight=1;
		UICtrl.getMp().add(UICtrl.getDp().getTabbedPane(),UICtrl.gbc);
	}
	

}
