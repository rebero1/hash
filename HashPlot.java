import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.*;


public class HashPlot extends JPanel {


	final int SPACING = 50;


	/**
	 *
	 * @param g G
	 */

	protected void paintComponent(Graphics g) {
		ArrayList<Integer> inputs= new ArrayList<>();
		ArrayList<Integer> outputs= new ArrayList<>();


		/***
		 * Reading data from a file and plot them
		 */
		try {

			BufferedReader br = new BufferedReader(new FileReader("input_sequence.txt"));
			BufferedReader br1 = new BufferedReader(new FileReader("a=50b=50m=130.txt"));

			String line;
			String line2;
			while ((line = br.readLine()) != null &&  (line2 = br1.readLine()) != null) {


				String[] input = line.split(","+"\\s");
				line2 = line2.substring(0,line2.length()-2);
				// System.out.println(line2);
				String[] output = line2.split(","+"\\s");
				if( input.length==output.length){
					System.err.println("Not SAME LENGTH");
				}

				for (int i = 0; i < input.length; i++) {
					inputs.add(Integer.parseInt(input[i]));
					outputs.add(Integer.parseInt(output[i]));
				}


			}





		} catch (IOException e){
			System.out.println("Error:"+e);
		}


		int[] input= new int[inputs.size()];
		int[] output= new int[outputs.size()];


		for(int i = 0; i < inputs.size(); i++) {
			if (inputs.get(i) != null && outputs.get(i)!=null) {
				input[i] = inputs.get(i);
				output[i]=outputs.get(i);
			}
		}


		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		g2.draw(new Line2D.Double(SPACING, SPACING, SPACING, h-SPACING));

		g2.draw(new Line2D.Double(SPACING, h-SPACING, w-SPACING, h-SPACING));

		// Mark data points.
		g2.setPaint(Color.black);
		for(int i = 0; i < input.length; i++) {
			double x = input[i];
			double y = output[i];
			System.out.println(x+" "+y);
			g2.fill(new Ellipse2D.Double((x-2)+SPACING, h-(y+10)-SPACING, 4, 4));
		}

		// setting label

		for (int i = 0; i < w+1; i+=w/10)
			g2.drawString(Integer.toString(i),i+40,h-15);

		Font font = new Font(null, Font.ITALIC, 10);
		g2.setFont(font);

		for (int i = 0; i < h+1; i+=h/10)
			g2.drawString(Integer.toString(i),25-(Integer.toString(i).toCharArray().length+3),h-(i+SPACING));



		g2.setColor(Color.black);
		Font font2 = new Font(null, Font.BOLD, 5);
		g2.setFont(font2);
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(-90), 0, 0);
		Font rotatedFont = font.deriveFont(affineTransform);
		g2.setFont(rotatedFont);
		g2.drawString("TESTING HASH FUNCTION",10,3*h/4);
		g2.dispose();

	}



	public static void main(String[] args) {

		HashedOutput.main(args);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new HashPlot());
		f.setTitle("Distribution");
		f.setSize(1200,120);
		f.setLocation(300,300);
		f.setFocusable(false);
		f.setVisible(true);
	}
}


class HashedOutput {

	//@modulus  and @hashfunction implements the hashfunction given in the question
	public static int modulus(int a, int m){

		return a%m;
	}


	public static int hashfunction(int a, int k, int b, int m){
		int num = (a*k)+b;
		return modulus(num,m);
	}

	public static void main(String[] args) {

		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		int m = Integer.parseInt(args[2]);
String out = "a=50b=50m=130.txt";
		HashTable<Integer,Integer> hTable = new HashTable <>(m);

		String str = args[3]+".txt";
		Integer[] keys =  readFile(str);


//create an output file for the keys as well as create a hash table
		try {
			File file = new File(out);
			FileWriter fw = new FileWriter(file,true);






			for (int i = 0; i < keys.length; i++) {


				int key = hashfunction(a,keys[i],b,m);
				hTable.add(key, keys[i]);       //creating the hashtable
				fw.append(key+", ");
			}

			fw.close();

		} catch (IOException E){}

		//use the readFile below to get values of k
		//then use hasfunction to calculate the keys of the hash function
		//then create a hash table with keys from the hashfunction , and value of k
	}

	public static Integer[] readFile(String str){
		LinkedList<Integer> linkedList = new LinkedList <>();
		String path =new File("").getAbsolutePath()+File.separator+str;

		try {

			BufferedReader br = new BufferedReader(new FileReader(path));
				String line;
				while ((line = br.readLine()) != null) {


					String[] s = line.split(","+"\\s");
					Arrays.stream(s).forEach(st -> linkedList.add(Integer.parseInt(st)));
					System.out.println(s.length);

				}

		} catch (IOException e){

		}
		return linkedList.toArray(new Integer[linkedList.size()]);
	}

}

class HNode<Key, E>
{
	Key key;
	E element;

	// Reference to next node
	HNode<Key, E> next;

	// Constructor
	public HNode(Key key, E element)
	{
		this.key = key;
		this.element = element;
	}
}


class HashTable<K, E> {
	// arrayList is used to store array of chains
	private ArrayList <HNode <K, E>> arrayList;

	// Current capacity of array list
	private int maxSize;



	// Current size of array list
	private int size;

	// Constructor ( capacity, size, a & b used in hash function and
	// empty chains.



	public HashTable(int maxSize) {
		arrayList = new ArrayList <>();
		this.maxSize = maxSize;

		size = 0;

		// Create empty chains
		for (int i = 0; i < maxSize; i++)
			arrayList.add(null);


	}



	// This implements a hash function to find index
	// for a key
	private int getIndex(K key) {

		return (key.hashCode())% maxSize;
	}


	// Adds a key--element pair to hash
	public void add(K key, E element) {

		int index = getIndex(key);



		HNode <K, E> front = arrayList.get(index);

		// Check if key is already present
		while (front != null) {
			if (front.key.equals(key)) {
				front.element = element;
				return;
			}
			front = front.next;
		}
		size++;
		// Insert key in chain

		front = arrayList.get(index);
		HNode <K, E> newNode = new HNode <K, E>(key, element);
		newNode.next = front;
		arrayList.set(index, newNode);

		// If load factor goes beyond threshold, then
		// double hash table size
		//1.0 * size converts to double
		if ((1.0 * size) / maxSize >= 0.75) {
			ArrayList <HNode <K, E>> temp = arrayList;
			maxSize = 2 * maxSize;
			arrayList = new ArrayList <>(maxSize);
			size = 0;
			for (int i = 0; i < maxSize; i++)
				arrayList.add(null);

			for (HNode <K, E> frontNode : temp) {
				while (frontNode != null) {
					add(frontNode.key, frontNode.element);
					frontNode = frontNode.next;
				}
			}
		}
	}
}

