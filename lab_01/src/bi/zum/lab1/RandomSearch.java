package bi.zum.lab1;

import cz.cvut.fit.zum.api.AbstractAlgorithm;
import cz.cvut.fit.zum.api.Node;
import cz.cvut.fit.zum.api.UninformedSearch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 * WARNINIG: this is very stupid algorithm!!!
 *
 * Should serve only as an example of UninformedSearch usage
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = AbstractAlgorithm.class, position = 100)
public class RandomSearch extends AbstractAlgorithm implements UninformedSearch {

    private HashSet<Node> closed;
    private LinkedList<NodeWrapper> opened;
    private List<Node> path;
    //private Map<Node, Node> prev;

    @Override
    public String getName() {
        return "random search";
    }

    @Override
    public List<Node> findPath(Node startNode) {
        opened = new LinkedList<NodeWrapper>();
        closed = new HashSet<Node>();
        //prev = new HashMap<Node, Node>();
        NodeWrapper current;
        path = null;
        opened.add(new NodeWrapper(startNode, null));
        
        while (!opened.isEmpty()) {

            current = random(opened);

            if(closed.contains(current))
            {
                continue;
            }
            
            if (current.isTarget()) {
                return buildPath(startNode, current);
            }

            for (Node y : current.expand()) {
                if (!closed.contains(y)) {
                    opened.add(new NodeWrapper(y, current));
                    //prev.put(y, current);
                }
            }
            closed.add(current);

        }

        return path;
    }

    private List<Node> buildPath(Node start, NodeWrapper target) {
        path = new ArrayList<Node>();
        
        NodeWrapper current = target;
        path.add(current.getNode());
        while((current = current.getPrev()) != null)
        {
            path.add(current.getNode());
        }
        
        return path;
    }

    /**
     * Select random node from list
     *
     * @param list
     * @return Node
     */
    private NodeWrapper random(List<NodeWrapper> list) {
        int min = 0;
        int max = list.size();

        if (max == 1) {
            return list.remove(0);
        }
        int num = min + (int) (Math.random() * ((max - min)));

        //we want to remove explored nodes
        return list.remove(num);
    }
}
