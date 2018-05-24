package mas.agents;


import env.EntityType;
import env.Environment;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.behaviours.*;
import mas.abstractAgent;


public class TankerAgent extends CustomAgent{

    private String wumpusPosition = null;
	private static final long serialVersionUID = -1784844593772918359L;



	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){
        super.setup();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Tanker");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }
        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new ExploreBehaviortanker(this),"Exp");
        fsmBehaviour.registerState(new CheckMailBehavior(this),"Ckm");
        fsmBehaviour.registerState(new RequestConnectionBehaviour(this),"Com");
        fsmBehaviour.registerState(new SendMapBehaviour(this),"Smp");
        fsmBehaviour.registerState(new ReceiveMapBehaviour(this),"Rmp");

        fsmBehaviour.registerState(new TankerBehaviour(this),"Tnk");
        fsmBehaviour.registerState(new ReceiveMapTankerBehaviour(this),"Rcv");

        fsmBehaviour.registerTransition("Exp","Ckm",1); //explore to check mail
        fsmBehaviour.registerTransition("Exp","Tnk",2); //explore to Thk

        fsmBehaviour.registerTransition("Ckm","Com",1); //check mail to start com
        fsmBehaviour.registerTransition("Ckm","Smp",2); //check mail to send map

        fsmBehaviour.registerTransition("Com","Rmp",1); //com to receive

        fsmBehaviour.registerTransition("Smp","Rmp",1); // send to receive
        fsmBehaviour.registerTransition("Smp","Exp",2); // send to exp

        fsmBehaviour.registerTransition("Rmp","Exp",1); // receive to explore
        fsmBehaviour.registerTransition("Rmp","Smp",2); // receive to send

        fsmBehaviour.registerTransition("Tnk","Rcv",1);
        fsmBehaviour.registerTransition("Rcv","Tnk",1);

        addBehaviour(fsmBehaviour);
	}


	public void wumpusPosition(String pos){
	    wumpusPosition = pos;
    }

    public void clearwumpusPosition(){
	    wumpusPosition = null;
    }

    public String getWumpusPosition() {
        return wumpusPosition;
    }

    protected void takeDown(){

	}


}


