package bi.zum.lab2;

import bi.zum.lab1.PathRecSearch;
import bi.zum.lab2.util.ZumPriorityQueue;
import cz.cvut.fit.zum.api.AbstractAlgorithm;
import cz.cvut.fit.zum.api.InformedSearch;
import cz.cvut.fit.zum.api.Node;
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
public class AStar extends PathRecSearch implements InformedSearch  {
    
    private ZumPriorityQueue<Node> open;
    private HashSet<Node> closed;
    
    @Override
    public String getName() {
        return "A*";
    }
    
    @Override
    public List<Node> findPath(Node startNode, Node endNode) {

        open = new ZumPriorityQueue<Node>();
        closed = new HashSet<Node>();
        prev = new HashMap<Node, Node>();
        
        Map<Node, Double> dist = new HashMap<Node, Double>();
        
        open.enqueue(startNode, GetHeuristic(startNode, endNode));
        dist.put(startNode, 0.0);
        
        while(!open.isEmpty())
        {
            Node current = open.dequeue();
            if(current.isTarget())
            {
                return buildPath(current);
            }
            
            closed.add(current);
            double curDist = dist.get(current);
            
            for(Node y : current.expand())
            {
                if(!closed.contains(y))
                {
                    double startDist = curDist + Distance(current, y);
                    
                    double neighDist = startDist;
                    if(dist.containsKey(y))
                    {
                        neighDist = dist.get(y);
                    }
                    
                    if(!open.contains(y) || neighDist > startDist)
                    {
                        dist.put(y, startDist);
                        prev.put(y, current);
                        
                        if(!open.contains(y))
                        {
                            open.enqueue(y, startDist + GetHeuristic(y, endNode));
                            //dist.put(y, startDist);
                        }
                        else
                        {
                            open.updateKey(y, startDist + GetHeuristic(y, endNode));
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private double GetHeuristic(Node current, Node end)
    {
        return Distance(current, end);
    }
}
