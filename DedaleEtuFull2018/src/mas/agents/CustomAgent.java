package mas.agents;

import env.Environment;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import mas.abstractAgent;
import scala.util.parsing.combinator.testing.Str;

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
    private AID comuicatingAgent;
    private String Previousbehaviour;

    protected void setup(){
        super.setup();
        map = new HashMap<>();
        iter = new ArrayList<>();
        //get the parameters given into the object[]. In the current case, the environment where the agent will evolve
        final Object[] args = getArguments();
        if(args[0]!=null){

            deployAgent((Environment) args[0]);

        }else{
            System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
            System.exit(-1);
        }
        iter = new ArrayList<>();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("explorer");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }

        System.out.println("the agent "+this.getLocalName()+ " is started");
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



    public AID getComuicatingAgent() {
        return comuicatingAgent;
    }

    public void setComuicatingAgent(AID comuicatingAgent) {
        this.comuicatingAgent = comuicatingAgent;
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
}