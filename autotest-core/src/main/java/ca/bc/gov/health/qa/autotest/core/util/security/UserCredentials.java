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
     * TODO (AZ) - it is highly recommended to securely clear the returned array,
     *             as well as this user credentials, as soon as possible
     *
     * @return a new character array, containing a copy of the password,
     *         or {@code null} if the password is not set
     *
     * @see ArraySupport#clear(char[])
     */
    public char[] getPassword()
    {
        return ArraySupport.copy(password_);
    }

    /**
     * Returns a new character array, containing a copy of the username.
     * <p>
     * TODO (AZ) - it is highly recommended to securely clear the returned array,
     *             as well as this user credentials, as soon as possible
     *
     * @return a new character array, containing a copy of the username,
     *         or {@code null} if the username is not set
     *
     * @see ArraySupport#clear(char[])
     */
    public char[] getUsername()
    {
        return ArraySupport.copy(username_);
    }

    /**
     * TODO (AZ) - doc
     * TODO (AZ) - secure clear previous value, if any
     * <p>
     * This method stores a copy of the input parameter supplied.
     * It is highly recommended to securely clear the input parameter
     * after calling this method.
     *
     * @param password
     *        ???
     *
     * @see ArraySupport#clear(char[])
     */
    public void setPassword(char[] password)
    {
        ArraySupport.clear(password_);
        password_ = ArraySupport.copy(password);
    }

    /**
     * TODO (AZ) - doc
     * TODO (AZ) - secure clear previous value, if any
     * <p>
     * This method stores a copy of the input parameter supplied.
     * It is highly recommended to securely clear the input parameter
     * after calling this method.
     *
     * @param username
     *        ???
     *
     * @see ArraySupport#clear(char[])
     */
    public void setUsername(char[] username)
    {
        ArraySupport.clear(username_);
        username_ = ArraySupport.copy(username);
    }
}
