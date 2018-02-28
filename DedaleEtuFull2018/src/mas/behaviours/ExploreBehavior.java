package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.Aagent;

import java.util.*;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Dijkstra.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSourceDGS;


public class ExploreBehavior extends SimpleBehaviour {
    /**
     * When an agent choose to move
     *
     */
    private static final long serialVersionUID = 9088209402507795289L;
    private Aagent myagent;
    private boolean passToSendBehaviour;


    public ExploreBehavior(final Aagent myagent) {
        super(myagent);
        this.myagent = myagent;
        //super(myagent);
        passToSendBehaviour = false;
    }

    @Override
    public void action() {
        String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();

        if (myPosition != "") {
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


            if (notvisited != null) {
                boolean test = ((mas.abstractAgent) this.myAgent).moveTo(notvisited);
                if(!test) {
                    passToSendBehaviour = true;
                    /*ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setSender(this.myAgent.getAID());
                    try {
                        msg.setContentObject(myagent.getMap());
                        if (!myAgent.getLocalName().equals("Agent1")) {
                            msg.addReceiver(new AID("Agent1", AID.ISLOCALNAME));
                        } else {
                            msg.addReceiver(new AID("Agent2", AID.ISLOCALNAME));
                        }
                        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
                    } catch (IOException e){
                        block();
                    }*/
                }
            } else {
                /*Backtracking
                System.out.println(this.myagent.getLocalName()+" "+myagent.cpt);
                try {
                    String s = myagent.finIter();
                    if (s.equals(myPosition))
                        s = myagent.finIter();
                    ((mas.abstractAgent) this.myAgent).moveTo(s);
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(this.myagent.getLocalName()+" I explored the whole map");
                }*/
                // Plus court chemin vers non exploré plus proche
                HashMap<String,String[]> m = this.myagent.getMap();
                Set<String> explored = m.keySet();
                Set<String[]> unexplored = new HashSet<>();
                unexplored.addAll(m.values());
                //Cette ligne ne marche pas putain
                unexplored.removeAll(m.keySet());

                System.out.println("--------------");
                for (String s : explored) {
                    System.out.print(s+",");

                }
                System.out.println();
                System.out.println("");
                for (String[] s : unexplored) {
                    for(String t:s){
                        System.out.print(t+",");
                    }
                }
                System.out.println("");
                System.out.println("--------------");

                /*unexplored.addAll(m.values());
                unexplored.removeAll(m.keySet());
                Graph g = new SingleGraph("PCC");
                for (String s : explored){
                    g.addNode(s);
                }
                for (String[] s: unexplored){
                    for (String t : s)
                        try {
                            g.addNode(t);
                        }catch (Exception e){
                            System.out.println("lol");
                        }

                }

                for (String s : explored){
                    for (int i = 0; i < m.get(s).length; i++){
                        try {
                            g.addEdge(s + m.get(s)[i], s, m.get(s)[i]);
                        } catch (Exception e){
                            System.out.println("lol2");
                        }
                    }
                }

                Graph graph = new DefaultGraph("Dijkstra Test");

                Dijkstra dijkstra = new Dijkstra();//   (Element.edge, "weight", "A");
                dijkstra.init(graph);
                dijkstra.setSource(graph.getNode(myPosition));
                dijkstra.compute();
                System.out.println(dijkstra.getPath(graph.getNode((String) unexplored.toArray()[0])));
                */
            }

            }


        }


    @Override
    public int onEnd() {
        return 1;
    }

    @Override
    public boolean done() {
        return passToSendBehaviour;
    }
}