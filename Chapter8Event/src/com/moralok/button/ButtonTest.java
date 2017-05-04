package com.moralok.button;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ButtonTest {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				ButtonFrame buttonFrame = new ButtonFrame();
				buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				buttonFrame.setVisible(true);
			}
		});
	}

}
