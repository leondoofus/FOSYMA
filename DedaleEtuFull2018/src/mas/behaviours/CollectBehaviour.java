package mas.behaviours;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.CustomAgent;
import mas.util.Tools;

import java.util.*;

public class CollectBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 9088209402507795289L;
    private CustomAgent customAgent;
    private String myName;



    public CollectBehaviour(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
        myName = customAgent.getLocalName();


    }

    @Override
    public void action() {
        List<Couple<String, List<Attribute>>> lobs = ((abstractAgent) this.myAgent).observe();
        List<Attribute> lattribute= lobs.get(0).getRight();
        for(Attribute a:lattribute){
            switch (a) {
                case TREASURE : case DIAMONDS :
                    /*
                    System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
                    System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
                    System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
                    System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
                    System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());*/
                    break;

                default:
                    break;
            }
        }
        String myPosition = (this.customAgent).getCurrentPosition();
        if (this.customAgent.stepsIsEmpty()) {
            if (!myPosition.equals("")) {
                Set<String> unexplored = customAgent.geUnexploredNodes();
                if (unexplored.isEmpty()) {
                    //System.out.println("I need more data on nodes");
                    randomMove();
                }else{
                    HashMap<String,List<Attribute>> nodesAttributes = this.customAgent.getNodesAttributes(unexplored);
                    for (Map.Entry<String, List<Attribute>> entry : nodesAttributes.entrySet()) {
                        String key = entry.getKey();
                        List<Attribute> value = entry.getValue();
                        //removing nodes that don't have treasure
                        for(Attribute a:lattribute){
                            switch (a) {
                                case TREASURE : case DIAMONDS :
                                    System.out.println("there is treasure in these nodes");
                                    break;
                                default:
                                    nodesAttributes.remove(key);
                                    break;
                            }
                        }
                    }
                    if(nodesAttributes.isEmpty()){
                        //System.out.println("I need more data on nodes");
                        randomMove();
                    }else{
                        //going to the node with treasure that is the closest to me
                        this.customAgent.setSteps(Tools.dijkstraNoeudPlusProche(customAgent.getMap()
                                ,myPosition,nodesAttributes.keySet().toArray(new String[nodesAttributes.size()])));
                        String step = this.customAgent.popStep();
                        if (!(this.customAgent).moveTo(step)) {
                            this.customAgent.clearSteps();
                        }
                    }
                }
            }
        }else{
            String step = this.customAgent.popStep();
            if (!((abstractAgent) this.myAgent).moveTo(step)) {
                this.customAgent.clearSteps();
            }
        }
    }


    private void randomMove(){
        List<Couple<String, List<Attribute>>> lobs = (this.customAgent).observe();
        Random r= new Random();
        int moveId=r.nextInt(lobs.size());
        while (!(this.customAgent).moveTo(lobs.get(moveId).getLeft()))
            moveId=r.nextInt(lobs.size());
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
