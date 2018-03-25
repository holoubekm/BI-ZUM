/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zum.sem;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author holoubekm
 */

class Utils 
{
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    public static String getExtension(File file) 
    {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) 
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}

class ImageFilter extends FileFilter
{
    public String getTypeDescription(File f) {
    String extension = Utils.getExtension(f);
    String type = null;

    if (extension != null) {
        switch (extension)
        {
            case Utils.jpeg:
            case Utils.jpg:
                type = "JPEG Image";
                break;
            case Utils.gif:
                type = "GIF Image";
                break;
            case Utils.tiff:
            case Utils.tif:
                type = "TIFF Image";
                break;
            case Utils.png:
                type = "PNG Image";
                break;
        }
    }
    return type;
} 

    @Override
    public boolean accept(File f)
    {
        if(f.isDirectory()) return true;
        
        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif) ||
                    extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg) ||
                    extension.equals(Utils.png);
        }
        return false;
    }

    @Override
    public String getDescription()
    {
        return "Just images";
    }
}
