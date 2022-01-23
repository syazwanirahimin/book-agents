/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import jade.core.Agent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import javax.swing.*;

import gui.BookBuyerGui;

public class BookBuyerAgent extends Agent {
    private String bookTitle;
    private AID[] sellerAgents;
    private int ticker_timer = 10000;
    private BookBuyerAgent this_agent = this;
    private BookBuyerGui gui;

    protected void setup() {
        System.out.println("Buyer agent " + getAID().getName() + " is ready");

        //Start GUI
        gui = new BookBuyerGui(this);
        gui.showGui();

    }

    public void startProcess(){
        if(bookTitle.length() > 0) {
            System.out.println("Book: " + bookTitle);

            addBehaviour(new TickerBehaviour(this, ticker_timer) {
                protected void onTick() {
                    System.out.println("Trying to buy " + bookTitle);

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Found the following seller agents:");
                        sellerAgents = new AID[result.length];
                        for(int i = 0; i < result.length; i++) {
                            sellerAgents[i] = result[i].getName();
                            JFrame f = new JFrame();
                            JOptionPane.showMessageDialog(f,"Found the following seller agents: " + "\n" + sellerAgents[i].getName(),"info", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println(sellerAgents[i].getName());
                        }

                    }catch(FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new RequestPerformer(this_agent));
                }
            });
        } else {
            System.out.println("No target book title specified");
            JFrame f1 = new JFrame();
            JOptionPane.showMessageDialog(f1,"No target book title specified","error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void takeDown() {
        JFrame f2 = new JFrame();
        JOptionPane.showMessageDialog(f2,"Buyer agent " + getAID().getName() + " terminating","info", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Buyer agent " + getAID().getName() + " terminating");
        gui.dispose();
    }

    public AID[] getSellerAgents() {
        return sellerAgents;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle){
        this.bookTitle = bookTitle;
    }
}