package mas.util;

import env.Attribute;

import java.util.ArrayList;
import java.util.List;

public class nodeData {

    private List<Attribute> attrs;
    private List<String> neighbours;
    private long time;

    public nodeData(List<Attribute> attrs,List<String> neighbours,long time){
        this.attrs = attrs;
        this.neighbours = neighbours;
        this.time = time;
    }

    public List<Attribute> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Attribute> attrs) {
        this.attrs = attrs;
    }

    public List<String> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<String> neighbours) {
        this.neighbours = neighbours;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
