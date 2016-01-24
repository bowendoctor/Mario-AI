package competition.cig.TeamOasis.astar;

import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

import java.util.*;

/**
 * Created by xiaoyu on 11/12/15.
 */
public class AStar {
    private LevelScene currentState;

    public LevelScene getCurrentState() {
        return currentState;
    }

    private Queue<Node> open;

    public AStar(){
        init();
    }

    public void init(){


        currentState = new LevelScene();
        currentState.init();
        currentState.level = new Level(1500,15);
    }
    //method to get the action
    public boolean[] search(float startTime){
        // open list for the candidate node to select
        open = new PriorityQueue<Node>();
        boolean[] f = {false,true,false,false,false};
        Node node = new Node(0,null,currentState,f);
        open.add(node);
        //start point ,used to judge whether get the goal
        float start = currentState.mario.x;
        int debug = 0;
        while(!open.isEmpty() && node.worldstate.mario.x - start < 100){
            //not die or block
            node = open.poll();
            node.generateChild();
            for (int i = 0; i < node.children.size(); i++) {
                Node temp = node.children.get(i);
                if(temp.stopMove() || temp.worldstate.mario.damage - temp.parent.worldstate.mario.damage != 0) {
                    continue;

                }
                else{
                    temp.evaluate(start + 100);
                    open.add(temp);
                }
            }
            debug ++;

        }

        List<Node> path = getPath(node);
        //t is action that jump forward t2 is moving forward
        boolean[] t = {false,true,false,true,false};
        boolean[] t2 = {false,true,false,false,false};
        //if not path found keep running
        if(path.size() < 1) return node.worldstate.mario.mayJump() || node.worldstate.mario.jumpTime > 0 ? t : t2;
        return path.get(0).action;
    }
    public List<Node> getPath(Node node){
        //node is the goal node,generate the path by tracking the parent
        List<Node> path = new ArrayList<Node>();
        while(node.parent != null){
            path.add(0,node);
            node = node.parent;
        }
        return path;
    }

    public void advance(boolean[] action){
        currentState.mario.setKeys(action);
        currentState.tick();


    }
    public void updateState(byte[][] levelPart, float[] enemies){
        currentState.setLevelScene(levelPart);
        currentState.setEnemies(enemies);
    }
}
class Node implements Comparable<Node>{
    int depth;
    int passsedTime;// g
    int remainingTime;//h
    LevelScene worldstate;
    Node parent;
    List<Node> children;
    boolean[] action;
    float fitness;
    public Node(int depth,Node parent,LevelScene worldstate,boolean[] action){
        this.depth = depth;
        this.parent = parent;
        this.worldstate = worldstate;
        this.action = action;
        this.passsedTime = parent == null ? 0 : parent.passsedTime + 1;
    }
    @Override
    public int compareTo(Node o) {
        return Float.compare(this.fitness,o.fitness);
    }
    public boolean[] createAction(boolean jump,boolean down,boolean left,boolean right,boolean speed){
        boolean[] action = new boolean[5];
        action[competition.cig.TeamOasis.sprites.Mario.KEY_JUMP] = jump;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_DOWN] = down;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_LEFT] = left;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_RIGHT] = right;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_SPEED] = speed;
        return action;

    }

    public boolean stopMove(){
        //used to judge if mario stop move
        boolean[] t = {false,true,false,false,false};
        boolean[] t2 = {false,true,false,true,false};
        boolean[] t3 = {false,true,false,false,true};
        boolean[] t4 = {false,true,false,true,true};
        //if the current action is moving forward
        if(Arrays.equals(t, worldstate.mario.keys) || Arrays.equals(t3, worldstate.mario.keys)){
            //and the moving distance too short
            if(worldstate.mario.xa < 0.7f){
                //if mario is on the ground it must get stuck,if mario is not on the ground and it is falling then it will get stuck
                if(!worldstate.mario.isOnGround() && worldstate.mario.jumpTime == 0){
                    if((parent != null && Arrays.equals(parent.worldstate.mario.keys,t2)) || (parent != null && Arrays.equals(parent.worldstate.mario.keys,t4))){
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
            //else not possible to get stuck
            return false;
        }

    }
    public void generateChild(){
        children = new ArrayList<Node>();

        //forward
        boolean[] forward = createAction(false, false, false, true, false);
        LevelScene forwardCopy = getStateCopy();
        Node node1 = new Node(this.depth + 1,this,forwardCopy,forward);
        node1.advance();
        children.add(node1);
        //acc forward
        boolean[] forwardSpeed = createAction(false, false, false, true, true);
        LevelScene fsCopy = getStateCopy();
        Node node2 = new Node(this.depth + 1,this,fsCopy,forwardSpeed);
        node2.advance();
        children.add(node2);
        //if mario can jump then
        if(worldstate.mario.mayJump() || worldstate.mario.jumpTime > 0) {
            //can jump higher
            boolean[] jumpForward = createAction(true, false, false, true, false);
            LevelScene jfCopy = getStateCopy();
            Node node3 = new Node(this.depth + 1, this, jfCopy,jumpForward);
            node3.advance();
            children.add(node3);
//            acc jump forward
            boolean[] forwardJumpSpeed = createAction(true, false, false, true, true);
            LevelScene fjsCopy = getStateCopy();
            Node node4 = new Node(this.depth + 1, this, fjsCopy,forwardJumpSpeed);
            node4.advance();
            children.add(node4);

        }


    }
    public void evaluate(float end){
        //heuristic function if mario come across the block and jump then use more time
        if(parent == null) remainingTime = (int)end;
        remainingTime = worldstate.mario.xa < 0.1f ? parent.remainingTime + 5 : (int)(end / worldstate.mario.xa);
        fitness = passsedTime + remainingTime;
    }
    //clone the world state
    public LevelScene getStateCopy(){
        LevelScene copy = null;
        try {
            copy = (LevelScene)worldstate.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }
    public void advance(){
        worldstate.mario.setKeys(action);
        worldstate.tick();
    }
}
