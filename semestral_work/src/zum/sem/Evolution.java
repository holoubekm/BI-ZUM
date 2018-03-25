package zum.sem;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;

/**
 *
 * @author Martin
 */
public class Evolution
{
    public interface EvolutionListener
    {
        public void StepReached(Evolution ev);
        public void ActionStarted();
        public void ActionFinished();
    }
    
    public int mGenCount;
    public int mLoopCount;
    public int mCount;
    public double mBestFit;
    public double mAvgFit;
    public boolean mRunning;
    
    public static double mMutProb;
    public static double mCrossProb;
    
    private final EvolutionListener mWindow;
    private List<Individuum> mSols;
    private Individuum mBest;
    private int mBestSol;

    private final Random mRand;
    
    private List<Rectangle> mBestRects;
    
    public Evolution(EvolutionListener window)
    {
        mWindow = window;
        mGenCount = 100000;
        mCount = 40;
        
        mMutProb = 0.8;
        mCrossProb = 0.35;
        
        mBestFit = 0.0;
        mBestSol = 0;
        
        mRand = new Random();
        
        mSols = new ArrayList<>(mCount);
        for(int x = 0; x < mCount; x++)
        {
            mSols.add(new Individuum(true));
            if(mSols.get(x).GetFitness() > mBestFit)
            {
                mBestFit = mSols.get(x).GetFitness();
                mBestSol = x;
                mBest = mSols.get(x).DeepCopy();
            }
        }
    }
    
    public void Stop()
    {
        mRunning = false;
    }
    
    public void Start()
    {
        mRunning = true;
        new Thread()
        {
            @Override
            public void run()
            {
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        mWindow.ActionStarted();
                    }
                });
                
                for(mLoopCount = 0; mLoopCount < mGenCount && mRunning; mLoopCount++)
                {
                    MakeEvolutionStep();
                }
                
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        mWindow.ActionFinished();
                    }
                });
            }
        }.start();
    }
    
    private void MakeEvolutionStep()
    {
        NextSolution();
        //mBestFit = 0;
        for(int x = 0; x < mCount; x++)
        {
            double fitness = mSols.get(x).GetFitness();
            if(fitness > mBestFit)
            {
                mBestFit = fitness;
                mBestSol = x;
                mBest = mSols.get(x).DeepCopy();
            }
        }
         
        //mBestSol = mRand.nextInt(mCount);
        //mBest = mSols.get(mBestSol).DeepCopy();
        
        
        if(mLoopCount % 50 == 0)
        {
            mBestRects = mBest.GetRectangles();
            mWindow.StepReached(this);
        }
    }
    
    private void NextSolution()
    {
        int newCount = 0;
        List<Individuum> mBuf = new ArrayList<>(mCount);
        
        
        mBuf.add(mBest.DeepCopy()); newCount++;
        mBuf.add(mBest.DeepCopy().Mutate()); newCount++;
        
        while(newCount < mCount)
        {
            //mBuf.add(mBest.DeepCopy().Mutate()); newCount++;
                       
            List<Individuum> parents = SelectTwo();
            List<Individuum> offsprings = new ArrayList<>(2);
            
            if(mRand.nextDouble() < mCrossProb)
            {
               offsprings = parents.get(0).DeepCopy().Cross(parents.get(1).DeepCopy());
            }
            else
            {
                offsprings.add(parents.get(0).DeepCopy());
                offsprings.add(parents.get(1).DeepCopy());
            }
            
            mBuf.add(offsprings.get(0).Mutate());
            newCount++;
            
            if(newCount < mCount)
            {
                mBuf.add(offsprings.get(1).Mutate());
                newCount++;
            }
        }
        
        mSols = mBuf;
    }
    
    private List<Individuum> SelectTwo()
    {
        List<Individuum> selected = new ArrayList<>(2);
        while (selected.size() < 2) 
        {
            double total = 0;
            for (Individuum elem : mSols)
            {
               total += elem.GetFitness();
            }

            double value = mRand.nextDouble() * total;
            for (Individuum elem : mSols)
            {
               if (value < elem.GetFitness())
               {
                   if(selected.size() > 0)
                   {
                       if(selected.get(0) != elem)
                       {
                           //mSols.remove(elem);
                           selected.add(elem);
                       }
                   }
                   else
                   {
                       //mSols.remove(elem);
                       selected.add(elem);
                   }
                   break;
               }
               value -= elem.GetFitness();
            }
        }
        return selected;
    }

    
    public List<Rectangle> GetRectangles()
    {
        return mBestRects;
    }
}
