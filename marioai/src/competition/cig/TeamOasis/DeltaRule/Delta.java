package competition.cig.TeamOasis.DeltaRule;

import competition.cig.TeamOasis.InsTest;
import competition.cig.TeamOasis.util.JsonHelper;
import org.json.JSONException;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Delta {
    private double alpha;
    private double[] weight;
    private double bias;
    private static final double DIFF = 0.0000001; //tolerance level to determine convergence
    public Delta(){
        init();
    }

    public boolean train(double[] s,double t){
        //training process
        double y = 0;
        for(int i = 0;i < s.length;i ++){
            y += s[i] * weight[i];
        }
        y += bias;
        return update(s,y,t);

    }
    public double test(double[] s){
        double y = 0;
        for(int i = 0;i < s.length;i ++){
            y += s[i] * weight[i];
        }
        y += bias;
        return y > 0 ? 1 : -1;
    };
    public boolean update(double[] s,double y,double t){
        //update the weights and bias
        boolean ans = true;
        double[] de = new double[weight.length];
        for(int i = 0;i < weight.length;i ++){
            de[i] = weight[i] - 2 * alpha * (y - t) * s[i];
        }
        if(2 * alpha * (y - t) > DIFF) ans = false;
        bias  = bias - 2 * alpha * (y - t);
        double sum = 0;
        for(int i = 0;i < weight.length;i ++){
            sum += Math.pow((de[i] - weight[i]),2);
        }
        //sum represents the Euclidean distance between weights and old weights
        sum = Math.sqrt(sum);
        weight = de;
        if(sum > Delta.DIFF) return false;
        return true; //converge
    }
    public boolean[] getActions(int[] feature){
        double[] temp = new double[feature.length];
        for(int i = 0;i < feature.length;i ++){
            temp[i] = (double)feature[i];
        }
        double y = test(temp);
        boolean[] action = {false,true,false,false,false};
        action[3] = y == 1?true:false;
        return action;
    }
    public void init(){
        List<InsTest> trainingSet = new ArrayList<InsTest>();
        try {
            //read data
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
    //initialize alpha, weights and bias
        this.weight = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        this.alpha = 0.13;
        this.bias = 0.0;
        int i = 1;
        int count = 0;
        while(count < 100){
            //train at most 100 times
            i = i % trainingSet.size();
            train(trainingSet.get(i).generateFeature2(),Double.valueOf(trainingSet.get(i).getTarget()));
            i = i + 1;
            count ++;
        }
    }
}

