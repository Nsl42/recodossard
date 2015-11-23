/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package review;

import controller.ProcessingCtrl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author nsl
 */
public class NoPlPanel extends JPanel{

	public NoPlPanel(){
		super();
		final ProcessingCtrl processCtrl = new ProcessingCtrl();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
					processCtrl.acknowledge(jfc.getSelectedFile());
				}
			}
		});
		this.add(noGallery);
		this.add(addGalToStart);
		this.add(add);
	}
	
}
