package ca.bc.gov.health.qa.autotest.core.util.security;

/**
 * TODO (AZ) - doc
 */
public class UserCredentials
{
    private char[] username_;
    private char[] password_;

    /**
     * TODO (AZ) - doc
     */
    public UserCredentials()
    {}

    /**
     * TODO (AZ) - doc
     */
    public void clear()
    {
        setPassword(null);
        setUsername(null);
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public char[] getPassword()
    {
        return ArraySupport.copy(password_);
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public char[] getUsername()
    {
        return ArraySupport.copy(username_);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param password
     *        ???
     */
    public void setPassword(char[] password)
    {
        ArraySupport.clear(password_);
        password_ = ArraySupport.copy(password);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param username
     *        ???
     */
    public void setUsername(char[] username)
    {
        ArraySupport.clear(username_);
        username_ = ArraySupport.copy(username);
    }
}
