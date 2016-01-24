package competition.cig.TeamOasis.DeltaRule;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;
import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

public class DRAgent implements Agent {
    private String name = "DRAgent";
    private Delta delta;
    private boolean[] action;
    private int jumptimer;
    private LevelScene world;
    @Override
    public void reset() {
        world = new LevelScene();
        world.init();
        world.level = new Level(1500,15);
        delta = new Delta();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        world.mario.setKeys(action);
        world.tick();
        world.setLevelScene(observation.getLevelSceneObservationZ(0));
        world.setEnemies(observation.getEnemiesFloatPos());

        int[] feature = new int[6];
        byte[][] levelScene = observation.getCompleteObservation(/*1, 0*/);
        int count = 0;
        int assign = 0;
        //get eight cells around mario and some extra features like Danger of gap or may Mario jump
        for(int i = 10;i <= 12;i ++){
            for(int j = 10;j <= 12;j ++){
                if(i == 11 && j == 11) continue;
                if(count == 2 || count == 4 || count == 6){
                    feature[assign] = levelScene[i][j] == 0 ? 0 : 1;
                    count ++;
                    assign ++;
                }
                else if(count == 7){
                    feature[assign] = DangerOfGap(levelScene) ? 0 : 1;
                    assign ++;
                }
                else{
                    count ++;
                }
            }
        }
        feature[assign] = (world.mario.mayJump() || world.mario.jumpTime > 0 )? 1 : 0;
        assign ++;
        feature[assign] = (levelScene[11][13] == -10 || levelScene[11][13] == -11 ||levelScene[11][13] == 20||levelScene[11][13] == 16||levelScene[11][13] == 21 || levelScene[11][13] == 0 )? 0 : 1;
        action = delta.getActions(feature);
        //if Mario is stuck before a obstacle and the jump button hasn't been released correctly
        if(action[3] == true)
            jumptimer++;
        else
            jumptimer = 0;
        if(jumptimer == 30){
            action[3] = false;
            jumptimer = 0;
        }
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
    private boolean DangerOfGap(byte[][] levelScene)
    {
        //if there is a gap
        for(int i = 12;i < 22;i ++){
            if(levelScene[i][12] == -10 || levelScene[i][12] == -11 ||levelScene[i][12] == 20||levelScene[i][12] == 16||levelScene[i][12] == 21) return false;
            else if(levelScene[i][12] == 9 || levelScene[i][12] == 10){
                return true;
            }
        }
        return true;
    }
}
