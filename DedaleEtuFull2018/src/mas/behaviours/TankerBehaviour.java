package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.TankerAgent;
import mas.util.Tools;

import java.util.List;
import java.util.Random;

public class TankerBehaviour extends SimpleBehaviour {

    private TankerAgent tankerAgent;
    private String myName;

    public TankerBehaviour (final TankerAgent tankerAgent) {
        super(tankerAgent);
        this.tankerAgent = tankerAgent;
        myName = tankerAgent.getLocalName();
    }

    @Override
    public void action() {
        //Example to retrieve the current position
        String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
        if (myPosition.equals(tankerAgent.getTankerPos())){
            return;
        }
        List<Couple<String, List<Attribute>>> lobs = ((mas.abstractAgent) this.myAgent).observe();//myPosition
        tankerAgent.updateMap(lobs, myPosition);
        if (tankerAgent.getTankerPos() == null) {
            if (myPosition != "") {
                //List of observable from the agent's current position
                randomMove(lobs);
            }
        } else {
            if (!myPosition.equals(tankerAgent.getTankerPos())){
                if (tankerAgent.stepsIsEmpty())
                    tankerAgent.setSteps(Tools.dijkstra(tankerAgent.getMapSons(),myPosition,tankerAgent.getTankerPos(),tankerAgent.getWumpusPosition()));
                    tankerAgent.clearwumpusPosition();
                String step = tankerAgent.popStep();
                if (!tankerAgent.moveTo(step)) {
                    for( Attribute a :lobs.get(0).getRight()){
                        if(a.getName().equals("Stench")){
                            this.tankerAgent.wumpusPosition(step);
                            break;
                        }
                    }
                    this.tankerAgent.clearSteps();
                    randomMove(lobs);
                }
            }
        }

    }

    @Override
    public int onEnd() {
        if (tankerAgent.getUnexploredNodes().isEmpty()){
            if (tankerAgent.getTankerPos() == null){
                for (int i = 5; i > 0; i--){
                    String s = Tools.centralize(tankerAgent.getMapSons());
                    if (s != null){
                        tankerAgent.setTankerPos(s);
                        break;
                    }
                }
            }
        }
            return 1;
    }

    @Override
    public boolean done() {
        tankerAgent.setPreviousBehaviour("TankerBehaviour");
        return true;
    }

    private void randomMove(List<Couple<String, List<Attribute>>> lobs){
        Random r= new Random();
        int moveId=r.nextInt(lobs.size());
        while (!tankerAgent.moveTo(lobs.get(moveId).getLeft()))
            moveId=r.nextInt(lobs.size());
    }

}
