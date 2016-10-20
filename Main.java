import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

public class Main implements Runnable {

	private boolean running;
	public static final int WIDTH = 650, HEIGHT = 650;
	private Random rand = new Random();
	private Thread thread;
	private static Canvas canvas;	
	private int attempts = 3, addressStart = rand.nextInt(65152);
	private String word;
	private char[] text = new char[384];
	private HashMap<Integer, ArrayList<String>> sizedDict = new HashMap<Integer, ArrayList<String>>();

	public Main() {
		canvas = new Canvas();
		canvas.setSize(WIDTH, HEIGHT);

		File file = new File("dict.txt");
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(file));
       		while ((line = br.readLine()) != null) {
				String temp = line.replace("\n", "");
				if (sizedDict.get(temp.length()) == null) {
					sizedDict.put(temp.length(), new ArrayList<String>());
				}
				ArrayList<String> list = sizedDict.get(temp.length());
				list.add(temp);
			}
			br.close();
		} catch (IOException e) {
     		e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Main main = new Main();
		frame.setTitle("Fallout Hacking");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();

		main.start();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000 / 60;
		double delta = 0;
		while (running) {
			render();
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
		}
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		running = true;
		run();
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
	}

	public void generateWords() {
		int size = rand.nextInt(3) + 3;
		ArrayList<String> words = sizedDict.get(size);
		word = words.get(rand.nextInt(words.size()));

		for (int i = 0; i < )
	}
	
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		Font font = new Font("VirtualDJ", Font.PLAIN, 20);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.GREEN);
		g.setFont(font);
		
		g.drawString("Welcome to ROBCO Industries (TM) Termlink", 10, 30);
		g.drawString("Password Required", 10, 60);
		g.drawString("Attempts Remaining:", 10, 100);
		for (int i = 0; i < attempts; i++) {
			g.fillRect(200 + (i * 20), 88, 12, 14);
		}	
		
		for (int i = 0; i < 32; i++) {
			int xoff = (i < 16) ? 10 : 260, yoff = (i % 16) * 30 + 130;
			String temp = Integer.toHexString(addressStart + 12 * i).toUpperCase();
			for (int j = temp.length(); j < 4; j++)
				temp = "0" + temp;
			g.drawString("0x" + temp, xoff, yoff);
		}

		g.dispose();
		bs.show();
	}
}

