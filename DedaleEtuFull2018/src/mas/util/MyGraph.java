package mas.util;

import java.util.*;

public class MyGraph {
    public static ArrayList<String> dijkstra (HashMap<String,String[]> map, String src, String dst)  {
        System.out.println("Dijkstra called :");
        if (src.equals(dst))
            return new ArrayList<String>();
        ArrayList<String> explored = new ArrayList<>(map.keySet());
        //for (String s : explored)
        //    System.out.println(s);
        //System.out.println("------");
        ArrayList<String> unexplored = new ArrayList<>();
        for (String[] s : map.values())
            unexplored.addAll(Arrays.asList(s));
        HashSet<String> tmp = new LinkedHashSet<>(unexplored);
        unexplored.clear();
        unexplored.addAll(tmp);
        unexplored.removeAll(explored);
        //for (String s : unexplored)
        //    System.out.println(s);
        // explored and unexplored are well computed

        ArrayList<ArrayList<String>> graph = new ArrayList<>();
        boolean dstInGraph = false;
        ArrayList<String> tmp2 = new ArrayList<>();
        tmp2.add(src);
        graph.add(tmp2);
        while (!dstInGraph){
            tmp2 = new ArrayList<>();
            for (String higherLevel : graph.get(graph.size()-1)){
                if (map.get(higherLevel) != null)
                    for (String neighbour : map.get(higherLevel)){
                        if (!inGraph(graph,neighbour) && !inArray(tmp2,neighbour)) {
                            tmp2.add(neighbour);
                            if (neighbour.equals(dst))
                                dstInGraph = true;
                        }
                    }
            }
            graph.add(tmp2);
        }

        /*for (ArrayList<String> a : graph) {
            for (String s : a) {
                System.out.print(s+"\t");
            }
            System.out.println();
        }
        System.out.println("Fin print graph");*/
        ArrayList<String> chemin = new ArrayList<>();
        chemin.add(dst);
        for (int i = graph.size() - 2; i > 0; i--){
            boolean ok = false;
            for (String s : graph.get(i)){
                if (!ok) {
                    if (!unexplored.contains(s)) {
                        for (String t : map.get(s)) {
                            if (!ok)
                                if (t.equals(chemin.get(0))) {
                                    chemin.add(0,s);
                                    ok = true;
                                }
                        }
                    }
                }
            }
        }
        /*for (String s : chemin)
            System.out.println(s+"\t");
        System.out.println("---");*/

        return chemin;
    }

    private static boolean inGraph(ArrayList<ArrayList<String>> graph, String node){
        for (ArrayList<String> hauteur : graph)
            for (String noeud : hauteur)
                if (noeud.equals(node))
                    return true;
        return false;
    }

    private static boolean inArray(ArrayList<String> array, String node){
        for (String s : array)
            if (s.equals(node))
                return true;
        return false;

    }
}
