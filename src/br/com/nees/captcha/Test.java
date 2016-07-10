package br.com.nees.captcha;

import java.awt.image.BufferedImage;

public class Test {
	public static void main(String[] args) {

		// ImagePreProcessor iProcessor = ImagePreProcessor.getInstance();
		//
		// BufferedImage img = iProcessor.readImage("captcha/captcha1.png");
		//
		// img = iProcessor.removeBackground(img);
		//
		// iProcessor.writeImage(img, "captcha/no_background.png");
		//
		// iProcessor.splitImage(img);
		// //
		// iProcessor.writeImage(iProcessor.markImage(img),
		// "captcha/marked.png");
		// iProcessor.writeImage(KB.getInstance().getCaptcha(),
		// "captcha/downloaded.png");

		// KB.getInstance().showGui();

		// System.out.println(Solver.getInstance().getSimilarity(iProcessor.readImage("captcha/l4.png"),
		// iProcessor.readImage("captcha/l4.png")));

		// Solver sol = Solver.getInstance();
		// System.out.println(sol.solve(img));

		new Test().download();
	}

	private void download() {

		int total = 0;
		String result = "";
		for (int i = 0; i < 100; i++) {

			BufferedImage img = KB.getInstance().getCaptcha();
		//	ImagePreProcessor.getInstance().writeImage(img, "captcha/" + i + "");
			result = Solver.getInstance().solve(img);
			System.out.println(i + "-->" + result);

			if (result.length() == 4)
				total++;
		}
		System.out.println(total);

	}
}
