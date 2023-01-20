package cs321.btree;

/**
 * Filename: BTreeCacheObject
 * Objects from BTree Cache 
 * 
 * @author Brian Helekler 
 * 
 */
public class BTreeCacheObject {
	private long key;
	private BTreeNode node;

	/**
	 * 
	 * Constructor of object from B Tree Cache
	 * 
	 * @param key
	 * @param node
	 */
	public BTreeCacheObject(long key, BTreeNode node) {
		this.key = key;
		this.node = node;
	}
	
	/**
	 * 
	 * Getting the key's variable 
	 * 
	 * @return key value
	 */
	public long getKey() {
		return key;
	}

	/**
	 * 
	 * Getting the node variable
	 * 
	 * @return node
	 */
	public BTreeNode getNode() {
		return node;
	}
}
