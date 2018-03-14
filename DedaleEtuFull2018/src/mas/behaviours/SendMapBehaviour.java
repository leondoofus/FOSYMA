package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import mas.agents.CustomAgent;

import java.io.IOException;
import java.io.Serializable;

public class SendMapBehaviour extends SimpleBehaviour {

    private CustomAgent customAgent;
    private int nextBehaviourSelect; //1 = go to recive map , 2 = go back to explore behaviour


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
        msg.addReceiver(this.customAgent.getComuicatingAgent());
        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
    }

    @Override
    public boolean done() {
        if(customAgent.getPreviousbehaviour() == "CheckMailBehavior"){
            nextBehaviourSelect =1;
        }else{
            nextBehaviourSelect = 2;
        }
        customAgent.setPreviousbehaviour("SendMapBehaviour");
        return true;
    }

    public int onEnd() {
        return nextBehaviourSelect;
    }
}
