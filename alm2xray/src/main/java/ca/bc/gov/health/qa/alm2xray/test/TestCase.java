package ca.bc.gov.health.qa.alm2xray.test;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO (AZ) - doc
 */
public final class TestCase
{
    private final String         description_;
    private final String         name_;
    private final List<TestStep> testStepList_;

    /**
     * TODO (AZ) - doc
     */
    public static class Builder
    {
        private final List<TestStep> testStepList_;

        private String description_;
        private String name_;

        private Builder()
        {
            testStepList_ = new ArrayList<>();
        }

        /**
         * TODO (AZ) - doc
         *
         * @return ???
         *
         * @throws IllegalStateException
         *         ???
         */
        public TestCase build()
        {
            if (name_ == null)
            {
                throw new IllegalStateException("No test case name specified.");
            }
            else if (name_.isBlank())
            {
                throw new IllegalStateException("Blank test case name.");
            }
            if (description_ == null)
            {
                String msg = String.format("No test case description specified (%s).", name_);
                throw new IllegalStateException(msg);
            }
            if (testStepList_.isEmpty())
            {
                String msg = String.format("No test steps specified for test case. (%s).", name_);
                throw new IllegalStateException(msg);
            }
            return new TestCase(this);
        }

        /**
         * TODO (AZ) - doc
         *
         * @param description
         *        ???
         *
         * @return this builder
         */
        public Builder description(String description)
        {
            description_ = description;
            return this;
        }

        /**
         * TODO (AZ) - doc
         *
         * @param name
         *        ???
         *
         * @return this builder
         */
        public Builder name(String name)
        {
            name_ = name;
            return this;
        }

        /**
         * TODO (AZ) - doc
         *
         * @param action
         *        ???
         *
         * @param expectedResult
         *        ???
         *
         * @return this builder
         *
         * @throws NullPointerException
         *         if either {@code action} or {@code expectedResult} is {@code null}
         */
        public Builder step(String action, String expectedResult)
        {
            return step(new TestStep(action, expectedResult));
        }

        /**
         * TODO (AZ) - doc
         *
         * @param step
         *        ???
         *
         * @return this builder
         *
         * @throws NullPointerException
         *         if {@code step} is {@code null}
         */
        public Builder step(TestStep step)
        {
            testStepList_.add(requireNonNull(step, "Null test step."));
            return this;
        }
    }

    /**
     * TODO (AZ) - doc
     */
    private TestCase(Builder builder)
    {
        name_         = builder.name_;
        description_  = builder.description_;
        testStepList_ = Collections.unmodifiableList(new ArrayList<>(builder.testStepList_));
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public String getDescription()
    {
        return description_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public String getName()
    {
        return name_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public List<TestStep> getTestStepList()
    {
        return testStepList_;
    }
}
