package behaviours.example5;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


/***
 * This behaviour allows the agent who possess it to send nb random int within [0-100[ to another agent whose local name is given in parameters
 * 
 * There is not loop here in order to reduce the duration of the behaviour (an action() method is not preemptive)
 * The loop is made by the behaviour itslef
 * 
 * @author Cédric Herpson
 *
 */

public class SendNbValuesBehaviour extends TickerBehaviour{
	
	private static final long serialVersionUID = 9088209402507795289L;

	/**
	 * number of values already sent
	 */
	private int nbMessagesSent=0;
	
	/**
	 * Name of the agent that should receive the values
	 */
	private String receiverName;
	
	Random r;
	
	private boolean ok;

	/**
	 * 
	 * @param myagent the Agent this behaviour is linked to
	 * @param nbValues the number of messages that should be sent to the receiver
	 * @param receiverName The local name of the receiver agent
	 */
	public SendNbValuesBehaviour(final Agent myagent, String receiverName) {
		super(myagent, 100);
		this.receiverName=receiverName;
		this.r= new Random();
		this.ok = false;

	}


	public void onTick() {
		//1) create the reception template (inform + name of the sender)
		final MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
																MessageTemplate.MatchSender(new AID(this.receiverName, AID.ISLOCALNAME)));

		//2) get the message
		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		if (msg != null) {
			if( msg.getContent().equals("stop")){
				ok = false;
				System.out.println("I stopped");
				block();
			}else{
				System.out.println(this.myAgent.getLocalName()+ "<----Message received from "+msg.getSender().getLocalName()+" ,content= "+msg.getContent());
				ok = true;
			}
		}

		if(ok){
			final ACLMessage msgsend = new ACLMessage(ACLMessage.INFORM);
			msgsend.setSender(this.myAgent.getAID());
			msgsend.addReceiver(new AID(this.receiverName, AID.ISLOCALNAME));  
				
			//3° compute the random value		
			msgsend.setContent(((Integer)r.nextInt(100)).toString());
			this.myAgent.send(msgsend);
			System.out.println(this.myAgent.getLocalName()+" ----> Message number "+this.nbMessagesSent+" sent to "+this.receiverName+" ,content= "+msgsend.getContent());
		}
	}

}
