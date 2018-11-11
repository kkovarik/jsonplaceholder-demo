package cz.kkovarik.jsonplaceholder.cli;

import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.kkovarik.jsonplaceholder.core.JsonPlaceholderClient;
import cz.kkovarik.jsonplaceholder.core.domain.UserInfo;
import cz.kkovarik.jsonplaceholder.core.impl.DefaultJsonPlaceholderClient;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple CLI interface for {@link JsonPlaceholderClient}.
 *
 * @author Karel Kovarik
 */
public class JsonplaceholderCli {
    private static Logger LOG = LoggerFactory.getLogger(JsonplaceholderCli.class);

    private static final String DEFAULT_HOST = "https://jsonplaceholder.typicode.com/";
    private static final String USERID_SHORT = "u";
    private static final String USERID_FULL = "userId";
    private static final String HOST_SHORT = "h";
    private static final String HOST_FULL = "host";
    private static final int TIMEOUT_SEC = 60;

    public static void main(String[] args) {
        final Options options = new Options();

        options.addOption(Option.builder(USERID_SHORT)
                                .argName("userId")
                                .hasArg()
                                .desc("Fetch detail of user identified by id")
                                .longOpt(USERID_FULL)
                                .build());
        options.addOption(Option.builder(HOST_SHORT)
                                .argName("host")
                                .hasArg()
                                .desc("Backend host url (not mandatory)")
                                .longOpt(HOST_FULL)
                                .build());

        if (args.length < 1) {
            printHelp(options);
            return;
        }

        // create the parser
        final CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            final String hostPathArgument = line.getOptionValue(HOST_SHORT);
            final JsonPlaceholderClient jsonPlaceholderClient = new DefaultJsonPlaceholderClient(
                    hostPathArgument != null ? hostPathArgument : DEFAULT_HOST);

            // set input and output files
            if(line.hasOption(USERID_SHORT)) {
                String userIdString = line.getOptionValue(USERID_SHORT);
                // fetch user info using JsonPlaceholderClient
                final UserInfo userInfo = jsonPlaceholderClient.userInfo(Long.parseLong(userIdString))
                                                         .block(Duration.ofSeconds(TIMEOUT_SEC));
                // output to stdout as JSON
                System.out.print(new ObjectMapper().writeValueAsString(userInfo));
                return;
            }

            // print help only, if no valid argument was provided
            printHelp(options);
        }
        catch (ParseException exp ) {
            LOG.error("Parsing failed, reason: {}, see help.", exp.getMessage(), exp);
        }
        catch (JsonProcessingException ex) {
            LOG.error("Could not process response: ", ex);
        }
    }

    private static void printHelp(Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "jsonplaceholder-cli.jar <options>",
                "Jsonplaceholder CLI",
                options,
                "");
    }
}
