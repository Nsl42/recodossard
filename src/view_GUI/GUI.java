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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame{


	private static JTextField search;
	private static JPanel mainPanel;
	private static JButton addPicture;
	private static JPanel galPicture;
	private static GridBagConstraints gbc;
	static Border lineborder = BorderFactory.createLineBorder(Color.black, 1); 
	static ProgressBarAnalyze pbaInit;
	static JTabbedPane tabbedPane;
	static JPanel card3;
	static JPanel card2;
	static JPanel card1;
	static File[] listFiles;
	static ArrayList<JLabel> listJLab = new ArrayList<JLabel>();
	private static ArrayList<JLabel> indexPicSelected = new ArrayList<JLabel>();
	static ArrayList<File> list;
	static JLabel labPic;


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
			System.out.println("My Gallery : "+width/5+" ; 25");
			myGallery.setPreferredSize(new Dimension(7*width/100,25));
			myGallery.setBorder(lineborder);
			mainPanel.add(myGallery, gbc);

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 3;
			gbc.gridy = 1;
			JLabel listGal = new JLabel(" List galleries");
			System.out.println("List galleries : "+width/5+" ; "+(height-50));
			listGal.setPreferredSize(new Dimension(7*width/100, 70*height/100));
			listGal.setBorder(lineborder);
			mainPanel.add(listGal, gbc);

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.LAST_LINE_START;
			JButton addGallery = new JButton("Add gallery");
			System.out.println("Add gallery : "+width/5+" ; 25");
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
			gbc.gridwidth = 6;
			gbc.gridheight = 2;
			gbc.gridy = 1;
			galPicture = new JPanel();
			GridLayout gl = new GridLayout(0,7);
			gl.setVgap(10);
			JScrollPane jScrollPane = new JScrollPane(galPicture);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			System.out.println("Gal picture : "+4*width/5+" ; "+(2*height-25)/3);
			jScrollPane.setPreferredSize(new Dimension(36*width/100, 70*height/100));
			galPicture.setLayout(gl);

			mainPanel.add(jScrollPane, gbc);

			gbc.gridx = 1;
			gbc.gridwidth = 6;
			gbc.gridheight = 2;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.PAGE_END;
			String dataGal = "Gallery data";
			String data = "Datas";
			String analyze = "Analyze";
			tabbedPane = new JTabbedPane();
			card1 = new JPanel();
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
			card2 = new JPanel();
			card2.add(new JLabel("2"));
			card3 = new JPanel(new BorderLayout());
			pbaInit = new ProgressBarAnalyze(false, false);
			pbaInit.setVisible(true);
			card3.add(pbaInit);
			tabbedPane.addTab(dataGal, card1);
			tabbedPane.addTab(data,card2);
			tabbedPane.addTab(analyze,card3);
			System.out.println("Onglet : "+4*width/5+" ; "+(height-25)/3);
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
					addAnalyzeButtonActionListener(event);           
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
			search = new JTextField("Search bib...");
			search.setPreferredSize(new Dimension(50,25));
			mainPanel.add(search,gbc);
			search.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					search.setText("");
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
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "jpeg");
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
	private static void addAnalyzeButtonActionListener(ActionEvent event){
		ProgressBarAnalyze pba = new ProgressBarAnalyze(true, true);
		pbaInit.setVisible(false);
		JLabel nbPic = new JLabel("Picture"+"../..");
		JLabel nbBib = new JLabel("Number of bibs founded : ");
		card3.add(pba, BorderLayout.NORTH);
		card3.add(nbPic, BorderLayout.CENTER);
		card3.add(nbBib, BorderLayout.SOUTH);
		tabbedPane.setSelectedIndex(2);

	}

	/**
	 * Add in indexPicSelected list the object selected
	 * @param object
	 */
	private static void labPicActionPerformed(Object object) {		
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

	private static void afficheList(ArrayList l){
		for(int i=0; i<l.size(); i++){
			System.out.println("List "+i+" : "+l.get(i));
		}
	}

}
