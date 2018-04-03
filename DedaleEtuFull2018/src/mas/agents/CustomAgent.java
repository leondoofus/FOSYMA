package mas.agents;

import env.Environment;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.abstractAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomAgent extends abstractAgent {

    /**
     *
     */
    private static final long serialVersionUID = -1784844593772918359L;

    private HashMap<String,String[]> map;
    private List<String> iter;
    private AID comunicatingAgent;
    private String Previousbehaviour;
    private ArrayList<String> steps;

    protected void setup(){
        super.setup();
        map = new HashMap<>();
        iter = new ArrayList<>();
        steps = new ArrayList<>();
        final Object[] args = getArguments();
        if(args[0]!=null){
            deployAgent((Environment) args[0]);
        }else{
            System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
            System.exit(-1);
        }
        iter = new ArrayList<>();
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



    public AID getCommunicatingAgent() {
        return comunicatingAgent;
    }

    public void setCommunicatingAgent(AID comunicatingAgent) {
        this.comunicatingAgent = comunicatingAgent;
    }

    protected void takeDown(){ }

    public HashMap<String, String[]> getMap() {
        return map;
    }

    public void addNode(String node,String[] fils){
        map.put(node, fils);
    }

    public String finIter (){
        return iter.remove(iter.size()-1);
    }

    public void pushPosition (String pos){
        iter.add(pos);
    }

    public void fusion(HashMap<String,String[]> map2) {
        map.putAll(map2);
    }

    public String getPreviousbehaviour() {
        return Previousbehaviour;
    }

    public void setPreviousbehaviour(String previousbehaviour) {
        Previousbehaviour = previousbehaviour;
    }

    public void die(){
        System.err.println("Agent-1 "+getAID().getLocalName()+" terminating.");
        takeDown();
        this.doDelete();
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

    public void clearMap () { map.clear(); }
}