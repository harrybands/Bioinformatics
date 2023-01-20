package cs321.btree;

import java.io.File;
import java.io.IOException;

/**
 * Filename: BTreeInterface
 * Interface for BTree, used to create A Binary Search tree with a provided degree and key sequence
 * @author Joshua Corrales
 */

public interface BTreeInterface
{
    /**
     * Default constructor for initializing a new BTree and BTree gbk file
     * @param cache - Integer that decides if a cache should be used
     * @param degree - Degree of each node in tree
     * @param sequenceLength - The Length of the DNA sequence to process
     * @param cacheSize - The size of cache to process (Defaults to 0 if no cache is used)
     */
    //public BTree(int degree, int sequenceLength, int cacheSize, string file);
	
    /**
     * Default constructor for initializing a new BTree and BTree gbk file
     * @param degree - Degree of each node in tree
     * @param sequenceLength - The Length of the DNA sequence to process
     * @param f - File for constructing BTree
     */
	// public BTree(int degree, int sequenceLength, File f);

	//public BTree(int degree, int sequenceLength, File f)

    /**
     * Overloaded constructor that creates a BTree when loading from a BTree File
     * @param useCache
     * @param cacheSize
     */
    //public BTree(int useCache, String fileName, int cacheSize);


    /**
     * Method for inserting a new key into tree
     * @param key - Key value of node
     * @throws IOException 
     */
    public void insert(long key) throws IOException;


    /**
     * Method for splitting roots
     * @throws IOException 
     */
    public BTreeNode splitRoot() throws IOException;

    /**
     *
     * @param currentNode
     * @param index - The index entry in array to split
     * @throws IOException 
     */
    public void splitChild(BTreeNode currentNode, int index) throws IOException;


    /**
     * Method that searches recursively for a specified key going down from the root of tree
     * @param currentNode - The current node, the root for where the search starts
     * @param key - Key value to search for
     * @throws IOException 
     */
    public long search(BTreeNode currentNode, long key) throws IOException;

    /**
     * Method that recursively inserts a new key into a node if it's not full
     * @param currentNode - Non full node currently being processed
     * @param key - Key value of node
     * @throws IOException 
     */
    public void insertNonFull(BTreeNode currentNode, long key) throws IOException;

}
