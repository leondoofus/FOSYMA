package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiveStepsBehavior extends SimpleBehaviour {

    private final CustomAgent customAgent;

    public ReceiveStepsBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
    }

    public void action() {
        System.out.println(this.myAgent.getLocalName() +" Is waiting for Steps");
        final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        final ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            System.out.println(this.myAgent.getLocalName() + "<----Result received from " + msg.getSender().getLocalName());
            try {
                if (msg.getContentObject() instanceof ArrayList) {
                    customAgent.setSteps((ArrayList<String>) msg.getContentObject());
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }

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
