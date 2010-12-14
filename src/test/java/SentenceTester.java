import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SentenceTester {

	public static void main(String[] args) {
		Pattern p2 		= Pattern.compile("(?i)[\\s+|\\.|\\!|\\?|,|:|;|\\(|\\)|-](denied)[\\s+|\\.|\\!|\\?|,|:|;|\\(|\\)|-]");
		Matcher m		= p2.matcher(".Patient denied   [PHRASE]pain[PHRASE]..");
		
		while (m.find() == true ) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!Got match.\n\n\n\n");
	
		}
		
	/**
	  	System.out.println("In here");
	 
		MaxentModel model = null;
		try {
			model = new BinaryGISModelReader(new File("/Users/ryancornia/Downloads/EnglishSD.bin.gz")).getModel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String[] results = sentenceDetector.sentDetect("This is a test. My second sentence is here.  Of course there are 3 sentences.");
		for (String s: results) {
			System.out.println(s);
		}
	*/
	}

}

