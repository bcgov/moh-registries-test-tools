package ca.bc.gov.health.qa.alm2xray;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ca.bc.gov.health.qa.autotest.core.util.io.ProjectInfo;


/**
 * TODO (AZ) - doc
 */
public class Alm2Xray
{
    private static enum CommandMode
    {
        CONVERT,
        HELP,
        VERSION;
    };

    /**
     * TODO (AZ) - doc
     *
     * @param args
     *        ???
     */
    public static void main(String[] args)
    {
        Options options = new Options()
                .addOption(Option
                        .builder("f")
                        .longOpt("force")
                        .desc("force overwrite existing file")
                        .build())
                .addOption(Option
                        .builder("h")
                        .longOpt("help")
                        .desc("shows help information")
                        .build())
                .addOption(Option
                        .builder("i")
                        .longOpt("input")
                        .hasArg()
                        .argName("file")
                        .desc("input file")
                        .build())
                .addOption(Option
                        .builder("o")
                        .longOpt("output")
                        .hasArg()
                        .argName("file")
                        .desc("output file")
                        .build())
                .addOption(Option
                        .builder("v")
                        .longOpt("version")
                        .desc("shows version information")
                        .build());
        String artifactId = Alm2XrayConverter.getProjectInfo().getArtifactId();
        try
        {
            CommandLine cli = new DefaultParser().parse(options, args);
            if (!cli.getArgList().isEmpty())
            {
                throw new IllegalArgumentException("Too many arguments.");
            }
            switch(getCommandMode(cli))
            {
                case CONVERT:
                {
                    Path    inputPath      = getPath(cli, "i", "input file");
                    Path    outputPath     = getPath(cli, "o", "output file");
                    boolean forceOverwrite = cli.hasOption("f");
                    convert(inputPath, outputPath, forceOverwrite);
                    break;
                }

                case HELP:
                {
                    printHelpInfo(options, false);
                    break;
                }

                case VERSION:
                {
                    printVersionInfo();
                    break;
                }
            }
        }
        catch (IllegalArgumentException | ParseException e)
        {
            exitWithError(e.getMessage(), options);
        }
        catch (IOException e)
        {
            String msg = "I/O ERROR";
            System.err.println(artifactId + ": " + msg);
            throw new IllegalStateException(msg, e);
        }
        catch (Throwable t)
        {
            System.err.println(artifactId + ": ERROR");
            throw t;
        }
    }

    private Alm2Xray()
    {}

    private static void convert(Path inputPath, Path outputPath, boolean forceOverwrite)
    throws IOException
    {
        printVersionInfo();

        // FIXME
        System.out.printf("Converting (%s) -> (%s) ...%n", inputPath, outputPath);

        // FIXME
        Alm2XrayConverter converter = new Alm2XrayConverter();
        converter.output(outputPath, forceOverwrite);

        System.out.println("Done.");
    }

    private static void exitWithError(String errorMessage, Options options)
    {
        String artifactId = Alm2XrayConverter.getProjectInfo().getArtifactId();
        System.err.println(artifactId + ": " + errorMessage);
        System.err.println();
        printHelpInfo(options, true);
        System.exit(1);
    }

    private static CommandMode getCommandMode(CommandLine cli)
    {
        CommandMode mode = null;
        int count = 0;
        if (cli.hasOption("i") || cli.hasOption("o"))
        {
            mode = CommandMode.CONVERT;
            count++;
        }
        if (cli.hasOption("h"))
        {
            mode = CommandMode.HELP;
            count++;
        }
        if (cli.hasOption("v"))
        {
            mode = CommandMode.VERSION;
            count++;
        }
        if (mode == null)
        {
            throw new IllegalArgumentException("Missing required argument(s).");
        }
        if (count > 1)
        {
            throw new IllegalArgumentException("Too many options specified.");
        }
        return mode;
    }

    private static String getOptionValue(CommandLine cli, String option, String description)
    {
        String value = cli.getOptionValue(option);
        if (value == null)
        {
            String msg = String.format("Missing %s.", description);
            throw new IllegalArgumentException(msg);
        }
        if (cli.getOptionValues(option).length > 1)
        {
            String msg = String.format("Too many arguments for %s.", description);
            throw new IllegalArgumentException(msg);
        }
        return value;
    }

    private static Path getPath(CommandLine cli, String option, String description)
    {
        Path path;
        try
        {
            path = Path.of(getOptionValue(cli, option, description));
        }
        catch (InvalidPathException e)
        {
            String msg = String.format("Invalid %s path (%s).", description, e.getMessage());
            throw new IllegalArgumentException(msg, e);
        }
        return path;
    }

    private static void printHelpInfo(Options options, boolean errorMode)
    {
        OutputStream stream = errorMode ? System.err : System.out;
        HelpFormatter formatter = HelpFormatter
                .builder()
                .setPrintWriter(new PrintWriter(stream, false, StandardCharsets.UTF_8))
                .setShowDeprecated(true)
                .get();
        formatter.setWidth(80);
        formatter.setSyntaxPrefix("Usage: ");
        String newLine = formatter.getNewLine();
        String artifactId = Alm2XrayConverter.getProjectInfo().getArtifactId();
        String syntax = new StringBuilder()
                .append(artifactId)
                .append(" [-f] -i <file> -o <file>")
                .append(newLine)
                .append("       ")
                .append(artifactId)
                .append(" -h | -v")
                .toString();
        String header;
        String footer;
        if (!errorMode)
        {
            header = new StringBuilder()
                    .append(newLine)
                    .append("Options:")
                    .toString();
            footer = new StringBuilder()
                    .append(newLine)
                    .append("Example:")
                    .append(newLine)
                    .append("  ")
                    .append(artifactId)
                    .append(" -i input.html -o output.csv")
                    .toString();
        }
        else
        {
            header = newLine;
            footer = null;
        }
        formatter.printHelp(syntax, header, options, footer);
    }

    private static void printVersionInfo()
    {
        ProjectInfo projectInfo = Alm2XrayConverter.getProjectInfo();
        System.out.println(projectInfo.getName() + " " + projectInfo.getVersion());
    }
}
