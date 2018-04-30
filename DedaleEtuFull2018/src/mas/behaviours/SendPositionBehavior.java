package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.agents.CustomAgent;

import java.io.IOException;
import java.util.HashMap;

public class SendPositionBehavior extends SimpleBehaviour {

    private CustomAgent customAgent;

    public SendPositionBehavior(final CustomAgent customAgent){
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        System.err.println(customAgent.getName() + "deb send pos");
        ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
        msg.setSender(this.customAgent.getAID());
        msg.setContent(this.customAgent.getCurrentPosition());
        msg.addReceiver(this.customAgent.getCommunicatingAgent());
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
        System.err.println(customAgent.getName() + "fin send pos");
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("SendPositionBehaviour");
        return true;
    }

    public int onEnd() {
        return 1; //pass to receive steps
    }
}
