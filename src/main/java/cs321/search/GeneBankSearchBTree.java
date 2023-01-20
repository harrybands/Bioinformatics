package cs321.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import cs321.btree.BTree;
import cs321.common.ParseArgumentException;
import cs321.common.ParseArgumentUtils;
import cs321.create.SequenceUtils;
/**
 * Filename: GeneBankSearchBTree.java
 * Allowing queries to run on BTree created by GeneBankCreateBTree then 
 * compares the DNA sequences with B Tree File. 
 * @author Harry Nguyen, Brian Heleker 
 * 
 */
public class GeneBankSearchBTree
{
	static String btreeFile; // gbk Filename 
	static String queryFile; // query File 
    static int cacheUsed; 
    static int degree; 
    static String gbkFilename; 
    static int subsequenceLength;
    static int debugLevel; 
    static int cacheSize;
    static BTree btree;

	/**
	 * 
	 * Main search driver using to run the query file on btree 
	 * @param args - arguments that are being parsed in 
	 * @throws Exception - Throws exception if there are issues
	 */
    public static void main(String[] args) throws Exception {
    	GeneBankSearchBTreeArguments gS;
    	ArrayList<String> textQuer = new ArrayList<String>();
    	ArrayList<Long> binaryQuer = new ArrayList<Long>();
    	String queryFile = "";
    	
    	try {
    		gS = new GeneBankSearchBTreeArguments(args);
    		queryFile += gS.queryFile();
    		File file = new File(queryFile);
    		Scanner scanner = new Scanner(file);
    		subsequenceLength = gS.getSequenceLength();
    		debugLevel = gS.debugLevel();
    	if (gS.usingCache()) { // checking if the cache is being used or not 
            cacheUsed = 1;
            cacheSize = gS.cacheSize();
            btree = new BTree(gS.getDegree(), subsequenceLength, new File(gS.bTreeFile()), true, cacheSize);
    	} else {
    		btree = new BTree(gS.getDegree(), subsequenceLength, new File(gS.bTreeFile()));
    	}
    	
    	while (scanner.hasNextLine()) { // checking whether the scanner could get more sequence from the file 
    		String line = scanner.nextLine();
        	
        	if (line.length() != btree.getSequenceLength()) { // terminate the program if the lengths of the BTree file and the scanner is not compatible  
        		GeneBankSearchBTreeArguments.printUsageAndExit("The sequence length did not match!\n");
        		System.exit(1);
        	}
        	//Adds all queries to ArrayList
        	textQuer.add(line);        	
        	long binary = SequenceUtils.DNAStringToLong(line.toLowerCase());
        	binaryQuer.add(binary);
    	}
    	
    	scanner.close();
    	
        	for (int i = 0; i < binaryQuer.size(); i++) {
        		int j = 0;
        		long complement = binaryQuer.get(i);
    			long result = btree.searchTwo(binaryQuer.get(i));
    			while(j < subsequenceLength * 2) { // masking and shifting the binary queries to get complement
    				complement = complement^(1<<j);
    				j++;
    			}
    			long complementResult = btree.searchTwo(complement);
    			result += complementResult;
        			System.out.println(textQuer.get(i).toLowerCase() + " " + result); // print out the queries 
        	}

    	btree.close();
    	} catch (ParseArgumentException pae) {
    		GeneBankSearchBTreeArguments.printUsageAndExit("Parse Argument Exception");
    		System.exit(1);
    	} catch (Exception e) {
    		e.printStackTrace();
    		GeneBankSearchBTreeArguments.printUsageAndExit("Parse Argument Exception");
    	}
    }
}



