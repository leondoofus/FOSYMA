package mas.util;
import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.HashMap;


public class Tools {


    public static ArrayList<String> dijkstra (HashMap<String,String[]> map, String src, String dst, String tankerPos)  {
        if (src.equals(dst) || dst.equals(tankerPos) ||src.equals(tankerPos)){
            return new ArrayList<>();
        }
        if(src.equals("")){
            System.out.println("aaaaaa");
        }
        for (String s : map.get(src)){
            if (s.equals(dst)){
                ArrayList<String> res = new ArrayList<>();
                res.add(dst);
                return res;
            }
        }
        Graph graph = new SingleGraph("GF1");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        for(String father : map.keySet()){
            for (String sons :map.get(father)){
                graph.addEdge(father+sons,father,sons);
            }
        }
        if (tankerPos != null){
            graph.removeNode(tankerPos);
        }
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.setSource(graph.getNode(src));
        dijkstra.init(graph);
        dijkstra.compute();
        Path p = dijkstra.getPath(graph.getNode(dst));
        ArrayList<String> res = new ArrayList<>();
        for (Node N: p.getNodeSet()){
            res.add(N.toString());
        }
        if(res.size() == 0){
            return new ArrayList<>();
        }
        res.remove(0);
        return res;
    }


    public static ArrayList<String> dijkstraClosestNode(HashMap<String, String[]> map, String src, String [] unexplored,String tankerPos){
        if (unexplored.length == 0) return new ArrayList<>();
        ArrayList<String> res = dijkstra(map,src,unexplored[0],tankerPos);
        for (int i = 1; i < unexplored.length; i++){
            ArrayList<String> tmp = dijkstra(map,src,unexplored[i],tankerPos);
            if (tmp.size() < res.size() && tmp.size()!=0){
                res = tmp;
            }
        }
        return res;
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

    public static boolean inCommunicationRange (HashMap<String,String[]> map, String src, String dst)  {
        ArrayList<ArrayList<String>> graph = new ArrayList<>();
        ArrayList<String> tmp2 = new ArrayList<>();
        tmp2.add(src);
        graph.add(tmp2);
        for (int i = 0; i < 3; i++){
            tmp2 = new ArrayList<>();
            for (String higherLevel : graph.get(graph.size()-1)){
                if (map.get(higherLevel) != null)
                    for (String neighbour : map.get(higherLevel)){
                        if (neighbour.equals(dst))
                            return true;
                        if (!inGraph(graph,neighbour) && !inArray(tmp2,neighbour)) {
                            tmp2.add(neighbour);
                        }
                    }
            }
            graph.add(tmp2);
        }
        return false;
    }


    public static String centralize (HashMap<String, String[]> map){
        Graph graph = new SingleGraph("GF1");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        for(String father : map.keySet()){
            for (String sons :map.get(father)){
                graph.addEdge(father+sons,father,sons);
            }
        }
        BetweennessCentrality bcb = new BetweennessCentrality();
        bcb.init(graph);
        bcb.compute();
        double maxCb = 0;
        Node node = graph.getNode(0);
        for(Node n :graph.getNodeSet()){
            double tmp =  n.getAttribute("Cb");
            if(maxCb < tmp){
                maxCb = tmp;
                node = graph.getNode(n.toString());
            }
        }
        return node.toString();
    }
}


