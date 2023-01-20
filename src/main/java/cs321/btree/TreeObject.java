package cs321.btree;

/**
 * Filename: TreeObject.java
 * Objects that can be stored in BTree Node
 * 
 * @author Brian Helekler
 */
public class TreeObject
{
	private long key;
	private int frequency;
	/**
	 * 
	 * Objects that being stored in B Tree
	 * 
	 * @param key - the key of the TreeObject 
	 * @param frequency - frequency of the TreeObject
	 */
	public TreeObject(long key, int frequency) {
		this.key = key;
		this.frequency = frequency;
	}

	/**
	 * 
	 * increase the frequency variables by one. 
	 * 
	 */
	public void incrementFrequency() {
		frequency++;
	}
	
	/**
	 * getter for frequency
	 * @return frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * getter for key
	 * @return key
	 */
	public long getKey() {
		return key;
	}
	
}
