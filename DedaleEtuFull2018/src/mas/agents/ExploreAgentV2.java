package mas.agents;

import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.behaviours.*;


public class ExploreAgentV2 extends CustomAgent {

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
        fsmBehaviour.registerFirstState(new ExploreBehavior(this),"Exp");
        fsmBehaviour.registerState(new CheckMailBehavior(this),"Ckm");
        fsmBehaviour.registerState(new RequestConnectionBehaviour(this),"Com");
        fsmBehaviour.registerState(new SendMapBehaviour(this),"Smp");
        fsmBehaviour.registerState(new ReceiveMapBehaviour(this),"Rmp");

        fsmBehaviour.registerTransition("Exp","Ckm",1); //explore to check mail

        fsmBehaviour.registerTransition("Ckm","Com",1); //check mail to start com
        fsmBehaviour.registerTransition("Ckm","Smp",2); //check mail to send map

        fsmBehaviour.registerTransition("Com","Rmp",1); //com to recive

        fsmBehaviour.registerTransition("Smp","Rmp",1); // send to recive
        fsmBehaviour.registerTransition("Smp","Exp",2); // send to recive

        fsmBehaviour.registerTransition("Rmp","Exp",1); // send to recive
        fsmBehaviour.registerTransition("Rmp","Smp",2); // send to recive


        addBehaviour(fsmBehaviour);

        //addBehaviour(new ExploreBehavior(this));
        //addBehaviour(new ReceiveMessageBehaviour(this));
        //addBehaviour(new SendMessageBehaviour(this));

    }

    protected void takeDown(){

    }
}