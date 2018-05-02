package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import mas.agents.TankerAgent;
import mas.util.Tools;

import java.util.List;
import java.util.Random;

public class BroadcastBehaviour extends SimpleBehaviour {

    private TankerAgent tankerAgent;
    private String myName;

    public BroadcastBehaviour (final TankerAgent tankerAgent) {
        super(tankerAgent);
        this.tankerAgent = tankerAgent;
        myName = tankerAgent.getLocalName();
    }

    @Override
    public void action() {
        tankerAgent.broadcastTanker();
    }

    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        tankerAgent.setPreviousBehaviour("BroadcastBehaviour");
        return true;
    }
}
