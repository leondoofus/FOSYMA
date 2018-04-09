package mas.agents;

import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.behaviours.*;


public class CollectorAgent extends CustomAgent {

    protected void setup() {
        super.setup();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("collector");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }

        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new CollectBehaviour(this),"Col");
        fsmBehaviour.registerState(new CheckMailBehavior(this),"Ckm");
        fsmBehaviour.registerState(new RequestConnectionBehaviour(this),"Com");
        fsmBehaviour.registerState(new SendMapBehaviour(this),"Smp");
        fsmBehaviour.registerState(new ReceiveMapBehaviour(this),"Rmp");
        fsmBehaviour.registerState(new SendPositionBehavior(this),"Spos");
        fsmBehaviour.registerState(new ReceiveStepsBehavior(this),"Rstep");
        fsmBehaviour.registerState(new SendStepsBehavior(this),"Sstep");


        fsmBehaviour.registerTransition("Col","Ckm",1); //explore to check mail

        fsmBehaviour.registerTransition("Ckm","Com",1); //check mail to start com
        fsmBehaviour.registerTransition("Ckm","Smp",2); //check mail to send map

        fsmBehaviour.registerTransition("Com","Rmp",1); //com to receive

        fsmBehaviour.registerTransition("Smp","Rmp",1); // send to receive
        fsmBehaviour.registerTransition("Smp","Spos",2); // send to sendpos

        fsmBehaviour.registerTransition("Rmp","Col",1); // receive to explore
        fsmBehaviour.registerTransition("Rmp","Smp",2); // receive to send
        fsmBehaviour.registerTransition("Rmp","Spos",3); //receive to sendPos

        fsmBehaviour.registerTransition("Spos","Rstep",1); // sendpos to receiveSteps
        fsmBehaviour.registerTransition("Rstep","Col",1); // receiveSteps to Exp
        fsmBehaviour.registerTransition("Sstep","Col",1); // sendSteps to Exp

        addBehaviour(fsmBehaviour);


    }

    protected void takeDown(){

    }
}