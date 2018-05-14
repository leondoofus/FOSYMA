package mas.uselessbehaviours;

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
        ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE),200);
        if (msg != null) {
            String receiverPosition = msg.getContent();

            msg = new ACLMessage(ACLMessage.PROXY);
            msg.setSender(this.customAgent.getAID());
            HashMap<String,String[]> map = this.customAgent.getMapSons();
            //TODO pas sur de la verification
            if (this.customAgent.getMap().get(receiverPosition).getNeighbours().isEmpty() || !Tools.inCommunicationRange(map,customAgent.getCurrentPosition(),receiverPosition)){
                try {
                    msg.setContentObject (new ArrayList<>());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msg.addReceiver(this.customAgent.getCommunicatingAgent());
                ((mas.abstractAgent) this.myAgent).sendMessage(msg);
                return;
            }
            ArrayList<String> steps = Tools.dijkstra(map, customAgent.getCurrentPosition(), receiverPosition,customAgent.getTankerPos()); //step de sender to receiver
            if(steps.size() == 0){
                try {
                    msg.setContentObject (new ArrayList<>());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msg.addReceiver(this.customAgent.getCommunicatingAgent());
                ((mas.abstractAgent) this.myAgent).sendMessage(msg);
                return;
            }
            ArrayList<String> step1 = new ArrayList<>();
            ArrayList<String> step2 = new ArrayList<>();
            for (String s : map.get(customAgent.getCurrentPosition())){
                if (!s.equals(steps.get(0))){
                    step1.add(s);
                    break;
                }
            }
            if (steps.size() == 1){
                String moveId = map.get(receiverPosition)[new Random().nextInt(map.get(receiverPosition).length)];
                while (moveId.equals(customAgent.getCurrentPosition()))
                    moveId = map.get(receiverPosition)[new Random().nextInt(map.get(receiverPosition).length)];
                step2.add(moveId);
            }else {
                String moveId = map.get(receiverPosition)[new Random().nextInt(map.get(receiverPosition).length)];
                while (moveId.equals(steps.get(steps.size() - 2)))
                    moveId = map.get(receiverPosition)[new Random().nextInt(map.get(receiverPosition).length)];
                step2.add(moveId);
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
        customAgent.setPreviousBehaviour("SendStepsBehaviour");
        return true;
    }

    public int onEnd() {
        return 1;
    }
}
