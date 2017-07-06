import java.io.*;
import java.awt.*;
import java.util.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import javaxt.io.Image;

public class Collager extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// http://www.javaxt.com/documentation?jar=javaxt-core&package=javaxt.io&class=Image
	//link to Image lib
	private int panelWidth;
	private int panelHeight;
	// empty constructor
	public Collager() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout l = new FlowLayout();
		l.setVgap(0);
		l.setHgap(0);
		ImagePanel pp = new ImagePanel();
		getContentPane().add(pp); // set panel on frame
		setSize(panelWidth, panelHeight);// Set the size of the frame
		setVisible(true); // Show the frame
	}

	// single variable constructor
	public Collager(String s) {
		//System.out.println(s);
	}

	// method
	public void method() {
		//System.out.println("method");
	}

	// Panel
	public class ImagePanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
		}
		private BufferedImage image;

		public ImagePanel() throws MalformedURLException  {

			// keeps place of the pixel from the og image so the mini image
			// knows where to start
			
			final int ratioNumber = 48;
			setLayout(new GridLayout(ratioNumber, ratioNumber));
			JOptionPane.showMessageDialog(null, "Welcome to Googllage. Googllage allows you to create collage/mosaic of photos\nusing pictures from the internet!\nTo use, choose a photo file, then enter a search term.\nA collage representation of the photo will be created using photos from the internet.");
			String picFile = JOptionPane.showInputDialog("Input a picture file name and extension please!");
			Image imgOG = new Image(picFile);
			final int miniImageHeight = imgOG.getHeight() / ratioNumber;
			final int miniImageWidth = imgOG.getWidth() / ratioNumber;
			panelWidth = imgOG.getWidth();
			panelHeight = imgOG.getHeight();

			// setSize(miniImageWidth*ratioNumber,miniImageHeight*ratioNumber);
			Image imageFinal = new Image(imgOG.getWidth(), imgOG.getHeight());

			// convert google pic to size needed
			
			System.out.println("Reading urls");
			ArrayList<String> urls;
			urls = URLReaderUtil.getURLs();
			ArrayList<BufferedImage> aolImgs = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> thatIwannaUse = new ArrayList<BufferedImage>();
			
			int count = 0;
			for(String url: urls){
				try {
					BufferedImage i = ImageIO.read(new URL(url));
					aolImgs.add(i);
					count++;
					if(count > 25)break;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(url+" was read!");
			}
			System.out.println("URLS DONE REDING");
			
			for(BufferedImage im : aolImgs){
				if(new Image(im).getBufferedImage() != null){
					Image aol = new Image(im);
					BufferedImage aolBImg = aol.getBufferedImage(miniImageWidth, miniImageHeight, false);
					thatIwannaUse.add(aolBImg);
				}
			}
			System.out.println(thatIwannaUse.toString());
			
			//Image googlePic = new Image(googlePicBuffer);
			
			int r = 0, g = 0, b = 0;
			Color[][] colorArray = new Color[ratioNumber][ratioNumber];
			// two for loops for 32x32 image made up of smaller images

			for (int i = 0; i < ratioNumber; i++) {
				for (int e = 0; e < ratioNumber; e++) {
					// reset color values to 0 for new mini image
					r = 0;
					g = 0;
					b = 0;
					// double for loop to get color of each pixel in mini image
					for (int x = 0; x < miniImageWidth; x++)
						for (int y = 0; y < miniImageHeight; y++) {
							Color temp = imgOG.getColor(i*(miniImageWidth)+x, e*(miniImageHeight)+y);
							r += temp.getRed();
							g += temp.getGreen();
							b += temp.getBlue();
						}
					
					int totNumSMALLPic = miniImageHeight * miniImageWidth;
					colorArray[i][e] = new Color((r / totNumSMALLPic), (g / totNumSMALLPic),
							(b / totNumSMALLPic));
				}

			}

			for (int i = 0; i < ratioNumber; i++) {
				for (int e = 0; e < ratioNumber; e++) {
					
					BufferedImage aolBImg = thatIwannaUse.get((int)(Math.random()*thatIwannaUse.size()));
					BufferedImage tintBuffer = new BufferedImage(aolBImg.getWidth(),aolBImg.getHeight(),BufferedImage.TYPE_INT_ARGB);
				    Graphics2D gr = tintBuffer.createGraphics();
				    gr.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP, 0.5f));
				    gr.setColor(colorArray[e][i]);
				    gr.fillRect(0,0,aolBImg.getWidth(),aolBImg.getHeight());
				    gr.dispose();
					imageFinal.addImage(aolBImg, e * aolBImg.getWidth(), i * aolBImg.getHeight(), false);
					imageFinal.addImage(tintBuffer, e * aolBImg.getWidth(), i * aolBImg.getHeight(), false);
				}
			}
			image = imageFinal.getBufferedImage();
			String fileSaveName = JOptionPane.showInputDialog("Please enter a file name to save your mosaic to.");
			try{
			    File outputfile = new File(fileSaveName);
			    ImageIO.write(image, "png", outputfile);
			} catch (IOException e) {
			    e.printStackTrace();
			}

		}

		public ImagePanel(String filePath) {
			try {
				image = ImageIO.read(new File(filePath));
			} catch (IOException ex) {
				// handle exception...
			}
		}	
	}
	
	//change from default option pane colors
	public static void changeJOP()
	{
		UIManager.put("OptionPane.messageForeground",
				new Color(74,186,101));
		UIManager.put("TextField.background", 
				new Color(106, 79, 52));
		UIManager.put("TextField.font", new FontUIResource(new Font
				("Dialog", Font.ITALIC, 24)));
		UIManager.put("Button.font", new FontUIResource	(new Font
				("Tempus Sans ITC", Font.BOLD, 24)));
		UIManager.put("Label.font", new FontUIResource (new Font
				("Tempus Sans ITC", Font.BOLD, 28)));
		UIManager.put("TextField.foreground", Color.green);
		UIManager.put("Panel.background",new Color(46,112,62));
		UIManager.put("OptionPane.background",new Color(106,79,52));
		UIManager.put("Button.background",new Color(74,137,63));
		UIManager.put("Button.foreground", new Color(72,61,139));	
	}
	
	public static void main(String[] args) throws Exception {
		changeJOP();
		new Collager();
	}
}