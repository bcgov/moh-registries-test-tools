package ca.bc.gov.health.qa.alm2xray.test;

import static java.util.Objects.requireNonNull;

/**
 * A test step describing an action and the expected result.
 * <p>
 * Instances of this class are immutable.
 */
public final class TestStep
{
    private final String action_;
    private final String expectedResult_;

    /**
     * Creates a new test step.
     *
     * @param action
     *        the test step action
     *
     * @param expectedResult
     *        the test step expected result
     *
     * @throws NullPointerException
     *         if either {@code action} or {@code expectedResult} is {@code null}
     */
    public TestStep(String action, String expectedResult)
    {
        action_         = requireNonNull(action,         "Null action.");
        expectedResult_ = requireNonNull(expectedResult, "Null expected result.");
    }

    /**
     * Returns the test step action.
     *
     * @return the test step action
     */
    public String getAction()
    {
        return action_;
    }

    /**
     * Returns the test step expected result.
     *
     * @return the test step expected result
     */
    public String getExpectedResult()
    {
        return expectedResult_;
    }
}
