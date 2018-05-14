package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.CustomAgent;
import mas.util.Tools;

import java.util.List;
import java.util.Random;
import java.util.Set;


public class ExploreBehaviortanker extends ExploreBehavior {


    public ExploreBehaviortanker(final CustomAgent customAgent) {
        super(customAgent);
    }

    @Override
    public int onEnd() {
        if (this.customAgent.getTankerPos()!=null){
            return 2;
        }
        return 1;
    }

}