package competition.cig.TeamOasis.ga;


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
    public void mutate(){
        number =(number  + 1)%4;
        setAction();
    }

}