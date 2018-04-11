package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.agents.CustomAgent;
import mas.agents.TankerAgent;

public class CheckMailBehavior extends SimpleBehaviour {

    private int nextBehaviourSelect; //1 = request connection , 2 = send map
    private CustomAgent customAgent;
    private int count1,count2;

    public CheckMailBehavior(final CustomAgent customAgent) {
        super(customAgent);
        this.customAgent = customAgent;
        count1 = 0;
        count2 = 0;
    }

    @Override
    public void action() {
        if (customAgent instanceof TankerAgent) System.out.println(this.customAgent.getLocalName() +" Is checking his mailbox");
        final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        final ACLMessage msg = this.customAgent.receive(msgTemplate);
        if (msg != null) {
            if(msg.getPostTimeStamp() - System.currentTimeMillis() < 50){
                customAgent.setCommunicatingAgent(msg.getSender());
                //System.out.println(this.customAgent.getLocalName() + " : --Result received from " + msg.getSender().getLocalName());
                nextBehaviourSelect = 2;
                count1 ++;
            }else{
                //System.out.println(this.customAgent.getLocalName() + " : --Warning message from  "+msg.getSender().getLocalName()+" too old !--");
                nextBehaviourSelect = 1;
                count2 ++;
            }
        }else{
            nextBehaviourSelect = 1; //no message was found
        }
        if (customAgent instanceof TankerAgent)System.out.println("tanker 1 : "+count1+" 2 : "+count2);

    }

    @Override
    public boolean done() {
        customAgent.setPreviousbehaviour("CheckMailBehavior");
        return true;
    }

    public int onEnd() {
        if (customAgent instanceof TankerAgent)System.out.println(nextBehaviourSelect);
        return nextBehaviourSelect;

    }
}
