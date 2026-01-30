package ui.data;

import core.util.platform.environment.TestEnvironment;

public class CTFLearnerTestData {

    // Base URL from environment
    public static final String BASE_URL = TestEnvironment.getValue("ctflearner_url", "https://ctflearn.com");

    // Test User Credentials from environment
    public static final String VALID_USERNAME = TestEnvironment.getValue("ctflearner_username", "SamBui");
    public static final String VALID_PASSWORD = TestEnvironment.getValue("ctflearner_password", "Test@1234");

    public static final String INVALID_USERNAME = "invaliduser";
    public static final String INVALID_PASSWORD = "wrongpassword";

    // Challenge Test Data
    public static class ChallengeData {
        private static final String BASE_CHALLENGE_TITLE = "SQL Injection Challenge";

        public static String getChallengeTitle() {
            return BASE_CHALLENGE_TITLE + "_" + System.currentTimeMillis();
        }

        public static final String CHALLENGE_DESCRIPTION = "Learn SQL injection techniques and protect your applications";
        public static final String CHALLENGE_CATEGORY = "Web";
        public static final String CHALLENGE_POINTS = "50";
        public static final String CHALLENGE_FLAG = "CTFlearn{}";
        public static final String HOW_TO_SOLVE = "Use SQL injection to bypass login forms";

        public static final String CHALLENGE_EXISTING_TITLE = "Sample Title";
    }

    // Time Constants
    public static final int DEFAULT_WAIT_TIME = 10;
    public static final int SHORT_WAIT_TIME = 5;
}