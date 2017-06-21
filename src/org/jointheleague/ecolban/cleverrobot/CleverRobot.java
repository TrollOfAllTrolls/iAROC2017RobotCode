package org.jointheleague.ecolban.cleverrobot;

/*********************************************************************************************
 * Vic's ultrasonic sensor running with Erik's Clever Robot for Pi
 * version 0.9, 170227
 **********************************************************************************************/
import java.io.IOException;

import org.jointheleague.ecolban.rpirobot.IRobotAdapter;
import org.jointheleague.ecolban.rpirobot.IRobotInterface;
import org.jointheleague.ecolban.rpirobot.SimpleIRobot;

public class CleverRobot extends IRobotAdapter {
	Sonar sonar = new Sonar();
	private boolean tailLight;
	Camera cam;
	int distance;
	int sonarDist = 150;
	int[] pix;
	int camRuns = 0;
	double[] RGBValues = new double[2];
	// runType 0 = Maze || runType 1 = DragRace || runType 2 = GoldRush
	int runType = 0;
	int hasCam = 1;

	public CleverRobot(IRobotInterface iRobot) {
		super(iRobot);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Try event listner, rev Monday 2030");
		IRobotInterface base = new SimpleIRobot();
		CleverRobot rob = new CleverRobot(base);
		rob.setup();
		while (rob.loop()) {
		}
		rob.shutDown();

	}

	private void setup() throws Exception {
		if (runType == 0 && hasCam == 1) {
			// Maze Code
			cam = new Camera(50, 50);
			cam.enableBurst();
			cam.setTimeout(200);
			Thread picTake = new Thread(new Runnable() {
				public void run() {
					while (true) {
						camRuns++;
						cam.takeRGBPicture();
						System.out.println("Picture " + camRuns);
						RGBValues[0] = cam.getRedPercentage(35, false);
						RGBValues[1] = cam.getGreenPercentage(10, false);
						System.out.println();
						System.out.println("Red Percent: " + RGBValues[0]);
						System.out.println("Green Percent: " + RGBValues[1]);
						System.out.println(" ");

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			});

			picTake.start();
		}
	}

	private boolean loop() throws Exception {
		if (runType == 0) {
			// Maze Code
			readSensors(100);
			distance = getWallSignal();

			if (isBumpRight() || isBumpLeft()) {

				driveDirect(-500, -500);
				Thread.sleep(200);
				driveDirect(-500, 500);
				Thread.sleep(300);
			} else if (distance > 10) {
				driveDirect(125, 500);
			} else {
				driveDirect(500, 125);
			}
		} else if (runType == 1) {
			// DragRace Code
			readSensors(100);
			distance = getWallSignal();

			driveDirect(500, 500);
			if (isBumpRight() || isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(350);
				driveDirect(-500, 500);
				Thread.sleep(400);
			} else if (distance > 2) {
				driveDirect(320, 500);
			} else {
				driveDirect(500, 450);
			}
		} else if (runType == 2) {
			// GoldRush Code

		}

		return true;
	}

	private void shutDown() throws IOException {
		reset();
		stop();
		closeConnection();
	}
}
