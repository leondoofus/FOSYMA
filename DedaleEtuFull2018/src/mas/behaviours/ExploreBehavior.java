package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.CustomAgent;
import mas.util.*;

import java.io.IOException;
import java.util.*;



public class ExploreBehavior extends SimpleBehaviour {
    /**
     * When an agent choose to move
     *
     */
    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent myagent;
    private boolean passToSendBehaviour;


    public ExploreBehavior(final CustomAgent myagent) {
        super(myagent);
        this.myagent = myagent;
        //super(myagent);
        passToSendBehaviour = false;
    }

    @Override
    public void action() {
        String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();
        if (!myPosition.equals("")) {
            myagent.pushPosition(myPosition);
            // recupere tous les voisins
            List<Couple<String, List<Attribute>>> lobs = ((mas.abstractAgent) this.myAgent).observe();
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
            System.out.println("printing the next case where i want to go");
            System.out.println(notvisited);
            try {
                System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (notvisited != null) {
                boolean test = ((mas.abstractAgent) this.myAgent).moveTo(notvisited);
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
                    //converting the set to an array of strings because FUCK YOU JAVA
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
                System.out.println("my destination : " + unexploredAsString[0] + " my position : "+ myPosition);
                ArrayList<String> path = ShortestPath.solve(unexploredAsString[0],myPosition,myMap,new ArrayList<String>());
                String[] pathAstring = path.toArray(new String[path.size()]);
                //TDO REVERSE THIS ARRAY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                for(String s:pathAstring){
                    System.out.println("path : "+s);
                    ((mas.abstractAgent) this.myAgent).moveTo(s);
                }

            }
        }
    }


    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        return false;
    }
}