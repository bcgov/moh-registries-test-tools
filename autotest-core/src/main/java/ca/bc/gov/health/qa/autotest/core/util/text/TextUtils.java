package ca.bc.gov.health.qa.autotest.core.util.text;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utilities for processing text.
 */
public class TextUtils
{
    /**
     * Carriage return (CR) line ending ({@code "\r"}).
     */
    public static final String CR = "\r";

    /**
     * Line feed (LF) line ending ({@code "\n"}).
     */
    public static final String LF = "\n";

    /**
     * Carriage return and line feed (CR LF) line ending ({@code "\r\n"}).
     */
    public static final String CRLF = CR + LF;

    /**
     * TODO (AZ) - doc
     */
    public static final String MASK_INDICATOR = "*".repeat(8);

    private static final Set<String> DEFAULT_MASKED_HEADER_NAME_SET = Set.of("authorization");

    private static final String TABLE_COLUMN_SEPARATOR = " ";
    private static final String TABLE_NULL_TEXT        = "null";
    private static final String TABLE_ROW_SEPARATOR    = LF;

    private TextUtils()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param list1
     *        ???
     *
     * @param list2
     *        ???
     *
     * @return ???
     *
     * @throws NullPointerException
     *         if {@code list1} or {@code list2} is {@code null},
     *         or a {@code null} element is encountered
     */
    public static int compareStringLists(List<String> list1, List<String> list2)
    {
        requireNonNull(list1, "Null list 1.");
        requireNonNull(list2, "Null list 2.");
        int result = 0;
        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++)
        {
            result = list1.get(i).compareTo(list2.get(i));
            if (result != 0)
            {
                break;
            }
        }
        if (result == 0)
        {
            result = Integer.compare(list1.size(), list2.size());
        }
        return result;
    }

    /**
     * Formats header data into a textual representation.
     * TODO (AZ) - doc masking of header values (default header names)
     *
     * <p>The header data consists of a map of header names to lists of header values.
     *
     * @param headerMap
     *        a map of headers to format
     *
     * @return a string containing a textual representation of the formatted header data
     */
    public static String formatHeaders(Map<String,List<String>> headerMap)
    {
        return formatHeaders(headerMap, DEFAULT_MASKED_HEADER_NAME_SET);
    }

    /**
     * Formats header data into a textual representation.
     * TODO (AZ) - doc masking of header values
     *
     * <p>The header data consists of a map of header names to lists of header values.
     *
     * @param headerMap
     *        a map of headers to format
     *
     * @param maskedHeaderNameSet
     *        a set of header names whose values to mask
     *
     * @return a string containing a textual representation of the formatted header data
     */
    public static String formatHeaders(
            Map<String,List<String>> headerMap, Set<String> maskedHeaderNameSet)
    {
        requireNonNull(headerMap,           "Null header map.");
        requireNonNull(maskedHeaderNameSet, "Null masked header name set.");
        List<List<String>> tableData = new ArrayList<>();
        for (Entry<String,List<String>> entry : headerMap.entrySet())
        {
            String name = entry.getKey();
            if (!maskedHeaderNameSet.contains(name.toLowerCase(Locale.ROOT)))
            {
                for (String value : entry.getValue())
                {
                    tableData.add(List.of(name, ":", value));
                }
            }
            else
            {
                tableData.add(List.of(name, ":", MASK_INDICATOR));
            }
        }
        return formatTable(tableData);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param stringMap
     *        ???
     *
     * @return ???
     */
    public static String formatStringMap(Map<String,String> stringMap)
    {
        List<List<String>> tableData = new ArrayList<>();
        for (Entry<String,String> entry: stringMap.entrySet())
        {
            tableData.add(List.of(entry.getKey(), ":", entry.getValue()));
        }
        return formatTable(tableData);
    }

    /**
     * Formats table data into a textual representation.
     *
     * <p>The table data consists of a list of rows,
     * where each row contains a list of horizontal cells.
     *
     * @param tableData
     *        the table data to format
     *
     * @return a string containing a textual representation of the formatted table data
     */
    public static String formatTable(List<List<String>> tableData)
    {
        // Determine column widths.
        List<Integer> columnWidthList = new ArrayList<>();
        for (List<String> cellList : tableData)
        {
            for (int i = 0; i < cellList.size(); i++)
            {
                String cell = requireNonNullElse(cellList.get(i), TABLE_NULL_TEXT);
                int width = cell.length();
                if (columnWidthList.size() <= i)
                {
                    columnWidthList.add(0);
                }
                if (width > columnWidthList.get(i))
                {
                    columnWidthList.set(i, width);
                }
            }
        }

        // Format table data.
        StringBuilder tableBuilder = new StringBuilder();
        for (List<String> cellList : tableData)
        {
            for (int i = 0; i < cellList.size(); i++)
            {
                String cell = requireNonNullElse(cellList.get(i), TABLE_NULL_TEXT);
                if (i < cellList.size() - 1)
                {
                    tableBuilder
                            .append(String.format("%-" + columnWidthList.get(i) + "s", cell))
                            .append(TABLE_COLUMN_SEPARATOR);
                }
                else
                {
                    tableBuilder.append(cell);
                }
            }
            tableBuilder.append(TABLE_ROW_SEPARATOR);
        }

        return tableBuilder.toString();
    }

    /**
     * Normalize all lines of text using the line feed (LF) line ending.
     *
     * @param text
     *        the text whose lines to normalize
     *
     * @return a string containing the normalized text
     *
     * @throws NullPointerException
     *         if {@code text} is {@code null}
     */
    public static String normalizeLines(CharSequence text)
    {
        return normalizeLines(text, LF);
    }

    /**
     * Normalize all lines of text using the specified line ending.
     *
     * @param text
     *        the text whose lines to normalize
     *
     * @param lineEnding
     *        the line ending to use
     *
     * @return a string containing the normalized text
     *
     * @throws NullPointerException
     *         if either {@code text} or {@code lineEnding} is {@code null}
     */
    public static String normalizeLines(CharSequence text, String lineEnding)
    {
        requireNonNull(text,       "Null text.");
        requireNonNull(lineEnding, "Null line ending.");
        return text.toString().lines().collect(Collectors.joining(lineEnding, "", lineEnding));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param string
     *        ???
     *
     * @return ???
     */
    public static String toMixedCase(String string)
    {
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            builder.append((i % 2 == 0) ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return builder.toString();
    }
}
