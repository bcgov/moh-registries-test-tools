package ca.bc.gov.health.qa.autotest.core.util.security;

import static java.util.Objects.requireNonNull;

import java.io.Console;

/**
 * TODO (AZ) - doc
 */
public class CredentialUtils
{
    /**
     * Returns the system console.
     *
     * @return the system console
     *
     * @throws IllegalStateException
     *         if the system console cannot be retrieved
     */
    public static Console getConsole()
    {
        Console console = System.console();
        if (console == null)
        {
            throw new IllegalStateException("Failed to retrieve the system console.");
        }
        return console;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param userInfo
     *        ???
     *
     * @param promptPrefix
     *        ???
     *
     * @return ???
     *
     * @throws IllegalStateException
     *         ???
     */
    public static UserCredentials getUserCredentials(char[] userInfo, String promptPrefix)
    {
        String usernamePrompt = "username: ";
        String passwordPrompt = "password: ";
        if (promptPrefix != null)
        {
            usernamePrompt =
                    new StringBuilder(promptPrefix).append(" ").append(usernamePrompt).toString();
            passwordPrompt =
                    new StringBuilder(promptPrefix).append(" ").append(passwordPrompt).toString();
        }
        return getUserCredentials(userInfo, usernamePrompt, passwordPrompt);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param userInfo
     *        ???
     *
     * @param usernamePrompt
     *        ???
     *
     * @param passwordPrompt
     *        ???
     *
     * @return ???
     *
     * @throws IllegalStateException
     *         ???
     *
     * @throws NullPointerException
     *         if either {@code usernamePrompt} or {@code passwordPrompt} is {@code null}
     */
    public static UserCredentials getUserCredentials(
            char[] userInfo, String usernamePrompt, String passwordPrompt)
    {
        UserCredentials credentials;
        char[] username = null;
        char[] password = null;
        try
        {
            if (userInfo != null)
            {
                int index = ArraySupport.findFirstIndex(userInfo, ':');
                if (index == ArraySupport.NOT_FOUND)
                {
                    username = ArraySupport.copy(userInfo);
                }
                else
                {
                    username = ArraySupport.split(userInfo, index,     false);
                    password = ArraySupport.split(userInfo, index + 1, true);
                }
            }
            credentials = getUserCredentials(username, password, usernamePrompt, passwordPrompt);
        }
        finally
        {
            ArraySupport.clear(password);
            ArraySupport.clear(username);
        }
        return credentials;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param username
     *        ???
     *
     * @param password
     *        ???
     *
     * @param usernamePrompt
     *        ???
     *
     * @param passwordPrompt
     *        ???
     *
     * @return ???
     *
     * @throws IllegalStateException
     *         ???
     *
     * @throws NullPointerException
     *         if either {@code usernamePrompt} or {@code passwordPrompt} is {@code null}
     */
    public static UserCredentials getUserCredentials(
            char[] username, char[] password, String usernamePrompt, String passwordPrompt)
    {
        UserCredentials credentials;
        requireNonNull(usernamePrompt, "Null username prompt.");
        requireNonNull(passwordPrompt, "Null password prompt.");
        char[] user = ArraySupport.copy(username);
        char[] pass = ArraySupport.copy(password);
        try
        {
            if (user == null || pass == null)
            {
                Console console = getConsole();
                if (user == null)
                {
                    String str = console.readLine("%s", usernamePrompt);
                    if (str != null)
                    {
                        user = str.toCharArray();
                    }
                    if (user == null)
                    {
                        System.out.println();
                        throw new IllegalStateException("Username is not specified.");
                    }
                }
                if (pass == null)
                {
                    pass = console.readPassword("%s", passwordPrompt);
                    if (pass == null)
                    {
                        throw new IllegalStateException("Password is not specified.");
                    }
                }
            }
            credentials = new UserCredentials();
            credentials.setUsername(user);
            credentials.setPassword(pass);
        }
        finally
        {
            ArraySupport.clear(pass);
            ArraySupport.clear(user);
        }
        return credentials;
    }

    private CredentialUtils()
    {}
}
