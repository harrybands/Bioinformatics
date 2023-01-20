package cs321.btree;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Filename: BTreeNode
 * Implementing the BTree node
 * @author Brian Heleker
 * @author Harry Nquyen
 */
public class BTreeNode {
	private int numKeys;
	private boolean isLeaf;
	private long address;
	TreeObject[] keys;
	long[] children;

	/**
	 * default constructor for BTreeNode
	 * @param treeDegree - Degree of tree
	 * @param address - Long integer representation of address
	 */
	public BTreeNode(int treeDegree, long address) {
		numKeys = 0;
		keys = new TreeObject[2*treeDegree];
		children = new long[2*treeDegree+1];
		for(int i = 1; i <= 2*treeDegree; i++) {
			children[i] = -1;
		}
		
		this.address = address;
	}

	/**
	 * Overloaded constructor for BTreeNode
	 * @param address - Long integer representation of address
	 * @param buffer - Byte array of sequences
	 * @param degree - Degree of BTree
	 * @throws IOException - Throws IO exception if there are issues
	 */
	public BTreeNode(long address, ByteBuffer buffer, int degree) throws IOException {
		keys = new TreeObject[2*degree];
		children = new long[2*degree+1];
		for(int i = 1; i <= 2*degree; i++) {
			children[i] = -1;
		}
		this.address = address;
		numKeys = buffer.getInt();
		if (buffer.getInt() == 1) {
			isLeaf = true;
		} else {
			isLeaf = false;
		}
		
		for(int i = 1; i <= numKeys; i++) {
			long insertKey = buffer.getLong();
			int insertFrequency = buffer.getInt();
			TreeObject tempObject = new TreeObject(insertKey, insertFrequency);
			keys[i] = tempObject;
		}
		for(int i = 1; i <= numKeys + 1; i++) {
			children[i] = buffer.getLong();
		}
	}

	/**
	 * Method for serializing buffer
	 * @param buffer - Byte array of data
	 */
	public void serialize(ByteBuffer buffer) {		
		buffer.clear();
		buffer.putInt(numKeys);
		if(isLeaf) {
		buffer.putInt(1);
		} else {
			buffer.putInt(0);
		}		
		for(int i = 1; i <= numKeys; i++) {
			buffer.putLong(keys[i].getKey());
			buffer.putInt(keys[i].getFrequency());
		}
		for(int i = 1; i <= numKeys + 1; i++) {
				buffer.putLong(children[i]);
		}
	}
	
	/**
	 * getter for numKeys
	 * @return numKeys
	 */
	public int getNumKeys() {
		return numKeys;
	}
	/**
	 * increments numKeys
	 */
	public void incrementNumKeys() {
		numKeys++;
	}
	
	/**
	 * setter for numKeys
	 * @param num
	 */
	public void setNumKeys(int num) {
		numKeys = num;
	}
	
	/**
	 * getter for isLeaf
	 * @return isLeaf
	 */
	public boolean getIsLeaf() {
		return isLeaf;
	}
	
	/**
	 * setter for isLeaf
	 * @param leaf - if is leaf or not
	 */
	public void setIsLeaf(boolean leaf) {
		isLeaf = leaf;
	}

	/**
	 * getter for address
	 * @return address
	 */
	public long getAddress() {
		return address;
	}
	
	
	/**
	 * toString method for printing stat and other relevant information
	 * @return - Relevant information formatted as string
	 */
	public String toString() {
		String returnString = "";
		for(int i = 1; i <= numKeys; i++) {
			returnString += keys[i].getKey();
			if(i < numKeys) {
				returnString += " ";
			}
		}
		return returnString;
	}
}