package cs321.create;

import cs321.btree.BTree;
import cs321.common.ParseArgumentException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

/**
 * Filename: GeneBankCreateBTree.java
 * Class to create B Tree from gene banks and create arguments 
 * @author Brian Helekler, Harry Nguyen 
 * 
 */

public class GeneBankCreateBTree
{
    static int usingCache; // specifies whether the program should use cache (value 1) or no cache (value 0); if the value is 1, 
						//the <cache_size> has to be specified
    static int degree; // degree of the B Tree 
    static String gbk_file; // the input *.gbk file containing the input DNA sequences
    static int sequenceLength; // n integer that must be between 1 and 31 (inclusive)
    static int debugLevel; // an optional argument with a default value of zero.
    static int cacheSize; // maximum number of BTreeNode objects that can be stored in memory
    static int originalDegree; // initial degree

	
	/**
	 * Main driver of the Gene Bank Create B Tree 
	 * @param args - arguments about to be parsed in 
	 * @throws Exception - throws exceptions if there is any issue 
	 */
    public static void main(String[] args)
    {
    	try{
    		GeneBankCreateBTreeArguments gC = parseArguments(args);
    		
    		usingCache = gC.useCache();
    		degree = gC.getDegree();
    		gbk_file = gC.getGbkFileName();
    		sequenceLength = gC.getSubsequenceLength();
    		cacheSize = gC.getCacheSize();
    		debugLevel = gC.getDebugLevel();
    		String fileToRead = gbk_file;
    		BTree tree;
    		
    		String newFileName = gbk_file.replace("data/files_gbk/", "");  
    		newFileName += ".btree.data." + sequenceLength + "." + originalDegree; // file name
    		if(usingCache == 0) {
    			tree = new BTree(degree, sequenceLength, new File(newFileName));    		
    		} else {
    			tree = new BTree(degree, sequenceLength, new File(newFileName), true, cacheSize);
    		}
    		BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
    		String readLine = reader.readLine();
    		if(debugLevel == 2) {
    			long startTime = System.currentTimeMillis();
        		while(readLine != null) {
        			if(readLine.contains("ORIGIN")) {
        				buildTreeFromContents(reader, tree, sequenceLength);
        			}
        			readLine = reader.readLine();
        		}
        		System.out.println("Time to build tree -> " + (System.currentTimeMillis() - startTime));
    		} else {
        		while(readLine != null) {
        			if(readLine.contains("ORIGIN")) {
        				buildTreeFromContents(reader, tree, sequenceLength);
        			}
        			readLine = reader.readLine();
        		}
    		}

    		reader.close();
    		String SQLDumpName = gbk_file.replace("data/files_gbk/", "") + "." + sequenceLength + ".SQL.db";
    		if(debugLevel > 0) {
    			String dumpName = "dump";
    			tree.dump(dumpName);
    			try {
    				// create a database connection
    				Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SQLDumpName);
    				Statement statement = connection.createStatement();
    				statement.executeUpdate("drop table if exists sequences");
    				statement.executeUpdate("CREATE table sequences (DNA string, frequency integer)");
    				tree.dumpSQL(statement);
    			} catch(SQLException e) {
    	    		System.out.println("There was an error vreating the SQL Database");
    	    	}
    			tree.closeTree();
    			tree.close();
    		} else {
    			try {
    				// create a database connection
    				Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SQLDumpName);
    				Statement statement = connection.createStatement();
    				statement.executeUpdate("drop table if exists sequences");
    				statement.executeUpdate("CREATE table sequences (DNA string, frequency integer)");
    				tree.dumpSQL(statement); 				
    			} catch(SQLException e) {
    	    		System.err.println("There was an error vreating the SQL Database");
    	    	}
    			tree.closeTree();
    			tree.close();
    		}
    	} catch(IOException e) {
    		System.err.println("There was an error creating the BTree.");
    	} catch (ParseArgumentException e) {
			System.err.println("There was an error parsing the arguments");
		} catch(Exception e) {
			System.err.println("Error in argument formatting or file");
		}
    }

	/**
	 * 
	 * printing the usage and error messages, and exiting the program 
	 * 
	 * @param errorMessage
	 */
    private static void printUsageAndExit(String errorMessage)
    {
        System.out.println("java -jar build/libs/GeneBankCreateBTree.jar --cache=<0|1>  --degree=<btree degree>" 
        + " --gbkfile=<gbk file> --length=<sequence length> [--cachesize=<n>] [--debug=0|1|2]"); // usage of create b tree
        System.err.println(errorMessage); // error message
        System.exit(1); // exiting program 
    }

	/**
	 * 
	 * Checking whether the arguments are valid or not, returning the user arguments 
	 * 
	 * @param args - arguments about to be parsed in 
	 * @return
	 * @throws ParseArgumentException
	 */

    public static GeneBankCreateBTreeArguments parseArguments(String[] args) throws ParseArgumentException
    {	
    	try {
    		debugLevel = 0;
    		//checking valid number of arguments
    		if(args.length < 4 || args.length > 6) {
    			printUsageAndExit("Invalid number of arguments!");
    		}
    		for(int i = 0; i < args.length; i++) {
    			if(args[i].contains("--cache=")) {
    	    		//checking whether cache is being used or not
    	    		usingCache = Integer.parseInt(args[i].substring(8));
    	    		if(usingCache != 0 && usingCache != 1) {
    	    			printUsageAndExit("Invalid cache usage!");
    	    		}
    			} else if(args[i].contains("--degree=")) {
    	    		degree = originalDegree = Integer.parseInt(args[i].substring(9));
    	    		if(degree == 0) {
    	    			degree = 10;
    	    		} 
    			} else if(args[i].contains("--gbkfile=")) {
	    			gbk_file = args[2].substring(10);
	    		} else if(args[i].contains("--length=")) {
	        		sequenceLength = Integer.parseInt(args[i].substring(9));
	        		if(sequenceLength < 1 || sequenceLength > 31) {
	        			printUsageAndExit("Invalid subsequence length!");
	        		}
	    		} else if(args[i].contains("--cachesize=")) {
	    			cacheSize = Integer.parseInt(args[i].substring(12));
	    			if(cacheSize < 100 || cacheSize > 5000) {
	    				printUsageAndExit("Invalid cache size!");
	    			}
	    		} else if(args[i].contains("--debug=")) {
	    			debugLevel = Integer.parseInt(args[i].substring(8));
	    			if (debugLevel < 0 || debugLevel > 2) {
	    				printUsageAndExit("Invalid debug level!");
	    			}
	    		}
    		}
    		if(usingCache == 0) {
    			return new GeneBankCreateBTreeArguments(0, degree, gbk_file, sequenceLength, cacheSize, debugLevel);
    		}
    		else {
    			return new GeneBankCreateBTreeArguments(1, degree, gbk_file, sequenceLength, cacheSize, debugLevel);
    		}   		
    	} catch(Exception e) {
    		printUsageAndExit("Incorrect argument format");
    		return null;
    	}
    }
/**
 * 
 * Using buffered reader to read the gbk files 
 * 
 * @param reader - the Buffered Reader
 * @param tree - the b tree contents that being read 
 * @param sequenceLength - length of the sequence 
 * @throws IOException
 */    
    private static void buildTreeFromContents(BufferedReader reader, BTree tree, int sequenceLength) throws IOException {
    	String line = reader.readLine();
    	StringBuilder sequenceToParse = new StringBuilder();
    	if(debugLevel == 2) {
    		long startTime = System.currentTimeMillis();
        	while(!line.contains("//")) {
        		line = line.replaceAll("\\d", "").replaceAll("\\s+", "");
        		sequenceToParse.append(line);
        		line = reader.readLine();
        	}
        	addSequencesToTree(tree, sequenceLength, sequenceToParse.toString());  
        	System.out.println("Time to insert subsequences from sequence of size " + sequenceToParse.length());
        	System.out.println(" -> " + (System.currentTimeMillis() - startTime));
    	} else {
        	while(!line.contains("//")) {
        		line = line.replaceAll("\\d", "").replaceAll("\\s+", "");
        		sequenceToParse.append(line);
        		line = reader.readLine();
        	}
        	addSequencesToTree(tree, sequenceLength, sequenceToParse.toString());    	
    	}
    }
    
	/**
	 * 
	 * Adding sequences to tree 
	 * 
	 * @param tree
	 * @param sequenceLength
	 * @param sequence
	 * @throws IOException
	 */
    private static void addSequencesToTree(BTree tree, int sequenceLength, String sequence) throws IOException {
    	long insertValue = 0;
    	int i = 0;
    	String subString;
    	while(i <= sequence.length() - sequenceLength) {
    		subString = sequence.substring(i,sequenceLength + i);
    		if(!subString.contains("n")) {
    			insertValue = sequenceToLong(subString);		
    			tree.insert(insertValue);
    		}
    		i++;	
    	}
    }

	/**
	 *
	 * Method for converting a DNA string into a long integer  
	 * @param sequence - the initial DNA string 
	 * @return longRep - long integer
	 */
    private static long sequenceToLong(String sequence) {
    	long longRep = 0;

    	for(int i = 0; i < sequence.length(); i++) { 
    		switch(sequence.charAt(i)) {
    		
    		case 'a' :
    			longRep = (longRep << 2);
    			break;
    		case 'c' :
    			longRep = (longRep << 2) + 1;
    			break;
    		case 'g' :
    			longRep = (longRep << 2) + 2;
    			break;
    		case 't' :
    			longRep = (longRep << 2) + 3;
    			break;
    		}
    	} 
    	
    	return longRep;
    }
    
}
