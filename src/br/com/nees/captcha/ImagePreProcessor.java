package br.com.nees.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImagePreProcessor {

	public static ImagePreProcessor instance;

	private ImagePreProcessor() {

	}

	public static ImagePreProcessor getInstance() {
		if (instance == null)
			instance = new ImagePreProcessor();
		return instance;
	}

	public BufferedImage readImage(String name) {

		BufferedImage img = null;

		try {
			img = ImageIO.read(new File(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public void writeImage(BufferedImage image, String name) {
		try {
			ImageIO.write(image, "PNG", new File(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage removeBackground(BufferedImage image) {

		// crop image
		image = image.getSubimage(32, 0, 110, image.getHeight());
		int width = image.getWidth();
		int height = image.getHeight();

		try {

			Boolean isWhite;

			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					Color c = new Color(image.getRGB(j, i));

					int red = (int) c.getRed();
					int green = (int) c.getGreen();
					int blue = (int) c.getBlue();

					isWhite = ((red + green + blue) > 650) ? true : false;

					Color newColor;

					newColor = isWhite ? new Color(0, 0, 0) : new Color(255, 255, 255);

					image.setRGB(j, i, newColor.getRGB());
				}
			}

		} catch (Exception e) {
		}

		return cropSides(image);
		// return image;

	}

	public BufferedImage drawLine(BufferedImage image, int x) {

		// red color
		Color lineColor = new Color(255, 0, 0);

		// draws a vertical line
		for (int y = 0; y < image.getHeight(); y++)
			image.setRGB(x, y, lineColor.getRGB());

		return image;
	}

	public int findCropPoint(BufferedImage image, int guess) {

		// letter height
		int h = image.getHeight();

		// transition area
		int range = 10;

		// number of black pixels in a horizontal line
		int position = guess;

		// Line with the minimum number of pixels
		int minK = Integer.MAX_VALUE;

		for (int x = (guess - range); x < (guess + range); x++) {

			int total = 0;

			for (int y = 0; y < h; y++) {
				Color c = new Color(image.getRGB(x, y));

				int red = (int) c.getRed();
				int green = (int) c.getGreen();
				int blue = (int) c.getBlue();

				if (red < 10 && green < 10 && blue < 10)
					total++;
			}

			if (total < minK) {
				minK = total;
				position = x;
			}
		}

		return position;
	}

	public List<BufferedImage> splitImage(BufferedImage input) {
		List<BufferedImage> imgs = new ArrayList<>();
		BufferedImage temp;
		
		input = removeBackground(input);

		// Letter dimension
		int width = input.getWidth() / 4;
		int height = input.getHeight();
		int cropX;

		// First letter
		cropX = findCropPoint(input, width);
		temp = input.getSubimage(0, 0, cropX, height);
		temp = cropSides(temp);
		imgs.add(temp);
		// writeImage(output, BASE_DIR + "l1.png");

		// Second letter
		cropX = findCropPoint(input, 2 * width);
		temp = input.getSubimage(cropX - width, 0, width, height);
		temp = cropSides(temp);
		imgs.add(temp);
		// writeImage(output, BASE_DIR + "l2.png");

		// Third letter
		cropX = findCropPoint(input, 3 * width);
		temp = input.getSubimage(cropX - width, 0, width, height);
		temp = cropSides(temp);
		imgs.add(temp);
		// writeImage(output, BASE_DIR + "l3.png");

		// Fourth letter
		temp = input.getSubimage(cropX, 0, input.getWidth() - cropX, height);
		temp = cropSides(temp);
		imgs.add(temp);
		// writeImage(output, BASE_DIR + "l4.png");

		return imgs;
	}

	public BufferedImage markImage(BufferedImage image) {
		int x1 = findCropPoint(image, 26);
		int x2 = findCropPoint(image, 52);
		int x3 = findCropPoint(image, 78);

		return drawLine(drawLine(drawLine(image, x1), x2), x3);
	}

	public BufferedImage cropSides(BufferedImage image) {

		// image dimension
		int h = image.getHeight();
		int w = image.getWidth();

		// left crop point
		int startX = 0;

		loop1: for (int x = 0; x < w; x++) {
			startX = x;
			for (int y = 0; y < h; y++) {

				Color c = new Color(image.getRGB(x, y));

				int red = (int) c.getRed();
				int green = (int) c.getGreen();
				int blue = (int) c.getBlue();

				if ((red + green + blue) < 3) {
					break loop1;
				}
			}
		}

		// right crop point
		int endX = w;

		loop2: for (int x = w - 1; x > 0; x--) {
			endX = x;
			for (int y = 0; y < h; y++) {
				Color c = new Color(image.getRGB(x, y));

				int red = (int) c.getRed();
				int green = (int) c.getGreen();
				int blue = (int) c.getBlue();

				if ((red + green + blue) < 3) {
					break loop2;
				}
			}

		}

		if (endX > startX)
			image = image.getSubimage(startX, 0, endX - startX, h);

		return image;
	}
}
