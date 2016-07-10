package br.com.nees.captcha;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class KB {

	public static KB instance;

	private final String BASE_DIR = "KB/";

	private final String[] SUB_DIRS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

	private KB() {
		for (String dir : SUB_DIRS) {
			new File(BASE_DIR + dir + "/").mkdirs();
		}
	}

	public static KB getInstance() {
		if (instance == null)
			instance = new KB();
		return instance;
	}

	public List<String> getSubDirs() {

		List<String> out = new ArrayList<>();
		for (String dir : SUB_DIRS) {
			out.add(BASE_DIR + dir + "/");
		}

		return out;
	}

	public BufferedImage getCaptcha() {
		BufferedImage captcha = null;
		try {
			captcha = ImageIO
					.read(new URL("http://buscatextual.cnpq.br/buscatextual/servlet/captcha?metodo=getImagemCaptcha"));
		} catch (Exception e) {

			e.printStackTrace();
		}

		return captcha;
	}

	public void showGui() {

		BufferedImage image = getCaptcha();
		JFrame frame = new JFrame();

		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setIconImage(getCaptcha());

		JTextField input = new JTextField(20);
		JButton okButton = new JButton("Ok");
		JButton skipButton = new JButton("Skip");
		JButton finishButton = new JButton("Finish");

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = input.getText();

				System.out.println(text);

				storeCaptcha(image, text.toUpperCase());

				frame.dispose();

				showGui();
			}
		});

		skipButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				showGui();
			}
		});

		finishButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

		frame.add(input);
		frame.add(okButton);
		frame.add(skipButton);
		frame.add(finishButton);

		frame.setSize(500, 200);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void storeCaptcha(BufferedImage image, String text) {

		List<BufferedImage> imgs = ImagePreProcessor.getInstance().splitImage(image);

		for (int i = 0; i < 4; i++) {
			char letter = text.charAt(i);

			String fileName = BASE_DIR + letter + "/" + new File(BASE_DIR + text.charAt(i)).listFiles().length;

			ImagePreProcessor.getInstance().writeImage(imgs.get(i), fileName);

			System.out.println("Saving " + text.charAt(i) + " to: " + fileName);
		}

	}

}
