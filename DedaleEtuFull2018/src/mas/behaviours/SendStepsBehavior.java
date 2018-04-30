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
        System.err.println(customAgent.getName() + "deb send steps");
        ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE),500);
        //MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE);
        //ACLMessage msg = this.customAgent.receive(msgTemplate);
        if (msg != null) {
            //System.out.println(this.customAgent.getLocalName() + "<----Position received from " + msg.getSender().getLocalName());
            String receiverPosition = msg.getContent();

            msg = new ACLMessage(ACLMessage.PROPAGATE);
            msg.setSender(this.customAgent.getAID());
            HashMap<String,String[]> map = this.customAgent.getMap();
            System.err.println(customAgent.getName() + "iciiiiiiiiiiiii 44444444444444444");
            /*ArrayList<String> explored = new ArrayList<>(map.keySet());
            ArrayList<String> unexplored = new ArrayList<>();
            for (String[] s : map.values())
                unexplored.addAll(Arrays.asList(s));
            HashSet<String> tmp = new LinkedHashSet<>(unexplored);
            unexplored.clear();
            unexplored.addAll(tmp);
            unexplored.removeAll(explored);

            ArrayList<String> steps = Tools.dijkstraNoeudPlusProche(map,receiverPosition,unexplored.toArray(new String[unexplored.size()]));*/
            System.out.println(customAgent.getCurrentPosition());
            System.out.println(receiverPosition);
            ArrayList<String> steps = Tools.dijkstra(map, customAgent.getCurrentPosition(), receiverPosition); //step de sender to receiver
            System.out.println(steps);
            System.err.println("iciiiiiiiiiiii 9999999999999");
            ArrayList<String> step1 = new ArrayList<>();
            ArrayList<String> step2 = new ArrayList<>();
            System.err.println("iciiiiiiiiiiiii 555555555555555");
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
            System.err.println("iciiiiiiiiiiiii 2222222222");

            try {
                msg.setContentObject (step2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg.addReceiver(this.customAgent.getCommunicatingAgent());
            ((mas.abstractAgent) this.myAgent).sendMessage(msg);
            this.customAgent.setSteps(step1);
            System.err.println("iciiiiiiiiiiiii 3333333333");
        }
        System.err.println(customAgent.getName() + "fin send steps");
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
