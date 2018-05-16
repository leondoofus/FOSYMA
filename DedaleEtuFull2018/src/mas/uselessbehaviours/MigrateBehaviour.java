package mas.uselessbehaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.abstractAgent;

import java.util.List;
import java.util.Random;

import static jade.lang.acl.MessageTemplate.MatchPerformative;

public class MigrateBehaviour extends TickerBehaviour {
    private abstractAgent myAgent;
    private boolean accepted = false;

    public MigrateBehaviour(abstractAgent myAgent) {
        super(myAgent,1000);
        this.myAgent = myAgent;
    }

    @Override
    protected void onTick() {
        if (!accepted) {
            final ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.or(MatchPerformative(ACLMessage.CONFIRM), MatchPerformative(ACLMessage.AGREE)));
            accepted = true;
        }
        //System.out.println("Server accepted");
        if (accepted) {
            String myPosition = myAgent.getCurrentPosition();
            List<Couple<String, List<Attribute>>> lobs = (this.myAgent).observe();
            randomMove(lobs);
        }
    }

    private void randomMove(List<Couple<String, List<Attribute>>> lobs){
        Random r= new Random();
        int moveId=r.nextInt(lobs.size());
        while (!(this.myAgent).moveTo(lobs.get(moveId).getLeft()))
            moveId=r.nextInt(lobs.size());
    }
}

