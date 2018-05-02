package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.TankerAgent;

import java.util.HashMap;

public class ReceiveMapTankerBehaviour extends SimpleBehaviour {

    private final TankerAgent tankerAgent;

    public ReceiveMapTankerBehaviour(final TankerAgent tankerAgent) {
        super(tankerAgent);
        this.tankerAgent = tankerAgent;
    }

    public void action() {
        ACLMessage received = this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM),500);
        if(received != null){
            try {
                if (received.getContentObject() instanceof HashMap) {
                    HashMap map = (HashMap) received.getContentObject();
                    tankerAgent.fusion(map);
                    tankerAgent.clearSteps();
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean done() {
        tankerAgent.setPreviousBehaviour("ReceiveMapBehaviour");
        return true;
    }

    public int onEnd() {
        return 1;
    }
}
