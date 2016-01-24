package competition.cig.TeamOasis.decisiontree;
import competition.cig.TeamOasis.InsTest;

import java.util.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import competition.cig.TeamOasis.util.JsonHelper;
import org.json.JSONException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.io.IOException;

public class DecisionTree {
    private Node root;
    //	List<Instance> traningSet;
    class Node{
        int featureNum;
        Node left;// sub examples whose featureNum value equals to 0
        Node right;//sub examples whose featureNum value doesnt equals to 0
        String target;//only leaf have
        public Node(int num){
            this.featureNum = num;
        }
    }

    public DecisionTree(){
        int[] attributes = new int[6];//feature
        List<InsTest> trainingSet = new ArrayList<InsTest>();
        // read data to set up training set
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
//            JsonHelper.toJavaBean(sketch, jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        root = constructTree(trainingSet,attributes);
    }
    public Node constructTree(List<InsTest> trainingSet,int[] attributes){
        if(helper(trainingSet)){
            //if all example in set have same target then stop and return leaf node
            Node leaf = new Node(-1);
            leaf.target = trainingSet.get(0).getTarget();
            return leaf;
        }
        else{
            //select the most important feature by ID3
            int attribute = importance(trainingSet,attributes);
            //take it away
            attributes[attribute] = 1;
            Node root = new Node(attribute);
            List<InsTest> left = new ArrayList<>();// = 0
            List<InsTest> right = new ArrayList<>();// = 1
            for(int i = 0;i < trainingSet.size();i ++){
                //feature value equals 0 is left tree,1 is right tree
                if(trainingSet.get(i).Attribute(attribute).equals("0")){
                    left.add(trainingSet.get(i));
                }
                else{
                    right.add(trainingSet.get(i));
                }
            }
            root.left = constructTree(left,attributes);
            root.right = constructTree(right,attributes);
            return root;
        }
    }
    public boolean helper(List<InsTest> examples){
        //helper method judge if all example from set  have same target
        InsTest instance = examples.get(0);
        String str = instance.getTarget();
        for(int i = 1;i < examples.size();i ++){
            if(!examples.get(i).getTarget().equals(str)) return false;
        }
        return true;
    }

    public int importance(List<InsTest> examples,int[] attributes){
        //ID3 algorithm to evaluate the importance of feature
        int importance = 0;
        double max = Double.MIN_VALUE;
        for(int i = 0;i < attributes.length;i ++){
            if(attributes[i] != 1){
                //traversal the examples class by attributes i
                List<InsTest> n = new ArrayList<>();
                List<InsTest> p = new ArrayList<>();
                for(int j = 0;j < examples.size();j ++){
                    if(examples.get(j).Attribute(i).equals("1")){
                        p.add(examples.get(j));
                    }
                    else{
                        n.add(examples.get(j));
                    }

                }
                if(p.size() == 0 || n.size() == 0) continue;
                double entropyN = entropy(n);
                double entropyP = entropy(p);
                double entropyST = ((double)n.size() / (double)examples.size()) * entropyN + ((double)p.size() / (double)examples.size()) * entropyP;
                double entropyS = entropy(examples);
                double ig = entropyS - entropyST;
                if(Double.compare(ig, max) > 0){
                    importance = i;
                    max = ig;
                }
            }
        }
        return importance;

    }
    public double entropy(List<InsTest> examples){
        int a = 0;// target == 1
        int b = 0;// target == -1
        for(int i = 0;i < examples.size();i ++){
            if(Integer.valueOf(examples.get(i).getTarget()) == 1){
                a ++;
            }
            else{
                b ++;
            }
        }
        double pa = (double) a / (double) (examples.size());

        double pb = (double) b / (double) (examples.size());
        double entropy = -a * Math.log(pa) - b * Math.log(pb);
        return entropy;
    }
    public boolean[] search(int[] feature){
        String str = rec(root,feature);
        boolean[] action = {false,true,false,false,false};
        action[3] = str.equals("1") ? true : false;
        return action;
    }
    //dfs travers the decision tree to get target
    public String rec(Node node,int[] feature){
        if(node.target != null) return node.target;
        else{
            if(feature[node.featureNum] == 0){
                return rec(node.left,feature);
            }
            else{
                return rec(node.right,feature);
            }
        }
    }


}