package zum.sem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author holoubekm
 */
class Rect
{
    public int mX, mY, mWidth, mHeight, mPixCount;
    private int maxW, maxH;
    private Random mRand;
    public double mFitness;
    public double mCoverage;
    public boolean mChanged;
    public int mArea;
    
    public Rect() 
    {
        mChanged = true;
        mRand = new Random();
        maxW = ImageHandler.mWidth;
        maxH = ImageHandler.mHeight;
        mArea = maxW * maxH;
    }
    
    public Rect(int x, int y, int width, int height)
    {
        this();
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mArea = mWidth * mHeight;
        mChanged = true;
    }
    
    void SetPos(int newX, int newY)
    {
        mX = newX;
        mY = newY;
        mChanged = true;
    }

    void SetSize(int newWidth, int newHeight)
    {
        mWidth = newWidth;
        mHeight = newHeight;
        mArea = mWidth * mHeight;
        mChanged = true;
    }
    
    public void RandomInit()
    {
        mX = mRand.nextInt(maxW);
        mY = mRand.nextInt(maxH);
        mWidth = mRand.nextInt(maxW - mX);
        mHeight = mRand.nextInt(maxH - mY);
        mArea = mWidth * mHeight;
        mPixCount = mWidth * mHeight;
        mChanged = true;
        //mFitness = ComputeFitness();
    }
   
    public double GetFitness()
    {
        if(mChanged == true)
        {
            ImageHandler.Compute(mX, mY, mWidth, mHeight);
            mCoverage = ImageHandler.GetCoverage();
            mFitness = ImageHandler.GetFitness();
            mChanged = false;
        }
        return mFitness;
    }
    
    public double GetCoverage()
    {
        if(mChanged == true)
        {
            ImageHandler.Compute(mX, mY, mWidth, mHeight);
            mFitness = ImageHandler.GetFitness();
            mCoverage = ImageHandler.GetCoverage();
            mChanged = false;
        }
        return mCoverage;
    }

    public double GetArea()
    {
        return mArea;
    }
    
    void FillImage()
    {
        mX = 5;
        mY = 5;
        mWidth = maxW - 5;
        mHeight = maxH - 5;
        mChanged = true;
    }
    
    public List<Rect> Split()
    {
        List<Rect> offsprings = new ArrayList<>();
        double ratio = 0.8 * mRand.nextDouble() + 0.1;
        Rect childA, childB;
        if(mRand.nextDouble() > 0.0)
        {
            childA = new Rect(mX, mY, mWidth, (int)(mHeight * ratio * 0.95));
            childB = new Rect(mX, (int)(mY + mHeight * ratio + 1), mWidth, (int)((mHeight - mHeight * (ratio * 1.05) - 1) ));
        }
        else
        {
            childA = new Rect(mX, mY, (int)(mWidth * ratio * 0.95), mHeight);
            childB = new Rect((int)(mX + mWidth * ratio + 1), mY, (int)((mWidth - mWidth * (ratio * 1.05) - 1)), mHeight);
        }
       
        if(childA.mWidth < 100 || childA.mHeight < 15 || childB.mWidth < 100 || childB.mHeight < 15) 
        {
            return null;
        }
        offsprings.add(childA);
        offsprings.add(childB);
            
        return offsprings;
    }

    boolean CheckIntersection(int x, int y, int width, int height)
    {
        boolean inters =  !(
                x > mX + mWidth || 
                x + width < mX || 
                y > mY + mHeight ||
                y + height < mY);
        boolean bound = !(x >= 0 && y >= 0 && x + width <= ImageHandler.mWidth && y + height <= ImageHandler.mHeight);
        return inters || bound;
    }

    Rect DeepCopy()
    {
        Rect newOne = new Rect(mX, mY, mWidth, mHeight);
        newOne.mFitness = mFitness;
        newOne.mCoverage = mCoverage;
        newOne.mChanged = mChanged;
        newOne.mArea = mArea;
        return newOne;
    }
}

class Compare implements Comparator<Rect>
{
    @Override
    public int compare(Rect t, Rect t1)
    {
        if(t.mX < t1.mX)
        {
            return -1;
        }
        else if(t.mX > t1.mX)
        {
            return 1;
        }
        else
        {
            if(t.mY < t1.mY)
            {
                return -1;
            }
            else if(t.mY > t1.mY)
            {
                return 1;
            }
        }
        return 0;
    }
}


