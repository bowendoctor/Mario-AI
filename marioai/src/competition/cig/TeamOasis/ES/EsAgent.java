package competition.cig.TeamOasis.ES;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;


public class EsAgent implements Agent{
    private String name = "esAgent";
    private ES es;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;
    @Override
    public void reset() {
        es = new ES();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        es.advance(action);

        es.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());
        action = es.search();
        return action;
    }

    @Override
    public AGENT_TYPE getType() {
        return AGENT_TYPE.AI;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
