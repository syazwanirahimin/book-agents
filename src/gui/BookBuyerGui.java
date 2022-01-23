/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import agents.BookBuyerAgent;

/**
 *
 * @author aisyah
 */

public class BookBuyerGui extends JFrame{
    private BookBuyerAgent myAgent;

    private JTextField titleField, priceField;

    public BookBuyerGui(BookBuyerAgent a) {
        super(a.getLocalName());

        myAgent = a;

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 3));
        p.add(new JLabel("              Book title:"));


        // list of books available for buyer
        String books[] = {"Harry Potter","To Kill a Mockingbird","The Great Gatsby","Da Vinci Code","Pride and Prejudice"};
        JComboBox cb = new JComboBox(books); // create combobox


        p.add(cb); //add comboBox to panel
        JLabel output = new JLabel();
        p.add(output);


        getContentPane().add(p, BorderLayout.CENTER);
        p = new JPanel();
        JButton addButton = new JButton("Buy");
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);


        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    //String title = titleField.getText().trim();
                    String title = (String) cb.getItemAt(cb.getSelectedIndex());

                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f,"Trying to buy: " + "\n" + (String) cb.getItemAt(cb.getSelectedIndex()),"info", JOptionPane.INFORMATION_MESSAGE);


                    myAgent.setBookTitle(title); //Define the title of the book based on what was entered


                    myAgent.startProcess(); //Start purchase process

                }catch(Exception e) {
                    JOptionPane.showMessageDialog(BookBuyerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });

        setResizable(true);
    }

    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)screenSize.getWidth() / 2;
        int centerY = (int)screenSize.getHeight() / 2;

        setSize(550,300);
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}
