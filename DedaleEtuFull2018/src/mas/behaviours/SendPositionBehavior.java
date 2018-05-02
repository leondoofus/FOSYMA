package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import mas.agents.CustomAgent;

public class SendPositionBehavior extends SimpleBehaviour {

    private CustomAgent customAgent;

    public SendPositionBehavior(final CustomAgent customAgent){
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
        msg.setSender(this.customAgent.getAID());
        msg.setContent(this.customAgent.getCurrentPosition());
        msg.addReceiver(this.customAgent.getCommunicatingAgent());
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
    }

    @Override
    public boolean done() {
        customAgent.setPreviousBehaviour("SendPositionBehaviour");
        return true;
    }

    public int onEnd() {
        return 1; //pass to receive steps
    }
}
