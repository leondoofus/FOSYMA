package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.agents.CustomAgent;
import mas.util.Tools;

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
        MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE);
        ACLMessage msg = this.customAgent.receive(msgTemplate);
        if (msg != null) {

            //System.out.println(this.customAgent.getLocalName() + "<----Position received from " + msg.getSender().getLocalName());
            String receiverPosition = msg.getContent();

            msg = new ACLMessage(ACLMessage.PROPAGATE);
            msg.setSender(this.customAgent.getAID());
            HashMap<String,String[]> map = this.customAgent.getMap();
            /*ArrayList<String> explored = new ArrayList<>(map.keySet());
            ArrayList<String> unexplored = new ArrayList<>();
            for (String[] s : map.values())
                unexplored.addAll(Arrays.asList(s));
            HashSet<String> tmp = new LinkedHashSet<>(unexplored);
            unexplored.clear();
            unexplored.addAll(tmp);
            unexplored.removeAll(explored);

            ArrayList<String> steps = Tools.dijkstraNoeudPlusProche(map,receiverPosition,unexplored.toArray(new String[unexplored.size()]));*/
            ArrayList<String> steps = Tools.dijkstra(map, customAgent.getCurrentPosition(), receiverPosition); //step de sender to receiver
            ArrayList<String> step1 = new ArrayList<>();
            ArrayList<String> step2 = new ArrayList<>();
            for (String s : map.get(customAgent.getCurrentPosition())){
                if (!s.equals(steps.get(0))){
                    step1.add(s);
                    break;
                }
            }
            for (String s : map.get(receiverPosition)){
                if (!s.equals(steps.get(steps.size()-2))){
                    step2.add(s);
                    break;
                }
            }


            try {
                msg.setContentObject (step2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg.addReceiver(this.customAgent.getCommunicatingAgent());
            ((mas.abstractAgent) this.myAgent).sendMessage(msg);
            this.customAgent.setSteps(step1);
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
