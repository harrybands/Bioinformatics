package cs321.common;

/**
 * Filename: ParseArgumentUtils.java
 * Some utilities of the parsed arguments served for other classes 
 * 
 * @author Joshua Corrales 
 * 
 */
public class ParseArgumentUtils
{
    /**
     * Verifies if lowRangeInclusive <= argument <= highRangeInclusive
     */
    public static void verifyRanges(int argument, int lowRangeInclusive, int highRangeInclusive) throws ParseArgumentException
    {
        if (argument < lowRangeInclusive) {
            throw new ParseArgumentException("Argument is smaller than low Range");

        }
        if(argument > highRangeInclusive) {
            throw new ParseArgumentException("Argument is bigger that high Range");
        }
    }

    /**
     * 
     * This method converts string to integer 
     * @param argument - argument to process
     * @return - returns integer representation of args or throws if there are issues
     * @throws ParseArgumentException - throws IO exception if there are issues
     */
    public static int convertStringToInt(String argument) throws ParseArgumentException
    {
        try {
            return Integer.parseInt(argument);
        }
        catch(NumberFormatException e) { // throws out exception if the variable type is invalid
            throw new ParseArgumentException("Argument Exception");
        }
    }
}
