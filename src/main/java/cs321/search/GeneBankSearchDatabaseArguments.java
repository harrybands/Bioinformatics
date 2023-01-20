package cs321.search;

/**
 * Filename: GeneBankSearchDatabaseArguments.java
 * class for Database search arguments
 * @author Joshua Corrales, Brian Helekler
 */
public class GeneBankSearchDatabaseArguments
{
    public static String dataBasePath;
    public static String queryFilePath;
    public final int debugLevel;

    /**
     * Constructor for GeneBankSearchDatabaseArguments given command 
     * line arguments
     * @param args - Arguments to pass in
     */
    public GeneBankSearchDatabaseArguments(String[] args)
    {
        GeneBankSearchDatabaseArguments.dataBasePath = getFilePath(args[0]);
        GeneBankSearchDatabaseArguments.queryFilePath = getFilePath(args[1]);

        if (args.length >= 3)
            this.debugLevel = getDebugLevel(args[2]);
        else
            this.debugLevel = 0;
    }

    /**
     * Method for printing debug level usage is invalid
     */
    public static void printUsage()
    {
        System.err.println("Not a valid debug level");
        System.err.println("java -jar build/libs/GeneBankSearchDatabase.jar <path_to_SQLite_database> <query_file> [<debug_level>]");
        System.err.println("[<debug level>]: 0 for standard output; 1 for additional files.");
        System.exit(1);
    }

    /**
     * Method for getting debugLevel
     * @return Returns debug level
     */
    public int getDebugLevel(String debugString)
    {
        int i = Integer.parseInt(debugString);
        if (i > 1)
            printUsage();

        return i;
    }

    /**
     * Method for getting filepath
     * @param pathString - Path to file
     * @return String of path to file
     */
    public String getFilePath(String pathString) { return pathString; }

    /**
     * toString method for parsing arguments
     * @return Returns string to parse DB arguments
     */
    @Override
    public String toString()
    {
        return "GeneBankSearchDatabaseArguments{" + "Path to Database= " + dataBasePath + ", Query file path= " + queryFilePath +
            ", debugLevel=" + debugLevel + '}';
    }

}
