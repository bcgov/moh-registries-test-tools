package ca.bc.gov.health.qa.autotest.core.util.data;

import static java.util.Objects.requireNonNull;

import java.util.List;

/**
 * Utilities for processing tabular data.
 */
public class TableUtils
{
    private TableUtils()
    {}

    /**
     * Validates tabular data.
     * <p>
     * Valid tabular data consists of zero or more rows of equal size.
     * Each row must contain at least one value,
     * and none of the values can be {@code null}.
     *
     * @param data
     *        the tabular data to validate
     *
     * @return {@code data}
     *
     * @throws IllegalStateException
     *         if the validation fails
     *
     * @throws NullPointerException
     *         if {@code data} is {@code null}, or
     *         if the tabular data contains a {@code null} row
     *         or a {@code null} value
     */
    public static List<List<String>> validateTabularData(List<List<String>> data)
    {
        int columnCount = 0;
        requireNonNull(data, "Null table data.");
        int rowNumber = 0;
        for (List<String> rowData : data)
        {
            rowNumber ++;
            if (rowData == null)
            {
                String msg = String.format("Null tabular data row (row:%d).", rowNumber);
                throw new NullPointerException(msg);
            }
            if (rowData.isEmpty())
            {
                String msg = String.format("Empty tabular data row (row:%d).", rowNumber);
                throw new IllegalStateException(msg);
            }
            int rowSize = rowData.size();
            if (rowNumber == 1)
            {
                columnCount = rowSize;
            }
            if (rowSize != columnCount)
            {
                String msg = String.format(
                        "Invalid tabular data row size (actual:%d; expected:%d; row:%d).",
                        rowSize,
                        columnCount,
                        rowNumber);
                throw new IllegalStateException(msg);
            }
            int columnNumber = 0;
            for (String value : rowData)
            {
                columnNumber++;
                if (value == null)
                {
                    String msg = String.format(
                            "Null tabular data value (row:%d; column:%d).",
                            rowNumber,
                            columnNumber);
                    throw new NullPointerException(msg);
                }
            }
        }
        return data;
    }
}
