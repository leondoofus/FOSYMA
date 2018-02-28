package agents.example6;

import java.security.Principal;

import behaviours.example5.SendNbValuesBehaviour;
import behaviours.example5.SumNbReceivedValuesBehaviour;
import jade.core.Agent;


/**
 * 
 * This agent is expects to receive K values from agentA, sum them and reply with the result
 * @author hc
 *
 */
public class AgentAny extends Agent{
	
	private int k;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499482209671348272L;

	protected void setup(){

		super.setup();
		k = 45;
		//get the parameters given into the object[]
		final Object[] args = getArguments();
		if(args.length!=0){
			System.err.println("Error while creating the sum agent");
			System.exit(-1);

		}else{
			String [] agents = new String [princ.Principal.n];
			//Add the behaviours
			for (int i = 0; i < princ.Principal.n; i++) {
				agents[i] = "Agent"+i;
			}
			addBehaviour(new SendNbValuesBehaviour(this,"agent"));
			addBehaviour(new SumNbReceivedValuesBehaviour(this, k,agents));
			System.out.println("the receiver agent "+this.getLocalName()+ " is started");
		}
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}
