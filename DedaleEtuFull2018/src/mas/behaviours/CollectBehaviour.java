package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.CollectorAgent;
import mas.util.Tools;

import java.util.*;

public class CollectBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 9088209402507795289L;
    private CollectorAgent collectorAgent;
    private String myName;



    public CollectBehaviour(final CollectorAgent collectorAgent) {
        super(collectorAgent);
        this.collectorAgent = collectorAgent;
        myName = collectorAgent.getLocalName();


    }

    /*
    @Override
    public void action() {
        List<Couple<String, List<Attribute>>> lobs = ((abstractAgent) this.myAgent).observe();
        List<Attribute> lattribute= lobs.get(0).getRight();
        for(Attribute a:lattribute){
            switch (a) {
                case TREASURE : case DIAMONDS :
                    System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
                    System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
                    System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
                    System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
                    System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
                    break;

                default:
                    break;
            }
        }
        customAgent.updateTreasure(lobs);
        String myPosition = (this.customAgent).getCurrentPosition();
        if (this.customAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                Set<String> unexplored = customAgent.getUnexploredNodes();
                if (unexplored.isEmpty()) {
                    //System.out.println("I need more data on nodes");
                    randomMove();
                }else{
                    HashMap<String,List<Attribute>> nodesAttributes = this.customAgent.getNodesAttributes(unexplored);
                    for (Map.Entry<String, List<Attribute>> entry : nodesAttributes.entrySet()) {
                        String key = entry.getKey();
                        List<Attribute> value = entry.getValue();
                        //removing nodes that don't have treasure
                        for(Attribute a:lattribute){
                            switch (a) {
                                case TREASURE : case DIAMONDS :
                                    System.out.println("there is treasure in these nodes");
                                    break;
                                default:
                                    nodesAttributes.remove(key);
                                    break;
                            }
                        }
                    }
                    if(nodesAttributes.isEmpty()){
                        //System.out.println("I need more data on nodes");
                        randomMove();
                    }else{
                        //going to the node with treasure that is the closest to me
                        this.customAgent.setSteps(Tools.dijkstraNoeudPlusProche(customAgent.getMap()
                                ,myPosition,nodesAttributes.keySet().toArray(new String[nodesAttributes.size()])));
                        String step = this.customAgent.popStep();
                        if (!(this.customAgent).moveTo(step)) {
                            this.customAgent.clearSteps();
                        }
                    }
                }
            }
        }else{
            String step = this.customAgent.popStep();
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                this.customAgent.clearSteps();
            }
        }
    }
    */

    @Override
    public void action() {
        String myPosition = (this.collectorAgent).getCurrentPosition();
        if (this.collectorAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                List<Couple<String, List<Attribute>>> lobs = (this.collectorAgent).observe();
                collectorAgent.updateMap(lobs,myPosition);

                if (collectorAgent.getBackPackFreeSpace() != 0)
                    grab(lobs);

                collectorAgent.updateTreasure(lobs);

                String notVisited = collectorAgent.getUnvisitedNode(myPosition);
                if (notVisited != null) {
                    boolean canMove = (this.collectorAgent.moveTo(notVisited));
                    if (!canMove) {
                        collectorAgent.clearSteps();
                        //System.out.println(myName + "I'm stuck ");

                    }
                } else {
                    if (collectorAgent.isMapCompleted()) {
                        if (collectorAgent.getBackPackFreeSpace() == 0){
                            if (collectorAgent.getTankerPos() != null)
                                collectorAgent.setSteps(Tools.dijkstra(collectorAgent.getMap(),collectorAgent.getCurrentPosition(),collectorAgent.getTankerPos()));
                        } else {
                            if (collectorAgent.treasureCasesIsEmpty())
                                if (collectorAgent.getInitBackpackCapacity() == collectorAgent.getBackPackFreeSpace())
                                    randomMove(lobs);
                                else
                                    if (collectorAgent.getTankerPos() != null)
                                        collectorAgent.setSteps(Tools.dijkstra(collectorAgent.getMap(),collectorAgent.getCurrentPosition(),collectorAgent.getTankerPos()));
                                    else
                                        randomMove(lobs);
                            else
                                collectorAgent.setSteps(Tools.dijkstraNoeudPlusProche(collectorAgent.getMap(),collectorAgent.getCurrentPosition(),collectorAgent.getMyTreasureCases()));
                        }
                    } else {
                        randomMove(lobs);
                    }
                }
            }
        } else {
            String step = this.collectorAgent.popStep();
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                this.collectorAgent.clearSteps();
                return;
            }
            List<Couple<String, List<Attribute>>> lobs = (this.collectorAgent).observe();
            collectorAgent.updateMap(lobs,myPosition);
            if (collectorAgent.getBackPackFreeSpace() != 0)
                grab(lobs);
            collectorAgent.updateTreasure(lobs);
        }
    }

    private void randomMove(List<Couple<String, List<Attribute>>> lobs){
        Random r= new Random();
        int moveId=r.nextInt(lobs.size());
        while (!(this.collectorAgent).moveTo(lobs.get(moveId).getLeft()))
            moveId=r.nextInt(lobs.size());
    }

    private void grab(List<Couple<String, List<Attribute>>> lobs){
        for(Attribute a:lobs.get(0).getRight()){ //try to grab sth
            switch (a) {
                case TREASURE : case DIAMONDS :
                    System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
                    System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
                    System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
                    System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
                    System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        collectorAgent.setPreviousBehaviour("CheckMailBehavior");
        return true;
    }
}
