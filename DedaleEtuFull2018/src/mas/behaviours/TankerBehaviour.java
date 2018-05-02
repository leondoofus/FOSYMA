package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.CustomAgent;

import java.util.List;

public class TankerBehaviour extends SimpleBehaviour {

    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent customAgent;
    private String myName;

    public TankerBehaviour (final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
        myName = customAgent.getLocalName();
    }

    @Override
    public void action() {
        //Example to retrieve the current position
        String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

        if (myPosition!=""){
            //List of observable from the agent's current position
            List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
            customAgent.updateMap(lobs,myPosition);
            //customAgent.printMap();
            //DO NOTHING. But it could be a good idea to change that
        }

    }

    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        customAgent.setPreviousBehaviour("TankerBehaviour");
        return true;
    }

}
