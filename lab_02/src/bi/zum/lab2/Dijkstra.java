package bi.zum.lab2;

import bi.zum.lab1.PathRecSearch;
import bi.zum.lab2.util.ZumPriorityQueue;
import cz.cvut.fit.zum.api.AbstractAlgorithm;
import cz.cvut.fit.zum.api.Node;
import cz.cvut.fit.zum.api.UninformedSearch;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomáš Řehořek
 */
@ServiceProvider(service = AbstractAlgorithm.class)
public class Dijkstra extends PathRecSearch implements UninformedSearch {

    private ZumPriorityQueue<Node> open;
    private HashSet<Node> closed;
    
    @Override
    public String getName() {
        return "Dijkstra";
    }
    
    @Override
    public List<Node> findPath(Node startNode) 
    {
        
        open = new ZumPriorityQueue<Node>();
        closed = new HashSet<Node>();
        prev = new HashMap<Node, Node>();
        
        Map<Node, Double> dist = new HashMap<Node, Double>();
        
        open.enqueue(startNode, 0);
        dist.put(startNode, 0.0);
        
        while(!open.isEmpty())
        {
            Node current = open.dequeue();
            if (current.isTarget()) {
                return buildPath(current);
            }
            
            double curDist = dist.get(current);
            for (Node y : current.expand()) 
            {
                if(!closed.contains(y))
                {
                    double nextDist = Distance(current, y) + curDist;
                    if(open.contains(y))
                    {
                        double oldDist = dist.get(y);
                        if(oldDist > nextDist)
                        {
                            prev.put(y, current);
                            dist.put(y, nextDist);
                            open.updateKey(y, nextDist);
                        }
                    }
                    else 
                    {
                        open.enqueue(y, nextDist);
                        dist.put(y, nextDist);
                        prev.put(y, current);
                    }
                }
            }
            closed.add(current);
        }

        return null;
    }
}
