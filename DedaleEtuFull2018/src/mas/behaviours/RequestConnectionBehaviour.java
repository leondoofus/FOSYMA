package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.agents.CustomAgent;

public class RequestConnectionBehaviour extends SimpleBehaviour {

    private CustomAgent customAgent;

    public RequestConnectionBehaviour(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        System.out.println(this.myAgent.getLocalName() + " : is requesting communication channel");
        DFAgentDescription[] allAgents = customAgent.getAgents();
        ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL); //acceptation de la proposition de communication
        msg.setSender(this.customAgent.getAID());
        msg.setContent("Starting Communication");
        for(DFAgentDescription agent: allAgents){
            if (!agent.getName().getName().equals(myAgent.getName()))
                msg.addReceiver(agent.getName());
        }
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("RequestConnectionBehaviour");
        return true;
    }
    @Override
    public int onEnd() {
        return 1;
    }
}
