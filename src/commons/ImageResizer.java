package commons;

public class ImageResizer {

	public static final int MAX_WIDTH = 160;
	
	public static void main(String[] args) {
		boolean retry = false;
		do {
			try {
				String inputPath = args[0];
				retry = true;
			} catch (Exception e) {
				System.out.println("Please enter \"commons-logos/[CompanyName].extension\"");
			}
		} while (!retry);
	}
	
	//TODO figure out how resize mechanism would work

}