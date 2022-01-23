package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;


import agents.BookSellerAgent;

public class BookSellerGui extends JFrame {
	private BookSellerAgent myAgent;

	private JTextField titleField, priceField;
	private JLabel soldField;

	public BookSellerGui(BookSellerAgent a) {
		super(a.getLocalName());

		myAgent = a;

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 2));
		p.add(new JLabel("      Book title:"));
		titleField = new JTextField(15);
		p.add(titleField);
		p.add(new JLabel("      Price:"));
		priceField = new JTextField(15);
		p.add(priceField);
		JRadioButton radFiction = new JRadioButton();
		JRadioButton radNonFiction = new JRadioButton();
		ButtonGroup G = new ButtonGroup();
		radFiction.setText("Fiction");
		radNonFiction.setText("Non-Fiction");
		radFiction.setBounds(120, 30, 120, 50);
		radNonFiction.setBounds(250, 30, 80, 50);
		p.add(radFiction);
		p.add(radNonFiction);
		G.add(radFiction);
		G.add(radNonFiction);
		getContentPane().add(p, BorderLayout.CENTER);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String title = titleField.getText().trim();
					String price = priceField.getText().trim();

					myAgent.updateCatalogue(title, Integer.parseInt(price));
					titleField.setText("");
					priceField.setText("");
					soldField.setText("");
				}catch(Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		p = new JPanel();
		p.setLayout(new GridLayout(2, 1));
		soldField = new JLabel("");
		p.add(soldField);
		p.add(addButton);
		getContentPane().add(p, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		});

		setResizable(true);
	}

	//To update the confirmation purchase
	public void setSoldField(String confirmation){
		soldField.setText(confirmation);
	}

	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;

		setSize(400,150);
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
}
