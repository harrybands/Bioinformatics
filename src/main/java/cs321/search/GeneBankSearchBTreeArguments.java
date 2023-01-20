package cs321.search;

import cs321.common.ParseArgumentException;
import cs321.create.GeneBankCreateBTreeArguments;

/**
 * Filename: GeneBankSearchBTreeArguments.java
 * Support class for BTree Arguments
 * @author Brian Helekler, Joshua Corrales
 */
public class GeneBankSearchBTreeArguments
{

    private boolean useCache; 
	private int cacheSize;
	private int cacheLevel; 
	private int debugLevel; // degree to be used for BTree. If the user specifies 0, then program should choose the optimum degree based on
							// a disk block size of 4096 bytes and the size of B-Tree node on disk
	private int subsequenceLength; // an integer that must be between 1 and 31
	private String bTreeFile;
	private String queryFile;
	private String gbkFileName; 
	private int degree;
	public final int MAX_DEGREE_SIZE = 50; 

	/**
	 * 
	 * Constructor of the GeneBankSearchTreeArguments 
	 * 
	 * @param useCache
	 * @param degree
	 * @param bTreeFile
	 * @param subsequenceLength
	 * @param queryFile
	 * @param cacheSize
	 * @param debugLevel
	 */
	public GeneBankSearchBTreeArguments(boolean useCache, int degree, String bTreeFile, int subsequenceLength, String queryFile, int cacheSize, int debugLevel)
    {
        this.useCache = useCache;
        this.degree = degree;
		this.bTreeFile = bTreeFile;
        this.subsequenceLength = subsequenceLength;
		this.queryFile = queryFile;
        this.cacheSize = cacheSize;
        this.debugLevel = debugLevel;
    }


    /**
	 * Create an instance of the class, in order to parse the arguments.
	 * 
	 * @param args - argument array that are being parsed in
	 * @throws ParseArgumentException - invalid arguments
	 */
	public GeneBankSearchBTreeArguments(String[] args) throws ParseArgumentException, Exception {
		
    	try {
    		debugLevel = 0;
    		//checking valid number of arguments
    		if(args.length < 4 || args.length > 7) {
    			printUsageAndExit("Invalid number of arguments!");
    		}
    		for(int i = 0; i < args.length; i++) {
    			if(args[i].contains("--cache=")) {
    	    		//checking whether cache is being used or not
    	    		cacheLevel = Integer.parseInt(args[i].substring(8));
    	    		if(cacheLevel != 0 && cacheLevel != 1) {
    	    			printUsageAndExit("Invalid cache usage!");
    	    		}
    			} else if(args[i].contains("--degree=")) {
    	    		degree = Integer.parseInt(args[i].substring(9));
    			} else if(args[i].contains("--btreefile=")) {
	    			bTreeFile= args[i].substring(12);
	    		} else if(args[i].contains("--queryfile=")) {
	    			queryFile= args[i].substring(12);
	    		} else if(args[i].contains("--length=")) {
	        		subsequenceLength = Integer.parseInt(args[i].substring(9));
	        		if(subsequenceLength < 1 || subsequenceLength > 31) {
	        			printUsageAndExit("Invalid subsequence length!");
	        		}
	    		} else if(args[i].contains("--cachesize=")) {
	    			cacheSize = Integer.parseInt(args[i].substring(12));
	    			if(cacheSize < 100 || cacheSize > 5000) {
	    				printUsageAndExit("Invalid cache size!");
	    			}
	    		} else if(args[i].contains("--debug=")) {
	    			debugLevel = Integer.parseInt(args[i].substring(8));
	    			if (debugLevel < 0 || debugLevel > 1) {
	    				printUsageAndExit("Invalid debug level!");
	    			}
	    		}

    		}   		
    	} catch(Exception e) {
    		e.printStackTrace();
    		printUsageAndExit("Incorrect argument format");
    	}
	}

	/**
	 * Getter method for using cache
	 * @return - Boolean value that determines if cache will be used
	 */
	public boolean usingCache() {
		return this.cacheLevel == 1;
	}

	/**
	 * Getter method for retrieving BTree File
	 * @return - Path to BTree file
	 */
	public String bTreeFile() {
		return this.bTreeFile;
	}

	/**
	 * Getter method for retrieving query file
	 * @return - Path to query file
	 */
	public String queryFile() {
		return this.queryFile;
	}

	/**
	 * Support method for cache size
	 * @return - Returns cache size
	 */
	public int cacheSize() {
		if (!usingCache()) {
			return 0;
		} else {
			return this.cacheSize;
		}
	}

	/**
	 * Getter method for debug level
	 * @return - Returns debug level
	 */
	public int debugLevel() {
		return this.debugLevel;
	}

	/**
	 * Getter method for degree
	 * @return - Returns degree of object
	 */
	public int getDegree() {
		return this.degree;
	}

	/**
	 * Getter method for sequence length
	 * @return - Returns sequence length
	 */
	public int getSequenceLength() {
		return this.subsequenceLength;
	}

	/**
	 * 
	 * Printing out the usage of the GeneBankSearchTree and throwing error Messages if there are any issues 
	 * @param errorMessage
	 */
	public static void printUsageAndExit(String errorMessage)
    {
		System.out.println("java -jar build/libs/GeneBankSearchBTree.jar --cache=<0/1> --degree=<btree degree>" + 
		" --btreefile=<BTree file> --length=<sequence length> --queryfile=<query file> [--cachesize=<n>] [--debug=0|1\n");
       System.err.println(errorMessage);
        System.exit(1);
    }
}
