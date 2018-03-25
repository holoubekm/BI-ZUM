/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zum.sem;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author holoubekm
 */
public class ImageHandler
{
    static ImageHandler mInstance;
    static String mFileName;
    static BufferedImage mImage;
    static byte[] mPixels;
    static int mWidth, mHeight;
    static double mScaleX, mScaleY;
    static int mCount, mGreyCount;
    static double mGreyThreshold;
    
    static double mCurFitness;
    static long mCurCoverage;
    
    public static ImageHandler Renew(String name, int width, int height, int threshold)
    {
        try
        {
            return new ImageHandler(name, width, height, threshold);
        }
        catch (IOException ex)
        {
            System.out.println("Can't load image, not found.");
        }
        
        return null;
    }
    
    private ImageHandler(String name, int width, int height, int threshold) throws IOException
    {
        mFileName = name;
        mImage = ImageIO.read(new File(name));
        mPixels = ((DataBufferByte) mImage.getRaster().getDataBuffer()).getData();
        mWidth = mImage.getWidth();
        mHeight = mImage.getHeight();
        mScaleX = width / (double)mWidth;
        mScaleY = height / (double)mHeight;
        mCount = mPixels.length;
        mGreyThreshold = threshold;
        mGreyCount = 0;

        for(int xPos = 0; xPos < mWidth; xPos++)
        {
            for(int yPos = 0; yPos < mHeight; yPos++)
            {
                if(GetPixelColor(xPos, yPos) < mGreyThreshold)
                {
                    mGreyCount++;
                }
            }
        }
    }
    
    private static int GetPixelColor(int x, int y)
    {
        int pos = (y * mWidth + x) * 3;
        return ((mPixels[pos] & 0xFF) + (mPixels[pos + 1] & 0xFF) + (mPixels[pos + 2] & 0xFF));
    }
    
    public static void Compute(int x, int y, int width, int height)
    {
        if(width * height == 0)
        {
            mCurFitness = 0;
            mCurCoverage = 0;
            return;
        }
        
        long totalF = 0, totalC = 0;
        for(int xPos = x; xPos < x + width; xPos++)
        {
            for(int yPos = y; yPos < y + height; yPos++)
            {
                long cur = (long)GetPixelColor(xPos, yPos);
                totalF += cur;
                if(cur < mGreyThreshold)
                {
                    totalC++;
                }
            }
        }
        mCurFitness = (double)((width * height * 0xFF) - totalF);
        //mCurFitness = (double)(totalF) / (width * height * 0xFF);
        mCurCoverage = totalC;
    }
    
    public static double GetFitness()
    {
        return mCurFitness;
    }
    
    public static long GetCoverage()
    {
        return mCurCoverage;
    }
}
