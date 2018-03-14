package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.lang.acl.ACLMessage;
import mas.agents.CustomAgent;
import jade.domain.FIPAAgentManagement.*;

import java.io.IOException;

//this method sends my map to all the agents in my range

public class SendMessageBehaviour extends SimpleBehaviour {

    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent customAgent;


    public SendMessageBehaviour(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        System.err.println("sending ---------------------------");
        DFAgentDescription [] allAgents = customAgent.getAgents();
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(this.customAgent.getAID());
        //msg.setContent(Langage.COMMUNICATION);
        for(DFAgentDescription agent: allAgents){
            msg.addReceiver(agent.getName());
        }
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        //System.out.println("end");
        return 1;
    }
}
