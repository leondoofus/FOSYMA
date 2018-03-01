package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

public class

SayHello extends TickerBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078521998L;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public SayHello (final Agent myagent) {
		super(myagent, 1000);
		//super(myagent);
	}

	@Override
	public void onTick() {

        String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();

        if (myPosition != "") {
            List<Couple<String, List<Attribute>>> lobs = ((mas.abstractAgent) this.myAgent).observe();
            String[] fils = new String[lobs.size() - 1];
            int i = 0;
            for (Couple c : lobs) {
                if (!myPosition.equals(c.getLeft())) {
                    for (Attribute s : (Attribute[]) c.getRight()) {
                        if (s.equals(Attribute.AGENT)) {
                            System.out.println("coucou");
                        }
                    }
                }
            }
        }

		/*ACLMessage msg=new ACLMessage(7);
		msg.setSender(this.myAgent.getAID());

		if (myPosition!=""){
			System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
			msg.setContent("Hello World, I'm at "+myPosition);
			if (!myAgent.getLocalName().equals("Agent1")){
				msg.addReceiver(new AID("Agent1",AID.ISLOCALNAME));
			}else{
				msg.addReceiver(new AID("Agent2",AID.ISLOCALNAME));
			}

			((mas.abstractAgent)this.myAgent).sendMessage(msg);

		}*/

	}

}