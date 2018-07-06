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

/**
 * Java program responsible for extracting necessary information from cURL command, resizing
 * and re-uploading the picture at the provided link to the correct size, and appending the new
 * information/file to the 'participants.yml' file within the commons.openshift.org GitHub repo
 * 
 * @author shusen - Summer 2018
 *
 */
public class ImageResizer {

	/** Specific text String located in cURL command output on same line as number of Issues */
	public static final String ISSUE_COUNT_FLAG = "number";
	/** Specific index within 'number' line in cURL output noting how many Issues exist in repo */
	public static final int ISSUE_COUNT_LOCATION = 10;
	/** Number of pieces of information extracted from each GitHub issue */
	public static final int ISSUE_DATA = 3;
	/**
	 * Specific text String located in cURL command output on previous line as extracted body
	 * information
	 */
	public static final String BODY_FLAG = "author_association";
	/**
	 * Regular expression pattern used to capture company name, url, and logo url from
	 * cURL output
	 */
	public static final String REGEX_PATTERN = "(?<=: )\\w[^\\\\\"]*+";
	/** Maximum pixel width for uploaded company logo */
	public static final int MAX_WIDTH = 120;
	
	/**
	 * Main method; contains most critical functionality of program including establishing
	 * Scanner for piped input and extracting critical body information from cURL output
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		
		//todo (TEMPORARY) Make program function by piping in cURL command output
		File issues = new File("/Users/shusen/Desktop/CommonsIssues.txt");
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(issues);
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not read from specified file");
			e.printStackTrace();
		}
		
		/**
		 * Following 6 lines initialize variables necessary for properly parsing text input and
		 * extracting company name, url, and logo url
		 */
		String line;
		int issueCount = 0;
		boolean found = false;
		String[][] companyInfo = null;
		String bodyLine = null;
		int i = 0;
		
		/**
		 * Searches input for first line of input containing "number," which indicates how many
		 * GitHub Issues are currently filed in the master repo
		 */
		while (fileReader.hasNextLine()) {
			line = fileReader.nextLine();
			if (line.contains(ISSUE_COUNT_FLAG) && !found) {
				line = line.trim();
				// todo Allow information to be successfully parsed with > 9 issues
				issueCount = Integer.parseInt(line.substring(ISSUE_COUNT_LOCATION, ISSUE_COUNT_LOCATION + 1));
				companyInfo = new String[issueCount][ISSUE_DATA];
				found = true;
			}
			/**
			 * While traversing each line of input file, stops at each line containing
			 * "author association," which will always be located in line before important body
			 * information. Once there, program uses a regular expression pattern to extract the
			 * company name, url, and logo url and stores the information in a 2D array
			 */
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
		
		//Prints the contents of the 2D array of Issue information for testing purposes
		for (int j = 0; j < issueCount; j++) {
			for (int k = 0; k < ISSUE_DATA; k++) {
				System.out.println(companyInfo[j][k]);
			}
		}
		
		/**
		 * Begins to test the process of appending extracted information to master
		 * 'participants.yml' file
		 */
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
		
		/**
		 * For each issue, passes the URL of the company's logo file to be resized and uploaded
		 * to master repo
		 */
		for (int cntr = 0; cntr < issueCount; cntr++) {
			resizeImage(companyInfo[cntr][2]);
		}
	}
	
	/**
	 * Void method responsible for processing each company's logo given its URL, properly resizing
	 * it, and outputting it to the GitHub repo in its correct location
	 * @param logoUrl the String representing the URL of where each company's logo is located online
	 */
	public static void resizeImage(String logoUrl) {
		BufferedImage logo = null;
		try {
			URL imageURL = new URL(logoUrl);
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
	}
	
}