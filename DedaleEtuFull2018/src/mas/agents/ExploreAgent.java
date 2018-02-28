package mas.agents;

import jade.core.behaviours.FSMBehaviour;
import mas.behaviours.ExploreBehavior;
import mas.behaviours.ReceiveMessageBehaviour;
import mas.behaviours.SayHello;


public class ExploreAgent extends Aagent{

    protected void setup() {
        super.setup();
        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new ExploreBehavior(this),"A");
        //Add send here
        fsmBehaviour.registerState(new ReceiveMessageBehaviour(this),"C");
        //Add wonder here , on verra
        fsmBehaviour.registerTransition("A","B",1);
        fsmBehaviour.registerTransition("B","C",1);
        fsmBehaviour.registerTransition("C","A",1);


        addBehaviour(new ExploreBehavior(this));
        addBehaviour(new ReceiveMessageBehaviour(this));
    }

    protected void takeDown(){

    }
}