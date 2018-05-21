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
import mas.util.NodeData;
import mas.util.Tools;

import java.util.*;

public class CustomAgent extends abstractAgent {

    private static final long serialVersionUID = -1784844593772918359L;

    private HashMap<String,NodeData> map;

    private AID comunicatingAgent;
    private String previousBehaviour;
    private ArrayList<String> steps;
    private String tankerPos = null;

    protected void setup(){
        super.setup();

        map = new HashMap<>();
        steps = new ArrayList<>();
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
    /* might be usefull in the future but not now
    public void updateMap( List<Couple<String, List<Attribute>>> lobs,String myPosition){
        if (mapCompleted) return;
        long currentTime = System.currentTimeMillis();
        List<String> sons = new ArrayList<>();
        NodeData NodeData;
        for(Couple node : lobs){
            if (!myPosition.equals(node.getLeft())) {
                NodeData = new NodeData((List<Attribute>)node.getRight(),new ArrayList<String>(),currentTime);
                map.put((String) node.getLeft(),NodeData);
                sons.add((String) node.getLeft());
            }
        }
        NodeData = new NodeData((List<Attribute>)lobs.get(0).getRight(),sons,currentTime);
        System.out.println(map.size());
        map.put(myPosition,NodeData);
    }
    */

    public void updateMap( List<Couple<String, List<Attribute>>> lobs,String myPosition){
        long currentTime = System.currentTimeMillis();
        List<String> sons = new ArrayList<>();
        NodeData nodeData;
        for(Couple node : lobs){
            if (!myPosition.equals(node.getLeft())) {
                sons.add((String) node.getLeft());
            }
        }
        nodeData = new NodeData((List<Attribute>)lobs.get(0).getRight(),sons,currentTime);
        map.put(myPosition,nodeData);
    }

    public String getUnvisitedNode(String myPosition){
        String res = null;
        List<String> sons = map.get(myPosition).getNeighbours();
        for (String s : sons) {
            if (!map.keySet().contains(s)) {
                res = s;
                break;
            }
        }
        return res;
    }

    public String getRandomNode(){
        ArrayList<String> givenList = new ArrayList<String>(map.keySet());
        Random rand = new Random();
        return givenList.get(rand.nextInt(givenList.size()));
    }



    public String getOldestNode(){
        long oldtime = Long.MAX_VALUE;
        long newtime = 0;
        String res = "";
        for(String str : map.keySet()){
            newtime = map.get(str).getTime();
            if(newtime < oldtime){
                if(tankerPos == null){
                    res = str;
                    oldtime = newtime;
                }else{
                    if( !str.equals(tankerPos)){
                        res = str;
                        oldtime = newtime;
                    }
                }
            }
        }
        return res;
    }

    public  Set<String> getUnexploredNodes(){
        Set<String> unexplored = new HashSet<>();
        for (String key : map.keySet()) {
            List<String> tmp = map.get(key).getNeighbours();
            for (String s : tmp){
                if(!map.keySet().contains(s)){
                    if(tankerPos == null){
                        unexplored.add(s);
                    }else{
                        if(!s.equals(tankerPos)){
                            unexplored.add(s);
                        }
                    }
                }
            }
        }
        return unexplored;
    }

    public HashMap<String,List<Attribute>> getNodesAttributes(Set<String> nodes){
        HashMap<String,List<Attribute>> res = new  HashMap<>();
        for(String node : nodes ){
            if(map.containsKey(node)){
                res.put(node,map.get(node).getAttrs());
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

    public HashMap<String, String[]> getMapSons() {
        HashMap<String,String[]> res = new  HashMap<>();
        for(String node : map.keySet() ){
            List<String> tmp = map.get(node).getNeighbours();
            String[] tmp2 = tmp.toArray(new String [0]);
            res.put(node,map.get(node).getNeighbours().toArray(new String [0]));
        }
        return res;
    }

    public  HashMap<String,NodeData> getMap(){
        return this.map;
    }

    public void fusion(HashMap<String,NodeData> map2) {
        for(String node : map2.keySet()){
            if(this.map.containsKey(node)){
                if(map.get(node).getTime() < map2.get(node).getTime()){
                    map.put(node, map2.get(node));
                }
            }else{
                map.put(node, map2.get(node));
            }
        }
        map.putAll(map2);
        computeTankerPos();
    }

    public boolean lastStep(){
        if(steps.size() == 1 && steps.get(0).equals(tankerPos)){
            return true;
        }
        return false;
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

    public boolean setpsIsEmpty(){
        return steps.isEmpty();
    }

    public String getTankerPos(){
        return tankerPos;
    }

    public HashMap<String, List<Attribute>> getData() {
        return this.getNodesAttributes(map.keySet());
    }

    public void setTankerPos(String pos){ tankerPos = pos; }

    public void computeTankerPos(){
        if (getUnexploredNodes().isEmpty()){
            if (tankerPos == null){
                String s = Tools.centralize(this.getMapSons());
                if (s != null)
                    setTankerPos(s);
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