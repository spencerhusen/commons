package commons;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.*;

import javax.imageio.ImageIO;

public class ImageResizer {

	public static final int ISSUE_DATA = 3;
	public static final int MAX_WIDTH = 160;
	public static final String BODY_FLAG = "author_association";
	public static final String REGEX_PATTERN = "(?<=: )\\w[^\\\\\"]*+";
	
	public static void main(String[] args) {
		File issues = new File("/Users/shusen/Desktop/CommonsIssues.txt");
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(issues);
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not read from specified file");
			e.printStackTrace();
		}
		String line;
		int issueCount = 0;
		boolean found = false;
		String[][] companyInfo = null;
		String bodyLine = null;
		int i = 0;
		while (fileReader.hasNextLine()) {
			line = fileReader.nextLine();
			if (line.contains("number") && !found) {
				line = line.trim();
				issueCount = Integer.parseInt(line.substring(10, 11));
				System.out.println(issueCount);
				companyInfo = new String[issueCount][ISSUE_DATA];
				found = true;
			}
			if (line.contains(BODY_FLAG)) {
				bodyLine = fileReader.nextLine().trim();
				Matcher m = Pattern.compile(REGEX_PATTERN).matcher(bodyLine);
				for (int j = 0; j < ISSUE_DATA; j++) {
					if (m.find()) {
						companyInfo[i][j] = m.group(0);
					}
				}
				i++;
			}
		}
		for (int j = 0; j < issueCount; j++) {
			for (int k = 0; k < ISSUE_DATA; k++) {
				System.out.println(companyInfo[j][k]);
			}
		}
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter("/Users/shusen/Desktop/TestOutput.txt"), true);
		} catch (IOException e) {
			System.out.println("Error: Could not write to specified file");
			e.printStackTrace();
		}
		for (int x = 0; x < issueCount; x++) {
			for (int y = 0; y < ISSUE_DATA; y++) {
				out.println(companyInfo[x][y]);
			}
		}
	}
	
	public static BufferedImage resizeImage(boolean path, String location) {
		BufferedImage logo = null;
		try {
			URL imageURL = new URL(location);
			try {
				logo = ImageIO.read(imageURL);
			} catch (IOException e) {
				System.out.println("Invalid URL");
				e.printStackTrace();
			} 
		} catch (MalformedURLException e) {
			System.out.println("Invalid URL");
			e.printStackTrace();
		}
		return logo;
	}
	
}