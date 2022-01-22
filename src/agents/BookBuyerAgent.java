package agents;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jade.core.Agent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

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
        }
    }

    protected void takeDown() {
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
