import java.util.Hashtable;
import java.util.Random;


/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;
	private Integer order;
	private Hashtable<String, Integer[]> hashTable;
	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		this.hashTable = new Hashtable<String, Integer[]>();
		int len = text.length();
		if (len < this.order) {
			return;
		}
		Integer cycles = text.length() - this.order;
		for (int i = 0; i < cycles; i++) {
			String currText = text.substring(i, i + this.order); //Get the kgram
			//Integer currKey = currText.hashCode(); // get Hashcode for kgram
			if (!this.hashTable.containsKey(currText)) {
				this.hashTable.put(currText, new Integer[256]);
				Integer[] intArr = this.hashTable.get(currText); //Returns the value (type Integer[]) associated with key
				if (intArr == null) {
					return;
				}
				for (int j = 0; j < 256; j++) { //Initialise every element to 0 in the array
					intArr[j] = 0;
				}
				char singleChar = text.charAt(i + this.order);
				int index = (int) singleChar;
				intArr[index] = intArr[index] + 1;
			} else {
				Integer[] intArr = this.hashTable.get(currText); // Returns the value associated with key
				if (intArr == null) {
					return;
				}
				char singleChar = text.charAt(i + this.order);
				int index = (int) singleChar;
				intArr[index] = intArr[index] + 1;
			}
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() <= 0 || kgram == null || kgram.length() != this.order) {
			return 0;
		}
		//Integer hashKey = kgram.hashCode();
		Integer[] intArr = this.hashTable.get(kgram);
		if (intArr == null) {
			return 0;
		}
		int count = 0;
		for (int i = 0; i < 256; i++) {
			count = count + intArr[i];
		}
		return count;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() <= 0 || kgram == null || kgram.length() != this.order) {
			return 0;
		}
		//Integer hashKey = kgram.hashCode();
		Integer[] intArr = this.hashTable.get(kgram);
		if (intArr == null) {
			return 0;
		}
		int index = (int) c;
		int count = intArr[index];
		return count;
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		// Initialise an array based on. Array that stores the char
		Integer size = this.getFrequency(kgram);
		if (size == 0) {
			return MarkovModel.NOCHARACTER;
		}
		Character[] intArrProbability = new Character[size]; //Probability array, stores char w.r.t to probability
		Integer[] intArr = this.hashTable.get(kgram);
		if (!this.hashTable.containsKey(kgram)) { //Edge Case
			return MarkovModel.NOCHARACTER;
		} else if (kgram.length() != this.order) { //Edge Case
			return MarkovModel.NOCHARACTER;
		}
		int index = 0;
		for (int i = 0; i < 256; i++) { //Initialisation of intArrProbability
			Integer countChar = intArr[i]; //countChar => no. of times char appears after kgram
			if (countChar != 0) {
				for (int j = 0; j < countChar; j++) {
					char character = (char) i;
					intArrProbability[index] = character;
					index++;
				}
			}
		}
		int nextCharIndex = this.generator.nextInt(size);
		char nextCharacter = intArrProbability[nextCharIndex];
		return nextCharacter;
	}
}
