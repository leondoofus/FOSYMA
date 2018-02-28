package mas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.Aagent;

import java.util.HashMap;

/**
 * This behaviour is a one Shot.
 * It receives a message tagged with an inform performative, print the content in the console and destroy itlself
 *
 * @author Cédric Herpson
 *
 */
public class ReceiveMessageBehaviour extends TickerBehaviour{

    private static final long serialVersionUID = 9088209402507795289L;

    private Aagent myagent;

    /**
     *
     * This behaviour is a one Shot.
     * It receives a message tagged with an inform performative, print the content in the console and destroy itlself
     * @param myagent
     */
    public ReceiveMessageBehaviour(final Aagent myagent) {
        super(myagent,100);
        this.myagent = myagent;

    }


    public void onTick() {
        //1) receive the message
        final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        final ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            System.out.println(this.myAgent.getLocalName()+"<----Result received from "+msg.getSender().getLocalName());
            try {
                if (msg.getContentObject() instanceof HashMap) {
                    HashMap map = (HashMap) msg.getContentObject();
                    myagent.fusion(map);

                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }


        }else{
            block();// the behaviour goes to sleep until the arrival of a new message in the agent's Inbox.
        }
    }

}
