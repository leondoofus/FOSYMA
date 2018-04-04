package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;

import java.util.List;

public class TankerBehaviour extends SimpleBehaviour {

    private static final long serialVersionUID = 9088209402507795289L;

    public TankerBehaviour (final mas.abstractAgent myagent) {
        super(myagent);
    }

    @Override
    public void action() {
        //Example to retrieve the current position
        String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

        if (myPosition!=""){
            //List of observable from the agent's current position
            List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition

            //DO NOTHING. But it could be a good idea to change that
        }

    }

    @Override
    public boolean done() {
        return false;
    }

}
