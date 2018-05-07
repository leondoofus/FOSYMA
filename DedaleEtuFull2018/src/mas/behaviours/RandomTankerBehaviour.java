package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;

import java.util.List;

/**************************************
 *
 *
 * 				BEHAVIOUR
 *
 *
 **************************************/

class RandomTankerBehaviour extends TickerBehaviour {
	/**
	 * When an agent choose to move
	 *
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	public RandomTankerBehaviour (final mas.abstractAgent myagent) {
		super(myagent, 10000);
		//super(myagent);
	}

	@Override
	public void onTick() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println("tanker idle");
			//DO NOTHING. But it could be a good idea to change that
		}

	}

}
