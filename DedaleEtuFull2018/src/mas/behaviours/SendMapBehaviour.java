package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import mas.agents.CustomAgent;

import java.io.IOException;

public class SendMapBehaviour extends SimpleBehaviour {

    private CustomAgent customAgent;
    private int nextBehaviourSelect; //1 = go to receive map , 2 = go to send position


    //TODO gerer la terminaison du behaviour.
    public SendMapBehaviour(final CustomAgent customAgent){
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(this.customAgent.getAID());
        try {
            msg.setContentObject (this.customAgent.getMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Broadcast map
        for(DFAgentDescription agent: customAgent.getAgents()){
            if (!agent.getName().getName().equals(myAgent.getName()))
                msg.addReceiver(agent.getName());
        }
        //msg.addReceiver(this.customAgent.getCommunicatingAgent());
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
    }

    @Override
    public boolean done() {
        if(customAgent.getPreviousBehaviour().equals("CheckMailBehavior")){
            nextBehaviourSelect = 1; // pass to receive map
        }else{
            nextBehaviourSelect = 2; //pass to "exp
        }
        customAgent.setPreviousBehaviour("SendMapBehaviour");
        return true;
    }

    public int onEnd() {
        return nextBehaviourSelect;
    }
}
