package zum.sem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class JImage extends JPanel
{
    private final int mWidth;
    private final int mHeight;
    
    private Image mImage;
    public JImage(int width, int height)
    {
        mWidth = width;
        mHeight = height;
        
        setBounds(10, 50, 0, 0);
        setSize(new Dimension(width, height));
        setLayout(null);
    }

    public void SetImage(String name)
    {
        try
        {
            mImage = ImageIO.read(new File(name));
        }
        catch (IOException ex)
        {
            System.out.println("Can't load image, not found.");
        }
        mImage = mImage.getScaledInstance(mWidth, mHeight, SCALE_SMOOTH);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        if(mImage != null)
        {
            g.drawImage(mImage, 0, 0, null);
    
        }
    }    
}

class JRects extends JPanel
{
    private List<Rectangle> mRects;

    public JRects(int width, int height)
    {
        setBounds(10, 50, 0, 0);
        setSize(new Dimension(width, height));
        setLayout(null);
        
        mRects = new ArrayList<>();
    }
    public void setSquares(List<Rectangle> rects)
    {
        mRects = rects;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for (Rectangle rect : mRects)
        {
            g2.setColor(new Color(35, 25, 205, 30));
            g2.fill(rect);
            g2.setPaint(Color.gray);
            g2.draw(rect);
        }
    }
}
