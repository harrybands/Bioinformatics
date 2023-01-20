package cs321.create;

import cs321.Utils;
import org.junit.Test;

import java.util.List;

/**
 * Filename: SequenceTest.java
 * Unit test for Sequence utils
 * Tests by inserting long integers or string into each method and compares if
 * expected values are correct or not
 * @author Joshua Corrales
 */

import static org.junit.Assert.*;

public class SequenceUtilsTest
{
    /**
     * Tester for checking if converting a long sequence to DNA String works
     * @throws Exception - Throws exeption if there is an error in converting values
     */
    @Test
    public void longToDNAStringTest() throws Exception
    {
        String[] testStrings = {"aaaa", "aaat", "actt", "tgcact"};
        long[] testLongs = {0, 3, 31, 3655};

        assertEquals(testStrings[0], SequenceUtils.longToDNAString(testLongs[0], 4));
        assertEquals(testStrings[1], SequenceUtils.longToDNAString(testLongs[1], 4));
        assertEquals(testStrings[2], SequenceUtils.longToDNAString(testLongs[2], 4));
        assertEquals(testStrings[3], SequenceUtils.longToDNAString(testLongs[3], 6));
	}

    /**
     * Tester for checking if conversion from DNA String to long int sequence works
     * @throws Exception - Throws exception if there is an error in converting values
     */
    @Test
    public void DNAStringToLongTest() throws Exception
    {
        long[] targetLong = {0, 255, 85, 170, 54};
        String[] testString = {"aaaa", "tttt", "cccc", "gggg", "atcg"};

        for (int i = 0; i < targetLong.length; i++)
        {
            assertEquals(targetLong[i], SequenceUtils.DNAStringToLong(testString[i]));
        }
	}

    /**
     * Test for checking if conversion from long int to complement sequence works
     * @throws Exception - Throws exception if there is an error in converting values
     */
	@Test
	public void getComplementTest() throws Exception
	{
        long[] targetLong = {11111111, 00000000, 10101010, 1010101, 11001001}; //testing aaaa, tttt, cccc, gggg, atcg
        long[] testLong = {0, 255, 85, 170, 54};


        for (int i = 0; i < targetLong.length; i++)
        {
            assertEquals(targetLong[i], SequenceUtils.getComplement(testLong[i], 4));
        }

	}
}
