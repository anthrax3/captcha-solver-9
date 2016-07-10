package br.com.nees.captcha;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Solver {
	private static Solver instance;

	private Solver() {
	}

	public static Solver getInstance() {
		if (instance == null)
			instance = new Solver();
		return instance;
	}

	public String solve(BufferedImage captcha) {
		String text = "";

		try {
			for (BufferedImage img : ImagePreProcessor.getInstance().splitImage(captcha))
				loop: for (String dir : KB.getInstance().getSubDirs()) {
					for (File imgFile : new File(dir).listFiles())
						if (getSimilarity(img, ImageIO.read(imgFile)) > 0.9) {
							text += dir.replace("KB/", "").replace("/", "");
							break loop;
						}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return text;
	}

	public double getSimilarity(BufferedImage img1, BufferedImage img2) {

		int total = 0;

		int width = img1.getWidth() <= img2.getWidth() ? img1.getWidth() : img2.getWidth();
		int height = img1.getHeight() <= img2.getHeight() ? img1.getHeight() : img2.getHeight();

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (img1.getRGB(i, j) == img2.getRGB(i, j)) {
					total++;
				}
			}

		return (new Double(total) / (width * height));

	}
}
