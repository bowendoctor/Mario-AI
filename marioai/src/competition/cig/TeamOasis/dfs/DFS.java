package competition.cig.TeamOasis.dfs;

import competition.cig.TeamOasis.level.LevelScene;
import competition.cig.TeamOasis.level.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;
public class DFS {
    private LevelScene currentState;

    public LevelScene getCurrentState() {
        return currentState;
    }

    private Stack<Node> open;

    public DFS(){
        init();
    }

    public void init(){


        currentState = new LevelScene();
        currentState.init();
        currentState.level = new Level(1500,15);
    }

    public boolean[] search(){
        open = new Stack<Node>();
        boolean[] f = {false,true,false,false,false};
        Node node = new Node(0,null,currentState,f);
        open.add(node);
        float start = currentState.mario.x;
        while(!open.isEmpty() && node.worldstate.mario.x - start < 170){// reduce the range since too many nodes to store leading to lag
            //not die or block
            node = open.pop();
            node.generateChild();
            for (int i = 0; i < node.children.size(); i++) {
                Node temp = node.children.get(i);
                int initialDamge = temp.worldstate.mario.damage;
                temp.advance();
                //if child node state is dead or get stuck then dont add it to stack
                if(temp.stopMove() || temp.worldstate.mario.damage - initialDamge != 0) {
                    continue;
                }
                else{
                    open.add(temp);
                }
            }
        }

        List<Node> path = getPath(node);
        boolean[] t = {false,true,false,true,false};
        boolean[] t2 = {false,true,false,false,false};
        //if not path found keep running
        if(path.size() < 1) return node.worldstate.mario.mayJump() || node.worldstate.mario.jumpTime > 0 ? t : t2;
        return path.get(0).action;
    }
    public List<Node> getPath(Node node){
        List<Node> path = new ArrayList<Node>();
        while(node.parent != null){
            path.add(0,node);
            node = node.parent;
        }
        return path;
    }
    //method to predict the world state by passing action
    public void advance(boolean[] action){
        currentState.mario.setKeys(action);
        currentState.tick();
    }
    public void updateState(byte[][] levelPart, float[] enemies){
        currentState.setLevelScene(levelPart);
        currentState.setEnemies(enemies);
    }
}
class Node{
    int depth;
    LevelScene worldstate;
    Node parent;
    List<Node> children;
    boolean[] action;
    public Node(int depth,Node parent,LevelScene worldstate,boolean[] action){
        this.depth = depth;
        this.parent = parent;
        this.worldstate = worldstate;
        this.action = action;
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
        //if the current action is moving forward
        //and the moving distance too short
        //if mario is on the ground it must get stuck,if mario is not on the ground and it is falling then it will get stuck
        if(Arrays.equals(t,worldstate.mario.keys)){
            if(worldstate.mario.xa < 0.7f){
                if(!worldstate.mario.isOnGround() && worldstate.mario.jumpTime == 0){
                    if(parent != null && Arrays.equals(parent.worldstate.mario.keys,t2)){
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
    public void generateChild(){
        children = new ArrayList<Node>();
        //forward
        boolean[] forward = createAction(false, false, false, true, false);
        LevelScene forwardCopy = getStateCopy();
        children.add(new Node(this.depth + 1,this,forwardCopy,forward));
        if(worldstate.mario.mayJump() || worldstate.mario.jumpTime > 0) {
            //can jump higher
            boolean[] jumpForward = createAction(true, false, false, true, false);
            LevelScene jfCopy = getStateCopy();
            children.add(new Node(this.depth + 1, this, jfCopy,jumpForward));
        }
    }

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
