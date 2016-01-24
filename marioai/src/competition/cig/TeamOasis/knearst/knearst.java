package competition.cig.TeamOasis.knearst;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import competition.cig.TeamOasis.instant;
import competition.cig.TeamOasis.util.JsonHelper;
import org.json.JSONException;

public class knearst {
    List<instant> trainingSet = new ArrayList<instant>();
    int k=1; //select the nearest neighbor
    public knearst(){
        //import data from training set file
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("/trainingset.txt"));
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while((ch = dis.read()) != -1){
                if(ch == '\n'){
                    String jsonString = sb.toString();
                    sb.delete(0,sb.length());
                    instant temp = new instant();
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
    //remove the same data
        int u,j;
        for(u=0;u<trainingSet.size()-1;u++) {
            instant temp = trainingSet.get(u);
            for(j=u+1;j<trainingSet.size();j++) {
                instant temp1 = trainingSet.get(j);
                if(temp.isEqual(temp1))
                    trainingSet.remove(j);
            }
        }
        for(u=0;u<trainingSet.size()-1;u++) {
            instant temp = trainingSet.get(u);
            for(j=u+1;j<trainingSet.size();j++) {
                instant temp1 = trainingSet.get(j);
                if(temp.isDiff(temp1)) {
                    if(temp1.getTarget().equals("1")) {
                        trainingSet.remove(u);
                        temp=temp1;
                    }
                    else
                        trainingSet.remove(j);
                }
            }
        }
    }

    public boolean[] getActions(double[] feature){
        int n = trainingSet.size();
        double[] distance=new double[n];
        int[] target=new int[n];
        int i,j,minindex,count=0;
        double sum,min;
        //store the distances between the new data with all training examples
        for(j=0;j<n;j++) {
            sum=0;
            instant temp = trainingSet.get(j);
            double[] data = temp.generateFeature();
            target[j]=Integer.valueOf(temp.getTarget());
            for(i=0;i<16;i++) {
                sum+=Math.pow((data[i]-feature[i]),2);
            }
            sum=Math.sqrt(sum);
            distance[j]=sum;
        }
        //find the minimum of distances
        for(i=0;i<k;i++) {
            min=distance[0];
            minindex=0;
            for(j=0;j<n;j++){
                if(distance[j]<min){
                    min=distance[j];
                    minindex=j;
                }
            }
            if(target[minindex]==1)
                count++;
            distance[minindex]=distance[n-1];
            n--;
        }
        boolean[] action = {false,true,false,false,false};
        action[3] = count>(k/2);
        return action;
    }
}
