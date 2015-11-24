/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nsl
 */
public class PlPanel {
	private JPanel galPicture;
	private JScrollPane scrollPane;
	private GridBagConstraints gbc ;
	private GridLayout gl ;
	public PlPanel()
	{
		gl = new GridLayout(0,7);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridwidth = 6;
		gbc.gridheight = 2;
		gbc.gridy = 1;
		this.galPicture = new JPanel();
		//Getting the labels of existing imgmodels

	}
	
	public void refresh()
	{
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		labels = Util.createlabels();
		for(JLabel l : labels)
		{
			galPicture.add(l);
		}


	}
	public GridBagConstraints getGbc(){
		gbc.anchor = GridBagConstraints.PAGE_START;
		return this.gbc;
	}
	public JScrollPane getscrollPane()
	{
		gl.setVgap(10);
		this.scrollPane = new JScrollPane(galPicture);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setPreferredSize(new Dimension(350,350));
		this.galPicture.setLayout(gl);
		return this.scrollPane;
	}

	
}
