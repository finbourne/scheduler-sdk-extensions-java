package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiClient;
import com.finbourne.scheduler.extensions.auth.FinbourneTokenException;

public class ApiFactoryBuilder {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    /**
    * Build a {@link ApiFactory} defining configuration using environment variables. For details on the environment arguments see https://support.lusid.com/getting-started-with-apis-sdks.
    *
    * @return
    */
    public static ApiFactory build() throws ApiConfigurationException, FinbourneTokenException {
        if (!areRequiredEnvironmentVariablesSet()) {
            throw new ApiConfigurationException("Environment variables to configure API client have not been set. See " +
                    " see https://support.lusid.com/getting-started-with-apis-sdks for details.");
        }
        return createApiFactory(null, 10, 10);
    }

    /**
    * Build a {@link ApiFactory} using the specified configuration file. For details on the format of the configuration file see https://support.lusid.com/getting-started-with-apis-sdks.
    */
    public static ApiFactory build(String configurationFile) throws ApiConfigurationException, FinbourneTokenException {
       return build(configurationFile, 10, 10);
    }

    public static ApiFactory build(String configurationFile, int readTimeout, int writeTimeout) throws ApiConfigurationException, FinbourneTokenException {
        return createApiFactory(configurationFile, readTimeout, writeTimeout);
    }

    private static ApiFactory createApiFactory(String configurationFile, int readTimeout, int writeTimeout) throws ApiConfigurationException, FinbourneTokenException {
        ApiConfiguration apiConfiguration = new ApiConfigurationBuilder().build(configurationFile);
        ApiClient apiClient = new ApiClientBuilder().build(apiConfiguration, readTimeout, writeTimeout);
        return new ApiFactory(apiClient);
    }

    private static boolean areRequiredEnvironmentVariablesSet(){
        return ((System.getenv("FBN_TOKEN_URL") != null &&
                System.getenv("FBN_USERNAME") != null &&
                System.getenv("FBN_PASSWORD") != null &&
                System.getenv("FBN_CLIENT_ID") != null &&
                System.getenv("FBN_CLIENT_SECRET") != null &&
                System.getenv("FBN_SCHEDULER_API_URL") != null) ||
                (System.getenv("FBN_SCHEDULER_API_URL") != null &&
                System.getenv("FBN_ACCESS_TOKEN") != null));
    }
}
