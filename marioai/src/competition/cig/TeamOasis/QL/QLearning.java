package competition.cig.TeamOasis.QL;

import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

import java.util.Arrays;
import java.util.Random;


public class QLearning {
    private final int STEP = 511;
    private double[][] Q;
    private int[][] R;
    private final double rate = 0.8;
    private LevelScene currentState;
    public QLearning(){
        this.Q = new double[STEP][STEP];
        this.R = new int[STEP][STEP];
        //each state only have two sub state so dont need to set 0 and -1
        for(int i = 1;i < 3;i ++){
            for(int j = (STEP - 1) / 2;j < (STEP - 3)/2 + 1;j ++){
                //set reward,state can reach goal is 100
                R[j][2 * j + i] = 100;
            }
        }
        currentState = new LevelScene();
        currentState.init();
        currentState.level = new Level(1500,15);

    }
    public void train(){
        //to optimize matrix Q and R
        //starting from 0
        //randomly chose one action ,0 or 1 left or right
        for(int i = 0;i < 300;i ++) {
            Random random = new Random();
            LevelScene copy = getStateCopy(currentState);
            boolean[] action = {false, true, false, false, false};
            boolean move = true;
            int state = 0;// starting from state 0e
            while (move && andHelper(state)) {
                int rand;
                //if mario can jump then random
                if(copy.mario.mayJump() || copy.mario.jumpTime > 0){
                    rand = random.nextInt(2);
                }
                //else dont action to jump
                else{
                    rand = 0;
                    Q[state][2 * state + 2] = -100;

                }
                action[3] = (rand == 1) ? true : false;
                int a = (rand == 1) ? 2 * state + 2 : 2 * state + 1;
                int stateN = a;
                int damage = copy.mario.damage;
                LevelScene parent = getStateCopy(copy);
                copy.mario.setKeys(action);
                copy.tick();
                //if dead or get stuck then penalize
                if (copy.mario.damage - damage > 0 || stopMove(copy,parent)) {
                    Q[state][a] = -100;
                    move = false;
                }
                //else update q matrix 
                else {
                    Q[state][a] = (orHeapler(stateN)) ? (double) R[state][a] + rate * Q[stateN][stateN] : (double) R[state][a] + rate * (Math.max(Q[stateN][2 * stateN + 1], Q[stateN][2 * stateN + 2]));
                    state = stateN;
                }
            }
        }
    }
    public boolean andHelper(int state){
        for(int i =(STEP - 1) / 2;i < STEP;i ++){
            if(state == i) return false;
        }
        return true;
    }
    public boolean orHeapler(int state){
        for(int i =(STEP - 1) / 2;i < STEP;i ++){
            if(state == i) return true;
        }
        return false;
    }
    public boolean stopMove(LevelScene copy, LevelScene parent){
        boolean[] t = {false,true,false,false,false};
        boolean[] t2 = {false,true,false,true,false};
        if(Arrays.equals(t, copy.mario.keys)){
            if(copy.mario.xa < 0.7f){
                if(!copy.mario.isOnGround() && copy.mario.jumpTime == 0){
                    if(Arrays.equals(parent.mario.keys,t2)){
                        return true;
                    }
                    else{
                        return false;
                    }

                }
                else{
                    return true;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }

    }
    public boolean[] getActiont(){
        boolean[] action = {false,true,false,false,false};
        train();
        int state = 0;
        action[3] = Double.compare(Q[state][2 * state + 1],Q[state][2 * state + 2]) >= 0 ? false : true;

        return action;

    }
    public void advance(boolean[] action){
        currentState.mario.setKeys(action);
        currentState.tick();
    }
    public void updateState(byte[][] levelPart, float[] enemies){
        currentState.setLevelScene(levelPart);
        currentState.setEnemies(enemies);
    }
    public LevelScene getStateCopy(LevelScene levelScene){
        LevelScene copy = null;
        try {
            copy = (LevelScene)levelScene.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }
}
