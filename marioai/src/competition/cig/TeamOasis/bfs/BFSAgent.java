package competition.cig.TeamOasis.bfs;
import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;


public class BFSAgent implements Agent{
    private String name = "bfsagent";
    private BFS bfs;
    private boolean[] action;
    private float lastX = 0;
    private float lastY = 0;
    @Override
    public void reset() {
        bfs = new BFS();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        bfs.advance(action);
        if(bfs.getCurrentState().mario.x != observation.getMarioFloatPos()[0] || bfs.getCurrentState().mario.y != observation.getMarioFloatPos()[1]){
            bfs.getCurrentState().mario.x = observation.getMarioFloatPos()[0];
            bfs.getCurrentState().mario.xa = (observation.getMarioFloatPos()[0] - lastX) * 0.89f;
            if(Math.abs(bfs.getCurrentState().mario.y - observation.getMarioFloatPos()[1]) > 0.1f){
                bfs.getCurrentState().mario.ya = (observation.getMarioFloatPos()[1] - lastY) * 0.85f;
            }
            bfs.getCurrentState().mario.y = observation.getMarioFloatPos()[1];
        }
        bfs.updateState(observation.getLevelSceneObservationZ(0),observation.getEnemiesFloatPos());

        lastX = observation.getMarioFloatPos()[0];
        lastY = observation.getMarioFloatPos()[1];
        action = bfs.search();
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
