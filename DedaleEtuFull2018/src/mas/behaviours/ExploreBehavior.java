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
    private int behaviorChoice;
    private ArrayList<String> steps;


    public ExploreBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
        steps = new ArrayList<>();
    }

    @Override
    public void action() {
        System.out.println("Explore .............");
        behaviorChoice = 1; // pass to receive par def
        String myPosition = ((abstractAgent) this.myAgent).getCurrentPosition();
        System.out.println("I'm at the case : " + myPosition);
        if (steps.isEmpty()) {
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
                System.out.print("printing the next case where i want to go : ");
                System.out.println(notvisited);
                if (notvisited != null) {
                    boolean test = ((abstractAgent) this.myAgent).moveTo(notvisited);
                    if (!test) {
                        behaviorChoice = 2;
                    }
                } else {
                    System.out.println("the agent is now blocked and cant move");
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
                    String[] exploredAsString = explored.toArray(new String[explored.size()]);
                    String[] unexploredAsString = unexplored.toArray(new String[unexplored.size()]);
                    System.out.println("print unexplored nodes");
                    for (String s : unexploredAsString) {
                        System.out.print(" "+s);
                    }
                    System.out.println();
                    if (unexplored.isEmpty())
                        System.err.println("WOooooooooooooooooo explored la carte");
                    if (unexploredAsString.length > 0) {
                        System.out.println("my destination : " + unexploredAsString[0] + " my position : " + myPosition);
                        steps = MyGraph.dijkstra(myMap, myPosition, unexploredAsString[0]);
                        String step = steps.remove(0);
                        if (!((abstractAgent) this.myAgent).moveTo(step)) {
                            steps.clear();
                            behaviorChoice = 2;
                        }
                    }
                }
            }
        } else {
            String step = steps.remove(0);
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                behaviorChoice = 2;
                steps.clear();
            }
        }
        //behaviorEnded = true;
    }


    @Override
    public int onEnd() {
        return behaviorChoice;
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("CheckMailBehavior");
        return true;
    }
}