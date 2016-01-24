package competition.cig.TeamOasis.simulatedAnnealing;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;

public class SAAgent implements Agent{
    private String name = "SAAgent";
    private SA sa;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;

    @Override
    public void reset() {
        sa = new SA();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        sa.advance(action);
        if(sa.getCurrentState().mario.x != observation.getMarioFloatPos()[0] || sa.getCurrentState().mario.y != observation.getMarioFloatPos()[1]){
            sa.getCurrentState().mario.x = observation.getMarioFloatPos()[0];
            sa.getCurrentState().mario.xa = (observation.getMarioFloatPos()[0] - lastX) * 0.89f;
            if(Math.abs(sa.getCurrentState().mario.y - observation.getMarioFloatPos()[1]) > 0.1f){
                sa.getCurrentState().mario.ya = (observation.getMarioFloatPos()[1] - lastY) * 0.85f;
            }
            sa.getCurrentState().mario.y = observation.getMarioFloatPos()[1];
        }
        sa.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());
        lastX = observation.getMarioFloatPos()[0];
        lastY = observation.getMarioFloatPos()[1];
        action = sa.search();
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
