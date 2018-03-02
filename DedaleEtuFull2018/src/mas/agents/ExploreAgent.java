package mas.agents;

import jade.core.behaviours.FSMBehaviour;
import mas.behaviours.ExploreBehavior;
import mas.behaviours.ReceiveMessageBehaviour;
import mas.behaviours.SendMessageBehaviour;


public class ExploreAgent extends CustomAgent {

    protected void setup() {
        super.setup();
        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new ExploreBehavior(this),"A");
        fsmBehaviour.registerState(new SendMessageBehaviour(this),"C");
        fsmBehaviour.registerState(new ReceiveMessageBehaviour(this),"C");

        fsmBehaviour.registerTransition("A","A",0);
        fsmBehaviour.registerTransition("A","B",1);
        fsmBehaviour.registerTransition("B","C",1);
        fsmBehaviour.registerTransition("C","A",1);
        fsmBehaviour.registerTransition("C","B",1);

        addBehaviour(new ExploreBehavior(this));
        addBehaviour(new ReceiveMessageBehaviour(this));
        addBehaviour(new SendMessageBehaviour(this));

    }

    protected void takeDown(){

    }
}