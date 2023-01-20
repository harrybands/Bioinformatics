package cs321.btree;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Filename: BTreeCache.java
 * Cache class for storing generic data types with a LinkedList
 * implementation. Maximum size is specified upon construction
 * and least-recently-used objects are removed before new
 * ones are added if that size is reached. If an object is
 * attempted to be re-added, it is removed from the cache
 * then added to the front. Number of cache hits and
 * references is recorded.
 *
 * @author Brian Heleker
 */
public class BTreeCache {
    private final int MAX_SIZE;
    private int cacheSize;
    private int cacheReferences;
    private int cacheHits;
    private LinkedHashMap<Long, BTreeCacheObject> cache;
    public LinkedList<Long> addresses;

    /**
     * Constructor for cache. Overrides removeEldestEntry
     * to ensure that cache does not become bigger than
     * specified in the provided size value.
     *
     * @param size - Maximum size of the cache
     */
    @SuppressWarnings("serial")
	public BTreeCache(int size) {
        MAX_SIZE = size;
        cacheSize = cacheReferences = cacheHits = 0;
        addresses = new LinkedList<>();
        cache = new LinkedHashMap<Long, BTreeCacheObject>(size) {
            protected boolean removeEldestEntry(Map.Entry<Long, BTreeCacheObject> eldest) {
                return size() > MAX_SIZE;
            }
        };
    }

    /**
     * Method to retrieve an object from the cache. If
     * Object is already in cache, cacheHits is incremented
     * and the object is removed and added to the front of
     * the cache. If the object is not in the cache, it is
     * added to the cache. If a new object is added to the
     * cache, but it is already at maximum size, the oldest
     * object is removed from the cache. cacheReferences is
     * always incremented.
     *
     * @param element - object to be added to the class
     * @return returnElement - object returned from the cache
     */
    public BTreeCacheObject getObject(BTreeCacheObject element) {
        BTreeCacheObject returnElement = element;

        cacheReferences++;

        if(contains(element)) {
            cacheHits++;
            removeObject(element);
            addObject(element);
        }
        else {
            if(cacheSize >= MAX_SIZE) {
                cache.remove(element.getKey());
                addresses.removeLast();
                cacheSize--;
            }
            addObject(element);
        }
        return returnElement;
    }
    
    /**
     * Method to retrieve an object from the cache. If
     * Object is already in cache, cacheHits is incremented
     * and the object is removed and added to the front of
     * the cache. If the object is not in the cache, it is
     * added to the cache. If a new object is added to the
     * cache, but it is already at maximum size, the oldest
     * object is removed from the cache. cacheReferences is
     * always incremented.
     *
     * @param address - address of BTreeCacheObject to be added to the class
     * @return returnElement - BTreeCacheObject returned from the cache
     */
    public BTreeCacheObject getObject(long address) {

        BTreeCacheObject returnElement;
        cacheReferences++;

        cacheHits++;
        returnElement = cache.get(address);
        removeObject(returnElement);
        addObject(returnElement);

        return returnElement;
    }

    /**
     * Adds specified object to the front of the cache.
     *
     * @param element - Object to be added to cache
     */
    private void addObject(BTreeCacheObject element) {
        cache.put(element.getKey(), element);
        addresses.addFirst(element.getKey());
        cacheSize++;
    }
    
    /**
     * Returns the eldest node in the cache
     * @return eldest node
     */
    public BTreeCacheObject getEldest() {
    	cacheSize--;
    	long key = addresses.removeLast();
    	return cache.remove(key);
    }

    /**
     * Getter method for getting cache size
     * @return - cache size
     */
    public int getSize() {
    	return cache.size();
    }

    /**
     * Getter method for getting address
     * @param address - address location from file
     * @return - address location
     */
    public boolean contains(long address) {
    	return (cache.containsKey(address));
    }

    /**
     * Getter method for getting node
     * @param node - Node with key value
     * @return - Node with key value
     */
    public boolean contains(BTreeCacheObject node) {
    	return (cache.containsKey(node.getKey()));
    }

    /**
     * Removes an object from the cache
     *
     * @param element - Object to be removed from the cache
     */
    private void removeObject(BTreeCacheObject element) {
    	cache.remove(element.getKey());
    	addresses.remove(element.getKey());
        cacheSize--;
    }

    /**
     * Clears all contents from the cache
     *
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Overridden toString for the class that prints
     * the stats with formatting.
     *
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        returnString.append("LinkedList Cache with ");
        returnString.append(MAX_SIZE);
        returnString.append(" entries has been created\n");
        returnString.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        returnString.append("Total number of references:        ");
        returnString.append(cacheReferences);
        returnString.append("\n");
        returnString.append("Total number of cache hits:        ");
        returnString.append(cacheHits);
        returnString.append("\n");
        returnString.append("1st-level cache hit ratio:         ");
        returnString.append(cacheHits / 1.0 / cacheReferences);
        returnString.append("\n");

        return returnString.toString();
    }
}