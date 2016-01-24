package competition.cig.TeamOasis.ga;


import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

import java.util.*;


public class Ga {
    private Queue<Individual> max;
    private Queue<Individual> min;
    private List<Individual> population;
    private LevelScene currentState;
    private final int POPULATION_NUMBER = 10;
    private final int GENERATION = 10;
    public Ga(){
        init();

    }
    public void init(){
        currentState = new LevelScene();
        currentState.init();
        currentState.level = new Level(1500,15);
    }
    public void advance(boolean[] action){
        currentState.mario.setKeys(action);
        currentState.tick();


    }
    public void updateState(byte[][] levelPart, float[] enemies){
        currentState.setLevelScene(levelPart);
        currentState.setEnemies(enemies);
    }
    public boolean[] search(float startTime){
        boolean[] action = new boolean[4];
        Random random = new Random();
        population = new ArrayList<Individual>();
        //generate population
        for(int i = 0;i < POPULATION_NUMBER;i ++){
            LevelScene copy = getStateCopy();
            Individual individual = new Individual(copy);
            population.add(individual);
        }
        for(int i = 0;i < GENERATION;i ++){
            List<Individual> newPopulation = new ArrayList<Individual>();
            for(int j = 0;j < 10;j ++){
                //select parent
                Individual x = population.get(random.nextInt(10));
                Individual y = population.get(random.nextInt(10));
                Individual child = Individual.reproduce(x, y);
                //possibility to mutate
                if(random.nextInt(10) < 3) child = new Individual(child,random.nextInt(20));
                newPopulation.add(child);

            }
            population = newPopulation;
        }

        return getAction(population);
    }
    public boolean[] getAction(List<Individual> population){
        float max = Float.MIN_VALUE;
        Individual ans = null;
        // find the max fitness individual in population
        for(int i = 0;i < population.size();i ++){
            if(Float.compare(max,population.get(i).fitness) <= 0){
                max = population.get(i).fitness;
                ans = population.get(i);
            }
        }
        return ans.actions[0].getAction();
    }
    public LevelScene getStateCopy(){
        LevelScene copy = null;
        try {
            copy = (LevelScene)currentState.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }

}
class Individual{
    LevelScene worldState;
    LevelScene oriState;
    float fitness;
    Action[] actions;
    float start;
    int damge;
    final int STEP_NUMBER = 20;
    //recombine the two parent by randomly select length of x and length of y
    public static Individual reproduce(Individual x,Individual y){
        Random random = new Random();
        int n = x.actions.length;
        int c = random.nextInt(n);
        Individual individual = new Individual(x,y,c);
        return individual;
    }
    //recombine the parent and  mutate the child
    public Individual(Individual x,Individual y,int c){
        this.worldState = x.oriState;
        this.oriState = x.oriState;
        this.start = oriState.mario.x;
        this.damge = oriState.mario.damage;
        actions = new Action[STEP_NUMBER];
        for(int i = 0;i < actions.length;i ++){
            if(i < c){
                actions[i] = x.actions[i];
            }
            else{
                actions[i] = y.actions[i];
            }
        }
        evaluate();
    }
    //mutate from the second parameter
    public Individual(Individual parent,int mutation){
        this.worldState = parent.oriState;
        this.oriState = parent.oriState;
        this.start = oriState.mario.x;
        this.damge = oriState.mario.damage;
        actions = new Action[STEP_NUMBER];
        for(int i = 0;i < STEP_NUMBER;i ++){
            actions[i] = new Action(parent.actions[i].getNumber());
        }
        actions[mutation].mutate();
        evaluate();
    }
    //this is used to generate population so the action squence is random
    public Individual(LevelScene worldState){
        this.worldState = worldState;
        this.oriState = worldState;
        this.start = worldState.mario.x;
        this.damge = worldState.mario.damage;
        actions = new Action[STEP_NUMBER];
        Random random = new Random();
        for(int i = 0;i < STEP_NUMBER;i ++){
            actions[i] = new Action(random.nextInt(4));
        }
        evaluate();
    }

    public void advance(){
        for(int i = 0;i < STEP_NUMBER;i ++){
            if(worldState.mario.status == worldState.mario.STATUS_DEAD) break;
            worldState.mario.setKeys(actions[i].getAction());
            worldState.tick();
        }
    }
    public void evaluate(){
        advance();
        if(worldState.mario.status == worldState.mario.STATUS_DEAD){
            fitness = Float.MIN_VALUE;
        }
        else{
            fitness = (worldState.mario.x - start) - 100 * (worldState.mario.damage - this.damge);
        }

    }
}

