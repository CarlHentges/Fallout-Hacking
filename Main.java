import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

public class Main implements Runnable {

	private boolean running;
	public static final int WIDTH = 500, HEIGHT = 500;
	private Random rand = new Random();
	private Thread thread;
	private static Canvas canvas;	
	private int attempts = 3;

	public Main() {
		canvas = new Canvas();
		canvas.setSize(WIDTH, HEIGHT);
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
			g.fillRect(230 + (i * 20), 88, 12, 14);
		}	
	
		g.dispose();
		bs.show();
	}
}

