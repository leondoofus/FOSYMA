package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;
import mas.agents.TankerAgent;

import java.util.HashMap;

public class ReceiveMapBehaviour extends SimpleBehaviour{

    private final CustomAgent customAgent;
    private int nextBehaviourSelect; // 1 = no map received return to explore 2 = map ok sending map

    public ReceiveMapBehaviour(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
    }

    public void action() {
        //System.out.println(this.myAgent.getLocalName() +" Is waiting for a Map");
        nextBehaviourSelect = 1;
        ACLMessage received = this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM),50);
        if(received != null){
            try {
                if (received.getContentObject() instanceof HashMap) {
                    this.customAgent.setCommunicatingAgent(received.getSender());
                    //System.out.println(this.myAgent.getLocalName() + " : Map received from " + received.getSender().getLocalName());
                    HashMap map = (HashMap) received.getContentObject();
                    customAgent.fusion(map);
                    customAgent.clearSteps();
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            nextBehaviourSelect = 2;
        }

    }

    @Override
    public boolean done() {
        if (nextBehaviourSelect != 1)
            if(customAgent.getPreviousbehaviour().equals("RequestConnectionBehaviour")){
                nextBehaviourSelect = 2 ; //pass to send map
            }else{ //ca vient de sendmap
                nextBehaviourSelect = 3; //pass to send step
            }
        customAgent.setPreviousbehaviour("ReceiveMapBehaviour");
        return true;
    }

    public int onEnd() {
        if (customAgent instanceof TankerAgent) System.err.println(nextBehaviourSelect);
        return nextBehaviourSelect;
    }
}
