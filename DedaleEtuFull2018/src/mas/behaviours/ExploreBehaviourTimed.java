package mas.behaviours;

import mas.agents.CustomAgent;
import mas.util.Tools;

public class ExploreBehaviourTimed extends ExploreBehavior{

    public ExploreBehaviourTimed(final CustomAgent customAgent){
        super(customAgent);
    }

    @Override
    public void startAfterExplore(String myPosition){
        customAgent.setSteps(Tools.dijkstra(customAgent.getMapSons(),myPosition,this.customAgent.getOldestNode(),customAgent.getTankerPos()));
    }
}
