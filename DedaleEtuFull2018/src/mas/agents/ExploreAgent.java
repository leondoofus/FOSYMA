package mas.agents;

import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.behaviours.ExploreBehavior;
import mas.behaviours.ReceiveMessageBehaviour;
import mas.behaviours.SendMessageBehaviour;


public class ExploreAgent extends CustomAgent {

    protected void setup() {
        super.setup();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("explorer");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }

        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new ExploreBehavior(this),"A");
        fsmBehaviour.registerState(new SendMessageBehaviour(this),"B");
        fsmBehaviour.registerState(new ReceiveMessageBehaviour(this),"C");

        fsmBehaviour.registerTransition("A","C",1); //explore to receive
        fsmBehaviour.registerTransition("A","B",2); //explore to send
        fsmBehaviour.registerTransition("B","C",1); //send to receive
        fsmBehaviour.registerTransition("C","A",1); //receive to explore
        fsmBehaviour.registerTransition("C","B",2); //receive to send

        addBehaviour(fsmBehaviour);

        //addBehaviour(new ExploreBehavior(this));
        //addBehaviour(new ReceiveMessageBehaviour(this));
        //addBehaviour(new SendMessageBehaviour(this));

    }

    protected void takeDown(){

    }
}