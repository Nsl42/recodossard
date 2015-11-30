/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.UICtrl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import model.ImgModel;
import model.PhotoList;
import view_GUI.ProgressBarAnalyze;

/**
 *
 * @author nsl
 */
public class DataPanel extends JPanel{
		private JTabbedPane tabbedPane;
		private int imgCount;
		private int analysedCount;
		private int successCount;
		private int failCount;
		private	JLabel picAnalyzed;
		private	JLabel success;
		private	JLabel fail;
		private JLabel result;
		private JLabel path;

	public DataPanel(){
		tabbedPane = new JTabbedPane();
		result = new JLabel();
		imgCount = 0;
		analysedCount = 0;
		successCount = 0;
		this.path = new JLabel();
		failCount = 0;
		JPanel card1 = new JPanel();
		JPanel card2 = new JPanel();
		JPanel card3 = new JPanel(new BorderLayout());
		String dataGal = "Gallery data";
		String data = "Datas";
		String analyze = "Analyze";
		card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));
		picAnalyzed = new JLabel("Pictures analyzed : ");
		success = new JLabel("Success : ");
		fail = new JLabel("Fail : ");
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
		card2.add(this.path);
		card2.add(this.result);
		ProgressBarAnalyze pbaInit = new ProgressBarAnalyze(false, false);
		pbaInit.setVisible(true);
		card3.add(pbaInit);
		tabbedPane.addTab(dataGal, card1);
		tabbedPane.addTab(data,card2);
		tabbedPane.addTab(analyze,card3);
		tabbedPane.setPreferredSize(new Dimension(350, 180));
		this.setVisible(true);
	}
	public DataPanel refreshData(){
		
	//	PhotoList pl = UICtrl.getCurrent();
	//	this.imgCount = pl.getPhotolist().size();
		this.picAnalyzed.setText("Pictures analyzed : " + this.analysedCount + "/" + this.imgCount );
		this.success.setText("Success : " + this.successCount);
		this.fail.setText("Fail : "+ this.failCount);
		return this;

	}
	public DataPanel refreshImg() {
		ImgModel im = UICtrl.getSelected();
		if (im == null) {
			this.result.setText("NO SELECTED FILE");
		} else {
			this.result.setText((im.getResult().isEmpty()) ? "THE FILE HAS NOT"
				+ "BEEN ANALYSED YET" : im.getResult().toString());
			}
		return this;
	}
	public JTabbedPane getTabbedPane(){
		return this.tabbedPane;
	}
	public DataPanel setGBC()
	{
		UICtrl.gbc.gridx = 1;
		UICtrl.gbc.gridwidth = 6;
		UICtrl.gbc.gridheight = 2;
		UICtrl.gbc.gridy = 3;
		UICtrl.gbc.anchor = GridBagConstraints.PAGE_END;
		return this;
		
	}
	
}
