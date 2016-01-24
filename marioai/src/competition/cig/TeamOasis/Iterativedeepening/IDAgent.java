package competition.cig.TeamOasis.Iterativedeepening;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;


public class IDAgent implements Agent{
    private String name = "idagent";
    private ID id;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;
    @Override
    public void reset() {
        id = new ID();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        id.advance(action);
        if(id.getCurrentState().mario.x != observation.getMarioFloatPos()[0] || id.getCurrentState().mario.y != observation.getMarioFloatPos()[1]){
            id.getCurrentState().mario.x = observation.getMarioFloatPos()[0];
            id.getCurrentState().mario.xa = (observation.getMarioFloatPos()[0] - lastX) * 0.89f;
            if(Math.abs(id.getCurrentState().mario.y - observation.getMarioFloatPos()[1]) > 0.1f){
                id.getCurrentState().mario.ya = (observation.getMarioFloatPos()[1] - lastY) * 0.85f;
            }
            id.getCurrentState().mario.y = observation.getMarioFloatPos()[1];
        }
        id.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());

        lastX = observation.getMarioFloatPos()[0];
        lastY = observation.getMarioFloatPos()[1];
        action = id.search();
        return action;
    }

    @Override
    public AGENT_TYPE getType() {
        return AGENT_TYPE.AI;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
