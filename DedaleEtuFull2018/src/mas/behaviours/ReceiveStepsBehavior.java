package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;
import mas.agents.TankerAgent;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiveStepsBehavior extends SimpleBehaviour {

    private final CustomAgent customAgent;

    public ReceiveStepsBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
    }

    public void action() {
        System.err.println(customAgent.getName() + "deb receive st");
        //System.out.println(this.myAgent.getLocalName() +" Is waiting for Steps");
        final ACLMessage msg = this.customAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
        if (msg != null) {
            //System.out.println(this.myAgent.getLocalName() + "<----Result received from " + msg.getSender().getLocalName());
            System.err.println("wooooooooo");
            try {
                if (msg.getContentObject() instanceof ArrayList) {
                    customAgent.setSteps((ArrayList<String>) msg.getContentObject());
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
        System.err.println(customAgent.getName() + "fin receive pos");
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("ReceiveStepsBehaviour");
        return true;
    }

    public int onEnd() {
        return 1; //return to explore behavior
    }
}
