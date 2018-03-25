/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zum.sem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import zum.sem.Evolution.EvolutionListener;

/**
 *
 * @author Martin
 */
public class Window extends JFrame implements ActionListener, EvolutionListener, ChangeListener
{
    private final JButton mStarter;
    private final JButton mImageSelector;
    private final JLabel mCurFitness;
    private final JLabel mCurGen;
    private final JSlider mThreshold;
    private final JLabel mLabThreshold;
    private final JSlider mCount;
    private final JLabel mLabCount;
    private final JSlider mRectCount;
    private final JLabel mLabRect;
    private final JImage mImage;
    private final JRects mRects;
    private Evolution mEvolution;
    private final int mWidth = 480;
    private final int mHeight = 600;
    private String mFilename = "";
    
    public Window()
    {
        super();
        setSize(mWidth, mHeight);
        setResizable(false);
        setDefaultLookAndFeelDecorated(true);
        setUndecorated(false);
        setTitle("Paragraph Detector");

        getContentPane().setLayout(null);
        
        mImageSelector = new JButton("Load");
        mImageSelector.setSize(70, 30);
        mImageSelector.setLocation(10, 10);
        mImageSelector.addActionListener(this);
        getContentPane().add(mImageSelector);
        
        mStarter = new JButton("Start");
        mStarter.setSize(70, 30);
        mStarter.setLocation(90, 10);
        mStarter.addActionListener(this);
        mStarter.setEnabled(false);
        getContentPane().add(mStarter, 0);
        
        mThreshold = new JSlider(200, 700, 600);
        mThreshold.setSnapToTicks(true);
        mThreshold.setSize(100, 20);
        mThreshold.setLocation(170, 10);
        mThreshold.addChangeListener(this);
        getContentPane().add(mThreshold, 0);
        
        mLabThreshold = new JLabel("Thres: 600");
        mLabThreshold.setSize(100, 10);
        mLabThreshold.setLocation(170, 30);
        mLabThreshold.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(mLabThreshold, 0);
        
        mCount = new JSlider(20, 50, 30);
        mCount.setSnapToTicks(true);
        mCount.setSize(100, 20);
        mCount.setLocation(270, 10);
        mCount.addChangeListener(this);
        getContentPane().add(mCount, 0);
        
        mLabCount = new JLabel("Gen: 30");
        mLabCount.setSize(100, 10);
        mLabCount.setLocation(270, 30);
        mLabCount.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(mLabCount, 0);
        
        mRectCount = new JSlider(7, 17, 11);
        mRectCount.setSnapToTicks(true);
        mRectCount.setSize(100, 20);
        mRectCount.setLocation(370, 10);
        mRectCount.addChangeListener(this);
        getContentPane().add(mRectCount, 0);
        
        mLabRect = new JLabel("Rects: 8 - 14");
        mLabRect.setSize(100, 10);
        mLabRect.setLocation(370, 30);
        mLabRect.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(mLabRect, 0);
        
        mCurGen = new JLabel("Generation: 0");
        mCurGen.setSize(100, 10);
        mCurGen.setLocation(10, mHeight - 50);
        mCurGen.setHorizontalAlignment(JLabel.LEFT);
        getContentPane().add(mCurGen, 0);
        
        mCurFitness = new JLabel("Best: 0.000");
        mCurFitness.setSize(100, 10);
        mCurFitness.setLocation(120, mHeight - 50);
        mCurFitness.setHorizontalAlignment(JLabel.LEFT);
        getContentPane().add(mCurFitness, 0);
        
        
        mRects = new JRects(mWidth - 30, mHeight - 110);
        getContentPane().add(mRects, 1);
        
        mImage = new JImage(mWidth - 30, mHeight - 110);
        getContentPane().add(mImage, 2);
        
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                if(mEvolution != null)
                {
                    mEvolution.Stop();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    { 
        if(ae.getSource() == mStarter)
        {
            if(mEvolution == null)
            {
                Individuum.setCount(mRectCount.getValue());
                ImageHandler.Renew(mFilename, mWidth - 30, mHeight - 110, mThreshold.getValue());
                mEvolution = new Evolution(this);
                mEvolution.Start();
            }
            else
            {
                mEvolution.Stop();
            }
        }
        else if(ae.getSource() == mImageSelector)
        {
            JFileChooser chooser = new JFileChooser(mFilename);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new ImageFilter());
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                mFilename = chooser.getSelectedFile().getAbsolutePath();
                mStarter.setEnabled(true);
                mImage.SetImage(mFilename);
                mImage.repaint();
            }
        }
    }
    @Override
    public void StepReached(Evolution ev)
    {
        System.out.println(ev.mLoopCount + ": Current maxFit = " + ev.mBestFit);
        
        mCurGen.setText("Generation: " + ev.mLoopCount);
        mCurFitness.setText("Best: " + (Math.round(ev.mBestFit * 1000) / 1000.0));
        mRects.setSquares(ev.GetRectangles());
        repaint();
    }

    @Override
    public void ActionStarted()
    {
        mStarter.setText("Stop");
        mImageSelector.setEnabled(false);
    }

    @Override
    public void ActionFinished()
    {
        System.out.println("Finished");
        mStarter.setText("Start");
        mImageSelector.setEnabled(true);
        mEvolution = null;
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        Object source = e.getSource();
        if(source == mThreshold)
        {
            mLabThreshold.setText("Thres: " + mThreshold.getValue());
        }
        else if(source == mCount)
        {
            mLabCount.setText("Gen: " + mCount.getValue());
        }
        else if(source == mRectCount)
        {
            mLabRect.setText("Rects: " + (mRectCount.getValue() - 3) + " - " + (mRectCount.getValue() + 3));
        }
    }
}
