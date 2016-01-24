package competition.cig.TeamOasis.perceptrons;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import competition.cig.TeamOasis.InsTest;
import competition.cig.TeamOasis.util.JsonHelper;
import org.json.JSONException;


public class Perceptron {
    private double[] weight;
    private double bias;
    private double alpha;
    private double theta;

    public Perceptron(){
        weight = new double[6];
        bias = 0;
        alpha = 1;
        theta = 0.2;
        // read data to set up the training set
        List<InsTest> trainingSet = new ArrayList<InsTest>();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("/trainingset2.txt"));
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while((ch = dis.read()) != -1){
                if(ch == '\n'){
                    String jsonString = sb.toString();
                    sb.delete(0,sb.length());
                    InsTest temp = new InsTest();
                    JsonHelper.toJavaBean(temp, jsonString);
                    trainingSet.add(temp);
                }
                else{
                    sb.append((char)ch);
                }
            }
            dis.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        train(trainingSet);
    }

    public void train(List<InsTest> trainingSet){
        int count = 0;// the correct classification
        int j = 0;
        int test = 0;
        //loop until the weight and bias can class all data correctly
        while(count != trainingSet.size()){
            j = j % trainingSet.size();
            InsTest temp = trainingSet.get(j);
            int[] feature = temp.generateFeature();
            int yi = 0;
            //use current weight and bias to compute output
            for(int i = 0;i < weight.length;i ++){
                yi += feature[i] * weight[i];
            }
            yi += bias;
            int yo = 0;
            if(yi <= theta && -theta <= yi){
                yo = 0;
            }
            else if(yi > theta){
                yo = 1;
            }
            else{
                yo = -1;
            }
            //if not correct then update w and b
            if(yo != Integer.valueOf(temp.getTarget())){
                count = 0;
                update(temp);
            }
            else{
                count ++;
            }
            j ++;
            test ++;
        }
    }
    public void update(InsTest temp){
        int[] feature = temp.generateFeature();
        for(int i = 0;i < weight.length;i ++){
            weight[i] = weight[i] + alpha * Integer.valueOf(temp.getTarget()) * feature[i];
        }
        bias += alpha * Integer.valueOf(temp.getTarget());
    }
    public boolean[] getActions(int[] feature){
        int yi = 0;
        for(int i = 0;i < weight.length;i ++){
            yi += feature[i] * weight[i];
        }
        yi += bias;
        int yo = 0;
        if(yi <= theta && -theta <= yi){
            yo = 0;
        }
        else if(yi > theta){
            yo = 1;
        }
        else{
            yo = -1;
        }

        boolean[] action = {false,true,false,false,false};
        action[3] = yo == 1 ? true : false;

        return action;
    }
    public  String[] helper(int[] nums){
        String[] str = new String[nums.length];
        for(int i = 0;i < nums.length;i ++){
            str[i] = String.valueOf(nums[i]);
        }
        return str;
    }
}