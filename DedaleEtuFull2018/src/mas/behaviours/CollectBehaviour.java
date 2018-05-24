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
                    ((abstractAgent)this.myAgent).getMyTreasureType();
                    ((abstractAgent)this.myAgent).getBackPackFreeSpace();
                    System.err.println("Value of the treasure on the current position: "+a.getName() +" : "+ a.getValue()+" picked : "+((abstractAgent)this.myAgent).pick());
                    ((abstractAgent)this.myAgent).getBackPackFreeSpace();
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
        if(this.collectorAgent.getTankerPos() == null){
            Explore();
        }else {
            Collect();
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
                ((abstractAgent) this.myAgent).getBackPackFreeSpace();
                ((abstractAgent) this.myAgent).emptyMyBackPack("Tanker");
                ((abstractAgent) this.myAgent).getBackPackFreeSpace();
                this.collectorAgent.clearSteps();
                randomMove(lobs);
            }
        }else{
            randomMove(lobs);
        }
    }

    public void Collect(){
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

    public void Explore(){
        //customAgent.printMap();
        String myPosition = (this.collectorAgent).getCurrentPosition();
        //System.out.println( myName+ " I'm at the case : " + myPosition+ " nb explore behaviour :"+nbexp);
        List<Couple<String, List<Attribute>>> lobs = (this.collectorAgent).observe();
        collectorAgent.updateMap(lobs,myPosition);
        if (this.collectorAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                String notVisited = collectorAgent.getUnvisitedNode(myPosition);
                //System.out.print( myName+ " printing the next case where i want to go : ");
                //System.out.println(notVisited);
                if (notVisited != null) {
                    boolean canMove = (this.collectorAgent.moveTo(notVisited));
                    if (!canMove) {
                        collectorAgent.clearSteps();
                        randomMove(lobs);
                        //System.out.println(myName + "I'm stuck ");
                    }
                } else {
                    Set<String> unexplored = collectorAgent.getUnexploredNodes();
                    if(unexplored.isEmpty()){
                        startAfterExplore(myPosition);
                        movetoStep(lobs);
                    } else {
                        //System.out.println( myName + " my destination : " + unexploredAsString[0] + " my position : " + myPosition);
                        //this.customAgent.setSteps(Tools.dijkstra(myMap, myPosition, unexploredAsString[0]))
                        this.collectorAgent.setSteps(Tools.dijkstraClosestNode(collectorAgent.getMapSons()
                                ,myPosition,unexplored.toArray(new String[unexplored.size()]),collectorAgent.getTankerPos()));
                        movetoStep(lobs);

                    }
                }
            }
        } else {
            collectorAgent.updateMap(lobs,myPosition);
            movetoStep(lobs);
        }
    }

    public void startAfterExplore(String myPosition){
        this.collectorAgent.setSteps(Tools.dijkstra(collectorAgent.getMapSons(),myPosition,this.collectorAgent.getRandomNode(),collectorAgent.getTankerPos()));
    }

}
