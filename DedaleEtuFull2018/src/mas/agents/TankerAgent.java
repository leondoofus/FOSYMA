package mas.agents;


import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.behaviours.*;


public class TankerAgent extends CustomAgent{

	/**
	 * 
	 */
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
        sd.setType("tanker");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }

        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new TankerBehaviour(this),"Tnk");
        fsmBehaviour.registerState(new ReceiveMapTankerBehaviour(this),"Rcv");
        fsmBehaviour.registerState(new BroadcastBehaviour(this),"Brc");
        fsmBehaviour.registerTransition("Tnk","Rcv",1);
        fsmBehaviour.registerTransition("Rcv","Tnk",1);
        fsmBehaviour.registerTransition("Tnk","Brc",2);
        fsmBehaviour.registerTransition("Brc","Tnk",1);
        addBehaviour(fsmBehaviour);


		}

	protected void takeDown(){

	}
}


