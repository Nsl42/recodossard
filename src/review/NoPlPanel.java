/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package review;

import controller.ProcessingCtrl;
import controller.UICtrl;
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
		this.add(noGallery);
		this.add(addGalToStart);
		this.add(UICtrl.getAddGalleryButton());
	}
	
}
