package competition.cig.TeamOasis.hillClimbing;

import competition.cig.TeamOasis.level.Level;
import competition.cig.TeamOasis.level.LevelScene;

import java.util.*;

public class HC {
    private LevelScene currentState;
    public LevelScene getCurrentState() {
        return currentState;
    }
    public HC(){
        init();
    }
    public void init(){
        currentState = new LevelScene();
        currentState.init();
        currentState.level = new Level(1500,15);
    }
    public boolean[] search(){
        boolean[] f = {false,true,false,true,false};
        boolean[] spe = {false,true,false,false,false};
        Node node = new Node(0,null,currentState,f,0);
        node.generateChild();
        if(node.worldstate.mario.xa<0.7f&&node.worldstate.mario.keys[competition.cig.TeamOasis.sprites.Mario.KEY_JUMP]==true) {
            //when Mario is stuck before a obstacle
            if(node.worldstate.mario.ya==0)
                return spe;//when Mario is stuck and jump button is not released
           else
                return f;
        }
        Node temp;
        for (int i = 0; i < node.children.size(); i++) {
            temp = node.children.get(i);
            int initialDamge = temp.worldstate.mario.damage;
            temp.advance();
            if(temp.worldstate.mario.damage - initialDamge != 0)
                temp.value -= 100;//Mario gets hurt
            if(temp.stopMove() || temp.worldstate.mario.status == temp.worldstate.mario.STATUS_DEAD)
                node.children.remove(i);//Mario is dead or stuck
            if (temp.worldstate.level.isGap[(int) (temp.worldstate.mario.x/16)+1])
                return f;//there is a gap in front
        }
        if(node.children.size()==0)
            return f;
        int max=node.children.get(0).value,maxindex=0;
        for (int i = 0; i < node.children.size(); i++) {
            //find the maximum value
            temp = node.children.get(i);
            if(temp.value>max) {
                max=temp.value;
                maxindex=i;
            }
        }
        return node.children.get(maxindex).action;
    }
    public List<Node> getPath(Node node){
        //get the path from root to this node
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
class Node{
    int depth;
    int value;
    LevelScene worldstate;
    Node parent;
    List<Node> children;
    boolean[] action;
    public Node(int depth,Node parent,LevelScene worldstate,boolean[] action,int value){
        this.depth = depth;
        this.parent = parent;
        this.worldstate = worldstate;
        this.action = action;
        this.value=value;
    }

    public boolean[] createAction(boolean left, boolean right, boolean down, boolean jump, boolean speed){
        boolean[] action = new boolean[5];
        action[competition.cig.TeamOasis.sprites.Mario.KEY_JUMP] = jump;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_DOWN] = down;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_LEFT] = left;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_RIGHT] = right;
        action[competition.cig.TeamOasis.sprites.Mario.KEY_SPEED] = speed;
        return action;

    }
    public boolean canJumpHigher(Node currentPos, boolean checkParent)
    {
        if (currentPos.parent != null && checkParent
                && canJumpHigher(currentPos.parent, false))
            return true;
        return currentPos.worldstate.mario.mayJump() || (currentPos.worldstate.mario.jumpTime > 0);
    }
    private ArrayList<boolean[]> createPossibleActions(Node currentPos)
    {
        ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
        // jump
        if (canJumpHigher(currentPos, true)) possibleActions.add(createAction(false, false, false, true, false));
        if (canJumpHigher(currentPos, true)) possibleActions.add(createAction(false, false, false, true, true));
        // run right
        possibleActions.add(createAction(false, true, false, false, true));
        if (canJumpHigher(currentPos, true))  possibleActions.add(createAction(false, true, false, true, true));
        possibleActions.add(createAction(false, true, false, false, false));
        if (canJumpHigher(currentPos, true))  possibleActions.add(createAction(false, true, false, true, false));
        // run left
        possibleActions.add(createAction(true, false, false, false, false));
        if (canJumpHigher(currentPos, true))  possibleActions.add(createAction(true, false, false, true, false));
        possibleActions.add(createAction(true, false, false, false, true));
        if (canJumpHigher(currentPos, true))  possibleActions.add(createAction(true, false, false, true, true));
        return possibleActions;
    }
    public boolean stopMove(){
        boolean[] t = {false,true,false,false,false};
        boolean[] t2 = {false,true,false,true,false};
        boolean[] t3={false,true,false,false,true};
        boolean[] t4={false,true,false,true,true};
        if(Arrays.equals(t, worldstate.mario.keys)||Arrays.equals(t2, worldstate.mario.keys)||Arrays.equals(t3, worldstate.mario.keys)||Arrays.equals(t4, worldstate.mario.keys)){
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
    public void generateChild()
    {
        children = new ArrayList<Node>();
        ArrayList<boolean[]> possibleActions = createPossibleActions(this);
        boolean[] t1 = createAction(false,true,false,true,true);
        boolean[] t2 = createAction(false,true,false,true,false);
        boolean[] t3 = createAction(false,true,false,false,true);
        boolean[] t4 = createAction(false,true,false,false,false);
        boolean[] t5 = createAction(false,false,false,true,false);
        boolean[] t6 = createAction(true,false,false,false,false);
        boolean[] t7 = createAction(true,false,false,false,true);
        boolean[] t8 = createAction(true,false,false,true,false);
        boolean[] t9 = createAction(true,false,false,true,true);
        int value=0;
        //generate children by assigning values based on their actions
        for (boolean[] action: possibleActions)
        {
            LevelScene jfCopy = getStateCopy();
            if (Arrays.equals(action,t1))
                value = this.value + 7;
            else if(Arrays.equals(action,t2))
                value=this.value+6;
            else if(Arrays.equals(action,t3))
                value=this.value+9;
            else if(Arrays.equals(action,t4))
                value=this.value+8;
            else if(Arrays.equals(action,t5))
                value=this.value+5;
            else if(Arrays.equals(action,t6))
                value=this.value+4;
            else if(Arrays.equals(action,t7))
                value=this.value+3;
            else if(Arrays.equals(action,t8))
                value=this.value+2;
            else if(Arrays.equals(action,t9))
                value=this.value+1;
            children.add(new Node(this.depth + 1, this,jfCopy ,action,value));
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
