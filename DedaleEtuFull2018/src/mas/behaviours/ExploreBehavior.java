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
    private CustomAgent customAgent;
    private String myName;



    public ExploreBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
        myName = customAgent.getLocalName();

    }

    @Override
    public void action() {
        String myPosition = (this.customAgent).getCurrentPosition();
        //System.out.println( myName+ " I'm at the case : " + myPosition+ " nb explore behaviour :"+nbexp);
        if (this.customAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                List<Couple<String, List<Attribute>>> lobs = (this.customAgent).observe();
                customAgent.updateMap(lobs,myPosition);
                String notVisited = customAgent.getUnvisitedNode(myPosition);
                //System.out.print( myName+ " printing the next case where i want to go : ");
                //System.out.println(notVisited);
                if (notVisited != null) {
                    boolean canMove = (this.customAgent.moveTo(notVisited));
                    if (!canMove) {
                        customAgent.clearSteps();
                        System.out.println(myName + "im stuck ");
                        Random r= new Random();
                        int moveId=r.nextInt(lobs.size());
                        while (!(this.customAgent).moveTo(lobs.get(moveId).getLeft()))
                             moveId=r.nextInt(lobs.size());
                    }
                } else {
                    //System.out.println( myName+ " the agent is now blocked and cant move");
                    Set<String> unexplored = customAgent.geUnexploredNodes();
                    if (unexplored.isEmpty()) {
                        System.err.println( myName+ " : I explored the map");
                        this.customAgent.clearMap(); //TODO to delete
                    }
                    if (unexplored.size() > 0) {
                        //System.out.println( myName + " my destination : " + unexploredAsString[0] + " my position : " + myPosition);
                        //this.customAgent.setSteps(Tools.dijkstra(myMap, myPosition, unexploredAsString[0]))
                        this.customAgent.setSteps(Tools.dijkstraNoeudPlusProche(customAgent.getMap()
                                ,myPosition,unexplored.toArray(new String[unexplored.size()])));
                        String step = this.customAgent.popStep();
                        if (!(this.customAgent).moveTo(step)) {
                            this.customAgent.clearSteps();
                        }
                    }
                }
            }
        } else {
            String step = this.customAgent.popStep();
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                this.customAgent.clearSteps();
            }
        }
    }


    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("CheckMailBehavior");
        return true;
    }
}