/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package review;

import controller.ProcessingCtrl;
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
		final ProcessingCtrl processCtrl = new ProcessingCtrl();
		this.setLayout(new GridBagLayout());
		// Useful tools instanciation
		GridBagConstraints gbc = new GridBagConstraints();
		Border lineborder; 
		lineborder = BorderFactory.createLineBorder(Color.black, 1);
		JPanel galPicture = new JPanel();
		GridLayout gl = new GridLayout(0,7);
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel card1 = new JPanel();
		JPanel card2 = new JPanel();
		JPanel card3 = new JPanel(new BorderLayout());
		ProgressBarAnalyze pbaInit = new ProgressBarAnalyze(false, false);
		File[] listFiles;
		ArrayList<JLabel> listJLab = new ArrayList<JLabel>();
		ArrayList<JLabel> indexPicSelected = new ArrayList<JLabel>();
		ArrayList<File> list;
		JLabel labPic;
		JTextField search;
		

		gbc.weightx = 1;
		gbc.weighty = 1;
		
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JLabel myGallery = new JLabel(" MyGallery");
		myGallery.setPreferredSize(new Dimension(70,25));
		myGallery.setBorder(lineborder);
		this.add(myGallery, gbc);
		
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 3;
		gbc.gridy = 1;
		JLabel listGal = new JLabel(" List galleries");
		listGal.setPreferredSize(new Dimension(70,350));
		listGal.setBorder(lineborder);
		this.add(listGal, gbc);
		
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		JButton addGallery = new JButton("Add gallery");
		addGallery.setPreferredSize(new Dimension(70,25));
		this.add(addGallery, gbc);
		
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		JLabel nameGal = new JLabel(" Name of gallery");
		nameGal.setPreferredSize(new Dimension(120,25));
		nameGal.setBorder(lineborder);
		this.add(nameGal, gbc);
		
		gbc.gridx = 1;
		gbc.gridwidth = 6;
		gbc.gridheight = 2;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.PAGE_END;
		String dataGal = "Gallery data";
		String data = "Datas";
		String analyze = "Analyze";
		card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));
		JLabel picAnalyzed = new JLabel("Pictures analyzed : ../..");
		JLabel success = new JLabel("Success : ../..");
		JLabel fail = new JLabel("Fail : ../..");
		JLabel advancedOptions = new JLabel("Advanced Options");
		JCheckBox dataEXIF = new JCheckBox("EXIF data");
		JCheckBox raceData = new JCheckBox("Race data");
		card1.add(picAnalyzed);
		card1.add(Box.createVerticalStrut(10));
		card1.add(success);
		card1.add(Box.createVerticalStrut(10));
		card1.add(fail);
		card1.add(new JSeparator(SwingConstants.HORIZONTAL));
		card1.add(advancedOptions);
		card1.add(Box.createVerticalStrut(10));
		card1.add(dataEXIF);
		card1.add(raceData);
		card2.add(new JLabel("2"));
		pbaInit = new ProgressBarAnalyze(false, false);
		pbaInit.setVisible(true);
		card3.add(pbaInit);
		tabbedPane.addTab(dataGal, card1);
		tabbedPane.addTab(data,card2);
		tabbedPane.addTab(analyze,card3);
		tabbedPane.setPreferredSize(new Dimension(350, 180));
		this.add(tabbedPane,gbc);
		
		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		JButton analyseButton = new JButton("Analyze");
		analyseButton.setPreferredSize(new Dimension(1,25));
		analyseButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
			} });
		this.add(analyseButton,gbc);
		
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		JButton addPicture = new JButton(new ImageIcon(((new ImageIcon("res/add_image.png").getImage()
			.getScaledInstance(25, 25,java.awt.Image.SCALE_SMOOTH)))));
		addPicture.setPreferredSize(new Dimension(1,25));
		addPicture.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			}
		});
		this.add(addPicture,gbc);
		
		
		gbc.gridx = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		JButton deletePicture = new JButton(new ImageIcon(((new ImageIcon("res/remove_image.png").getImage()
			.getScaledInstance(25, 25,java.awt.Image.SCALE_SMOOTH)))));
		deletePicture.setPreferredSize(new Dimension(1,25));
		deletePicture.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt){
			}
		});
		this.add(deletePicture,gbc);
		
		
		gbc.gridx = 6;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		search = new JTextField("Search bib...");
		search.setPreferredSize(new Dimension(50,25));
		this.add(search,gbc);
		search.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}
		});
		
	}
	

}
