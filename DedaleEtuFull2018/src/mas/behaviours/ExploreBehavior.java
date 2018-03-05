package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.CustomAgent;
import mas.util.*;

import java.io.IOException;
import java.util.*;
import java.util.Collections;



public class ExploreBehavior extends SimpleBehaviour {
    /**
     * When an agent choose to move
     *
     */
    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent myagent;
    private boolean passToSendBehaviour;


    public ExploreBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.myagent = customAgent;
        //super(customAgent);
        passToSendBehaviour = false;
    }

    @Override
    public void action() {
        passToSendBehaviour = false;
        try {
            System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to move");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPosition = ((abstractAgent) this.myAgent).getCurrentPosition();
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
                if(!test) {
                    passToSendBehaviour = true;
                }
            } else {
                System.out.println("the agent is now blocked and cant move");
                System.out.println("dijkstra");
                HashMap<String,String[]> myMap = myagent.getMap();
                Set<String> explored = myMap.keySet();
                Set<String> unexplored = new HashSet<String>();
                for (String key: myMap.keySet()) {
                    String[] tmp = myMap.get(key);
                    for (String s:tmp){
                        unexplored.add(s);
                    }
                }
                String[] unexploredAsStringtmp =unexplored.toArray(new String[unexplored.size()]);
                for(String s :unexploredAsStringtmp){
                    if(explored.contains(s)){
                        unexplored.remove(s);
                    }
                }
                String[] exploredAsString = explored.toArray(new String[explored.size()]);
                String[] unexploredAsString =unexplored.toArray(new String[unexplored.size()]);
                System.out.println("printing explored nodes");
                for(String s : exploredAsString){
                    System.out.println(s);
                }
                System.out.println("print unexplored nodes");
                for(String s : unexploredAsString){
                    System.out.println(s);
                }
                if (unexploredAsString.length > 0){
                    System.out.println("my destination : " + unexploredAsString[0] + " my position : "+ myPosition);
                    ArrayList<String> path = ShortestPath.solve(unexploredAsString[0],myPosition,myMap,new ArrayList<>());
                    Collections.reverse(path);
                    String[] pathAstring = path.toArray(new String[path.size()]);
                    for(String s:pathAstring){
                        System.out.println("path : "+s);
                        if(!((abstractAgent) this.myAgent).moveTo(s)){
                            passToSendBehaviour = true;
                            break;
                        }
                    }
                }
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