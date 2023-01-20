package cs321.create;



/**
 * SequenceUtils.java
 * Utility methods dealing with DNA sequences and its compact representation as long variables.
 * @author Joshua Corrales
 */
public class SequenceUtils
{

	private static final int MAX_SEQUENCE_LENGTH = 31;
	/**
	 * Method for converting a DNA string into a long integer
	 * @param DNAString - DNA sequence to process (a, t, c and g)
	 * @return Long integer representation of DNA String
	 */
	public static long DNAStringToLong(String DNAString)  {

		String binDNA = "";
		char[] DNAcharArray = DNAString.toLowerCase().toCharArray();

		//right-justify the binary string
		for (int i = 0; i < DNAString.length(); i++)
		{
			switch (DNAcharArray[i])
			{
				case 'a':
				binDNA = binDNA.concat("00");
				break;
				case 't':
				binDNA = binDNA.concat("11");
				break;
				case 'c':
				binDNA = binDNA.concat("01");
				break;
				case 'g':
				binDNA = binDNA.concat("10");
				break; 
			}
		}

		return Long.parseLong(binDNA, 2);
	}

	/**
	 * Method for converting a long integer to a DNA sequence (a, t, c, g)
	 * @param sequence - Long integer to convert into sequence
	 * @param seqLength - Length of the sequence to parse
	 * @return DNA sequence string representation of long integer
	 */
	public static String longToDNAString(long sequence, int seqLength) {
		String DNAseq;
		String[] binArray = new String[MAX_SEQUENCE_LENGTH];

		DNAseq = Long.toBinaryString(sequence);

		//Right-just if there is a leading c in sequence
		if (DNAseq.length() % 2 != 0)
			DNAseq = "0".concat(DNAseq);
		//Right-just if there are leading a's in sequence
		while (DNAseq.length() < seqLength * 2)
			DNAseq = "00".concat(DNAseq);

		//Right-just binary string
		for (int i = 0; i < MAX_SEQUENCE_LENGTH; i++)
		{
			binArray[i] = "00";
		}

		//split string into array of 2-bit binary strings
		for (int i = 0; 2 * i < DNAseq.length(); i++)
		{
			binArray[MAX_SEQUENCE_LENGTH - seqLength + i] = DNAseq.substring(i * 2, (i * 2) + 2);
		}

		DNAseq = ""; //Clear DNAseq so we can save final return value after processing

		for (int i = MAX_SEQUENCE_LENGTH - seqLength; i < MAX_SEQUENCE_LENGTH; i++)
		{
				switch(binArray[i]) {
					case "00": 
						DNAseq = DNAseq.concat("a");
						break;
					case "11": 
						DNAseq = DNAseq.concat("t");
						break;
					case "01": 
						DNAseq = DNAseq.concat("c");
						break;
					case "10": 
						DNAseq = DNAseq.concat("g");
						break;
					default: 
						DNAseq = DNAseq; 
				}
		}

		return DNAseq;
	}

	/**
	 * Method for getting the complement of DNA sequence
	 * @param sequence - Long integer to convert into sequence
	 * @param seqLength - Length of the sequence to parse
	 * @return Complement of string representation long integer
	 */
	public static long getComplement(long sequence, int seqLength) {
		String DNAseqComplement;
		String[] binArray = new String[MAX_SEQUENCE_LENGTH];

		DNAseqComplement = Long.toBinaryString(sequence);

		if (DNAseqComplement.length() % 2 != 0)
			DNAseqComplement = "0".concat(DNAseqComplement);
		while (DNAseqComplement.length() < seqLength * 2)
			DNAseqComplement = "00".concat(DNAseqComplement);

		for (int i = 0; i < MAX_SEQUENCE_LENGTH; i++)
		{
			binArray[i] = "00";
		}

		//split string into array of 2-bit binary strings
		for (int i = 0; 2 * i < DNAseqComplement.length(); i++)
		{
			binArray[MAX_SEQUENCE_LENGTH - seqLength + i] = DNAseqComplement.substring(i * 2, (i * 2) + 2);
		}

		DNAseqComplement = "";

		//Similar process to longToDNAString however t and a are swapped as are c and g
		for (int i = MAX_SEQUENCE_LENGTH - seqLength; i < MAX_SEQUENCE_LENGTH; i++)
		{
			switch (binArray[i])
			{
				case "00" :
					DNAseqComplement = DNAseqComplement.concat("11"); //T, A's complement in Binary
					break;
				case "11" :
					DNAseqComplement = DNAseqComplement.concat("00"); // A, T's complement in Binary
					break;
				case "01" :
					DNAseqComplement = DNAseqComplement.concat("10"); // G, C's complement in Binary
					break;
				case "10" :
					DNAseqComplement = DNAseqComplement.concat("01"); //C, G's complement in Binary
					break;
				default :
					DNAseqComplement = DNAseqComplement;
			}
		}

		//return long int of complement of DNA string.
		return Long.parseLong(DNAseqComplement);
	}

}
