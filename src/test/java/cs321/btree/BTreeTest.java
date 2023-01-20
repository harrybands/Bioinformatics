package cs321.btree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Filename: BTreeTest.java
 * Unit test for BTree
 * @author Brian Heleker
 */
public class BTreeTest
{
	/**
	 * Method for loading test files
	 * @return - File to be loaded in
	 * @throws IOException - Throws error if loading file causes an error
	 */
	public File buildTestFile() throws IOException {
		File f = new File("test.txt");
		ByteBuffer buffer = ByteBuffer.allocateDirect(1000);
		f.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		FileChannel file = raf.getChannel();
		file.position(0);
		buffer.clear();
		buffer.clear();
		buffer.putInt(3);
		buffer.putInt(1);
		for(int i = 1; i <= 3; i++) {
			buffer.putLong(11111 * i);
			buffer.putInt(1);
		}
		for(int i = 1; i <= 4; i++) {
				buffer.putLong(444444*i);
		}
		buffer.flip();
		file.write(buffer);
		raf.close();
		return f;
	}

	/**
	 * Method for comparing data in node to test file
	 * @param node - BTree node for comparison
	 * @param buffer - Raw data from file
	 * @return - Returns bool if test is match
	 */
	public boolean compareNodeToFile(BTreeNode node, ByteBuffer buffer) {
		boolean match = true;
		if(node.getAddress() != buffer.position()) {
			match = false;
		}
		if(node.getNumKeys() != buffer.getInt()) {
			match = false;
		}
		if(node.getIsLeaf()) {
			if(buffer.getInt() != 1) {
				match = false;
			}
		} else {
			if(buffer.getInt() != 0) {
				match = false;
			}
		}
		for(int i = 1; i <= node.getNumKeys(); i++) {
			if(node.keys[i].getKey() != buffer.getLong() || node.keys[i].getFrequency() !=buffer.getLong()) {
				match = false;
			}
		}
		for(int i = 1; i <= node.getNumKeys() + 1; i++) {
			if(node.children[i] != buffer.getLong()) {
				match = false;
			}
		}
		return match;
	}
    // HINT:
    //  instead of checking all intermediate states of constructing a tree
    //  you can check the final state of the tree and
    //  assert that the constructed tree has the expected number of nodes and
    //  assert that some (or all) of the nodes have the expected values

	/**
	 * Method for testing states od degrees
	 */
    @Test
    public void btreeDegree4Test()
    {
//        //TODO instantiate and populate a bTree object
//        int expectedNumberOfNodes = TBD;
//
//        // it is expected that these nodes values will appear in the tree when
//        // using a level traversal (i.e., root, then level 1 from left to right, then
//        // level 2 from left to right, etc.)
//        String[] expectedNodesContent = new String[]{
//                "TBD, TBD",      //root content
//                "TBD",           //first child of root content
//                "TBD, TBD, TBD", //second child of root content
//        };
//
//        assertEquals(expectedNumberOfNodes, bTree.getNumberOfNodes());
//        for (int indexNode = 0; indexNode < expectedNumberOfNodes; indexNode++)
//        {
//            // root has indexNode=0,
//            // first child of root has indexNode=1,
//            // second child of root has indexNode=2, and so on.
//            assertEquals(expectedNodesContent[indexNode], bTree.getArrayOfNodeContentsForNodeIndex(indexNode).toString());
//        }
    }

	/**
	 * Method for testing root building for BTree
	 * @throws IOException - Throws IO exception if there is error in loading file
	 */
    @Test
    public void rootBuildingTest() throws IOException {
        File tF = new File("rootBuildingTest"); 
        BTree test = new BTree(2, 1, tF); 
        test.insert(2);
        test.insert(1);
        test.insert(3);
        tF.delete();
        assert(test.getNodeAt(1).toString().equals("1 2 3"));
    }

	/**
	 * Test for splitting child
	 * @throws IOException - Throws IO exception if there is error in loading file
	 */
    @Test
    public void splitChildTest() throws IOException {
        File tF = new File("splitChildTest");
        String[] string = {"2", "1", "3 4"};
        BTree test = new BTree(2, 1, tF);
        test.insert(2); 
        test.insert(1); 
        test.insert(3); 
        test.insert(4);
        tF.delete();
        for (int i = 1; i < 4; i++) {
            BTreeNode curr = test.getNodeAt(i);
            assertEquals(string[i - 1], curr.toString());
        }
    }

	/**
	 * Test for checking if BTree is empty or not
	 */
	@Test
	public void emptyBTreeTest() {
		File testFile = new File("test.txt");
		BTree testTree = new BTree(72, 4, testFile);
		
		if(testTree.getDegree() != 72 | testTree.getSequenceLength() != 4 | !testTree.getFile().isOpen()) {
			testFile.delete();
			assert(false);
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void emptyBTreeInsert213() {
		File testFile = new File("emptyBTreeInsert213");
		try {
		BTree testTree = new BTree(2,1, testFile);
		
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testFile.delete();
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("1 2 3"));
		} catch (Exception e) {
			e.printStackTrace();
			testFile.delete();
			assert(false);
		}
		
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree123Insert4() {
		File testFile = new File("BTree123Insert4");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testFile.delete();
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3 4"));
		} catch(Exception e) {
			e.printStackTrace();
			testFile.delete();
			fail("exception encountered");
		}
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree1234Insert5() {
		File testFile = new File("BTree1234Insert5");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3 4 5"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree12345Insert6() {
		File testFile = new File("BTree12345Insert6");
		BTree testTree = new BTree(2,1, testFile);;
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2 4"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3"));
			assert(testTree.getNodeAt(4).toString().equals("5 6"));
		} catch(IOException e) {
			testFile.delete();
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree123456Insert7() {
		File testFile = new File("BTree123456Insert7");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2 4"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3"));
			assert(testTree.getNodeAt(4).toString().equals("5 6 7"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree1234567Insert8() {
		File testFile = new File("BTree1234567Insert8");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2 4 6"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3"));
			assert(testTree.getNodeAt(4).toString().equals("5"));
			assert(testTree.getNodeAt(5).toString().equals("7 8"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree1to8Insert9() {
		File testFile = new File("BTree1to8Insert9");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6"));
			assert(testTree.getNodeAt(4).toString().equals("1"));
			assert(testTree.getNodeAt(5).toString().equals("3"));
			assert(testTree.getNodeAt(6).toString().equals("5"));
			assert(testTree.getNodeAt(7).toString().equals("7 8 9"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree1to9Insert1011() {
		File testFile = new File("BTree1to9Insert1011");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6 8"));
			assert(testTree.getNodeAt(4).toString().equals("1"));
			assert(testTree.getNodeAt(5).toString().equals("3"));
			assert(testTree.getNodeAt(6).toString().equals("5"));
			assert(testTree.getNodeAt(7).toString().equals("7"));
			assert(testTree.getNodeAt(8).toString().equals("9 10 11"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree1to11Insert12() {
		File testFile = new File("BTree1to11Insert12");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6 8 10"));
			assert(testTree.getNodeAt(4).toString().equals("1"));
			assert(testTree.getNodeAt(5).toString().equals("3"));
			assert(testTree.getNodeAt(6).toString().equals("5"));
			assert(testTree.getNodeAt(7).toString().equals("7"));
			assert(testTree.getNodeAt(8).toString().equals("9"));
			assert(testTree.getNodeAt(9).toString().equals("11 12"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting static key values into tree
	 */
	@Test
	public void BTree1to12Insert13() {
		File testFile = new File("BTree1to12Insert13");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.insert(13);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4 8"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6"));
			assert(testTree.getNodeAt(4).toString().equals("10"));
			assert(testTree.getNodeAt(5).toString().equals("1"));
			assert(testTree.getNodeAt(6).toString().equals("3"));
			assert(testTree.getNodeAt(7).toString().equals("5"));
			assert(testTree.getNodeAt(8).toString().equals("7"));
			assert(testTree.getNodeAt(9).toString().equals("9"));
			assert(testTree.getNodeAt(10).toString().equals("11 12 13"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}


	/**
	 * Tester for inserting values into BTree and searching for them
	 */
	@Test
	public void BTree1to13three7Search7() {
		File testFile = new File("BTree1to12Insert13");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(7);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.insert(13);
			testTree.closeTree();
			assert(testTree.search(testTree.getNodeAt(1), 7) == 3);

		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void emptyBTreeInsert213Cache() {
		File testFile = new File("emptyBTreeInsert213Cache");
		try {
		BTree testTree = new BTree(2,1, testFile, true, 100);
		
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testFile.delete();
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("1 2 3"));
		} catch (Exception e) {
			e.printStackTrace();
			testFile.delete();
			assert(false);
		}
		
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree123Insert4Cache() {
		File testFile = new File("BTree123Insert4Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testFile.delete();
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3 4"));
		} catch(Exception e) {
			e.printStackTrace();
			testFile.delete();
			fail("exception encountered");
		}
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree1234Insert5Cache() {
		File testFile = new File("BTree1234Insert5Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3 4 5"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree12345Insert6Cache() {
		File testFile = new File("BTree12345Insert6Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);;
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2 4"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3"));
			assert(testTree.getNodeAt(4).toString().equals("5 6"));
		} catch(IOException e) {
			testFile.delete();
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree123456Insert7Cache() {
		File testFile = new File("BTree123456Insert7Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2 4"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3"));
			assert(testTree.getNodeAt(4).toString().equals("5 6 7"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree1234567Insert8Cache() {
		File testFile = new File("BTree1234567Insert8Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("2 4 6"));
			assert(testTree.getNodeAt(2).toString().equals("1"));
			assert(testTree.getNodeAt(3).toString().equals("3"));
			assert(testTree.getNodeAt(4).toString().equals("5"));
			assert(testTree.getNodeAt(5).toString().equals("7 8"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree1to8Insert9Cache() {
		File testFile = new File("BTree1to8Insert9Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6"));
			assert(testTree.getNodeAt(4).toString().equals("1"));
			assert(testTree.getNodeAt(5).toString().equals("3"));
			assert(testTree.getNodeAt(6).toString().equals("5"));
			assert(testTree.getNodeAt(7).toString().equals("7 8 9"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree1to9Insert1011Cache() {
		File testFile = new File("BTree1to9Insert1011Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6 8"));
			assert(testTree.getNodeAt(4).toString().equals("1"));
			assert(testTree.getNodeAt(5).toString().equals("3"));
			assert(testTree.getNodeAt(6).toString().equals("5"));
			assert(testTree.getNodeAt(7).toString().equals("7"));
			assert(testTree.getNodeAt(8).toString().equals("9 10 11"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree1to11Insert12Cache() {
		File testFile = new File("BTree1to11Insert12Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6 8 10"));
			assert(testTree.getNodeAt(4).toString().equals("1"));
			assert(testTree.getNodeAt(5).toString().equals("3"));
			assert(testTree.getNodeAt(6).toString().equals("5"));
			assert(testTree.getNodeAt(7).toString().equals("7"));
			assert(testTree.getNodeAt(8).toString().equals("9"));
			assert(testTree.getNodeAt(9).toString().equals("11 12"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting values into cache
	 */
	@Test
	public void BTree1to12Insert13Cache() {
		File testFile = new File("BTree1to12Insert13Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.insert(13);
			testTree.closeTree();
			assert(testTree.getNodeAt(1).toString().equals("4 8"));
			assert(testTree.getNodeAt(2).toString().equals("2"));
			assert(testTree.getNodeAt(3).toString().equals("6"));
			assert(testTree.getNodeAt(4).toString().equals("10"));
			assert(testTree.getNodeAt(5).toString().equals("1"));
			assert(testTree.getNodeAt(6).toString().equals("3"));
			assert(testTree.getNodeAt(7).toString().equals("5"));
			assert(testTree.getNodeAt(8).toString().equals("7"));
			assert(testTree.getNodeAt(9).toString().equals("9"));
			assert(testTree.getNodeAt(10).toString().equals("11 12 13"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for searching values into cache
	 */
	@Test
	public void BTree1to13three7Search7Cache() {
		File testFile = new File("BTree1to12Insert13Cache");
		BTree testTree = new BTree(2,1, testFile, true, 100);
		try {
			testTree.insert(2);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(5);
			testTree.insert(6);
			testTree.insert(7);
			testTree.insert(7);
			testTree.insert(7);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.insert(13);
			testTree.closeTree();
			assert(testTree.search(testTree.getNodeAt(1), 7) == 3);

		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting key values into BTree dump file
	 */
	@Test
	public void BTreeTestDump() {
		File testFile = new File("BTree1testDump1");
		BTree testTree = new BTree(2,1, testFile);
		try {
			testTree.insert(1);
			testTree.insert(0);
			testTree.insert(3);
			testTree.insert(2);
			testTree.insert(3);
			testTree.insert(0);
			testTree.insert(0);
			testTree.insert(1);
			testTree.insert(3);
			testTree.insert(1);
			testTree.closeTree();

		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}

	/**
	 * Tester for inserting key values into BTree dump file
	 */
	@Test
	public void BTreeTestDump2() {
		File testFile = new File("BTree1testDump12");
		BTree testTree = new BTree(2,2, testFile);
		try {
			testTree.insert(0);
			testTree.insert(1);
			testTree.insert(2);
			testTree.insert(2);
			testTree.insert(3);
			testTree.insert(3);
			testTree.insert(3);
			testTree.insert(4);
			testTree.insert(8);
			testTree.insert(9);
			testTree.insert(10);
			testTree.insert(11);
			testTree.insert(12);
			testTree.closeTree();

		} catch(IOException e) {
			e.printStackTrace();
		}
		testFile.delete();
	}
}
