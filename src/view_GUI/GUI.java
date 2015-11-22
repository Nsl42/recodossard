package view_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame{

	/**
	 * JTextField for searching a number ofbib
	 */
	private static JTextField search;
	/**
	 * JPanel which contains all the component
	 */
	private static JPanel mainPanel;
	/**
	 * JButton for adding a picture
	 */
	private static JButton addPicture;
	/**
	 * JPanel which contains all the pictures of the gallery
	 */
	private static JPanel galPicture;
	/**
	 * GridBagConstraints for all the components
	 */
	private static GridBagConstraints gbc;
	/**
	 * Border for different components
	 */
	static Border lineborder = BorderFactory.createLineBorder(Color.black, 1); 
	/**
	 * ProgressBar for analysis
	 */
	static ProgressBarAnalyze pbaInit;
	/**
	 * TabbedPane in the bottom of the window
	 */
	static JTabbedPane tabbedPane;
	/**
	 * 3rd tab of the tabbedpane
	 */
	static JPanel card3;
	/**
	 * 2nd tab of the tabbedpane
	 */
	static JPanel card2;
	/**
	 * 1st tab of the tabbedpane
	 */
	static JPanel card1;
	/**
	 * List of all the pictures of the gallery
	 */
	static File[] listFiles;
	/**
	 * List of all the JLabel of the gallery which contains for each a picture
	 */
	static ArrayList<JLabel> listJLab = new ArrayList<JLabel>();
	/**
	 * List of the JLabel selected in the gallery
	 */
	private static ArrayList<JLabel> indexPicSelected = new ArrayList<JLabel>();
	/**
	 * List of file
	 */
	static ArrayList<File> list;
	/**
	 * A label which contains a picture
	 */
	static JLabel labPic;
	/**
	 * JLabel with the number of pictures analyzed
	 */
	static JLabel picAnalyzed;
	/**
	 * JLabel with the number of pictures analysis successed
	 */
	static JLabel success;
	/**
	 * JLabel with the number of pictures analysis failed
	 */
	static JLabel fail;
	/**
	 * JLabel with the options for analysis
	 */
	static JLabel advancedOptions;
	/**
	 * CheckBox for the EXIF option
	 */
	static JCheckBox dataEXIF;
	/**
	 * JLabel to specify the location of the picture
	 */
	static JLabel location;
	/**
	 * HashMap which contains the JLabel of a picture and its path File
	 */
	static HashMap<JLabel, String> mapLabPath = new HashMap<JLabel, String>();
	/**
	 * CheckBox for ranking option
	 */
	static JCheckBox ranking;
	/**
	 * JButton to add a file for the ranking option
	 */
	static JButton browse;
	/**
	 * JFrame to select the options of the analysis
	 */
	static JFrame fOptions;
	/**
	 * File which contains ranking
	 */
	static File rankingFile;
	/**
	 * Button to start the research of a bib number
	 */
	static JButton searchButton;
	/**
	 * Button to launch the analysis
	 */
	static JButton launch;
	/**
	 * ArrayList of RadioButton which contains all the locations possible of the picture
	 */
	static ArrayList<JRadioButton> lrb;
	/**
	 * Button Group which contains the arrayList of RadioButton
	 */
	static ButtonGroup bg;
	/**
	 * The name of the location of the picture selected
	 */
	static String placeSelected;
	/**
	*  Boolean : true is the location option if selected
	*/
	static boolean placeIsSelected;

	/**
	 * Constructor
	 */
	public GUI(){
		super("RecoDossard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		boolean galleryIsExist = true;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 3*screenSize.width/4;
		int height = 3*screenSize.height/4;

		if (galleryIsExist){
			mainPanel.setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.weighty = 1;

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			JLabel myGallery = new JLabel(" MyGallery");
			myGallery.setPreferredSize(new Dimension(7*width/100,25));
			myGallery.setBorder(lineborder);
			mainPanel.add(myGallery, gbc);

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 3;
			gbc.gridy = 1;
			JLabel listGal = new JLabel(" List galleries");
			listGal.setPreferredSize(new Dimension(7*width/100, 70*height/100));
			listGal.setBorder(lineborder);
			mainPanel.add(listGal, gbc);

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.LAST_LINE_START;
			JButton addGallery = new JButton("Add gallery");
			addGallery.setPreferredSize(new Dimension(7*width/100,25));
			mainPanel.add(addGallery, gbc);

			gbc.gridx = 1;
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			JLabel nameGal = new JLabel(" Name of gallery");
			nameGal.setPreferredSize(new Dimension(120,25));
			nameGal.setBorder(lineborder);
			mainPanel.add(nameGal, gbc);

			gbc.gridx = 1;
			gbc.gridwidth = 7;
			gbc.gridheight = 2;
			gbc.gridy = 1;
			galPicture = new JPanel();
			GridLayout gl = new GridLayout(0,7);
			gl.setVgap(10);
			JScrollPane jScrollPane = new JScrollPane(galPicture);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setPreferredSize(new Dimension(36*width/100, 70*height/100));
			galPicture.setLayout(gl);
			mainPanel.add(jScrollPane, gbc);

			gbc.gridx = 1;
			gbc.gridwidth = 7;
			gbc.gridheight = 2;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.PAGE_END;
			String analysisRes = "Analysis result";
			String dataPic = "Picture data";
			String analyze = "Analyze";
			tabbedPane = new JTabbedPane();
			tabbedPane.setVisible(false);
			card3 = new JPanel();
			card3.setLayout(new BoxLayout(card3, BoxLayout.Y_AXIS));
			picAnalyzed = new JLabel("Pictures analyzed : ../..");
			success = new JLabel("Success : ../..");
			fail = new JLabel("Fail : ../..");
			card3.add(picAnalyzed);
			card3.add(Box.createVerticalStrut(10));
			card3.add(success);
			card3.add(Box.createVerticalStrut(10));
			card3.add(fail);

			card1 = new JPanel();
			card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));
			location = new JLabel();
			card1.add(location);
			card1.setVisible(false);
			card2 = new JPanel();
			card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));
			pbaInit = new ProgressBarAnalyze(false, false);
			pbaInit.setVisible(true);
			card2.add(pbaInit);
			tabbedPane.addTab(dataPic,card1);
			tabbedPane.addTab(analyze,card2);
			tabbedPane.addTab(analysisRes, card3);
			tabbedPane.setPreferredSize(new Dimension(36*width/100, 23*height/100));
			mainPanel.add(tabbedPane,gbc);

			gbc.gridx = 3;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			JButton analyseButton = new JButton("Analyze");
			analyseButton.setPreferredSize(new Dimension(1,25));
			analyseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent event){
					addAnalyzeButtonActionListener();           
				}
			}); 
			mainPanel.add(analyseButton,gbc);

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
					addPicActionPerformed(evt);
				}
			});
			mainPanel.add(addPicture,gbc);


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
					deletePicActionPerformed(list, listJLab, indexPicSelected);
				}
			});
			mainPanel.add(deletePicture,gbc);

			gbc.gridx = 6;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.FIRST_LINE_END;
			search = new JTextField("Enter bib ...");
			search.setPreferredSize(new Dimension(50,25));
			mainPanel.add(search,gbc);
			search.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					search.setText("");
				}
			});
			search.addKeyListener(new KeyListener(){
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						if(search.getText().matches("[0-9]*")){
							searchBib(search.getText());
						}else{
							JFrame f = new JFrame("Error !");
							if(search.getText().equals("Enter bib ...")){
								JOptionPane.showMessageDialog(f, "Please enter a number of bib.");
							}else{ 
								JOptionPane.showMessageDialog(f, "Please search only a number of bib.");
							}
						}
					}
				}
				public void keyPressed(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});

			gbc.gridx = 7;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.FIRST_LINE_END;
			searchButton = new JButton("Search");
			searchButton.setPreferredSize(new Dimension(20,25));
			mainPanel.add(searchButton,gbc);
			searchButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt){
					if(search.getText().matches("[0-9]*")){
						searchBib(search.getText());
					}else{
						JFrame f = new JFrame("Error !");
						if(search.getText().equals("Enter bib ...")){
							JOptionPane.showMessageDialog(f, "Please enter a number of bib.");
						}else{ 
							JOptionPane.showMessageDialog(f, "Please search only a number of bib.");
						}
					}
				}
			});

		}else{
			mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			JLabel noGallery = new JLabel("No gallery");
			noGallery.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			JLabel addGalToStart = new JLabel("To start : Add a gallery");
			addGalToStart.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			JButton add = new JButton("Add Gallery");
			add.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			mainPanel.add(noGallery);
			mainPanel.add(addGalToStart);
			mainPanel.add(add);
		}

		add(mainPanel);


		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}


	/**
	 * Main which create a JFrame containing GUI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JFrame frame = new GUI();

	}

	/**
	 * Method when click on add picture button. Select files in local directory.
	 * @param evt
	 */
	private static void addPicActionPerformed(ActionEvent evt) {
		//create file chooser
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & JPEG Images", "jpg", "jpeg");
		jfc.setFileFilter(filter);
		jfc.setMultiSelectionEnabled(true);
		if(jfc.showOpenDialog(addPicture) == JFileChooser.APPROVE_OPTION){
			listFiles = jfc.getSelectedFiles();
			list = new ArrayList<File>(Arrays.asList(listFiles));
			createGallery(list);
		}
	}

	/**
	 * Display progress bar when click on analyze button
	 * @param event
	 */
	private static void addAnalyzeButtonActionListener(){
		placeIsSelected = false;
		if(!listJLab.isEmpty()){
			fOptions = new JFrame("Select option");
			fOptions.setVisible(true);
			fOptions.setLocationRelativeTo(null);
			fOptions.setLayout(new BorderLayout());
			JPanel panOptions = new JPanel();
			panOptions.setBorder(BorderFactory.createTitledBorder("Ranking Options"));
			panOptions.setLayout(new BoxLayout(panOptions, BoxLayout.Y_AXIS));

			ranking = new JCheckBox("Ranking");
			panOptions.add(ranking);
			browse = new JButton("Browse...");
			browse.setEnabled(false);
			panOptions.add(browse);
			ranking.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (((JCheckBox)e.getSource()).isSelected()) {
						browse.setEnabled(true);
						browse.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser jfc = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS or CSV file", "xls", "csv");
								jfc.setFileFilter(filter);
								if(jfc.showOpenDialog(browse) == JFileChooser.APPROVE_OPTION){
									rankingFile = jfc.getSelectedFile();
								}
							}
						});
					}
				}
			});
			panOptions.add(Box.createVerticalStrut(20));

			JPanel placeRace = new JPanel();
			placeRace.setBorder(BorderFactory.createTitledBorder("Place Options"));
			placeRace.setLayout(new BoxLayout(placeRace, BoxLayout.Y_AXIS));
			JCheckBox place = new JCheckBox("Place of picture");
			placeRace.add(place);
			bg = new ButtonGroup();
			ArrayList<String> a = selectXLScolumnsForAnalyze();
			lrb = new  ArrayList<JRadioButton>();
			for(int i=0; i<a.size(); i++){
				JRadioButton pl = new JRadioButton(a.get(i).toString());
				lrb.add(pl);
				pl.setEnabled(false);
				bg.add(pl);
				placeRace.add(pl);
			}
			place.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (((JCheckBox)e.getSource()).isSelected()) {
						placeIsSelected=true;
						for(int i=0; i<lrb.size(); i++){
							lrb.get(i).setEnabled(true);
						}
					}
				}
			});

			fOptions.add(panOptions, BorderLayout.NORTH);
			fOptions.add(placeRace, BorderLayout.CENTER);
			launch = new JButton("Launch");
			fOptions.add(launch, BorderLayout.SOUTH);

			launch.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(placeIsSelected){
						AbstractButton button = null;
						for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
							button = buttons.nextElement();
						} 
						placeSelected = button.getText();
					}
					launchAnalyze(ranking.isSelected(), placeIsSelected);
					fOptions.dispose();
				}
			});
			fOptions.setSize(300, 300);
		}else{
			JFrame f = new JFrame("Error !");
			JOptionPane.showMessageDialog(f, "There is no picture to analyze.");
		}

	}


	private static ArrayList<String> selectXLScolumnsForAnalyze(){
		ArrayList<String> al = new ArrayList<String>();
		al.add("Finish");
		al.add("KM 10");
		al.add("KM 30");
		al.add("KM 50");
		return al;
	}

	/**
	 * Displays analyze tab which displays the informations of the analysis
	 * @param optionSelected
	 */
	private static void launchAnalyze(boolean optionSelected, boolean placeSelected){
		if(optionSelected){
			if(rankingFile != null){
				//analyse avec classement ajouter placeSelected
			}else{
				JFrame f = new JFrame("Error !");
				JOptionPane.showMessageDialog(f, "You have to put a ranking file. Please click on Browse.");
				addAnalyzeButtonActionListener();
			}
		}else{
			// analyse sans classement
		}
		ProgressBarAnalyze pba = new ProgressBarAnalyze(true, true);
		pbaInit.setVisible(false);
		tabbedPane.setVisible(true);

		JLabel nbPic = new JLabel("Picture "+"../..");
		JLabel nbBib = new JLabel("Number of bibs founded : ");
		card2.add(pba, BorderLayout.NORTH);
		card2.add(Box.createVerticalStrut(10));
		card2.add(nbPic, BorderLayout.CENTER);
		card2.add(Box.createVerticalStrut(10));
		card2.add(nbBib, BorderLayout.SOUTH);
		tabbedPane.setSelectedIndex(1);
		// Si analyse terminée
		if(true){
			tabbedPane.setSelectedIndex(2);
		}
	}

	/**
	 * Add in indexPicSelected list the object selected
	 * @param object
	 */
	private static void labPicActionPerformed(Object object) {	
		manageTabs(object);
		if (indexPicSelected.contains(object)) {
			indexPicSelected.remove(object);
		} else {
			indexPicSelected.add((JLabel) object);
		}
		for (JLabel myJLabel : listJLab) {
			myJLabel.setBackground(null);
		}
		for (JLabel myJLabel : indexPicSelected) {
			myJLabel.setBackground(Color.BLUE);
		}
	}

	/**
	 * Delete the label/picture select in the gallery and refresh the gallery
	 * @param listF
	 * @param listJLab
	 * @param indexPicSelected
	 */
	private static void deletePicActionPerformed(ArrayList<File> listF, ArrayList<JLabel> listJL, ArrayList<JLabel> indexPicSelected){
		list = listF;
		if(!indexPicSelected.isEmpty()){
			int sizeIndexPic = indexPicSelected.size();
			int sizeListLab = listJL.size();
			listJLab = listJL;
			for(int j=0; j<sizeIndexPic; j++){
				for(int i=0; i<sizeListLab; i++){
					if(indexPicSelected.get(j) == listJLab.get(i)){
						list.remove(i);
						listJLab.remove(i);
						indexPicSelected.remove(j);
						j--;
						if(j==-1) j=0;
						sizeListLab--;
						sizeIndexPic--;
					}
				}
			}
			listJLab.removeAll(listJLab);
			galPicture.removeAll();
			createGallery(list);
		}else{
			JFrame f = new JFrame("Error !");
			JOptionPane.showMessageDialog(f, "You have to select a picture.");
		}

	}


	/**
	 * Create the gallery (create JLabel for each picture and put an ImageIcon in it). 
	 * Add a listener on each JLabel in case of the user select a JLabel of the gallery
	 * @param files
	 */
	private static void createGallery(ArrayList<File> files){
		list = files;
		for(int i=0;i<files.size(); i++){
			labPic = new JLabel();
			labPic.setIcon(new ImageIcon(
					new ImageIcon(files.get(i).toString()).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
			labPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			labPic.setBorder(BorderFactory.createRaisedBevelBorder());
			mapLabPath.put(labPic, files.get(i).getAbsolutePath());
			listJLab.add(labPic);
			labPic.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					labPicActionPerformed(me.getSource());
				}	
			});
			galPicture.add(labPic);
		}
		galPicture.repaint();
		mainPanel.validate();
		mainPanel.repaint();
	}

	/**
	 * When a picture is selected, tab display informations of the picture
	 * @param object
	 */
	private static void manageTabs(Object object){
		JLabel lab = ((JLabel) object);
		ImageIcon imgIcon = (ImageIcon) lab.getIcon();
		tabbedPane.setSelectedIndex(0);
		//set Labels of card1 and card2 :
		// images analysées, succès, échecs, checkbox
		location = new JLabel("Location : "+mapLabPath.get(lab).toString());
		card1.add(location);
		card1.add(new JSeparator(SwingConstants.HORIZONTAL));
		card1.add(new JLabel("EXIF data"));
		card1.add(Box.createVerticalStrut(10));
		tabbedPane.setVisible(true);
	}

	/**
	 * Search all the pictures in the gallery which have this number of bib
	 * @param bibNumber
	 */
	private static void searchBib(String bibNumber){
		System.out.println("WORKS !");
		//Implementer recherche de dossard
	}

}
