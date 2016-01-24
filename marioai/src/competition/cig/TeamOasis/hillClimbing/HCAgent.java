package competition.cig.TeamOasis.hillClimbing;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;

public class HCAgent implements Agent {
    private String name = "HCAgent";
    private HC hc;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;

    @Override
    public void reset() {
        hc = new HC();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        hc.advance(action);
        if(hc.getCurrentState().mario.x != observation.getMarioFloatPos()[0] || hc.getCurrentState().mario.y != observation.getMarioFloatPos()[1]){
            hc.getCurrentState().mario.x = observation.getMarioFloatPos()[0];
            hc.getCurrentState().mario.xa = (observation.getMarioFloatPos()[0] - lastX) * 0.89f;
            if(Math.abs(hc.getCurrentState().mario.y - observation.getMarioFloatPos()[1]) > 0.1f){
                hc.getCurrentState().mario.ya = (observation.getMarioFloatPos()[1] - lastY) * 0.85f;
            }
            hc.getCurrentState().mario.y = observation.getMarioFloatPos()[1];
        }
        hc.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());
        lastX = observation.getMarioFloatPos()[0];
        lastY = observation.getMarioFloatPos()[1];
        action = hc.search();
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
