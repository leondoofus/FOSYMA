package mas.agents;

import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.abstractAgent;
import mas.util.Tools;
import mas.util.nodeData;

import java.util.*;

public class CustomAgentv2 extends abstractAgent {

    private static final long serialVersionUID = -1784844593772918359L;

    private HashMap<String,nodeData> map;

    private AID comunicatingAgent;
    private String previousBehaviour;
    private ArrayList<String> steps;
    private boolean mapCompleted;
    private String tankerPos = null;

    protected void setup(){
        super.setup();

        map = new HashMap<>();
        steps = new ArrayList<>();
        mapCompleted = false;
        final Object[] args = getArguments();
        if(args[0]!=null){
            deployAgent((Environment) args[0],(EntityType)args[1]);
        }else{
            System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
            System.exit(-1);
        }
    }

    public DFAgentDescription[] getAgents() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        //return all agents
        //sd.setType("explorer");
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
        if (mapCompleted) return;
        long currentTime = System.currentTimeMillis();
        List<String> sons = new ArrayList<>();
        nodeData nodeData;
        for(Couple node : lobs){
            if (!myPosition.equals(node.getLeft())) {
                nodeData = new nodeData((List<Attribute>)node.getRight(),new ArrayList<String>(),currentTime);
                map.put((String) node.getLeft(),nodeData);
                sons.add((String) node.getLeft());
            }
        }
        nodeData = new nodeData((List<Attribute>)lobs.get(0).getRight(),sons,currentTime);
        map.put(myPosition,nodeData);
    }

    public String getUnvisitedNode(String myPosition){
        String res = null;
        List<String> sons = map.get(myPosition).getNeighbours();
        for (String s : sons) {
            if (!getMap().keySet().contains(s)) {
                res = s;
                break;
            }
        }
        return res;
    }


    public  Set<String> getUnexploredNodes(){
        if (mapCompleted) return null;
        Set<String> explored = map.keySet();
        Set<String> unexplored = new HashSet<>();
        for (String key : map.keySet()) {
            List<String> tmp = map.get(key).getNeighbours();
            unexplored.addAll(tmp);
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
        return ;
    }

    public void clearMap () { map.clear(); }

    public void fusion(HashMap<String,String[]> map2) {
        if (mapCompleted) return;
        map.putAll(map2);
        computeTankerPos();
    }

    public boolean lastStep(){
        if(steps.size() == 1 && steps.get(0).equals(tankerPos)){
            return true;
        }
        return false;
    }

    public boolean isMapCompleted(){ return mapCompleted; }

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

    public String getTankerPos(){
        return tankerPos;
    }

    public HashMap<String, List<Attribute>> getData() {
        return data;
    }

    public void setTankerPos(String pos){ tankerPos = pos; }

    public void computeTankerPos(){
        if (getUnexploredNodes() == null || getUnexploredNodes().isEmpty()){
            mapCompleted = true;
            if (tankerPos == null){
                for (int i = 5; i > 0; i--){
                    String s = Tools.centralize(map,i,i);
                    if (s != null){
                        setTankerPos(s);
                        break;
                    }
                }
            }
        }
    }
    public Set<String> getAllNodeskey(){
        Set<String> allnodes = new HashSet<String>();
        if(getUnexploredNodes() != null){
            allnodes.addAll(getUnexploredNodes());
        }
        allnodes.addAll(map.keySet());
        return allnodes;
    }
}