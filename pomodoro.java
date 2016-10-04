/*
 * @author Heng-Yu Pan
 * Pomodoro timer program for time management
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class pomodoro {

	private JFrame mainFrame;
	private JButton start;
	private ImageIcon img;
	private JLabel tomatoTimer;
	private int starting = 25;
	private JTextArea text;
	private JButton stop;
	private boolean stopClicked = false;
	private int intervals = 0;
	private boolean increaseInterval = true;
	private boolean breakTime = false;
	private JLabel numIntervals;

	public pomodoro() throws InterruptedException {
		buildGui();
	}

	public static void main(String[] args) throws InterruptedException {
		new pomodoro();
	}

	private void buildGui() throws InterruptedException {
		mainFrame = new JFrame("Pomodoro Time Management Timer");
		mainFrame.setSize(500, 300);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.getContentPane().setBackground(Color.gray);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		JPanel top = new JPanel();

		start = new JButton("Start Timer");
		top.add(start);
		stop = new JButton("Stop Timer");
		top.add(stop);
		img = new ImageIcon(this.getClass().getResource("Tomato.jpg"));

		Image image = img.getImage();
		Image newimg = image.getScaledInstance(150, 150,
				java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newimg);

		tomatoTimer = new JLabel(img);

		top.add(numIntervals = new JLabel("Number of Intervals: " + intervals));

		mainFrame.add(top);
		mainFrame.add(tomatoTimer);
		text = new JTextArea(1, 3);
		text.setEditable(false);
		Font font = new Font("Arial", Font.BOLD, 42);
		text.setFont(font);
		mainFrame.add(text);

		class startStop implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == start) {
					final Timer timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						int currTime = starting * 60;

						public void run() {
							int currMinutes = currTime / 60;
							int currSeconds = currTime % 60;
							String clock = String.format("%02d:%02d",
									currMinutes, currSeconds);
							text.setText(clock);
							text.update(text.getGraphics());
							currTime--;
							if (stopClicked == true) {
								timer.cancel();
								stopClicked = false;
							}
							// timer is 00:00
							if (currMinutes == 0 && currSeconds < 1) {
								timer.cancel();
								if (increaseInterval && !breakTime) { // 25
																		// minute
																		// interval
									numIntervals
											.setText("Number of Intervals: "
													+ (++intervals));
									breakTime = true;
									increaseInterval = false;
									if (intervals == 4) {
										starting = 15;
									} else {
										text.setForeground(Color.RED);
										starting = 5;
									}
								} else { // on a break
									text.setForeground(Color.BLACK);
									starting = 25;
									breakTime = false;
									increaseInterval = true;
								}
							}
						}
					}, 0, 1000);
				} else if (e.getSource() == stop) {
					stopClicked = true;
				}
			}
		}
		start.addActionListener(new startStop());
		stop.addActionListener(new startStop());
		mainFrame.setVisible(true);
	}
}
