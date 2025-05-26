package view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class ImgLibrary {
    protected static ResourceBundle resources;
    static{
        try{
            resources = ResourceBundle.getBundle("lib.Source",Locale.getDefault());
        }catch(Exception e){
            System.out.println("Image library source not found !");
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Library source not found !",
                    "Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    public String getResourceString(String key){
        String str;
        try{
            str = resources.getString(key);
        }catch(Exception e){
            str = null;
        }
        return str;
    }
    protected URL getResource(String key){
        String name = getResourceString(key);
        if(name != null){
            URL url = this.getClass().getResource(name);
            return url;
        }
        return null;
    }
}
