package competition.cig.TeamOasis.ga;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;


public class GaAgent implements Agent{
    private String name = "gaAgent";
    private Ga ga;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;
    @Override
    public void reset() {
        ga = new Ga();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        ga.advance(action);

        ga.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());
        action = ga.search(System.currentTimeMillis());
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
