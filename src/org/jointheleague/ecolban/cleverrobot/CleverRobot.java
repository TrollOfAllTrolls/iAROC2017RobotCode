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
	int sideDist;
	int sonarDist = 150;
	int[] pix;
	int camRuns = 0;
	double redPercent;
	// runType 0 = Maze || runType 1 = DragRace || runType 2 = GoldRush
	int runType = 1;

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
		if (runType == 0) {
			// Maze Code
			cam = new Camera(150, 50);
			cam.enableBurst();
			cam.setTimeout(250);
			Thread picTake = new Thread(new Runnable() {
				public void run() {
					while (true) {
						camRuns++;
						cam.takeRGBPicture();
						System.out.println("Picture " + camRuns);
						redPercent = cam.getRedPercentage(80, false);
						System.out.println();
						System.out.println("Red Percent: " + redPercent);
						System.out.println(" ");

					}
				}

			});

			picTake.start();
		}
	}

	private boolean loop() throws Exception {
		System.out.println("");
		if (runType == 0) {
			// Maze Code
			readSensors(6);
			sideDist = getWallSignal();
			if (isBumpRight() || isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(100);
				driveDirect(-500, 500);
				Thread.sleep(325);
			} else if (redPercent > 8) {
				System.out.println("Found Red");
				driveDirect(-500, 500);
				Thread.sleep(750);
				driveDirect(400, 400);
				Thread.sleep(1000);
			} else if (sideDist > 6) {
				driveDirect(200, 500);
			} else {
				driveDirect(500, 110);
			}
		} else if (runType == 1) {
			// DragRace Code
			readSensors(100);
			sideDist = getWallSignal();

			driveDirect(500, 500);
			if (isBumpRight() || isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(350);
				driveDirect(-500, 500);
				Thread.sleep(400);
			} else if (sideDist > 2) {
				driveDirect(320, 500);
			} else {
				driveDirect(500, 450);
			}
		} else if (runType == 2) {
			// GoldRush Code
			````
		}

		return true;
	}

	private void shutDown() throws IOException {
		reset();
		stop();
		closeConnection();
	}
}
