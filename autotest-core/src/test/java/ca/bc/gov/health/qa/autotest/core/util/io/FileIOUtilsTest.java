package ca.bc.gov.health.qa.autotest.core.util.io;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class FileIOUtilsTest
{
    public FileIOUtilsTest()
    {}

    @Test
    public void testSanitizeFileName()
    {
        assertEquals(FileIOUtils.sanitizeFileName("test"),     "test");
        assertEquals(FileIOUtils.sanitizeFileName("Test1"),    "Test1");
        assertEquals(FileIOUtils.sanitizeFileName("Test.,-1"), "Test.,-1");

        assertEquals(FileIOUtils.sanitizeFileName("Test2!"),      "Test2");
        assertEquals(FileIOUtils.sanitizeFileName("@Test2!"),     "Test2");
        assertEquals(FileIOUtils.sanitizeFileName("@Test$2!"),    "Test_2");
        assertEquals(FileIOUtils.sanitizeFileName("@@Test$$2!!"), "Test_2");

        assertEquals(FileIOUtils.sanitizeFileName("Test/3"),      "Test_3");
        assertEquals(FileIOUtils.sanitizeFileName("/Test/3/"),    "Test_3");
        assertEquals(FileIOUtils.sanitizeFileName("Test\\3"),     "Test_3");
        assertEquals(FileIOUtils.sanitizeFileName("\\Test\\3\\"), "Test_3");

        assertEquals(FileIOUtils.sanitizeFileName("Test 4"),          "Test_4");
        assertEquals(FileIOUtils.sanitizeFileName("Test  4"),         "Test_4");
        assertEquals(FileIOUtils.sanitizeFileName("  Test  4  "),     "Test_4");
        assertEquals(FileIOUtils.sanitizeFileName("  !Test$  ^4%  "), "Test_4");

        assertEquals(FileIOUtils.sanitizeFileName("Test_5"),      "Test_5");
        assertEquals(FileIOUtils.sanitizeFileName("_Test_5_"),    "Test_5");
        assertEquals(FileIOUtils.sanitizeFileName("__Test__5__"), "Test_5");

        assertEquals(FileIOUtils.sanitizeFileName("_"),   "_");
        assertEquals(FileIOUtils.sanitizeFileName("__"),  "_");
        assertEquals(FileIOUtils.sanitizeFileName("___"), "_");

        assertEquals(FileIOUtils.sanitizeFileName(""),   "_");
        assertEquals(FileIOUtils.sanitizeFileName(" "),  "_");
        assertEquals(FileIOUtils.sanitizeFileName("  "), "_");

        assertEquals(FileIOUtils.sanitizeFileName("."),   "_");
        assertEquals(FileIOUtils.sanitizeFileName(".."),  "_");
        assertEquals(FileIOUtils.sanitizeFileName("..."), "...");

        assertEquals(FileIOUtils.sanitizeFileName(" . "),   "_");
        assertEquals(FileIOUtils.sanitizeFileName(" .. "),  "_");
        assertEquals(FileIOUtils.sanitizeFileName(" . . "), "._.");
        assertEquals(FileIOUtils.sanitizeFileName(" ... "), "...");
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testSanitizeFileNameNull()
    {
        FileIOUtils.sanitizeFileName(null);
    }
}
