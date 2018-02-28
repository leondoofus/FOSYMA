package mas.util;

import java.util.*;

public class MyGraph {
    public static ArrayList<String> dijkstra (HashMap<String,String[]> map, String src, String dst) throws NodeUnknownException {
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
        // A voir plus la condition ci-dessous
        if (unexplored.contains(src) || !explored.contains(src) || (!explored.contains(dst) && !unexplored.contains(dst)))
            throw new NodeUnknownException("Src/Dst unknown");
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
                        if (neighbour.equals(dst))
                            dstInGraph = true;
                        if (!inGraph(graph,neighbour))
                            tmp2.add(neighbour);
                    }
            }
            graph.add(tmp2);
        }
        /*
        for (ArrayList<String> a : graph) {
            for (String s : a) {
                System.out.print(s);
            }
            System.out.println();
        }*/
        ArrayList<String> chemin = new ArrayList<>();
        chemin.add(dst);
        for (int i = graph.size() - 2; i >= 0; i--){
            if (map.get(chemin.get(0)) != null) {
                for (int j = 0; j < map.get(chemin.get(0)).length; j++) {
                    if (graph.get(i).contains(map.get(chemin.get(0))[j]) && !unexplored.contains(map.get(chemin.get(0))[j])) {
                        chemin.add(0, map.get(chemin.get(0))[j]);
                        break;
                    }
                }
            }
        }
        return chemin;
    }

    private static boolean inGraph(ArrayList<ArrayList<String>> graph, String node){
        for (ArrayList<String> hauteur : graph)
            for (String noeud : hauteur)
                if (noeud.equals(node))
                    return true;
        return false;
    }
}
