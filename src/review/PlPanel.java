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
	private GridBagConstraints gbc = new GridBagConstraints();
	public PlPanel()
	{
		GridLayout gl = new GridLayout(0,7);
		gbc.gridx = 1;
		gbc.gridwidth = 6;
		gbc.gridheight = 2;
		gbc.gridy = 1;
		gl.setVgap(10);
		this.galPicture = new JPanel();
		//Getting the labels of existing imgmodels
		this.scrollPane = new JScrollPane(galPicture);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setPreferredSize(new Dimension(350,350));
		galPicture.setLayout(gl);

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
		return this.gbc;
	}
	public JScrollPane getscrollPane()
	{
		return this.scrollPane;
	}

	
}
