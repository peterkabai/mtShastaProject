import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JProgressBar;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.*;

public class Classifier {
	
	// global stuff
	public static int currentImage = 0;
	public static ArrayList<File> allImages = new ArrayList<File>();
	public static JLabel image = new JLabel();
	public static JProgressBar progressBar = new JProgressBar();
	public static int goal;
	public static MyJPanel panel = new MyJPanel();
	public static String imagePath;
	public static String group1;
	public static String group2;
	public static Boolean sorting = false;
	
	// panel to actually sort the images
	public static void imageSortPanel(JFrame frame, String dir, int spinnerVal, String groupOne, String groupTwo) {
		
		// checks to see if the entire form has been filled out
		if (dir.equals("No directory selected yet...") || spinnerVal == 0 || groupOne.equals("") || groupTwo.equals("")) {
			JOptionPane.showMessageDialog(new JFrame(),"Please fill out the form to begin classifying","Error...",JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		// sets the goal to the spinner value sent from the info panel
		goal = spinnerVal;
		group1 = groupOne;
		group2 = groupTwo;
		
		// create sub directories
		File directory = new File(dir + "/" + groupOne);
		if (! directory.exists()){
			directory.mkdir();
		}
		directory = new File(dir + "/" + groupTwo);
		if (! directory.exists()){
			directory.mkdir();
		}
		
		// recreate panel
		sorting = true;
		panel.removeAll(); 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		
		// set what the close does
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// add the exit button
		constraints.gridx = 1;
		constraints.gridy = 2;
		JButton exit = new JButton("Exit");
		exit.setPreferredSize(new Dimension(100, 30));
		panel.add(exit, constraints);
			 
		// loading bar
		constraints.gridx = 1;
		constraints.gridy = 0;
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		panel.add(progressBar, constraints);
			 
		// gets a list of images for the goal
		allImages = getRandomFilesFromDir(goal, dir);
		
		// displays the image
		constraints.gridx = 1;
		constraints.gridy = 1;
		image = labelWithImage(300, 150);
		image.setPreferredSize(new Dimension(300, 150));
		panel.add(image, constraints);
		
		// displays category 1 button
		constraints.gridx = 0;
		constraints.gridy = 1;
		JButton one = new JButton(groupOne);
		one.setPreferredSize(new Dimension(100, 30));
		panel.add(one, constraints);
		
		// displays category 2 button
		constraints.gridx = 2;
		constraints.gridy = 1;
		JButton two = new JButton(groupTwo);
		two.setPreferredSize(new Dimension(100, 30));
		panel.add(two, constraints);
			  
		// add the panel to the frame
		frame.setContentPane(panel);
		
		// set frame size and position
		setPos(frame, 560, 230);
		
		// display the frame
		frame.setVisible(true);
		
		// listeners
		exit.addActionListener(ae -> exitApp(frame));
		one.addActionListener(ae -> oneSelected());
		two.addActionListener(ae -> twoSelected());
	}
	
    // set frame size and position
    private static void setPos(JFrame frameInput, int frameWidth, int frameHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        frameInput.setBounds((int) Math.round(width / 2 - frameWidth / 2), (int) Math.round(height / 2 - frameHeight / 2), frameWidth, frameHeight);
    }
		 
    // create main window
    public static void main(String[] a) {
		System.setProperty("apple.awt.application.name", "Image Classifier");
		getImageInfo();
	}
	
	// pick a directory with the images
	public static void pickAFile(JLabel dir) {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.showOpenDialog(null);
		File selectedFile = jfc.getSelectedFile();
		dir.setText(selectedFile.getPath()); 
	}
	
	// gather info such as the category names
	public static void getImageInfo() {
		
		// create stuff
		JFrame frame = new JFrame("Image Classifier");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
			
		// set what the close does
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// goal of how many photos to classify
		constraints.gridx = 0;
		constraints.gridy = 0;
		JSpinner spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(200, 30));
		panel.add(spinner, constraints);
		
		// name of group 1
		constraints.gridx = 0;
		constraints.gridy = 1;
		JTextField groupOne = new JTextField();
		groupOne.setPreferredSize(new Dimension(200, 30));
		panel.add(groupOne, constraints);
		
		// name of group 2
		constraints.gridx = 0;
		constraints.gridy = 2;
		JTextField groupTwo = new JTextField();
		groupTwo.setPreferredSize(new Dimension(200, 30));
		panel.add(groupTwo, constraints);
		
		// directory with images
		constraints.gridx = 0;
		constraints.gridy = 4;
		JLabel dir = new JLabel();
		dir.setText("No directory selected yet...");
		panel.add(dir, constraints);
		
		// directory with images
		constraints.gridx = 0;
		constraints.gridy = 3;
		JButton file = new JButton("Open Directory With Images");
		file.setPreferredSize(new Dimension(200, 30));
		file.setHorizontalAlignment(SwingConstants.CENTER);
		file.addActionListener(ae -> pickAFile(dir));
		panel.add(file, constraints);
		
		// continue button
		constraints.gridx = 0;
		constraints.gridy = 5;
		JButton cont = new JButton("Continue");
		cont.setPreferredSize(new Dimension(200, 30));
		panel.add(cont, constraints);
		
		// set frame size and position
		setPos(frame, 220, 200);
			 
		// add the panel to the frame
		frame.setContentPane(panel);
		
		// display the frame
		frame.setVisible(true);
			
		// listeners
		cont.addActionListener(ae -> imageSortPanel(frame, dir.getText(), (Integer)spinner.getValue(), groupOne.getText(), groupTwo.getText()));	
	}
	
	// exit the app
	public static void exitApp(JFrame frameInput) {
        frameInput.dispatchEvent(new WindowEvent(frameInput, WindowEvent.WINDOW_CLOSING));
    }
	
	// category one Selected
	public static void oneSelected() {
		moveImage(1);
		nextImage(image);
	}
	
	// category two Selected
	public static void twoSelected() {
		moveImage(2);
		nextImage(image);
	}
	
	// goes to next image if applicable 
	public static void nextImage(JLabel label) {
		currentImage++;
		if (currentImage < goal) {
			progressBar.setValue((int)(((double)currentImage/(double)goal)*(double)100));
			label.setIcon(labelWithImage(300, 150).getIcon());
		} else {
			progressBar.setValue((int)(((double)currentImage/(double)goal)*(double)100));
			reachedTheEnd();
		}
	}
	
	// returns a jLabel
	public static JLabel labelWithImage(int width, int height) {
		JLabel image = new JLabel();
		imagePath = allImages.get(currentImage).getPath();
		ImageIcon icon = new ImageIcon(imagePath);
		Image tempImage = icon.getImage();
		Image scaled = tempImage.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); 
		icon = new ImageIcon(scaled);
		image.setIcon(icon);
		return image;
	}
	
	// reached the end
	public static void reachedTheEnd() {
		JOptionPane.showMessageDialog(new JFrame(),"You've reached the end of the image set","Done!",JOptionPane.PLAIN_MESSAGE);
		System.exit(0);
	}
	
	// get a list of x images from the directory
	public static ArrayList<File> getRandomFilesFromDir(int goal, String dir) {
		
		// empty array of files
		ArrayList<File> allImages = new ArrayList<File>();
		
		// gets the folder from the input
		File folder = new File(dir);
		
		// gets all jpg files in the dir
		File [] files = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File folder, String name) {
		        return name.endsWith(".jpg");
		    }
		});
		
		// adds all the files to a list
		for (File file : files) {
		    allImages.add(file);
		}
		
		// sets various stuff
		int totalNumberOfImages = allImages.size();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		ArrayList<File> pickedImages = new ArrayList<File>();
		
		// While loop to get random indexes
		int i = 0;
		while (i < goal) {
			Random r = new Random();
			int newRandomNum = r.nextInt(totalNumberOfImages);
			if (!indexes.contains(newRandomNum)) {
				indexes.add(newRandomNum);
				i++;
			}
		}
		
		// gets the images with the random indexes and returns it
		for (int index : indexes) {
			pickedImages.add(allImages.get(index));
		}
		return pickedImages;
	}
	
	// custom JPanel for key presses
	static class MyJPanel extends JPanel implements KeyListener {
		
		public MyJPanel() {
			addKeyListener(this);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
		}
		
		public void keyTyped(KeyEvent e) { }

		public void keyReleased(KeyEvent e) { }
	
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT && sorting) {
				oneSelected();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && sorting) {
				twoSelected();
			}
		}
	}
	
	// move image 
	public static void moveImage(int category) {
		
		int index = imagePath.lastIndexOf('/');
		String dirPath = imagePath.substring(0, index);
		
		String moveTo;
		if (category == 1) { 
			moveTo = dirPath + "/" + group1 + imagePath.substring(index, imagePath.length());
		} else {
			moveTo = dirPath + "/" + group2 + imagePath.substring(index, imagePath.length());
		}
		
		try {
			Files.move(Paths.get(imagePath), Paths.get(moveTo));
		} catch(IOException e) { }
		
	}
	 
}