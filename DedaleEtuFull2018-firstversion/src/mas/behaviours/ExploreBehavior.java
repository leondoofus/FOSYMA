package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;
import mas.agents.Aagent;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import env.Attribute;
import env.Couple;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class ExploreBehavior extends TickerBehaviour {
    /**
     * When an agent choose to move
     *
     */
    private static final long serialVersionUID = 9088209402507795289L;
    private Aagent myagent;


    public ExploreBehavior(final Aagent myagent) {
        super(myagent, 1000);
        this.myagent = myagent;
        //super(myagent);
    }

    @Override
    public void onTick() {
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
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setSender(this.myAgent.getAID());
                    try {
                        msg.setContentObject(myagent.getMap());
                        //System.out.println("Agent " + this.myAgent.getLocalName() + " is trying to reach its friends");
                        if (!myAgent.getLocalName().equals("Agent1")) {
                            msg.addReceiver(new AID("Agent1", AID.ISLOCALNAME));
                        } else {
                            msg.addReceiver(new AID("Agent2", AID.ISLOCALNAME));
                        }
                        ((mas.abstractAgent) this.myAgent).sendMessage(msg);
                    } catch (IOException e){
                        block();
                    }
                }
            }else {
                System.out.println(this.myagent.getLocalName()+" "+myagent.cpt);
                try {
                    String s = myagent.finIter();
                    if (s.equals(myPosition))
                        s = myagent.finIter();
                    ((mas.abstractAgent) this.myAgent).moveTo(s);
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(this.myagent.getLocalName()+" I explored the whole map");
                }
            }


        }
    }
}