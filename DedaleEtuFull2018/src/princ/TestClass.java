package princ;

import mas.util.NodeUnknownException;

import java.util.ArrayList;
import java.util.HashMap;

public class TestClass {
    public static void main(String[] args) {
        System.out.println("This is a test");
        /*
        Supposons qu'on a une carte comme ça :
        A(depart)   - B (exploré)   - C(exploré)
        |             |               |
        D(non exp)  - E(non exp)   - F(exploré)     - K(non exp ds F's dict)
        |(ds A's dict) (ds B's dict) |
        G(non exp)  - H(non exp)     I(exploré)
        */
        HashMap<String,String[]> map = new HashMap<>();
        map.put("A",new String[]{"B","D"});
        map.put("B",new String[]{"A","E", "C"});
        map.put("C",new String[]{"B", "F"});
        map.put("F",new String[]{"C", "E","I","K"});
        map.put("I",new String[]{"F"});
        /*
        for (String s : map.keySet())
            for (String t : map.get(s))
                System.out.println(s+" "+t);
        */
        try {
            for (String s : mas.util.MyGraph.dijkstra(map,"I","A"))
                System.out.println(s);
        } catch (NodeUnknownException e) {
            e.printStackTrace();
        }
        System.out.println("lala");

        ArrayList<String> a = mas.util.ShortestPath.solve("A","I",map,new ArrayList<>(map.keySet()));
        if (a == null) System.out.println("shit");
        for (String s : a)
            System.out.println(s);
    }
}
