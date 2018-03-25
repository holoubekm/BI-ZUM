package zum.sem;

import java.awt.Rectangle;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author holoubekm
 */
public class Individuum
{
    public List<Rect> mGens;
    private int mCount;
    private static int mMinCount = 4;
    private static int mMaxCount = 10;
    private final Random mRand;
    private double mFitness;
    
    static void setCount(int value)
    {
        mMinCount = value - 3;
        mMaxCount = value + 3;
    }
       
    public Individuum(boolean initialize)
    {
        mRand = new Random();
        mCount = mMinCount + mRand.nextInt(mMaxCount - mMinCount);
        
        if(initialize)
        {
            mGens = new ArrayList<>();
            Rect first = new Rect();
            first.FillImage();
            mGens.add(first);
            
            while(mGens.size() < mCount)
            {
                int pos = mRand.nextInt(mGens.size());
                Rect random = mGens.get(pos);
                List<Rect> offsprings = random.Split();
                if(offsprings != null)
                {
                    mGens.remove(pos);
                    mGens.addAll(offsprings);
                }
                
                if(mGens.isEmpty())
                {
                    first = new Rect();
                    first.FillImage();
                    mGens.add(first);
                }
            }
        }
    }
    
    public List<Rectangle> GetRectangles()
    {
        List<Rectangle> rects = new ArrayList<>();
        for(Rect rct : mGens)
        {
            rects.add(new Rectangle(
                    (int)(rct.mX * ImageHandler.mScaleX), 
                    (int)(rct.mY * ImageHandler.mScaleY), 
                    (int)(rct.mWidth * ImageHandler.mScaleX), 
                    (int)(rct.mHeight * ImageHandler.mScaleY)));
        }
        return rects;
    }
    
    public double GetFitness()
    {
        double totalFitness = 0;
        int totalCoverage = 0;
        int totalArea = 0;
        for(Rect child : mGens)
        {
            totalFitness += child.GetFitness();
            totalCoverage += child.GetCoverage();
            totalArea += child.GetArea();
        }
        
        //Possible weighted sum?
        double fitA = 0.0;//totalFitness / (ImageHandler.mWidth * ImageHandler.mHeight * 0xFF) ; // 0.0 - 1.0
        //Based on the black pixel coverage
        double fitB = ((double)totalCoverage) / ImageHandler.mGreyCount; // 0.0 - 1.0
        //Based on the total area versus black pixels count
        fitB = pow((log(2 / (2 - fitB))) / (log(2)), 3); //Threshold function 0.0 - 1.0
        
        double fitC = -((double)totalArea / ImageHandler.mCount) * 0.5;
        
        //System.out.println(fitA + " - " + fitB/8 + " - " + fitC);
        mFitness = 6.5 * (fitB + fitC);
        mFitness = pow(2, mFitness);
        //System.out.println(mFitness);
        return mFitness;
    }
   
    public Individuum Mutate()
    {
        if(Evolution.mMutProb >= mRand.nextDouble())
        {
            int count = (int)(mCount * (mRand.nextDouble() * 0.2 + 0.1));
            for(int x = 0; x < count; x++)
            {
                int pos = mRand.nextInt(mCount);
                Rect cur = mGens.get(pos);
                
                double selector = mRand.nextDouble();
                if(selector < 0.15)
                {
                    //Cutting off the right side
                    /**********************************************/
                    
                    double scaleX = mRand.nextDouble() * 0.3 - 0.15;
                    int newWidth = (int)(ImageHandler.mWidth * scaleX + cur.mWidth);
                    //int newX = (int)(cur.mX - cur.mWidth * scaleX / 2);
                    if(newWidth > 100)
                    {
                        boolean intersects = false;
                        for(int y = 0; y < mCount; y++)
                        {
                            if(y != pos)
                            {
                                if(mGens.get(y).CheckIntersection(cur.mX, cur.mY, newWidth, cur.mHeight))
                                {
                                    intersects = true;
                                    break;
                                }
                            }
                        }

                        if(!intersects)
                        {
                            cur.SetSize(newWidth, cur.mHeight);
                            //cur.SetPos(newX, cur.mY);
                        }
                    }
                    /*********************************************/
                }
                if(selector < 0.30)
                {
                    //Cutting off the right side
                    /**********************************************/
                    
                    double scaleX = mRand.nextDouble() * 0.3 - 0.15;
                    int newWidth = (int)(ImageHandler.mWidth * scaleX + cur.mWidth);
                    int newX = (int)(cur.mX - newWidth + cur.mWidth);
                    if(newWidth > 100)
                    {
                        boolean intersects = false;
                        for(int y = 0; y < mCount; y++)
                        {
                            if(y != pos)
                            {
                                if(mGens.get(y).CheckIntersection(newX, cur.mY, newWidth, cur.mHeight))
                                {
                                    intersects = true;
                                    break;
                                }
                            }
                        }

                        if(!intersects)
                        {
                            cur.SetSize(newWidth, cur.mHeight);
                            cur.SetPos(newX, cur.mY);
                        }
                    }
                    /*********************************************/
                }
                else if(selector < 0.45)
                {
                    double scaleY = mRand.nextDouble() * 0.3 - 0.15;
                    int newHeight = (int)(ImageHandler.mHeight * scaleY + cur.mHeight);
                    
                    if(newHeight > 20)
                    {
                        boolean intersects = false;
                        for(int y = 0; y < mCount; y++)
                        {
                            if(y != pos)
                            {
                                if(mGens.get(y).CheckIntersection(cur.mX, cur.mY, cur.mWidth, newHeight))
                                {
                                    intersects = true;
                                    break;
                                }
                            }
                        }

                        if(!intersects)
                        {
                            cur.SetSize(cur.mWidth, newHeight);
                        }
                    }
                    /*********************************************/
                    
                }
                else if(selector < 0.60)
                {
                    double scaleY = mRand.nextDouble() * 0.3 - 0.15;
                    int newHeight = (int)(ImageHandler.mHeight * scaleY + cur.mHeight);
                    int newY = (int)(cur.mY - newHeight + cur.mHeight);
                    
                    if(newHeight > 20)
                    {
                        boolean intersects = false;
                        for(int y = 0; y < mCount; y++)
                        {
                            if(y != pos)
                            {
                                if(mGens.get(y).CheckIntersection(cur.mX, newY, cur.mWidth, newHeight))
                                {
                                    intersects = true;
                                    break;
                                }
                            }
                        }

                        if(!intersects)
                        {
                            cur.SetSize(cur.mWidth, newHeight);
                            cur.SetPos(cur.mX, newY);
                        }
                    }
                    /*********************************************/
                    
                }
                else if(selector < 0.85)
                {
                    int newX = (int)(cur.mX + mRand.nextDouble() * 20 - 10);
                    int newY = (int)(cur.mY + mRand.nextDouble() * 20 - 10);
                    
                    boolean intersects = false;
                    for(int y = 0; y < mCount; y++)
                    {
                        if(y != pos)
                        {
                            if(mGens.get(y).CheckIntersection(newX, newY, cur.mWidth, cur.mHeight))
                            {
                                intersects = true;
                                break;
                            }
                        }
                    }

                    if(!intersects)
                    {
                        cur.SetPos(newX, newY);
                    }
                    /*********************************************/
                }
                else if(selector < 0.89 && mCount > mMinCount)
                {
                    double smallest = Double.MAX_VALUE;
                    int smallPos = 0;
                    for(int y = 0; y < mGens.size(); y++)
                    {
                        if(mGens.get(y).GetCoverage() / ImageHandler.GetCoverage() < smallest)
                        {
                            smallest = mGens.get(y).GetCoverage() / ImageHandler.GetCoverage();
                            smallPos = y;
                        }
                    }
                    
                    mGens.remove(smallPos);
                    mCount = mGens.size();
                }
                else if(selector < 0.93 && mCount > mMinCount)
                {
                    Collections.sort(mGens, new Compare());
                    for(int y = 0; y < mCount - 1; y++)
                    {
                        Rect first = mGens.get(y);
                        Rect second = mGens.get(y + 1);
                        
                        if(Math.abs(first.mX + first.mHeight - second.mX) < 10)
                        {
                            int xPos = first.mX < second.mX ? first.mX : second.mX;
                            int yPos = first.mY < second.mY ? first.mY : second.mY;
                            int width = (first.mX + first.mWidth > second.mX + second.mWidth ? first.mX + first.mWidth : second.mX + second.mWidth) - xPos;
                            int height = (first.mY + first.mHeight> second.mY + second.mHeight? first.mY + first.mHeight : second.mY + second.mHeight) - yPos;
                            
                            boolean intersects = false;
                            for(int z = 0; z < mCount; z++)
                            {
                                if(z != y && z != y + 1)
                                {
                                    if(mGens.get(z).CheckIntersection(xPos, yPos, width, height))
                                    {
                                        intersects = true;
                                        break;
                                    }
                                }
                            }
                            
                            if(!intersects)
                            {
                                mGens.remove(y + 1);
                                mGens.remove(y);
                                mGens.add(new Rect(xPos, yPos, width, height));
                                mCount = mGens.size();
                                break;
                            }
                        }
                    }
                }
                else if(mCount < mMaxCount)
                {
                    Rect random = mGens.get(pos);
                    List<Rect> offsprings = random.Split();
                    if(offsprings != null)
                    {
                        mGens.remove(pos);
                        mGens.addAll(offsprings);
                        mCount = mGens.size();
                    }
                }
            }
        }
        return this;
    }
        
    public List<Individuum> Cross(Individuum wife)
    {
        List<Individuum> offsprings = new ArrayList<>(2);
        Collections.sort(mGens, new Compare());
        Collections.sort(wife.mGens, new Compare());
        
        int pos = mRand.nextInt(mCount > wife.mCount ? wife.mCount : mCount);
             
        int xPos = mGens.get(pos).mX;
        int yPos = mGens.get(pos).mY;
        int width = mGens.get(pos).mWidth;
        int height = mGens.get(pos).mHeight;
        
        for(int x = 0; x < wife.mCount; x++)
        {
            boolean error = false;
            for(int y = 0; y < wife.mCount; y++)
            {
                if(y != x)
                {
                    if(wife.mGens.get(y).CheckIntersection(xPos, yPos, width, height))
                    {
                        error = true;
                        break;
                    }
                }
            }
            if(error == false)
            {
                wife.mGens.get(x).SetPos(xPos, yPos);
                wife.mGens.get(x).SetSize(width, height);
                break;
            }
        }
        
        offsprings.add(this);
        offsprings.add(wife);
        return offsprings;
    }

    public Individuum DeepCopy()
    {
        Individuum newOne = new Individuum(false);
        newOne.mCount = mCount;
        newOne.mFitness = mFitness;
        newOne.mGens = new ArrayList<>(mCount);
        for(Rect rec : mGens)
        {
            newOne.mGens.add(rec.DeepCopy());
        }
        return newOne;
    }
}
