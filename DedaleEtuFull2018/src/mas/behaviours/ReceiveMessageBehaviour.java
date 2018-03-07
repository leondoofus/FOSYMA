package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;

import java.util.HashMap;


public class ReceiveMessageBehaviour extends SimpleBehaviour{

    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent customAgent;
    int returnValue;


    public ReceiveMessageBehaviour(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;

    }


    public void action() {
        System.out.println("receive ------------------");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        returnValue = 1;
        /*final ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            returnValue = 2;
            System.out.println(this.myAgent.getLocalName() + "<----Result received from " + msg.getSender().getLocalName());
            try {
                if (msg.getContentObject() instanceof HashMap) {
                    HashMap map = (HashMap) msg.getContentObject();
                    customAgent.fusion(map);

                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }*/
        final ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            returnValue = 2;
            System.out.println(this.myAgent.getLocalName() + "<----Result received from " + msg.getSender().getLocalName());
            try {
                if (msg.getContentObject() instanceof HashMap) {
                    HashMap map = (HashMap) msg.getContentObject();
                    customAgent.fusion(map);
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return returnValue;
    }

}
