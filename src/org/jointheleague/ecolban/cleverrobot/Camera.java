package org.jointheleague.ecolban.cleverrobot;

import java.io.IOException;

import javax.swing.plaf.synth.SynthScrollBarUI;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

public class Camera extends RPiCamera {
	int[] pixels;
	int width;
	int height;

	/**
	 * 
	 * @param width
	 * @param height
	 * @throws FailedToRunRaspistillException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	Camera(int width, int height) throws FailedToRunRaspistillException, IOException, InterruptedException {
		super("/home/pi/Desktop");
		this.width = width;
		this.height = height;

	}

	/**
	 * this method takes a picture, using the RPi camera, as an array of RGB
	 * values
	 */
	public void takeRGBPicture() {
		try {
			pixels = this.takeStillAsRGB(width, height, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method returns the array of RGB values taken by the RPi camera
	 * 
	 * @return returns the array of RGB values
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * this method gets amount of red in the taken image (in percentage form)
	 * 
	 * @param redThreshold,
	 *            the value representing what is considered red
	 * @param debug,
	 *            whether or not the image prints out 1s and 0s based on if the
	 *            pixels are red or not (used for debug purposes)
	 * @return returns the number of red pixels divided by the total amount
	 *         pixels (as a percentage)
	 */
	public double getRedPercentage(int redThreshold, boolean debug) {
		// returns the percentage of red pixels / total pixels

		int numPixels = (pixels.length) / 3;
		boolean[] redPixels = new boolean[numPixels];
		for (int i = 0; i < pixels.length; i += 3) {
			int[] currentPixel = new int[3];
			for (int j = 0; j < 3; j++) {
				currentPixel[j] = pixels[i + j];
			}
			int red = currentPixel[0];
			int green = currentPixel[1];
			int blue = currentPixel[2];
			int biggest = 0;
			if (blue >= green) {
				biggest = blue;
			} else {
				biggest = green;
			}
			int actualRed = red - biggest;
			// this is where the threshold parameter comes in
			if (actualRed < redThreshold) {
				redPixels[i / 3] = false;
			} else {
				redPixels[i / 3] = true;
			}
		}
		int trueRedPixels = 0;
		for (boolean pixel : redPixels) {
			if (pixel) {
				trueRedPixels++;
			}
		}
		if (debug) {
			int count = 0;
			for (boolean pix : redPixels) {

				if (pix) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
				if (count % 100 == 0) {
					System.out.println();
				}
				count++;
			}
		}
		System.out.println();

		double redPercentage = (double) trueRedPixels / numPixels;
		return redPercentage * 100;
	}

	/**
	 * this method gets amount of blue in the taken image (in percentage form)
	 * 
	 * @param blueThreshold,
	 *            the value representing what is considered blue
	 * @param debug,
	 *            whether or not the image prints out 1s and 0s based on if the
	 *            pixels are blue or not (used for debug purposes)
	 * @return returns the number of blue pixels divided by the total amount
	 *         pixels (as a percentage)
	 */
	public double getBluePercentage(int blueThreshold, boolean debug) {
		// returns the percentage of blue pixels / total pixels
		int numPixels = (pixels.length) / 3;
		boolean[] bluePixels = new boolean[numPixels];
		for (int i = 0; i < pixels.length; i += 3) {
			int[] currentPixel = new int[3];
			for (int j = 0; j < 3; j++) {
				currentPixel[j] = pixels[i + j];
			}
			int red = currentPixel[0];
			int green = currentPixel[1];
			int blue = currentPixel[2];
			int biggest = 0;
			if (red >= green) {
				biggest = red;
			} else {
				biggest = green;
			}
			int actualBlue = blue - biggest;
			// this is where the threshold parameter comes in
			if (actualBlue < blueThreshold) {
				bluePixels[i / 3] = false;
			} else {
				bluePixels[i / 3] = true;
			}
		}
		int trueBluePixels = 0;
		for (boolean pixel : bluePixels) {

			if (pixel) {
				trueBluePixels++;
			}
		}
		if (debug) {
			int count = 0;
			for (boolean pix : bluePixels) {
				if (count % 100 == 0) {
					System.out.println();
				}
				if (pix) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
				count++;
			}
		}
		System.out.println();
		double bluePercentage = (double) trueBluePixels / numPixels;
		return bluePercentage * 100;
	}

	/**
	 * this method gets amount of green in the taken image (in percentage form)
	 * 
	 * @param greenThreshold,
	 *            the value representing what is considered green
	 * @param debug,
	 *            whether or not the image prints out 1s and 0s based on if the
	 *            pixels are green or not (used for debug purposes)
	 * @return returns the number of green pixels divided by the total amount
	 *         pixels (as a percentage)
	 */
	public double getGreenPercentage(int greenThreshold, boolean debug) {

		int numPixels = (pixels.length) / 3;
		boolean[] greenPixels = new boolean[numPixels];
		for (int i = 0; i < pixels.length; i += 3) {
			int[] currentPixel = new int[3];
			for (int j = 0; j < 3; j++) {
				currentPixel[j] = pixels[i + j];
			}
			int red = currentPixel[0];
			int green = currentPixel[1];
			int blue = currentPixel[2];
			int biggest = 0;
			if (blue >= red) {
				biggest = blue;
			} else {
				biggest = red;
			}
			int actualGreen = green - biggest;
			// this is where the threshold parameter comes in
			if (actualGreen < greenThreshold) {
				greenPixels[i / 3] = false;
			} else {
				greenPixels[i / 3] = true;
			}
		}
		int trueGreenPixels = 0;

		for (boolean pixel : greenPixels) {

			if (pixel) {
				trueGreenPixels++;
			}

		}
		if (debug) {
			int count = 0;
			for (boolean pix : greenPixels) {
				if (count % 100 == 0) {
					System.out.println();
				}
				if (pix) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
				count++;
			}
		}
		System.out.println();
		double greenPercentage = (double) trueGreenPixels / numPixels;
		return greenPercentage * 100;
	}
}
