package bi.zum.lab2;

import bi.zum.lab1.PathRecSearch;
import bi.zum.lab2.util.ZumPriorityQueue;
import cz.cvut.fit.zum.api.AbstractAlgorithm;
import cz.cvut.fit.zum.api.InformedSearch;
import cz.cvut.fit.zum.api.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomáš Řehořek
 */
@ServiceProvider(service = AbstractAlgorithm.class)
public class GreedySearch extends PathRecSearch implements InformedSearch {
    
    private ZumPriorityQueue<Node> open;
    private HashSet<Node> closed;
    
    @Override
    public String getName() {
        return "Greedy search";
    }
    
    @Override
    public List<Node> findPath(Node startNode, Node endNode){
        
        open = new ZumPriorityQueue<Node>();
        closed = new HashSet<Node>();
        prev = new HashMap<Node, Node>();
        
        open.enqueue(startNode, 0.0);
        
        
        while(!open.isEmpty())
        {
            Node current = open.dequeue();
            if (current.isTarget()) {
                return buildPath(current);
            }
            
            for (Node y : current.expand()) 
            {
                if(!closed.contains(y) && !open.contains(y))
                {
                    double priority = Distance(endNode, y);
                    open.enqueue(y, priority);
                }
            }
            closed.add(current);
        }
        
        return null;
    }
        
}
