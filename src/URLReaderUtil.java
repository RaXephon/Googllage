
import java.net.*;
import java.util.ArrayList;
import java.io.*;

import javax.swing.JOptionPane;

public class URLReaderUtil {
    public static ArrayList<String> getURLs() throws MalformedURLException  {
    		ArrayList<String> picURLs = new ArrayList<String>();
    		String serch = JOptionPane.showInputDialog("Input a search term!");
    		new Thread(new Runnable(){
			    public void run() {
			        JOptionPane.showMessageDialog(null, "Please wait as your Mosaic is generated. (this can take a minute or two)");
			    }
			}).start();
    		serch = serch.replaceAll(" ", "%20");
    		URL url = new URL("http://search.aol.com/aol/image?s_it=sb-home&v_t=na&q="+serch);
            BufferedReader in;
            try {
            	in = new BufferedReader(new InputStreamReader(url.openStream()));
            	String inputLine;
				while ((inputLine = in.readLine()) != null)
					if(inputLine.contains("data-imgdetail-content="))picURLs.add(inputLine);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(picURLs.size());
            for(int i = 0; i < picURLs.size(); i++){
            	picURLs.set(i, picURLs.get(i).replace("data-imgdetail-content=\"", ""));
            	picURLs.set(i, picURLs.get(i).replace("\"", ""));
            }
            for(String pic: picURLs){
            	System.out.println(pic);
            }
			return picURLs;
    }
}
