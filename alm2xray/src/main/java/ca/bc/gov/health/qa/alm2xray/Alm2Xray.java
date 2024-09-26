package ca.bc.gov.health.qa.alm2xray;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.bc.gov.health.qa.autotest.core.util.capture.CaptureEvent;
import ca.bc.gov.health.qa.autotest.core.util.capture.ThrowableArtifact;
import ca.bc.gov.health.qa.autotest.core.util.config.Config;
import ca.bc.gov.health.qa.autotest.core.util.context.LocalContext;
import ca.bc.gov.health.qa.autotest.core.util.io.ProjectInfo;
import ca.bc.gov.health.qa.autotest.core.util.io.PropertyUtils;
import ca.bc.gov.health.qa.autotest.core.util.security.ArraySupport;
import ca.bc.gov.health.qa.autotest.core.util.security.CredentialUtils;
import ca.bc.gov.health.qa.autotest.core.util.security.UserCredentials;

/**
 * TODO (AZ) - doc
 */
public class Alm2Xray
{
    private static enum Command
    {
        CONVERT,
        HELP,
        QUERY,
        VERIFY_ACCESS,
        VERSION;
    }

    private static final Path   EVENT_PATH = Path.of("events");
    private static final Logger LOG        = LogManager.getLogger();

    /**
     * TODO (AZ) - doc
     *
     * @param args
     *        command-line arguments
     *
     * @throws IllegalStateException
     *         ???
     *
     * @throws InterruptedException
     *         if the current thread is interrupted
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static void main(String[] args)
    throws InterruptedException,
           IOException
    {
        try
        {
            Command command        = null;
            boolean forceOverwrite = false;
            Path    inputPath      = null;
            Path    outputPath     = null;
            String  projectName    = null;
            String  userInfo       = null;

            Options options = createCommandLineOptions();
            try
            {
                CommandLine cli = new DefaultParser().parse(options, args);
                if (!cli.getArgList().isEmpty())
                {
                    throw new IllegalArgumentException("Too many arguments.");
                }
                verifyMutuallyExclusiveOptions(cli, List.of("a", "c", "h", "q", "v"));
                verifyOptionValueCount(cli);
                if (cli.hasOption("a"))
                {
                    command     = Command.VERIFY_ACCESS;
                    projectName = getRequiredOptionValue("p", cli, options);
                    userInfo    = cli.getOptionValue("u");
                }
                else if (cli.hasOption("c"))
                {
                    command        = Command.CONVERT;
                    forceOverwrite = cli.hasOption("f");
                    inputPath      = getRequiredOptionPath("i", cli, options);
                    outputPath     = getRequiredOptionPath("o", cli, options);
                }
                else if (cli.hasOption("h"))
                {
                    command = Command.HELP;
                }
                else if (cli.hasOption("q"))
                {
                    command     = Command.QUERY;
                    projectName = getRequiredOptionValue("p", cli, options);
                    userInfo    = cli.getOptionValue("u");
                }
                else if (cli.hasOption("v"))
                {
                    command = Command.VERSION;
                }
                else
                {
                    throw new IllegalArgumentException("Missing required argument(s).");
                }
            }
            catch (IllegalArgumentException | ParseException e)
            {
                exitWithError(e.getMessage(), options);
            }

            switch (command)
            {
                case CONVERT:
                    convert(inputPath, outputPath, forceOverwrite);
                    break;

                case HELP:
                    printHelpInfo(options, false);
                    break;

                case QUERY:
                    queryAlm(projectName, userInfo);
                    break;

                case VERIFY_ACCESS:
                    verifyAlmProjectAccess(projectName, userInfo);
                    break;

                case VERSION:
                    printVersionInfo();
                    break;

                default:
                    String msg = String.format("Unknown command (%s).", command);
                    throw new IllegalStateException(msg);
            }
            throw new IllegalStateException(">>> DONE <<<"); // FIXME
        }
        catch (Throwable t)
        {
            LOG.error("ERROR", t);
            createFailureCaptureEvent(t);
            exitWithError("ERROR: " + t.getMessage(), null);
        }
    }

    private Alm2Xray()
    {}

    private static void convert(Path inputPath, Path outputPath, boolean forceOverwrite)
    throws IOException
    {
        // FIXME
        System.out.printf("Converting (%s) -> (%s) ...%n", inputPath, outputPath);

        // FIXME
        Alm2XrayConverter converter = new Alm2XrayConverter();
        converter.output(outputPath, forceOverwrite);

        System.out.println("Done.");
    }

    private static Options createCommandLineOptions()
    {
        Options options = new Options()
                .addOption(Option
                        .builder("a")
                        .longOpt("access")
                        .desc("verifies ALM project access")
                        .build())
                .addOption(Option
                        .builder("c")
                        .longOpt("convert")
                        .desc("Converts ALM test cases to Xray")
                        .build())
                .addOption(Option
                        .builder("f")
                        .longOpt("force")
                        .desc("overwrites existing output file")
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
                        .builder("p")
                        .longOpt("project")
                        .hasArg()
                        .argName("project")
                        .desc("ALM project name")
                        .build())
                .addOption(Option
                        .builder("q")
                        .longOpt("query")
                        .desc("queries ALM")
                        .build())
                .addOption(Option
                        .builder("u")
                        .longOpt("user")
                        .hasArg()
                        .argName("user")
                        .desc("ALM user info (<username>[:<password>])")
                        .build())
                .addOption(Option
                        .builder("v")
                        .longOpt("version")
                        .desc("shows version information")
                        .build());
        return options;
    }

    private static void createFailureCaptureEvent(Throwable throwable)
    {
        try
        {
            CaptureEvent captureEvent =
                    CaptureEvent.createCaptureEvent(EVENT_PATH, "failure", null);
            captureEvent.addSequentialArtifacts(
                    LocalContext.get().getArtifactBuffer().getArtifactList());
            captureEvent.addSummaryArtifact(new ThrowableArtifact("throwable.txt", throwable));
            captureEvent.renderArtifacts();
            LOG.error("Event captured ({}).", captureEvent.getPath());
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Unable to capture event.", e);
        }
    }

    private static void exitWithError(String errorMessage, Options options)
    {
        String artifactId = Alm2XrayConverter.getProjectInfo().getArtifactId();
        System.err.println(artifactId + ": " + errorMessage);
        if (options != null)
        {
            System.err.println();
            printHelpInfo(options, true);
        }
        System.exit(1);
        throw new IllegalStateException("SYSTEM EXIT FAILED!");
    }

    private static String getOptionDescription(String opt, Options options)
    {
        String description;
        Option option = options.getOption(opt);
        if (option != null)
        {
            description = option.getDescription();
        }
        else
        {
            description = opt;
        }
        return description;
    }

    private static Path getRequiredOptionPath(String opt, CommandLine cli, Options options)
    {
        Path path;
        try
        {
            path = Path.of(getRequiredOptionValue(opt, cli, options));
        }
        catch (InvalidPathException e)
        {
            String msg = String.format(
                    "Invalid %s path (%s).", getOptionDescription(opt, options), e.getMessage());
            throw new IllegalArgumentException(msg, e);
        }
        return path;
    }

    private static String getRequiredOptionValue(String opt, CommandLine cli, Options options)
    {
        String value = cli.getOptionValue(opt);
        if (value == null)
        {
            String msg = String.format("Missing %s.", getOptionDescription(opt, options));
            throw new IllegalArgumentException(msg);
        }
        return value;
    }

    private static UserCredentials getUserCredentials(String userInfo)
    {
        UserCredentials credentials;
        char[] userInfoArray = ArraySupport.getCharArray(userInfo);
        try
        {
            credentials = CredentialUtils.getUserCredentials(userInfoArray, "ALM");
        }
        finally
        {
            ArraySupport.clear(userInfoArray);
        }
        return credentials;
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
        String indent  = " ".repeat(formatter.getSyntaxPrefix().length());
        String newLine = formatter.getNewLine();
        String artifactId = Alm2XrayConverter.getProjectInfo().getArtifactId();
        String syntax = new StringBuilder()
                .append(artifactId)
                .append(" -a -p <project> [-u <user>]")
                .append(newLine)
                .append(indent)
                .append(artifactId)
                .append(" -c -i <file> -o <file> [-f]")
                .append(newLine)
                .append(indent)
                .append(artifactId)
                .append(" -q -p <project> [-u <user>]")
                .append(newLine)
                .append(indent)
                .append(artifactId)
                .append(" -h | -v")
                .toString();
        String header = new StringBuilder()
                .append(newLine)
                .append("Options:")
                .toString();
        String footer;
        if (!errorMode)
        {
            indent = "  ";
            footer = new StringBuilder()
                    .append(newLine)
                    .append("Examples:")
                    .append(newLine)
                    .append(indent)
                    .append(artifactId)
                    .append(" -a -p PLR_CGI_Internal -u jdoe")
                    .append(newLine)
                    .append(indent)
                    .append(artifactId)
                    .append(" -c -i input.json -o output.csv")
                    .append(newLine)
                    .append(indent)
                    .append(artifactId)
                    .append(" -q -p PLR_CGI_Internal -u jdoe")
                    .toString();
        }
        else
        {
            footer = null;
        }
        formatter.printHelp(syntax, header, options, footer);
    }

    private static void printVersionInfo()
    {
        ProjectInfo projectInfo = Alm2XrayConverter.getProjectInfo();
        System.out.println(projectInfo.getName() + " " + projectInfo.getVersion());
    }

    private static void queryAlm(String projectName, String userInfoString)
    throws IOException
    {
        Config config     = readConfig();
        URI    uri        = URI.create(config.get("alm.url"));
        String domainName = config.get("alm.domain");
        Alm2XrayConverter converter = new Alm2XrayConverter(uri);
        try (UserCredentials credentials = getUserCredentials(userInfoString))
        {
            // FIXME
            credentials.getUsername();
        }
    }

    private static Config readConfig()
    throws IOException
    {
        Path basePath = Alm2XrayConverter.getProjectInfo().getBasePath();
        Path configFilePath = basePath.resolve("config").resolve("config.properties");
        return new Config(PropertyUtils.loadPropertyMap(configFilePath));
    }

    private static void verifyAlmProjectAccess(String projectName, String userInfoString)
    throws InterruptedException,
           IOException
    {
        Config config     = readConfig();
        URI    uri        = URI.create(config.get("alm.url"));
        String domainName = config.get("alm.domain");
        try (UserCredentials credentials = getUserCredentials(userInfoString))
        {
            Alm2XrayConverter converter = new Alm2XrayConverter(uri);
            converter.verifyAlmProjectAccess(domainName, projectName, credentials);
        }
    }

    private static void verifyMutuallyExclusiveOptions(CommandLine cli, List<String> optList)
    {
        int optCount = 0;
        for (String opt : optList)
        {
            if (cli.hasOption(opt))
            {
                optCount++;
            }
        }
        if (optCount > 1)
        {
            throw new IllegalArgumentException("Too many options specified.");
        }
    }

    private static void verifyOptionValueCount(CommandLine cli)
    {
        for (Option option : cli.getOptions())
        {
            if (cli.hasOption(option) && option.hasArg())
            {
                if (cli.getOptionValues(option).length > 1)
                {
                    String opt     = option.getOpt();
                    String longOpt = option.getLongOpt();
                    List<String> list = new ArrayList<>();
                    if (opt != null)
                    {
                        list.add("-" + opt);
                    }
                    if (longOpt != null)
                    {
                        list.add("--" + longOpt);
                    }
                    String msg = String.format(
                            "Too many arguments for option (%s).", String.join(",", list));
                    throw new IllegalArgumentException(msg);
                }
            }
        }
    }
}
