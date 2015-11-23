package view_GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarAnalyze extends JPanel{
	private Thread t;
	private JProgressBar bar;
	private int valMax = 500; 

	public ProgressBarAnalyze(boolean start, boolean indeterminate){           
		t = new Thread(new Traitement());
		bar  = new JProgressBar();
		bar.setIndeterminate(indeterminate);
		bar.setPreferredSize(new Dimension(200,20));
		bar.setMaximum(valMax);
		bar.setMinimum(0);
	//	bar.setStringPainted(true);

		this.add(bar, BorderLayout.WEST);
		if(start){
			t.start();     
		}
		this.setVisible(true);      
	}

	class Traitement implements Runnable{   
		public void run(){
			for(int val = 0; val <= valMax; val++){
				bar.setValue(val);
				try {
					t.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}   
	}
}