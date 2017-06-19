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
	int[] pix;
	int runs = 1;
	double[] RGBValues = new double[3];

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
		cam = new Camera(50, 50);
		cam.enableBurst();
		cam.setTimeout(1);

	}

	private boolean loop() throws Exception {
		readSensors(100);
		distance = getWallSignal();
		Thread picTake = new Thread(new Runnable() {

			@Override
			public void run() {
				cam.takeRGBPicture();
				System.out.println();
				RGBValues[0] = cam.getRedPercentage(15, false);
				RGBValues[1] = cam.getBluePercentage(15, false);
				RGBValues[2] = cam.getGreenPercentage(15, false);
				System.out.println(RGBValues[0] + " " + RGBValues[1] + " " + RGBValues[2]);
			}

		});
		picTake.start();

		if (isBumpRight()) {
			driveDirect(-500, -500);
			Thread.sleep(200);
			driveDirect(-500, 500);
			Thread.sleep(200);
		} else if (distance > 10) {
			driveDirect(125, 500);
		} else {
			driveDirect(500, 125);
		}
		// System.out.println("LEFT SONAR: " + sonar.readSonar("left"));
		// Thread.sleep(1000);
		// setTailLight(tailLight = !tailLight);
		// System.out.println("RIGHT SONAR: " + sonar.readSonar("right"));
		// System.out.println("CENTER SONAR: " + sonar.readSonar("center"));

		return true;
	}

	private void shutDown() throws IOException {
		reset();
		stop();
		closeConnection();
	}
}
