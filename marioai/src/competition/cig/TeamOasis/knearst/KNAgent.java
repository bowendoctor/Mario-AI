package competition.cig.TeamOasis.knearst;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;
import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

public class KNAgent implements Agent {
    private String name = "KNAgent";
    private knearst kn;
    private boolean[] action;
    private int jumptimer;
    private LevelScene world;
    @Override
    public void reset() {
        world = new LevelScene();
        world.init();
        world.level = new Level(1500,15);
        kn = new knearst();
        action = new boolean[5];
    }

    @Override
    public boolean[] getAction(Environment observation) {
        world.mario.setKeys(action);
        world.tick();

        world.setLevelScene(observation.getLevelSceneObservationZ(0));
        world.setEnemies(observation.getEnemiesFloatPos());

        double[] feature = new double[16];
        byte[][] levelScene = observation.getCompleteObservation(/*1, 0*/);
        int count = 0;
        for(int i = 10;i <= 12;i ++){
            //get eight cells around mario
            for(int j = 10;j <= 12;j ++){
                if(i == 11 && j == 11) continue;
                feature[count] = levelScene[i][j] == 0 ? 0 : 1;
                count ++;
            }
        }
        //get some extra features
        feature[count] = (world.mario.mayJump() || world.mario.jumpTime > 0 )? 1 : 0;
        feature[count+1]=enemyFront(levelScene);
        feature[count+2]=enemyBehind(levelScene);
        feature[count+3]=enemyAbove(levelScene);
        feature[count+4]=obstacleDistance(levelScene);
        feature[count+5]=obstacleHeight(levelScene);
        feature[count+6]=gapDistance(levelScene);
        feature[count+7]=sizeOfGap(levelScene);
        action = kn.getActions(feature);
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

    private float enemyFront(byte[][] levelScene) {
        //number of enemies in front of Mario
        float a = 3;
        for(int i = 12; i<15; i++ ){
            int value = levelScene[11][i];
            if(value > 1 && value < 16)
                return a*0.33f;
            a--;
        }
        return 0;
    }

    private float enemyBehind(byte[][] levelScene) {
        //number of enemies in back of Mario
        float a = 3;
        for(int i = 10; i>7; i-- ){
            int value = levelScene[11][i];
            if(value > 1 && value < 16)
                return a*0.33f;
            a--;
        }
        return 0;
    }

    private float enemyAbove(byte[][] levelScene) {
        //number of enemies above Mario
        float a = 3;
        for(int i = 10; i>7; i--){
            int value = levelScene[i][11];
            if(value > 1 && value < 16)
                return a*0.33f;
            a--;
        }
        return 0;
    }

    private float obstacleDistance(byte[][] levelScene) {
        //distance between Mario and a obstalce in front
        float a = 3;
        for(int i = 12; i<15; i++ ){
            int value = levelScene[11][i];
            if(value < 0 || value ==  20)
                return a*0.33f;
            else
                a--;
        }
        return 0;
    }

    private float obstacleHeight(byte[][] levelScene) {
        //the height of obstacle
        if(obstacleDistance(levelScene) != 0) {
            int a =  (int) Math.round(obstacleDistance(levelScene)*3);
            int x = 0;
            if(a == 3){
                x=12;
            }
            if(a == 2){
                x=13;
            }
            if(a == 1){
                x=14;
            }
            float height = 0;
            for(int i = 11; i>8; i-- ){
                int value = levelScene[i][x];
                if(value < 0 || value ==  20)
                    height += 0.33;
            }
            return height;
        }
        else{
            return 0;
        }
    }

    private float gapDistance(byte[][] levelScene) {
        //the distance between a gap and Mario
        float a = 3;
        for(int i = 12; i<=15;i++){
            boolean isGap = true;
            for(int y = 10; y < 22; ++y)
            {
                if(levelScene[y][i] != 0){
                    isGap = false;
                }
            }
            if(isGap){
                return a*0.33f;
            }
            else{
                a--;
            }
        }
        return 0;
    }

    private float sizeOfGap(byte[][] levelScene){
        //the size of gap
        float gapcount = 0;
        for(int j = 12; j<22; j++){
            boolean isGap = true;
            for(int i = 1; i<22; i++ ){
                if(levelScene[i][j] != 0){
                    isGap = false;
                }
            }
            if(isGap) {
                gapcount += 0.33f;
            }

        }
        if(gapcount>0.99)
            gapcount = 0.99f;
        return gapcount;
    }
}
