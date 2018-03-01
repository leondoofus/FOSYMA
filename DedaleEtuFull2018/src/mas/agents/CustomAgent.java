package mas.agents;

import env.Environment;
import jade.core.AID;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
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
    public int cpt;


    protected void setup(){
        super.setup();
        map = new HashMap<>();
        iter = new ArrayList<>();
        cpt = 0;
        //get the parameters given into the object[]. In the current case, the environment where the agent will evolve
        final Object[] args = getArguments();
        if(args[0]!=null){

            deployAgent((Environment) args[0]);

        }else{
            System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
            System.exit(-1);
        }
        iter = new ArrayList<>();
        System.out.println("the agent "+this.getLocalName()+ " is started");
    }

    /**
     * This method is automatically called after doDelete()
     */
    protected void takeDown(){

    }

    public AMSAgentDescription[] getAgents() {
        AMSAgentDescription[] agents = null;
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults ( new Long(-1) );
            agents = AMSService.search( this, new AMSAgentDescription (), c );
        }catch(Exception e) {
            System.out.println( "Problem searching AMS: " + e );
            e.printStackTrace();

        }
        return agents;
    }




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

    public void increment(){
        cpt++;
    }

    public void fusion(HashMap<String,String[]> map2) {
        map.putAll(map2);
    }

}