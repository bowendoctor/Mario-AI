package competition.cig.TeamOasis.ES;


public class Action {
    private boolean[] action;
    private int number;
    public int  getNumber(){
        return this.number;
    }
    public Action(int actionNum){
        this.number = actionNum;
        setAction();
    }
    public boolean[] getAction(){
        return action;
    }
    //number the action from 0 to 1
    public void setAction(){
        switch(this.number){
            case 0:
                action = new boolean[]{false, true, false, false, false};
                break;
            case 1:
                action = new boolean[]{false, true, false, false, true};
                break;
            case 2:
                action = new boolean[]{false, true, false, true, false};
                break;
            case 3:
                action = new boolean[]{false, true, false, true, true};
                break;
            default:
                break;

        }
    }
    //by adding one on the action number to mutate,module by 4 to make sure less than 4
    public void mutate(){
        number =(number  + 1)%4;
        setAction();
    }
}
