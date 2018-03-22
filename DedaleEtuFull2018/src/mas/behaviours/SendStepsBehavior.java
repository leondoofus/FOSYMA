package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.agents.CustomAgent;
import mas.util.MyGraph;

import java.io.IOException;
import java.util.*;

public class SendStepsBehavior extends SimpleBehaviour {

    private CustomAgent customAgent;

    public SendStepsBehavior(final CustomAgent customAgent){
        super(customAgent);
        this.customAgent = customAgent;
    }

    @Override
    public void action() {
        MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = this.customAgent.receive(msgTemplate);
        if (msg != null) {
            System.out.println(this.customAgent.getLocalName() + "<----Position received from " + msg.getSender().getLocalName());
            String receiverPosition = msg.getContent();

            msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(this.customAgent.getAID());
            HashMap<String,String[]> map = this.customAgent.getMap();
            ArrayList<String> explored = new ArrayList<>(map.keySet());
            ArrayList<String> unexplored = new ArrayList<>();
            for (String[] s : map.values())
                unexplored.addAll(Arrays.asList(s));
            HashSet<String> tmp = new LinkedHashSet<>(unexplored);
            unexplored.clear();
            unexplored.addAll(tmp);
            unexplored.removeAll(explored);

            ArrayList<String> steps = new ArrayList<>();
            for (String s :  unexplored){
                steps = MyGraph.dijkstra(map,this.customAgent.getCurrentPosition(),s);
                if (!steps.get(0).equals(this.customAgent.getCurrentPosition())){
                    break;
                }
                else {
                    steps = new ArrayList<>();
                }
            }
            try {
                msg.setContentObject (steps);
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg.addReceiver(this.customAgent.getCommunicatingAgent());
            ((mas.abstractAgent) this.myAgent).sendMessage(msg);
            this.customAgent.setSteps(MyGraph.dijkstraNoeudPlusProche(map,this.customAgent.getCurrentPosition(),unexplored.toArray(new String[unexplored.size()])));
        }
    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("SendStepsBehaviour");
        return true;
    }

    public int onEnd() {
        return 1;
    }
}
