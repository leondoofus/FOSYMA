package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.CustomAgent;
import mas.util.MyGraph;

import java.util.*;



public class ExploreBehavior extends SimpleBehaviour {
    /**
     * When an agent choose to move
     *
     */
    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent customAgent;
    //private int behaviorChoice;
    private int nbexp = 0;



    public ExploreBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;

    }

    @Override
    public void action() {
        nbexp ++;
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //behaviorChoice = 1; // pass to receive par def
        String myPosition = ((abstractAgent) this.myAgent).getCurrentPosition();
        //System.out.println( this.myAgent.getLocalName()+ " I'm at the case : " + myPosition+ " nb explore behaviour :"+nbexp);
        if (this.customAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                customAgent.pushPosition(myPosition);
                // recupere tous les voisins
                List<Couple<String, List<Attribute>>> lobs = ((abstractAgent) this.myAgent).observe();
                if (!customAgent.getMap().containsKey(myPosition)) {
                    String[] fils = new String[lobs.size() - 1];
                    int i = 0;
                    for (Couple c : lobs) {
                        if (!myPosition.equals(c.getLeft())) {
                            fils[i] = (String) c.getLeft();
                            i++;
                        }
                    }
                    customAgent.addNode(myPosition, fils);
                }


                String[] fils = customAgent.getMap().get(myPosition);
                String notvisited = null;
                for (String s : fils) {
                    if (!customAgent.getMap().keySet().contains(s)) {
                        notvisited = s;
                        break;
                    }
                }
                //System.out.print( this.myAgent.getLocalName()+ " printing the next case where i want to go : ");
                //System.out.println(notvisited);
                if (notvisited != null) {
                    boolean test = ((abstractAgent) this.myAgent).moveTo(notvisited);
                    if (!test) {
                        System.out.println(this.myAgent.getLocalName() + "im stuck ");
                        Random r= new Random();
                        //1) get a couple <Node ID,list of percepts> from the list of observables
                        int moveId=r.nextInt(lobs.size());
                        //2) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
                        while (!((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft()))
                             moveId=r.nextInt(lobs.size());

                    }
                } else {
                    //System.out.println( this.myAgent.getLocalName()+ " the agent is now blocked and cant move");
                    HashMap<String, String[]> myMap = customAgent.getMap();
                    Set<String> explored = myMap.keySet();
                    Set<String> unexplored = new HashSet<>();
                    for (String key : myMap.keySet()) {
                        String[] tmp = myMap.get(key);
                        for (String s : tmp) {
                            unexplored.add(s);
                        }
                    }
                    String[] unexploredAsStringtmp = unexplored.toArray(new String[unexplored.size()]);
                    for (String s : unexploredAsStringtmp) {
                        if (explored.contains(s)) {
                            unexplored.remove(s);
                        }
                    }
                    String[] unexploredAsString = unexplored.toArray(new String[unexplored.size()]);
                    //System.out.println(  this.myAgent.getLocalName()+ " print unexplored nodes");
                    for (String s : unexploredAsString) {
                        //System.out.print(" "+s);
                    }
                    //System.out.println();
                    if (unexplored.isEmpty()) {
                        System.err.println( this.myAgent.getLocalName()+ " : I explored the map");
                        this.customAgent.clearMap();
                    }

                    if (unexploredAsString.length > 0) {
                        //System.out.println( this.myAgent.getLocalName()+ " my destination : " + unexploredAsString[0] + " my position : " + myPosition);
                        //this.customAgent.setSteps(MyGraph.dijkstra(myMap, myPosition, unexploredAsString[0]));
                        this.customAgent.setSteps(MyGraph.dijkstraNoeudPlusProche(myMap,myPosition,unexploredAsString));
                        String step = this.customAgent.popStep();
                        if (!((abstractAgent) this.myAgent).moveTo(step)) {
                            this.customAgent.clearSteps();
                            //behaviorChoice = 2;
                        }
                    }
                }
            }
        } else {
            String step = this.customAgent.popStep();
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                //behaviorChoice = 2;
                this.customAgent.clearSteps();
            }
        }
        //behaviorEnded = true;
    }


    @Override
    public int onEnd() {
        return 1;
        //return behaviorChoice;
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("CheckMailBehavior");
        return true;
    }
}