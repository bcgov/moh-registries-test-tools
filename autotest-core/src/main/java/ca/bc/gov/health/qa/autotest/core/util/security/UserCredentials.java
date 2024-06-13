package ca.bc.gov.health.qa.autotest.core.util.security;

/**
 * TODO (AZ) - doc
 */
public class UserCredentials
implements AutoCloseable
{
    private char[] username_;
    private char[] password_;

    /**
     * TODO (AZ) - doc
     */
    public UserCredentials()
    {}

    /**
     * Clears the credentials.
     * TODO (AZ) - secure clear the previous values, if any
     * <p>
     * This method is idempotent. Calling it more than once has no additional effect.
     */
    @Override
    public void close()
    {
        setPassword(null);
        setUsername(null);
    }

    /**
     * Returns a new character array, containing a copy of the password.
     * <p>
     * This method clears the password of this user credentials.
     * To prevent this from happening use the
     * {@link #getPassword(boolean)} method instead.
     *
     * @return a new character array, containing a copy of the password,
     *         or {@code null} if the password is not set
     *
     * @see #getPassword(boolean)
     */
    public char[] getPassword()
    {
        return getPassword(true);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param clearSource
     *        ???
     *
     * @return ???
     *
     * @see #getPassword()
     */
    public char[] getPassword(boolean clearSource)
    {
        char[] password = ArraySupport.copy(password_);
        if (clearSource)
        {
            setPassword(null);
        }
        return password;
    }

    /**
     * Returns a new character array, containing a copy of the username.
     * <p>
     * This method clears the username of this user credentials.
     * To prevent this from happening use the
     * {@link #getUsername(boolean)} method instead.
     *
     * @return a new character array, containing a copy of the username,
     *         or {@code null} if the username is not set
     *
     * @see #getUsername(boolean)
     */
    public char[] getUsername()
    {
        return getUsername(true);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param clearSource
     *        ???
     *
     * @return ???
     *
     * @see #getUsername()
     */
    public char[] getUsername(boolean clearSource)
    {
        char[] username = ArraySupport.copy(username_);
        if (clearSource)
        {
            setUsername(null);
        }
        return username;
    }

    /**
     * TODO (AZ) - doc
     * TODO (AZ) - secure clear previous value, if any
     * <p>
     * This method stores a copy of the input array supplied.
     * <p>
     * This method clears the input array supplied.
     * To prevent this from happening use the
     * {@link #setPassword(char[], boolean)} method instead.
     *
     * @param password
     *        ???
     *
     * @see #setPassword(char[], boolean)
     */
    public void setPassword(char[] password)
    {
        setPassword(password, true);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param password
     *        ???
     *
     * @param clearSource
     *        ???
     *
     * @see #setPassword(char[])
     */
    public void setPassword(char[] password, boolean clearSource)
    {
        ArraySupport.clear(password_);
        password_ = ArraySupport.copy(password);
        if (clearSource)
        {
            ArraySupport.clear(password);
        }
    }

    /**
     * TODO (AZ) - doc
     * TODO (AZ) - secure clear previous value, if any
     * <p>
     * This method stores a copy of the input array supplied.
     * <p>
     * This method clears the input array supplied.
     * To prevent this from happening use the
     * {@link #setUsername(char[], boolean)} method instead.
     *
     * @param username
     *        ???
     *
     * @see #setUsername(char[], boolean)
     */
    public void setUsername(char[] username)
    {
        setUsername(username, true);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param username
     *        ???
     *
     * @param clearSource
     *        ???
     *
     * @see #setUsername(char[])
     */
    public void setUsername(char[] username, boolean clearSource)
    {
        ArraySupport.clear(username_);
        username_ = ArraySupport.copy(username);
        if (clearSource)
        {
            ArraySupport.clear(username);
        }
    }
}
