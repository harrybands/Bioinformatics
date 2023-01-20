package cs321.search;

import cs321.common.*;
import cs321.create.SequenceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Filename: GeneBankSearchDatabase.java
 * Main driver class for searching Gene Bank Database
 * @author Joshua Corrales, Brian Helekler
 */
public class GeneBankSearchDatabase
{

    /**
     * Driver class for searching database
     * @param args - Argument parameters for searching
     */
    public static void main(String[] args)
    {
    	try {
            if (args.length < 2 || args.length > 3) {
                throw new ParseArgumentException(args.length + " is not a valid number of arguments");
            }
    		
    		GeneBankSearchDatabaseArguments argument = new GeneBankSearchDatabaseArguments(args);
    		String databasePath = argument.getFilePath(args[0]);
    		String queryPath = argument.getFilePath(args[1]);
    		File queryFile = new File(queryPath);
    		Scanner queryScanner = new Scanner(queryFile);
        	ArrayList<String> textQuer = new ArrayList<String>();
        	ArrayList<Long> binaryQuer = new ArrayList<Long>();
        	ArrayList<String> complementQuer = new ArrayList<String>();
        	int sequenceLength;
        	String line;
        	String complement;
        	
        	//Appends all queries to an ArrayList to be searched
        	while (queryScanner.hasNextLine()) { // checking whether the scanner could get more sequence from the file 
        		
        		line = queryScanner.nextLine().toLowerCase();
            	textQuer.add(line);        	
            	complement = line.replace('a', 'T').replace('c', 'G');
            	complement = complement.replace('t', 'a').replace('g', 'c').toLowerCase();
            	complementQuer.add(complement);
        	}
        	
        	//Looks for all queries in database and prints them and their frequencies
    		Connection con = null;
    		ResultSet rs = null;
    		try
    		{
    			con = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    			Statement statement = con.createStatement();
    			statement.setQueryTimeout(30);    			
    			
    			int result = 0;
    			StringBuilder builder = new StringBuilder();
    			for (int i = 0; i < textQuer.size(); i++) {
    				try {
    					builder.setLength(0);
    					builder.append("SELECT frequency FROM sequences WHERE DNA ='");
    					builder.append(textQuer.get(i));
    					builder.append("';");
    					rs = statement.executeQuery(builder.toString());
    					result = rs.getInt("frequency");
    					
    					builder.setLength(0);
    					builder.setLength(0);
    					builder.append("SELECT * FROM sequences WHERE DNA = '");
    					builder.append(complementQuer.get(i));
    					builder.append("';");
    					rs = statement.executeQuery(builder.toString());
    					result += rs.getInt("frequency");
    					
    					System.out.println(textQuer.get(i) + " " + result);   					
    				} catch(SQLException e) {
    					System.out.println(textQuer.get(i) + " " + 0);
    				}
    			}
    		}
    		catch (SQLException e)
    		{
    			System.out.println(e.getMessage());
    		}
    		finally
    		{
    			try
    			{
    				if (con != null)
    				{
    					con.close();
    				}
    			}
    			catch (SQLException e)
    			{
    				System.out.println(e.getMessage());
    			}
    		}
    		
    	} catch(ParseArgumentException e) {
    		System.err.println("Invalid number of arguments.");
    		GeneBankSearchDatabaseArguments.printUsage();
    	} catch (FileNotFoundException e) {
			System.err.print(e);
		}

    }

}
