package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;

import java.io.IOException;
import java.util.HashMap;


public class ReceiveMessageBehaviour extends SimpleBehaviour{

    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent customAgent;


    public ReceiveMessageBehaviour(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;

    }


    public void action() {
        try {
            System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to recive message");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        final ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            System.out.println(this.myAgent.getLocalName()+"<----Result received from "+msg.getSender().getLocalName());
            try {
                if (msg.getContentObject() instanceof HashMap) {
                    HashMap map = (HashMap) msg.getContentObject();
                    customAgent.fusion(map);

                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }


        }else{
            block();// the behaviour goes to sleep until the arrival of a new message in the agent's Inbox.
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
