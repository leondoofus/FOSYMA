package mas.agents;

import mas.behaviours.ExploreBehavior;
import mas.behaviours.ReceiveMessageBehaviour;
import mas.behaviours.SayHello;


public class ExploreAgent extends Aagent{

    protected void setup() {
        super.setup();
        addBehaviour(new ExploreBehavior(this));
        addBehaviour(new ReceiveMessageBehaviour(this));
    }

    protected void takeDown(){

    }
}