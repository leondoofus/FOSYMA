package mas.dummyagents;

import jade.core.ContainerID;
import mas.abstractAgent;
import mas.agents.interactions.protocols.deployMe.R1_deployMe;
import mas.agents.interactions.protocols.deployMe.R1_managerAnswer;
import mas.uselessbehaviours.MigrateBehaviour;

public class DummyMovingAgent extends abstractAgent {
    private static final long serialVersionUID = -5686331366676803589L;
    private String gateKeeperName="GK";

    protected void setup(){//Automatically called at agent’s creation
        super.setup();
        addBehaviour(new R1_deployMe(gateKeeperName,this));
        addBehaviour(new R1_managerAnswer(gateKeeperName,this));
        ContainerID cID= new ContainerID();
        cID.setName("MyDistantContainer0");
        cID.setPort("8888");
        cID.setAddress("132.227.113.205"); //IP of the host of the targeted container
        doMove(cID);// last method to call in the behaviour
        addBehaviour(new MigrateBehaviour(this));
    }
    protected void beforeMove(){//Automatically called before doMove()
        super.beforeMove();
        System.out.println("I migrate");
    }
    protected void afterMove(){//Automatically called after doMove()
        super.afterMove();
        addBehaviour(new R1_deployMe(gateKeeperName,this));
        System.out.println("I migrated");
    }
}