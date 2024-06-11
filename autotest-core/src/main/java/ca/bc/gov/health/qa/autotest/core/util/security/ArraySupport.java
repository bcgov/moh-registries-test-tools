package ca.bc.gov.health.qa.autotest.core.util.security;

import java.util.Arrays;

/**
 * TODO (AZ) - doc
 */
public class ArraySupport
{
    /**
     * TODO (AZ) - doc
     */
    public static final int NOT_FOUND = -1;

    /**
     * TODO (AZ) - doc
     *
     * @param array
     *        ???
     */
    public static void clear(char[] array)
    {
        if (array != null)
        {
            Arrays.fill(array, '\0');
        }
    }

    /**
     * TODO (AZ) - doc
     *
     * @param array
     *        ???
     *
     * @return ???
     */
    public static char[] copy(char[] array)
    {
        char[] copy;
        if (array != null)
        {
            copy = Arrays.copyOf(array, array.length);
        }
        else
        {
            copy = null;
        }
        return copy;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param array
     *        ???
     *
     * @param value
     *        ???
     *
     * @return ???
     */
    public static int findFirstIndex(char[] array, char value)
    {
        int index = NOT_FOUND;
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == value)
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param value
     *        ???
     *
     * @return ???
     */
    public static char[] getCharArray(String value)
    {
        char[] array;
        if (value != null)
        {
            array = value.toCharArray();
        }
        else
        {
            array = null;
        }
        return array;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param array
     *        ???
     *
     * @param index
     *        ???
     *
     * @param ending
     *        ???
     *
     * @return ???
     */
    public static char[] split(char[] array, int index, boolean ending)
    {
        char[] partArray;
        if (array != null)
        {
            if (!ending)
            {
                partArray = Arrays.copyOfRange(array, 0, index);
            }
            else
            {
                partArray = Arrays.copyOfRange(array, index, array.length);
            }
        }
        else
        {
            partArray = null;
        }
        return partArray;
    }

    private ArraySupport()
    {}
}
