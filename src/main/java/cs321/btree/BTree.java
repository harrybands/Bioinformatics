package cs321.btree;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

import cs321.common.ParseArgumentException;
import cs321.common.ParseArgumentUtils;

/**
 * Filename: BTree
 * Implementing the B Tree 
 * 
 *
 * @author Harry Nguyen 
 * @author Brian Heleker 
 *  
 */


public class BTree implements BTreeInterface, Closeable
{
	private int degree;
	private int sequenceLength;
    private FileChannel file;
    private ByteBuffer buffer;
	private long nextAddress;
	private int nodeSize;
	private RandomAccessFile raf;
	private BTreeCache cache;
	private int cacheSize;
	private long rootAddress;
	private BTreeNode root;
	private boolean usingCache;
	
	/**
	 * 
	 * Constructor of the B Tree 
	 * 
	 * @param degree - degree of sequence to process
	 * @param sequenceLength - Length of DNA sequence to process
	 * @param f - Name of file to process
	 */
	public BTree(int degree, int sequenceLength, File f) {
		this.degree = degree;
		if(degree == 0) {
			this.degree = 102;
		}
		this.sequenceLength = sequenceLength;
		nodeSize = 4096;
		rootAddress = 100;
		nextAddress = 4196;
		buffer = ByteBuffer.allocateDirect(nodeSize);
		
		try {
			if (!f.exists()) {
				f.createNewFile();
				raf = new RandomAccessFile(f, "rw");
				file = raf.getChannel();
				root = new BTreeNode(degree, rootAddress);
				root.setIsLeaf(true);
			} else {
				raf = new RandomAccessFile(f, "rw");
				file = raf.getChannel();
				readMetaData();
				root = diskRead(rootAddress);
			}
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Overloaded constructor of BTree
	 * 
	 * @param degree - degree of sequence to process
	 * @param sequenceLength - Length of DNA sequence to process
	 * @param f - Name of file to process
	 * @param useCache - Boolean value that determines if cache will be used
	 * @param cacheSize - Size of cache
	 */
	public BTree(int degree, int sequenceLength, File f, boolean useCache, int cacheSize) {
		this.usingCache = useCache;
		this.cacheSize = cacheSize;
		cache = new BTreeCache(cacheSize);
		this.degree = degree;
		if(degree == 0) {
			this.degree = 102;
		}
		this.sequenceLength = sequenceLength;
		nodeSize = 4096;
		rootAddress = 100;
		nextAddress = 4196;
		buffer = ByteBuffer.allocateDirect(nodeSize);
		
		try {
			if (!f.exists()) { // checking whether the file is existed or nto
				f.createNewFile();
				raf = new RandomAccessFile(f, "rw");
				file = raf.getChannel();
				root = new BTreeNode(degree, rootAddress);
				root.setIsLeaf(true);
			} else {
				raf = new RandomAccessFile(f, "rw");
				file = raf.getChannel();
				readMetaData();
				root = diskRead(rootAddress);
			}
		} catch (FileNotFoundException e) { // throwing exceptions if file not found
			System.err.println(e);
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * 
	 * Writing the BTree Node into disk 
	 * 
	 * @param n - the node that being written
	 * @throws IOException - throwing exception if encountering issues
	 */
	public void diskWrite(BTreeNode n) throws IOException {
		file.position(n.getAddress());
		n.serialize(buffer);
		buffer.flip();
		file.write(buffer);
	}

	/**
	 * 
	 * Reading the objects from the data stored on the disk at a particular address.
	 * 
	 * @param address - - the address that being read at
	 * @return - returns BTreeNew with address, buffer, and degree
	 * @throws IOException - If IO is different from what is expected
	 */
	public BTreeNode diskRead(long address) throws IOException {
		file.position(address);
		buffer.clear();
		file.read(buffer);
		buffer.flip();
		return new BTreeNode(address, buffer, degree);
	}
	
	public BTreeNode cacheWrite(BTreeNode n) throws IOException {
		BTreeCacheObject insertNode = new BTreeCacheObject(n.getAddress(), n);
		if(cache.getSize() == cacheSize && !cache.contains(n.getAddress())) {
			BTreeNode insertionNode = cache.getEldest().getNode();
			diskWrite(insertionNode);
		}
		return cache.getObject(insertNode).getNode();
	}
	
	/**
	 * 
	 * reading node from cache from a particular address
	 * 
	 * @param address - the address that being read at
	 * @return - cache that is being read
	 * @throws IOException - Throws IO exception if IO is different from expectation
	 */
	public BTreeNode cacheRead(long address) throws IOException {
		BTreeCacheObject returnNode;
		if(!cache.contains(address)) {
			if(cache.getSize() == cacheSize) {
				BTreeNode insertionNode = cache.getEldest().getNode();
				diskWrite(insertionNode);
			}
			returnNode = new BTreeCacheObject(address, diskRead(address));
			cache.getObject(returnNode);
			return returnNode.getNode();
		} else {
			return cache.getObject(address).getNode();
		}
	}
	
	/**
	 * 
	 * Allocating the node to the right places in B Tree 
	 * 
	 * @return node - the node that being allocated 
	 */
	public BTreeNode allocateNode() {
		BTreeNode node = new BTreeNode(degree, nextAddress); 
		nextAddress += nodeSize; 
		return node; 
	}
	
	/**
	 * 
	 * get degree variables
	 * 
	 * @return degree - of the B Tree
	 */
	public int getDegree() {
		return degree;
	}
	
	/**
	 * 
	 * get sequence length variables
	 * 
	 * @return sequenceLength - of the BTree
	 */

	public int getSequenceLength() {
		return sequenceLength;
	}
	
	/**
	 * 
	 * get the filename of the file that being used. 
	 * 
	 * @return file - the filename
	 */
	public FileChannel getFile() {
		return file;
	}
	
	/**
	 * 
	 * getting node at a particular index
	 * 
	 * @param index - the index where the node being gotten 
	 * @return
	 */
	public BTreeNode getNodeAt(int index) {
		try {
			if(index < 1) { // throwing exceptions if out of range
				throw new Exception("Index out of bounds");
			}
			Queue<BTreeNode> queue = new LinkedList<BTreeNode>();
			queue.add(root);
			BTreeNode current = null;
			int j = 0;
			while(j < index) {
				current = queue.remove();
				j++;
				if(!current.getIsLeaf()) {
					for(int c = 1; c <= current.getNumKeys() + 1; c++) {
						BTreeNode child = diskRead(current.children[c]);
						queue.add(child);
					}
				}
			}
			return current;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * Inserting keys into the B Tree
	 * 
	 * @param key - the key that being inserted
	 * @throws IOException - throwing exceptions if there are issues
	 */
	@Override
	public void insert(long key) throws IOException {
		if(root.getNumKeys() == (2 * degree - 1)) {
			BTreeNode s = splitRoot();
			insertNonFull(s, key);
		} else {
			insertNonFull(root, key);
		}
		
	}

	/**
	 * Method for splitting roots
	 * @return - returns split of BTreeNode
	 * @throws IOException - throws exception if there is issues
	 */
	@Override
	public BTreeNode splitRoot() throws IOException {
		BTreeNode split = allocateNode();
		split.setIsLeaf(false);
		split.children[1] = root.getAddress();
		root = split;
		splitChild(split, 1);
		rootAddress = split.getAddress();
		return split;
	}

	/**
	 * 
	 * Splitting nodes into two when the B Tree grows
	 * 
	 * @param currentNode - the starting node  
	 * @param index - location of the node 
	 * @throws IOException
	 * 
	 */
	@Override
	public void splitChild(BTreeNode currentNode, int index) throws IOException {
		BTreeNode y;
		if(usingCache) { // checking whether users using cache or not 
			y = cacheRead(currentNode.children[index]);
		} else {
			y = diskRead(currentNode.children[index]);							
		}
		BTreeNode z = allocateNode(); 
		z.setIsLeaf(y.getIsLeaf());
		z.setNumKeys(degree - 1);
		for(int j = 1; j < degree; j++) {
			z.keys[j] = y.keys[j+degree];
		}
		if(!y.getIsLeaf()) {
			for(int j = 1; j <= degree; j++) {
				z.children[j] = y.children[j + degree];
			}
		}
		y.setNumKeys(degree - 1);
		for(int j = currentNode.getNumKeys() + 1; j > index; j--) {
			currentNode.children[j + 1] = currentNode.children[j];
		}
		currentNode.children[index + 1] = z.getAddress();
		for(int j = currentNode.getNumKeys(); j >= index; j--) { // shifting the corresponding keys
			currentNode.keys[j+1] = currentNode.keys[j];
		}
		currentNode.keys[index] = y.keys[degree];
		currentNode.incrementNumKeys(); // updating number of keys 
		if(currentNode.getNumKeys() == degree * 2) {
			System.out.println(currentNode.getNumKeys());
		}
		if(usingCache) {
			cacheWrite(y);
			cacheWrite(z);
			cacheWrite(currentNode);
		} else {
			diskWrite(y);
			diskWrite(z);
			diskWrite(currentNode);							
		}
	}

	/**
	 * 
	 *	Searching the nodes from keys 
	 *  
	 * @param currentNode - the node that being searched from 
	 * @param key - the key to be checked for 
	 * 
	 */
	@Override
	public long search(BTreeNode currentNode, long key) throws IOException {
		int i = 1;
		while(i <= currentNode.getNumKeys() && key > currentNode.keys[i].getKey()) {
			i++;
		}
		if(i <= currentNode.getNumKeys() && key == currentNode.keys[i].getKey()) {
			return(currentNode.keys[i].getFrequency());
		} else if(currentNode.getIsLeaf()) {
			return 0;
		} else {
			BTreeNode nextNode;
			if(usingCache) {
				nextNode = cacheRead(currentNode.children[i]);
			} else {
				nextNode = diskRead(currentNode.children[i]);
			}
			
			return search(nextNode, key);
		}
	}

	/**
	 * 
	 * returning the frequency of the key if it exist
	 * 
	 * @param key - key value of the node
	 * @return - Returns root and key values of node
	 * @throws IOException - Throws IO exception if there are issues
	 */
	public long searchTwo(long key) throws IOException {
        return search(root, key);
    }

	/**
	 * recursively inserting a new key into a node if it's not full
	 * @param currentNode - the node that is being processed 
	 * @param key - key value of the node 
	 * @throws IOException - Throws IO exception if there are issues
	 */
	@Override
	public void insertNonFull(BTreeNode currentNode, long key) throws IOException {
		int i = currentNode.getNumKeys();
		// if node is leaf then increment frequency of or insert sequence
		if(currentNode.getIsLeaf()) {
			for(int j = 1; j <= currentNode.getNumKeys(); j++) {
				if(currentNode.keys[j].getKey() == key) {
					currentNode.keys[j].incrementFrequency();
					if(usingCache) {
						cacheWrite(currentNode);
					} else {
						diskWrite(currentNode);
					}
					return;
				}
			}
			while(i >= 1 && key < currentNode.keys[i].getKey()) {
				if(i == degree * 2) {
					System.out.println();
				}
				currentNode.keys[i+1] = currentNode.keys[i];
				i = i-1;
			}
			currentNode.keys[i+1] = new TreeObject(key, 1);
			currentNode.incrementNumKeys();
			if(currentNode.getNumKeys() == degree * 2) {
				System.out.println(currentNode.getNumKeys());
			}
			if(usingCache) {
				cacheWrite(currentNode);
			} else {
				diskWrite(currentNode);
			}
		} else { // if not leaf increment frequency of sequence, insert it, or find node to insert in
			while(i >= 1 && key < currentNode.keys[i].getKey()) {
				i = i - 1;
			}
			i++;
			if(i-1 <= currentNode.getNumKeys() && i-1 > 0) {
				if(currentNode.keys[i-1].getKey() == key) {
					currentNode.keys[i-1].incrementFrequency();
					if(usingCache) {
						cacheWrite(currentNode);
					} else {
						diskWrite(currentNode);
					}
					return;
				}
			}
			BTreeNode child;
			if(usingCache) {
				child = cacheRead(currentNode.children[i]);
			} else {
				child = diskRead(currentNode.children[i]);
			}
			for(int j = 1; j <= child.getNumKeys(); j++) {
				if(child.keys[j].getKey() == key) {
					child.keys[j].incrementFrequency();
					if(usingCache) {
						cacheWrite(child);
					} else {
						diskWrite(child);
					}
					return;
				}
			}
			if(child.getNumKeys() == (2*degree - 1)) {
				splitChild(currentNode, i);
				if(currentNode.keys[i].getKey() == key) {
					currentNode.keys[i].incrementFrequency();
					if(usingCache) {
						cacheWrite(currentNode);
					} else {
						diskWrite(currentNode);
					}
					return;
				}
				if(key > currentNode.keys[i].getKey()) {
					i++;
				}
			}
			if(usingCache) {
				child = cacheRead(currentNode.children[i]);
			} else {
				child = diskRead(currentNode.children[i]);
			}
			insertNonFull(child, key);
		}
	}

	/**
	 * Creates a dump file of BTree and prints data into it
	 * @param fileName - file name of dump file
	 */
	public void dump(String fileName) {
		try {
			PrintStream stream = new PrintStream(new File(fileName));
			BTreeDump(root, stream);
			System.setOut(stream);
			stream.close();
		} catch(Exception e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Recursively dump nodes in order to dump file
	 * 
	 * @param dumpNode - node being printed 
	 * @param stream - destination to print to
	 * @throws IOException
	 */
	private void BTreeDump(BTreeNode dumpNode, PrintStream stream) throws IOException {
		if(dumpNode.getIsLeaf()) {
			for(int i = 1; i <= dumpNode.getNumKeys(); i++) {
				stream.append(longToSequence(dumpNode.keys[i].getKey()) + " " + dumpNode.keys[i].getFrequency() + "\n");
			}
			return;
		}
		for(int i = 1; i <= dumpNode.getNumKeys(); i++) {
			BTreeNode child;
			if(usingCache) {
				child = cacheRead(dumpNode.children[i]);
			} else {
				child = diskRead(dumpNode.children[i]);			
			}
			BTreeDump(child, stream);
			stream.append(longToSequence(dumpNode.keys[i].getKey()) + " " + dumpNode.keys[i].getFrequency() + "\n");
		}
		BTreeNode child;
		if(usingCache) {
			child = cacheRead(dumpNode.children[dumpNode.getNumKeys() + 1]);
		} else {
			child = diskRead(dumpNode.children[dumpNode.getNumKeys() + 1]);			
		}
		BTreeDump(child, stream);
		
	}

	/**
	 * Helper method for SQL statement
	 * @param statement - SQL statement
	 */
	public void dumpSQL(Statement statement) {
		try {
			BTreeDumpSQL(root, statement);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Recursively prints all nodes inorder to SQL
	 * 
	 * @param dumpNode - node that is being printed out 
	 * @param statement - statement(s) of the SQL 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void BTreeDumpSQL(BTreeNode dumpNode, Statement statement) throws SQLException, IOException {
		if(dumpNode.getIsLeaf()) {
			for(int i = 1; i <= dumpNode.getNumKeys(); i++) {
				String insertString = longToSequence(dumpNode.keys[i].getKey());
				long frequency = dumpNode.keys[i].getFrequency();
				insertString = "insert into sequences values('" + insertString + "', " + frequency + ")";
				statement.executeUpdate(insertString);
			}
			return;
		}
		BTreeNode child;
		for(int i = 1; i <= dumpNode.getNumKeys(); i++) {
			if(usingCache) {
				child = cacheRead(dumpNode.children[i]);
			} else {
				child = diskRead(dumpNode.children[i]);			
			}
			BTreeDumpSQL(child, statement);
			String insertString = longToSequence(dumpNode.keys[i].getKey());
			long frequency = dumpNode.keys[i].getFrequency();
			insertString = "insert into sequences values('" + insertString + "', " + frequency + ")";
			statement.executeUpdate(insertString);
		}
		if(usingCache) {
			child = cacheRead(dumpNode.children[dumpNode.getNumKeys() + 1]);
		} else {
			child = diskRead(dumpNode.children[dumpNode.getNumKeys() + 1]);			
		}
		BTreeDumpSQL(child, statement);
	}

	/**
	 * Method for converting a long integer to a DNA sequence
	 * @param binary - Binary value of sequence
	 * @return - DNA string sequence
	 */
	private String longToSequence(long binary) {
		String returnString = "";
		String numRepresentation = Long.toBinaryString(binary);
		int j  = numRepresentation.length();
		while(j+1 < sequenceLength * 2) {
			returnString+= "a";
			j+= 2;
		}
		if(j < sequenceLength * 2){
			if(numRepresentation.substring(0, 1).equals("1")) {
				returnString += "c";				
			} else {
				returnString += "a";
			}
			numRepresentation = numRepresentation.substring(1);
		}
		String binaryVal;
		while(numRepresentation.length() > 1) {
			binaryVal = numRepresentation.substring(0, 2);
			if(binaryVal.equals("00")) {
				returnString+= "a";
				numRepresentation = numRepresentation.substring(2);
			} else if(binaryVal.equals("01")) {
				returnString += "c";
				numRepresentation = numRepresentation.substring(2);
			} else if(binaryVal.equals("10")) {
				returnString += "g";
				numRepresentation = numRepresentation.substring(2);
			} else if(binaryVal.equals("11")) {
				returnString += "t";
				numRepresentation = numRepresentation.substring(2);
			}
		}
		return returnString;
	}

	/**
	 * 
	 * Creating the metadata from the buffer
	 * 
	 * @throws IOException - Throws IO exception if there are issues
	 */
	private void createMetaData() throws IOException {
		file.position(0);
		buffer.clear();
		buffer.putLong(rootAddress);
		buffer.putLong(nextAddress);
		buffer.putInt(nodeSize);
		buffer.flip();
		file.write(buffer);
		
	}
	/**
	 * 
	 * read the meta data from the buffer
	 * 
	 * @throws IOException - Throws IO exception if there are issues
	 */
	private void readMetaData() throws IOException {
		file.position(0);
		buffer.clear();
		file.read(buffer);
		buffer.flip();
		rootAddress = buffer.getLong();
		nextAddress = buffer.getLong();
		nodeSize = buffer.getInt();
	}

	/**
	 * Method for closing writer for BTree for safety
	 * @throws IOException - Throws IO exception if there are issues
	 */
	public void closeTree() throws IOException {
		if(usingCache) {
			while(cache.getSize() > 0) {
				diskWrite(cache.getEldest().getNode());
			}			
		}
		createMetaData();
	}

	/**
	 * Helper method for closing streams to avoid leaking
	 * @throws IOException - Throws IO exception if there are issues
	 */
	public void close() throws IOException {
		raf.close();
		file.close();
	}
}
