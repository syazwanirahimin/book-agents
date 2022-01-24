package behaviours;

import agents.BookBuyerAgent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import agents.BookBuyerAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.*;

public class RequestPerformer extends Behaviour{
  private AID bestSeller;
  private int bestPrice;
  private int repliesCount = 0;
  private MessageTemplate mt;
  private int step = 0;
  private BookBuyerAgent bbAgent;
  private String bookTitle;
  Date currentDate = new Date();
  Calendar c = Calendar.getInstance();

  public RequestPerformer(BookBuyerAgent a) {
    bbAgent = a;
    bookTitle = a.getBookTitle();

  }

  public void action() {
    switch(step) {
      case 0:
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for(int i = 0; i < bbAgent.getSellerAgents().length; i++) {
          cfp.addReceiver(bbAgent.getSellerAgents()[i]);
        }

        cfp.setContent(bookTitle);
        cfp.setConversationId("book-trade");
        cfp.setReplyWith("cfp" + System.currentTimeMillis());
        myAgent.send(cfp);

        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
        step = 1;
        break;

      case 1:
        ACLMessage reply = bbAgent.receive(mt);
        if(reply != null) {
          if(reply.getPerformative() == ACLMessage.PROPOSE) {
            int price = Integer.parseInt(reply.getContent());
            if(bestSeller == null || price < bestPrice) {
              bestPrice = price;
              bestSeller = reply.getSender();
            }
          }
          repliesCount++;
          if(repliesCount >= bbAgent.getSellerAgents().length) {
            step = 2;
          }
        } else {
          block();
        }
        break;

      case 2:
        ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        order.addReceiver(bestSeller);
        order.setContent(bookTitle);
        order.setConversationId("book-trade");
        order.setReplyWith("order" + System.currentTimeMillis());
        bbAgent.send(order);

        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                MessageTemplate.MatchInReplyTo(order.getReplyWith()));

        step = 3;

        break;

      case 3:
        reply = myAgent.receive(mt);
        if (reply != null) {
          if (reply.getPerformative() == ACLMessage.INFORM) {
            JFrame f = new JFrame();
            c.setTime(currentDate);
            c.add(Calendar.DATE, 3);

            Date deliveryDate = c.getTime();
            JOptionPane.showMessageDialog(f,bookTitle+" successfully purchased from agent "+reply.getSender().getName() + "\nBook Price = RM"+bestPrice + ". The book will start process to deliver by: " + deliveryDate ,"info", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(bookTitle+" successfully purchased from agent "+reply.getSender().getName());
            System.out.println("Price = "+bestPrice);
            myAgent.doDelete();
          }
          else {
            System.out.println("Attempt failed: requested book already sold.");
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f,"Attempt failed: requested book already sold.","error", JOptionPane.WARNING_MESSAGE);
          }

          step = 4;
        }
        else {
          block();
        }
        break;
    }
  }

  public boolean done() {
    if (step == 2 && bestSeller == null) {
      JFrame f = new JFrame();
      JOptionPane.showMessageDialog(f,"Attempt failed: "+bookTitle+" not available for sale","info", JOptionPane.ERROR_MESSAGE);
      System.out.println("Attempt failed: "+bookTitle+" not available for sale");
    }
    return ((step == 2 && bestSeller == null) || step == 4);
  }
}
