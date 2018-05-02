package mas.agents;

import env.Attribute;
import env.Couple;
import env.Environment;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.abstractAgent;

import java.util.*;

public class CustomAgent extends abstractAgent {

    /**
     *
     */
    private static final long serialVersionUID = -1784844593772918359L;

    private HashMap<String,List<Attribute>> data;
    private HashMap<String,String[]> map;
    private AID comunicatingAgent;
    private String previousBehaviour;
    private ArrayList<String> steps;
    private String tankerPos = null;

    protected void setup(){
        super.setup();
        data = new HashMap<>();
        map = new HashMap<>();
        steps = new ArrayList<>();
        final Object[] args = getArguments();
        if(args[0]!=null){
            deployAgent((Environment) args[0]);
        }else{
            System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
            System.exit(-1);
        }
    }

    public DFAgentDescription[] getAgents() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("explorer");
        dfd.addServices(sd);
        DFAgentDescription [] result = new DFAgentDescription[0];
        try {
            result = DFService.search(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void updateMap( List<Couple<String, List<Attribute>>> lobs,String myPosition){
        if (!map.containsKey(myPosition)) {
            String[] sons = new String[lobs.size() - 1];
            int i = 0;
            for (Couple c : lobs) {
                data.put(myPosition,(List<Attribute>) c.getRight());
                if (!myPosition.equals(c.getLeft())) {
                    sons[i] = (String) c.getLeft();
                    i++;
                }
            }
            map.put(myPosition, sons);
        }
    }

    public String getUnvisitedNode(String myPosition){
        String res = null;
        String[] sons = map.get(myPosition);
        for (String s : sons) {
            if (!getMap().keySet().contains(s)) {
                res = s;
                break;
            }
        }
        return res;
    }


    public  Set<String> geUnexploredNodes(){
        Set<String> explored = map.keySet();
        Set<String> unexplored = new HashSet<>();
        for (String key : map.keySet()) {
            String[] tmp = map.get(key);
            unexplored.addAll(Arrays.asList(tmp));
        }
        String[] unexploredAsStringtmp = unexplored.toArray(new String[unexplored.size()]);
        for (String s : unexploredAsStringtmp) {
            if (explored.contains(s)) {
                unexplored.remove(s);
            }
        }
        return unexplored;
    }

    public HashMap<String,List<Attribute>> getNodesAttributes(Set<String> nodes){
        HashMap<String,List<Attribute>> res = new  HashMap<>();
        for(String node : nodes ){
            if(data.containsKey(node)){
                res.put(node,data.get(node));
            }
        }
        return res;
    }

    public AID getCommunicatingAgent() {
        return comunicatingAgent;
    }

    public void setCommunicatingAgent(AID comunicatingAgent) {
        this.comunicatingAgent = comunicatingAgent;
    }

    public HashMap<String, String[]> getMap() {
        return map;
    }

    public void clearMap () { map.clear(); }

    public void fusion(HashMap<String,String[]> map2) {
        map.putAll(map2);
    }

    public String getPreviousBehaviour() {
        return previousBehaviour;
    }

    public void setPreviousBehaviour(String previousBehaviour) {
        this.previousBehaviour = previousBehaviour;
    }

    public void setSteps(ArrayList<String> steps){
        this.steps = steps;
    }

    public String popStep(){
        return steps.remove(0);
    }

    public void clearSteps(){
        steps.clear();
    }

    public boolean stepsIsEmpty(){
        return steps.isEmpty();
    }

    public String getStep(){
        return steps.get(0);
    }

    public void printMap (){
        System.err.println("----------------");
        for (String s : map.keySet())
            System.out.print(s + " ");
        System.err.println("----------------");
    }

    public void die(){
        System.err.println("Agent-1 "+getAID().getLocalName()+" terminating.");
        takeDown();
        this.doDelete();
    }

    public boolean knowTanker(){
        return tankerPos != null;
    }

    public String getTankerPos(){
        return tankerPos;
    }
}