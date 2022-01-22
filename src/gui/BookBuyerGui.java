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
import agents.BookSellerAgent;

/**
 *
 * @author aisyah
 */

public class BookBuyerGui extends JFrame{
    private BookBuyerAgent myBuyerAgent;
    private BookSellerAgent mySellerAgent;
	
    private JTextField titleField, priceField;
	
    public BookBuyerGui(BookBuyerAgent a) {
            super(a.getLocalName());

            myBuyerAgent = a;

            JPanel p = new JPanel();
            p.setLayout(new GridLayout(2, 2));
            p.add(new JLabel("              Book title:"));
            
            
            // list of books available for buyer
            String books[] = {"Harry Potter","To Kill a Mockingbird","The Great Gatsby","Da Vinci Code","Pride and Prejudice"};    
            JComboBox cb = new JComboBox(books); // create combobox
            
            
            p.add(cb); //add comboBox to panel
            p.add(new JLabel("              Book Price:"));
            priceField = new JTextField(5);
            p.add(priceField);
            priceField.setEditable(false);
            priceField.setText("");
            
            getContentPane().add(p, BorderLayout.CENTER);
            p = new JPanel();
            JButton addButton = new JButton("Buy");
            JButton CheckButton = new JButton("Check Price");
            p.add(CheckButton);
            p.add(addButton);
            getContentPane().add(p, BorderLayout.SOUTH);
            

            

            addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                            try {
                                    //String title = titleField.getText().trim();
                                    String title = (String) cb.getItemAt(cb.getSelectedIndex());
                                  
                                    myBuyerAgent.setBookTitle(title); //Define the title of the book based on what was entered
                                                                        
                                    myBuyerAgent.startProcess(); //Start purchase process
                            }catch(Exception e) {
                                    JOptionPane.showMessageDialog(BookBuyerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
                            }
                    }
            });
            
            cb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                        try {
                                
                        		JComboBox cb = (JComboBox) ev.getSource();
                        		String displayPrice = "RM" + mySellerAgent.getPrice();
                        		
                        		Object selected = cb.getSelectedItem();
                                if(selected.toString().equals("Harry Potter"))
                                priceField.setText(displayPrice);
                                //else if(selected.toString().equals("item2"))
                                //  field.setText("40");
                        		
                                String titleBook = (String) cb.getItemAt(cb.getSelectedIndex());
                                String checkTitle = mySellerAgent.getName();
                                System.out.print(titleBook);
                                System.out.print(checkTitle);
                                
                                //if (cb.)
                                
                                if (titleBook == checkTitle ) {
                                	
                                	// Get the price of book
                                	
                                	System.out.print(displayPrice);
                                	priceField.setText(displayPrice);                               	
                                }
                                                                    
                                
                        }catch(Exception e) {
                                JOptionPane.showMessageDialog(BookBuyerGui.this, "Invalid values for price","Error",JOptionPane.ERROR_MESSAGE);
                                System.out.print(e);
                        }
                }
        });



            addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e) {
                myBuyerAgent.doDelete();            
              }
            });

            setResizable(true);
    }

    public void showGui() {
      pack();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int centerX = (int)screenSize.getWidth() / 2;
      int centerY = (int)screenSize.getHeight() / 2;
      
      setSize(300,200);
      setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
      super.setVisible(true);
    }
}
