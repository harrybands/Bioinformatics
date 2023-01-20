package cs321.create;

import cs321.common.ParseArgumentException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

/**
 * Filename: GeneBankCreateBTreeTest.java
 * Unit test for Creating BTree
 * @author Brian Heleker
 */
public class GeneBankCreateBTreeTest
{
    private String[] args;
    private GeneBankCreateBTreeArguments expectedConfiguration;
    private GeneBankCreateBTreeArguments actualConfiguration;

    /**
     * Test for testing parser for arguments
     * @throws ParseArgumentException - Throws when parsing arguments causes issues
     */
    @Test
    public void parse4CorrectArgumentsTest() throws ParseArgumentException
    {
        args = new String[4];
        args[0] = "0";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";

        expectedConfiguration = new GeneBankCreateBTreeArguments(0, 20, "fileNameGbk.gbk", 13, 0, 0);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    

}
