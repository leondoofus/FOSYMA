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
    private CustomAgent myagent;
    private boolean passToSendBehaviour;
    private ArrayList<String> steps;


    public ExploreBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.myagent = customAgent;
        //super(customAgent);
        passToSendBehaviour = false;
        steps = new ArrayList<>();
    }

    @Override
    public void action() {

            passToSendBehaviour = false;
            String myPosition = ((abstractAgent) this.myAgent).getCurrentPosition();
            System.out.println("I'm at the case : " + myPosition);
            try {
                System.out.println("Press Enter in the console to allow the agent " + this.myAgent.getLocalName() + " to move");
                //System.in.read();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (steps.isEmpty()) {
            if (!myPosition.equals("")) {
                myagent.pushPosition(myPosition);
                // recupere tous les voisins
                List<Couple<String, List<Attribute>>> lobs = ((abstractAgent) this.myAgent).observe();
                if (!myagent.getMap().containsKey(myPosition)) {
                    myagent.increment();
                    String[] fils = new String[lobs.size() - 1];
                    int i = 0;
                    for (Couple c : lobs) {
                        if (!myPosition.equals(c.getLeft())) {
                            fils[i] = (String) c.getLeft();
                            i++;
                        }
                    }
                    myagent.addNode(myPosition, fils);
                }


                String[] fils = myagent.getMap().get(myPosition);
                String notvisited = null;
                for (String s : fils) {
                    if (!myagent.getMap().keySet().contains(s)) {
                        notvisited = s;
                        break;
                    }
                }
                System.out.print("printing the next case where i want to go : ");
                System.out.println(notvisited);
                if (notvisited != null) {
                    boolean test = ((abstractAgent) this.myAgent).moveTo(notvisited);
                    System.out.println(test);
                    System.out.println(passToSendBehaviour);
                    if (!test) {
                        passToSendBehaviour = true;
                    }
                } else {
                    System.out.println("the agent is now blocked and cant move");
                    System.out.println("dijkstra");
                    HashMap<String, String[]> myMap = myagent.getMap();
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
                    System.out.println("printing explored nodes");
                    for (String s : exploredAsString) {
                        System.out.println(s);
                    }
                    System.out.println("print unexplored nodes");
                    for (String s : unexploredAsString) {
                        System.out.println(s);
                    }
                    if (unexploredAsString.length > 0) {
                        //TODO It works with my algo here
                        System.out.println("my destination : " + unexploredAsString[0] + " my position : " + myPosition);
                        steps = MyGraph.dijkstra(myMap, myPosition, unexploredAsString[0]);
                        String step = steps.remove(0);
                        if (!((abstractAgent) this.myAgent).moveTo(step)) {
                            steps.clear();
                        }

                        /*for (String s : path) {
                            System.out.println("path : " + s);
                            //TODO have to see this because the prof said that we can only move once for each iteration
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!((abstractAgent) this.myAgent).moveTo(s)) {
                                passToSendBehaviour = true;
                                break;
                            }
                        }*/
                        // TODO your algo doesn't work
                    /*
                    ArrayList<String> path = ShortestPath.solve(unexploredAsString[0],myPosition,myMap,new ArrayList<>());
                    Collections.reverse(path);
                    String[] pathAstring = path.toArray(new String[path.size()]);
                    for(String s:pathAstring){
                        System.out.println("path : "+s);
                        if(!((abstractAgent) this.myAgent).moveTo(s)){
                            passToSendBehaviour = true;
                            break;
                        }
                    }*/
                    }
                }
            }
        } else {
            String step = steps.remove(0);
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                //passToSendBehaviour = true;
                steps.clear();
            }
        }
    }


    @Override
    public int onEnd() {
        System.out.println("end");
        return 0;
    }

    @Override
    public boolean done() {
        System.out.println("done");
        return false;
    }
}