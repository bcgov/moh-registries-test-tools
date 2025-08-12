package ca.bc.gov.health.qa.autotest.core.util.data;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.opencsv.CSVWriter;

/**
 * Utilities for reading and writing CSV.
 */
public class CsvUtils
{
    /**
     * Writes data to a CSV file.
     * <p>
     * The data to write consists of a list of rows,
     * where each row consists of a list of values.
     *
     * @param data
     *        the data to write
     *
     * @param path
     *        a path to the CSV file to write to
     *
     * @param forceOverwrite
     *        a flag, if {@code true}, indicates that the
     *        CSV file is overwritten if it already exists,
     *        otherwise an {@code IOException} is thrown
     *        if the file already exists.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static void writeCsvFile(List<List<String>> data, Path path, boolean forceOverwrite)
    throws IOException
    {
        OpenOption[] options;
        if (forceOverwrite)
        {
            options = new OpenOption[]
                    {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        }
        else
        {
            options = new OpenOption[]
                    {StandardOpenOption.CREATE_NEW};
        }
        try (Writer    writer    = Files.newBufferedWriter(path, options);
             CSVWriter csvWriter = new CSVWriter(writer))
        {
            for (List<String> rowData : data)
            {
                String[] values = rowData.toArray(new String[rowData.size()]);
                csvWriter.writeNext(values, true);
            }
            if (csvWriter.checkError())
            {
                String msg = String.format("Error writing CSV file (%s).", path);
                throw new IOException(msg);
            }
        }
    }

    private CsvUtils()
    {}
}
