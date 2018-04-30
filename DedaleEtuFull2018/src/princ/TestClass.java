package princ;

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
        //for (String s : mas.util.Tools.dijkstra(map,"I","A"))
        //    System.out.println(s);
        System.out.println(mas.util.Tools.inCommunicationRange(map,"A","E"));

    }
}
