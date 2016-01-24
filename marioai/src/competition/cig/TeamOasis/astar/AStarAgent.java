package competition.cig.TeamOasis.astar;
import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;


public class AStarAgent implements Agent{
    private String name = "aStarAgent";
    private AStar aStar;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;
    @Override
    public void reset() {
        aStar = new AStar();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        aStar.advance(action);
        if(aStar.getCurrentState().mario.x != observation.getMarioFloatPos()[0] || aStar.getCurrentState().mario.y != observation.getMarioFloatPos()[1]){
            aStar.getCurrentState().mario.x = observation.getMarioFloatPos()[0];
            aStar.getCurrentState().mario.xa = (observation.getMarioFloatPos()[0] - lastX) * 0.89f;
            if(Math.abs(aStar.getCurrentState().mario.y - observation.getMarioFloatPos()[1]) > 0.1f){
                aStar.getCurrentState().mario.ya = (observation.getMarioFloatPos()[1] - lastY) * 0.85f;
            }
            aStar.getCurrentState().mario.y = observation.getMarioFloatPos()[1];
        }
        aStar.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());

        lastX = observation.getMarioFloatPos()[0];
        lastY = observation.getMarioFloatPos()[1];
        action = aStar.search(System.currentTimeMillis());
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
