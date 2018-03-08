package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;

import java.util.HashMap;

public class CheckMailBehavior extends SimpleBehaviour {

    private int nextBehaviourSelect; //1 = request connection , 2 = send map
    private int TIME; //need to get time from system
    private CustomAgent customAgent;

    public CheckMailBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        System.out.println(this.myAgent.getLocalName() +" Is checking his mailbox");
        final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        final ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            if(msg.getPostTimeStamp() - TIME < 50){
                customAgent.setComuicatingAgent(msg.getSender());
                System.out.println(this.myAgent.getLocalName() + " : --Result received from " + msg.getSender().getLocalName());
                nextBehaviourSelect = 2;
            }else{
                System.out.println(this.myAgent.getLocalName() + " : --Warning message from  "+msg.getSender().getLocalName()+" too old !--");
                nextBehaviourSelect = 1;
            }
        }else{
            nextBehaviourSelect = 1; //no message was found
        }
    }

    @Override
    public boolean done() {
        return true;
    }

    public int onEnd() {
        return nextBehaviourSelect;
    }
}
