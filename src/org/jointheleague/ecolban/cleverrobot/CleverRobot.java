package org.jointheleague.ecolban.cleverrobot;

/*********************************************************************************************
 * Vic's ultrasonic sensor running with Erik's Clever Robot for Pi
 * version 0.9, 170227
 **********************************************************************************************/
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.jointheleague.ecolban.rpirobot.IRobotAdapter;
import org.jointheleague.ecolban.rpirobot.IRobotInterface;
import org.jointheleague.ecolban.rpirobot.SimpleIRobot;

public class CleverRobot extends IRobotAdapter {
	Sonar sonar = new Sonar();
	private boolean tailLight;
	Camera cam;
	int distance;
	int[] lights;
	int[] pix;
	int camRuns = 0;
	Random rand = new Random();
	int randomNumber;
	double redPercent = 0;
	boolean isInField = false;
	// runType 0 = Maze || runType 1 = DragRace || runType 2 = GoldRush ||
	// runType 3 = LeftMaze
	int runType = 0;
	int useFrontLight = 1;
	int useCam = 1;

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
		if (runType == 0 && useCam == 1 || runType == 3 && useCam == 1) {
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
		readSensors(100);
		distance = getWallSignal();
		lights = getLightBumps();
		System.out.println(getInfraredByte());
		if (runType == 0) {
			// Maze Code
			if (lights[2] + lights[3] > 2 && getInfraredByte() == 0 && useFrontLight == 1) {
				driveDirect(-500, 500);
				Thread.sleep(325);
			} else if (isBumpRight() || isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(100);
				driveDirect(-500, 500);
				Thread.sleep(325);
			} else if (redPercent > 8 && useCam == 1) {
				System.out.println("Found Red");
				driveDirect(-500, 500);
				Thread.sleep(750);
				driveDirect(400, 400);
				Thread.sleep(1000);
			} else if (distance > 6) {
				driveDirect(200, 500);
			} else {
				driveDirect(500, 110);
			}
		} else if (runType == 1) {
			// DragRace Code
			if (isBumpRight() || isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(350);
				driveDirect(-500, 500);
				Thread.sleep(400);
			} else if (lights[2] + lights[3] > 2) {
				driveDirect(-500, 500);
				Thread.sleep(400);
			} else if (distance > 2) {
				driveDirect(350, 500);
			} else {
				driveDirect(500, 450);
			}
		} else if (runType == 2) {
			// GoldRush Code
			randomNumber = rand.nextInt(300) + 200;
			if (getInfraredByte() != 0 || isInField == false) {
				if (isBumpLeft() || isBumpRight()) {
					isInField = true;
				} else if (getInfraredByte() == 244 || getInfraredByte() == 246) {
					driveDirect(500, 200);
				} else if (getInfraredByte() == 248 || getInfraredByte() == 250) {
					driveDirect(200, 500);
				} else {
					driveDirect(300, 300);
				}
			} else if (isBumpRight()) {
				driveDirect(-500, -500);
				Thread.sleep(150);
				driveDirect(-300, 300);
				Thread.sleep(randomNumber);
			} else if (isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(150);
				driveDirect(300, -300);
				Thread.sleep(randomNumber);
			} else {
				isInField = false;
				driveDirect(500, 500);
			}

		} else if (runType == 3) {
			// Left Maze Code
			if (lights[2] + lights[3] > 2 && getInfraredByte() == 0 && useFrontLight == 1) {
				driveDirect(500, -500);
				Thread.sleep(325);
			} else if (isBumpRight() || isBumpLeft()) {
				driveDirect(-500, -500);
				Thread.sleep(100);
				driveDirect(500, -500);
				Thread.sleep(325);
			} else if (redPercent > 8 && useCam == 1) {
				System.out.println("Found Red");
				driveDirect(500, -500);
				Thread.sleep(750);
				driveDirect(400, 400);
				Thread.sleep(1000);
			} else if (lights[0] > 2) {
				driveDirect(500, -400);
			} else {
				driveDirect(110, 500);
			}
		}

		return true;
	}

	private void shutDown() throws IOException {
		reset();
		stop();
		closeConnection();
	}
}