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
        fsmBehaviour.registerState(new SendMessageBehaviour(this),"B");
        fsmBehaviour.registerState(new ReceiveMessageBehaviour(this),"C");

        fsmBehaviour.registerTransition("A","C",1); //explore to receive
        fsmBehaviour.registerTransition("B","C",1); //send to receive
        fsmBehaviour.registerTransition("C","A",1); //receive to explore
        fsmBehaviour.registerTransition("C","B",2); //receive to send

        addBehaviour(fsmBehaviour);
        //addBehaviour(new ExploreBehavior(this));
        //addBehaviour(new ReceiveMessageBehaviour(this));
        //addBehaviour(new SendMessageBehaviour(this));

    }

    protected void takeDown(){

    }
}