package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import mas.agents.CustomAgent;
import mas.util.Langage;

public class SendMapBehaviour extends SimpleBehaviour {

    private CustomAgent customAgent;


    //TODO gerer la terminaison du behaviour.
    public SendMapBehaviour(final CustomAgent customAgent){
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(this.customAgent.getAID());
        msg.setContent(Langage.COMMUNICATION);
        msg.addReceiver(this.customAgent.getComuicatingAgent());
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
    }

    @Override
    public boolean done() {
        return true;
    }

    public int onEnd() {
        //System.out.println("end");
        return 1;
    }
}
