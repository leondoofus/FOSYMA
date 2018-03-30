package mas.util;

import java.util.ArrayList;
import java.util.HashMap;


public class ShortestPath {

    public static ArrayList<String> solve(String dst, String src, HashMap<String,String[]> map,ArrayList<String> explored){
        //System.out.println(dst+" "+src);
        if(dst.equals(src)){
            //System.out.println("go to spep");
            ArrayList<String> res = new ArrayList<>();
            res.add(src);
            return res;
        }else{
            String[] tmp = map.get(src);
            explored.add(src);
            for(String s : tmp){
                //System.out.println("- son" + s);
                if(!explored.contains(s)){
                    ArrayList<String> res =solve(dst,s,map,explored);
                    res.add(s);
                    return res;
                }
            }
        }
        return null;
    }
}
