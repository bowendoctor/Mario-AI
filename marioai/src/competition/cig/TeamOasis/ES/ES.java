package competition.cig.TeamOasis.ES;

import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

import java.util.Random;
import java.util.*;

public class ES {
    private Queue<Individual> max;
    private Queue<Individual> min;
    private List<Individual> population;
    private LevelScene currentState;
    private final int POPULATION_NUMBER = 10;
    private final int GENERATION = 30;
    public ES(){
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
    public boolean[] search(){
        boolean[] action = new boolean[4];
        Random random = new Random();
        // to pop best
        max = new PriorityQueue<Individual>(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return -(Float.compare(o1.fitness,o2.fitness));
            }
        });
        //to pop worst
        min = new PriorityQueue<Individual>(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Float.compare(o1.fitness,o2.fitness);
            }
        });
//        population = new ArrayList<Individual>();
        //generate population
        for(int i = 0;i < POPULATION_NUMBER;i ++){
            LevelScene copy = getStateCopy();
            Individual individual = new Individual(copy);
            max.add(individual);
            min.add(individual);
//            population.add(individual);
        }
        //
        for(int i = 0;i < GENERATION;i ++){
            for(int j = 0;j < 5;j ++){
                Individual temp = max.poll();
                //mutate
                int mutation = random.nextInt(50);
                Individual offspring = new Individual(temp,mutation);
                max.add(temp);


                //add offspring to population replace worst one
                Individual badOne = min.poll();
                max.remove(badOne);
//                population.remove(badOne);
                min.add(offspring);
                max.add(offspring);


            }
        }
        action = max.peek().actions[0].getAction();
        return action;
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
    final int STEP_NUMBER = 50;
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
    // this constructor is uesd to mutate,second parameter is the origin individual
    public Individual(Individual parent,int mutation){
        this.worldState = parent.oriState;
        this.oriState = parent.oriState;
        this.start = oriState.mario.x;
        this.damge = oriState.mario.damage;
        actions = new Action[STEP_NUMBER];
        //copy action from origin
        for(int i = 0;i < STEP_NUMBER;i ++){
            actions[i] = new Action(parent.actions[i].getNumber());
        }
        //mutate
        actions[mutation].mutate();
        evaluate();
    }
    public void advance(){
        for(int i = 0;i < STEP_NUMBER;i ++){
            if(worldState.mario.status == worldState.mario.STATUS_DEAD) break;
            worldState.mario.setKeys(actions[i].getAction());
            worldState.tick();
        }
    }
    //fitness function
    //dead is min value,else is the distance to the goal point
    public void evaluate(){
        advance();
        if(worldState.mario.status == worldState.mario.STATUS_DEAD){
            fitness = Float.MIN_VALUE;
        }
        else{
            fitness = (worldState.mario.x - start) - 150 * (worldState.mario.damage - this.damge);
        }

    }
}
