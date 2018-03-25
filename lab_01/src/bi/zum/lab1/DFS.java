package bi.zum.lab1;

import cz.cvut.fit.zum.api.AbstractAlgorithm;
import cz.cvut.fit.zum.api.Node;
import cz.cvut.fit.zum.api.UninformedSearch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;

/**
 * Depth-first search
 *
 * @see http://en.wikipedia.org/wiki/Depth-first_search
 */
@ServiceProvider(service = AbstractAlgorithm.class, position = 10)
public class DFS extends AbstractAlgorithm implements UninformedSearch {

    private LinkedList<NodeWrapper> opened;
    private HashSet<Node> closed;
    //private Map<Node, Node> prev;
    private List<Node> path;

    @Override
    public String getName() {
        return "DFS";
    }

    @Override
    public List<Node> findPath(Node startNode) {
        opened = new LinkedList<NodeWrapper>();
        closed = new HashSet<Node>();
        //prev = new HashMap<Node, Node>();
        path = new ArrayList<Node>();
        
        boolean found = false;
        NodeWrapper current = null;
        opened.push(new NodeWrapper(startNode, null));
        while(opened.size() > 0)
        {          
            current = opened.pollLast();
            if(current.isTarget())
            {
                found = true;
                break;
            }
            
            if(closed.contains(current))
            {
                continue;
            }
            
            List<Node> children = current.expand();
            closed.add(current);
            
            for(Node child : children)
            {
                if(!closed.contains(child))
                {
                    opened.add(new NodeWrapper(child, current));
                    //prev.put(child, current);
                }
            }
        }
        
        path.add(current.getNode());
        while((current = current.getPrev()) != null)
        {
            path.add(current.getNode());
        }
        
        return path;
    }
}
