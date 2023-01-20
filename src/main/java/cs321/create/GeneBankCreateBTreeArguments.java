package cs321.create;

/**
 * Filename: GeneBankCreateBTreeArguments.java
 * Class core creating and parsing BTree Arguments
 *
 * @author Joshua Corrales
 */
public class GeneBankCreateBTreeArguments
{
    private final int useCache;
    private final int degree;
    private final String gbkFileName;
    private final int subsequenceLength;
    private final int cacheSize;
    private final int debugLevel;

    /**
     * 
     * Constructor of BTree Arguments 
     * 
     * @param useCache
     * @param degree
     * @param gbkFileName
     * @param subsequenceLength
     * @param cacheSize
     * @param debugLevel
     */
    public GeneBankCreateBTreeArguments(int useCache, int degree, String gbkFileName, int subsequenceLength, int cacheSize, int debugLevel)
    {
        this.useCache = useCache;
        this.degree = degree;
        this.gbkFileName = gbkFileName;
        this.subsequenceLength = subsequenceLength;
        this.cacheSize = cacheSize;
        this.debugLevel = debugLevel;
    }

    /**
     * get information whether the users uses cache or not
     * @return useCache - 1 if they are using, 0 if they are not
     */
    public int useCache() {
        return useCache;
    }

    /**
     * 
     * get degree information from B Tree 
     * 
     * @return degree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * get Gbk filename information 
     * @return gbkFileName
     */
    public String getGbkFileName() {
        return gbkFileName;
    }
    /**
     * 
     * get the subsequence length of the B Tree 
     * @return subsequenceLength 
     */
    public int getSubsequenceLength() {
        return subsequenceLength;
    }

    /**
     * 
     * get the cache size of the B Tree
     * @return cacheSize
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * 
     * get debug level from the users 
     * 
     * @return debugLevel
     */
    public int getDebugLevel() { return debugLevel; }

    @Override
    public boolean equals(Object obj)
    {
        //this method was generated using an IDE
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        GeneBankCreateBTreeArguments other = (GeneBankCreateBTreeArguments) obj;
        if (cacheSize != other.cacheSize)
        {
            return false;
        }
        if (debugLevel != other.debugLevel)
        {
            return false;
        }
        if (degree != other.degree)
        {
            return false;
        }
        if (gbkFileName == null)
        {
            if (other.gbkFileName != null)
            {
                return false;
            }
        }
        else
        {
            if (!gbkFileName.equals(other.gbkFileName))
            {
                return false;
            }
        }
        if (subsequenceLength != other.subsequenceLength)
        {
            return false;
        }
        if (useCache != other.useCache)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        //this method was generated using an IDE
        return "GeneBankCreateBTreeArguments{" +
                "useCache=" + useCache +
                ", degree=" + degree +
                ", gbkFileName='" + gbkFileName + '\'' +
                ", subsequenceLength=" + subsequenceLength +
                ", cacheSize=" + cacheSize +
                ", debugLevel=" + debugLevel +
                '}';
    }
}
