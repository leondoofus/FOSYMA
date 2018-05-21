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



    private void randomMove(List<Couple<String, List<Attribute>>> lobs){
        Random r= new Random();
        int moveId=r.nextInt(lobs.size());
        while (!(this.collectorAgent).moveTo(lobs.get(moveId).getLeft()))
            moveId=r.nextInt(lobs.size());
    }

    @Override
    public void action() {
        String myPosition = (this.collectorAgent).getCurrentPosition();
        if (!myPosition.equals("")) {
            List<Couple<String, List<Attribute>>> lobs = (this.collectorAgent).observe();
            grab(lobs);
            lobs = (this.collectorAgent).observe();
            collectorAgent.updateMap(lobs, myPosition);
            if (this.collectorAgent.stepsIsEmpty()) {
                if (collectorAgent.getBackPackFreeSpace() != 0) {
                    //I have space in my backpack
                    //take tresure if possible
                    Set<String> allnodes = collectorAgent.getAllNodeskey();
                    HashMap<String, List<Attribute>> nodesAttributes = this.collectorAgent.getNodesAttributes(allnodes);
                    ArrayList<String> myPrecious = getMyTreasureNodes(nodesAttributes);
                    //I cant find any more treasure to take :(
                    if (myPrecious.isEmpty()) {
                        //System.err.println(collectorAgent.isMapCompleted()+" "+collectorAgent.getTankerPos());
                        if (collectorAgent.getTankerPos() != null) {
                            collectorAgent.setSteps(Tools.dijkstra(collectorAgent.getMapSons(), collectorAgent.getCurrentPosition(), collectorAgent.getTankerPos(),null));
                            movetoStep(lobs);
                        } else {
                            //im waiting for the tanker position to be found(i need the full map to do so)
                            randomMove(lobs);
                        }
                    } else {
                        collectorAgent.setSteps(Tools.dijkstraClosestNode(collectorAgent.getMapSons(), collectorAgent.getCurrentPosition(), myPrecious.toArray(new String[myPrecious.size()]),collectorAgent.getTankerPos()));
                        movetoStep(lobs);

                    }
                    //I have no space in my backpack
                } else {
                    collectorAgent.computeTankerPos();
                    //System.err.println(collectorAgent.isMapCompleted()+" "+collectorAgent.getTankerPos());
                    if (collectorAgent.getTankerPos() != null) {
                        collectorAgent.setSteps(Tools.dijkstra(collectorAgent.getMapSons(), collectorAgent.getCurrentPosition(), collectorAgent.getTankerPos(),null));
                        movetoStep(lobs);

                    } else {
                        //im waiting for the tanker position to be found(i need the full map to do so)
                        randomMove(lobs);
                    }
                }
            } else {
                movetoStep(lobs);
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

    private ArrayList<String> getMyTreasureNodes(HashMap<String,List<Attribute>> nodesAttributes){
        String mytype = collectorAgent.getMyTreasureType();
        ArrayList<String> myPrecious = new ArrayList<>();
        for(String node:nodesAttributes.keySet()){
            for(Attribute a : nodesAttributes.get(node)){
                if(a.getName().equals(mytype)){
                    myPrecious.add(node);
                    //System.out.println("there is my  treasure in this node");
                }
            }
        }
        return  myPrecious;
    }

    private void movetoStep(List<Couple<String, List<Attribute>>> lobs){
        if(!this.collectorAgent.setpsIsEmpty()){
            String step = this.collectorAgent.popStep();
            if(!step.equals(this.collectorAgent.getTankerPos())){
                if (!((abstractAgent) this.myAgent).moveTo(step)) {
                    this.collectorAgent.clearSteps();
                    randomMove(lobs);
                }
            }else{
                System.out.println(this.myAgent.getLocalName() + " - My current backpack capacity is:" + ((mas.abstractAgent) this.myAgent).getBackPackFreeSpace());
                System.out.println(this.myAgent.getLocalName() + " - The agent tries to transfer is load into the Silo (if reachable); succes ? : " + ((mas.abstractAgent) this.myAgent).emptyMyBackPack("Tanker"));
                System.out.println("My current backpack capacity is:" + ((mas.abstractAgent) this.myAgent).getBackPackFreeSpace());
                this.collectorAgent.clearSteps();
                randomMove(lobs);
            }
        }else{
            randomMove(lobs);
        }
    }

}
