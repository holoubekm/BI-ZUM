/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zum.sem;

import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 *
 * @author holoubekm
 */
public class ZUMSem 
{
    public static void main(String[] args) throws IOException 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                Window mGui = new Window();
                mGui.setVisible(true);
            }
       });
        
        //ImageHandler.GetInstance("text.jpg");
        //Individual in = new Individual(100);
        
    }
}
