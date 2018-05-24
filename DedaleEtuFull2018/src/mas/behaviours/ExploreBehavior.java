package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.CustomAgent;
import mas.util.Tools;

import java.util.*;



public class ExploreBehavior extends SimpleBehaviour {

    private static final long serialVersionUID = 9088209402507795289L;
    protected CustomAgent customAgent;
    private String myName;



    public ExploreBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
        myName = customAgent.getLocalName();

    }

    @Override
    public void action() {
        //customAgent.printMap();
        String myPosition = (this.customAgent).getCurrentPosition();
        //System.out.println( myName+ " I'm at the case : " + myPosition+ " nb explore behaviour :"+nbexp);
        List<Couple<String, List<Attribute>>> lobs = (this.customAgent).observe();
        customAgent.updateMap(lobs,myPosition);
        if (this.customAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                String notVisited = customAgent.getUnvisitedNode(myPosition);
                //System.out.print( myName+ " printing the next case where i want to go : ");
                //System.out.println(notVisited);
                if (notVisited != null) {
                    boolean canMove = (this.customAgent.moveTo(notVisited));
                    if (!canMove) {
                        customAgent.clearSteps();
                        randomMove(lobs);
                        //System.out.println(myName + "I'm stuck ");
                    }
                } else {
                    Set<String> unexplored = customAgent.getUnexploredNodes();
                    if(unexplored.isEmpty()){
                        startAfterExplore(myPosition);
                        movetoStep(lobs);
                    } else {
                        //System.out.println( myName + " my destination : " + unexploredAsString[0] + " my position : " + myPosition);
                        //this.customAgent.setSteps(Tools.dijkstra(myMap, myPosition, unexploredAsString[0]))
                        this.customAgent.setSteps(Tools.dijkstraClosestNode(customAgent.getMapSons()
                                ,myPosition,unexplored.toArray(new String[unexplored.size()]),customAgent.getTankerPos()));
                        movetoStep(lobs);

                    }
                }
            }
        } else {
            customAgent.updateMap(lobs,myPosition);
            movetoStep(lobs);
        }
    }


    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        customAgent.setPreviousBehaviour("CheckMailBehavior");
        return true;
    }

    private void randomMove(List<Couple<String, List<Attribute>>> lobs){
        Random r= new Random();
        int moveId=r.nextInt(lobs.size());
        while (!(this.customAgent).moveTo(lobs.get(moveId).getLeft()))
            moveId=r.nextInt(lobs.size());
    }
    private void movetoStep(List<Couple<String, List<Attribute>>> lobs){
        if(!this.customAgent.setpsIsEmpty()){
            String step = this.customAgent.popStep();
            if(!step.equals(this.customAgent.getTankerPos())){
                if (!((abstractAgent) this.myAgent).moveTo(step)) {
                    randomMove(lobs);
                    this.customAgent.clearSteps();
                }
            }else{
                randomMove(lobs);
            }
        }
    }
    public void startAfterExplore(String myPosition){
        this.customAgent.setSteps(Tools.dijkstra(customAgent.getMapSons(),myPosition,this.customAgent.getRandomNode(),customAgent.getTankerPos()));
    }
}