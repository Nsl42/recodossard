/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package review;

import controller.ProcessingCtrl;
import controller.UICtrlV2;
import java.util.UUID;
import javax.swing.JFileChooser;
import javax.swing.JTree;

/**
 *
 * @author nsl
 */
public class MainPanelV2 extends javax.swing.JPanel {

	/**
	 * Creates new form MainPanelV2
	 */
	public MainPanelV2() {
		initComponents();
		this.plPanelV22.setVisible(true);
	}
	public  PlPanelV2 getPlp()
	{
		return this.plPanelV22;
	}
	public  DataPanelV2 getDp()
	{
		return this.dataPanelV21;
	}
	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                plPanelV22 = new review.PlPanelV2();
                jToolBar1 = new javax.swing.JToolBar();
                jButton3 = new javax.swing.JButton();
                jButton4 = new javax.swing.JButton();
                jButton1 = new javax.swing.JButton();
                jButton2 = new javax.swing.JButton();
                jSeparator1 = new javax.swing.JToolBar.Separator();
                jToolBar2 = new javax.swing.JToolBar();
                analyseGal = new javax.swing.JButton();
                analysePhoto = new javax.swing.JButton();
                jButton7 = new javax.swing.JButton();
                jButton8 = new javax.swing.JButton();
                dataPanelV21 = new review.DataPanelV2();
                jScrollPane2 = new javax.swing.JScrollPane();
                plList1 = new review.PlList<>();

                jToolBar1.setRollover(true);

                jButton3.setText("Add Gallery");
                jButton3.setFocusable(false);
                jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });
                jToolBar1.add(jButton3);

                jButton4.setText("Delete Gallery");
                jButton4.setFocusable(false);
                jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar1.add(jButton4);

                jButton1.setText("Add Picture");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });
                jToolBar1.add(jButton1);

                jButton2.setText("Delete Picture");
                jToolBar1.add(jButton2);

                jSeparator1.setBackground(new java.awt.Color(102, 255, 102));
                jToolBar1.add(jSeparator1);

                jToolBar2.setRollover(true);

                analyseGal.setText("Analyse Gallery");
                analyseGal.setFocusable(false);
                analyseGal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                analyseGal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                analyseGal.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                analyseGalActionPerformed(evt);
                        }
                });
                jToolBar2.add(analyseGal);

                analysePhoto.setText("Analyse Photo");
                analysePhoto.setFocusable(false);
                analysePhoto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                analysePhoto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                analysePhoto.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                analysePhotoActionPerformed(evt);
                        }
                });
                jToolBar2.add(analysePhoto);

                jButton7.setText("Settings");
                jButton7.setFocusable(false);
                jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar2.add(jButton7);

                jButton8.setText("Export as JSON");
                jButton8.setFocusable(false);
                jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar2.add(jButton8);

                plList1.setModel(new javax.swing.AbstractListModel<String>() {
                        String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
                        public int getSize() { return strings.length; }
                        public String getElementAt(int i) { return strings[i]; }
                });
                jScrollPane2.setViewportView(plList1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                                .addGap(6, 6, 6))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(plPanelV22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dataPanelV21, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(plPanelV22, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(dataPanelV21, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane2))
                                .addContainerGap())
                );

                getAccessibleContext().setAccessibleParent(this);
        }// </editor-fold>//GEN-END:initComponents

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
                // TODO add your handling code here:d
        }//GEN-LAST:event_jButton1ActionPerformed

        private void analyseGalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analyseGalActionPerformed
		System.out.println("Processing in progress");
		System.out.println(ProcessingCtrl.listProcessing(UICtrlV2.getCurrent()));
		
        }//GEN-LAST:event_analyseGalActionPerformed

        private void analysePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysePhotoActionPerformed
		if(UICtrlV2.getSelected() != null) {
			System.out.println("Processing in progress");
			System.out.println(ProcessingCtrl.imgProcessing(UICtrlV2.getSelected()));
		}
        }//GEN-LAST:event_analysePhotoActionPerformed

        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                // TODO add your handling code here:
		UICtrlV2.addGalleryAction(this.jButton1);
        }//GEN-LAST:event_jButton3ActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton analyseGal;
        private javax.swing.JButton analysePhoto;
        private review.DataPanelV2 dataPanelV21;
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JButton jButton7;
        private javax.swing.JButton jButton8;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JToolBar.Separator jSeparator1;
        private javax.swing.JToolBar jToolBar1;
        private javax.swing.JToolBar jToolBar2;
        private review.PlList<String> plList1;
        private review.PlPanelV2 plPanelV22;
        // End of variables declaration//GEN-END:variables

	public PlList<String> getPll() {
		return this.plList1;
	}

}
