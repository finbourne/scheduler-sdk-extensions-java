package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.extensions.auth.*;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public class HttpFinbourneTokenProviderTests {

    private HttpFinbourneTokenProvider httpFinbourneTokenProvider;

    @Before
    public void setUp() throws ApiConfigurationException {
        ApiConfiguration apiConfiguration = new ApiConfigurationBuilder().build(CredentialsSource.credentialsFile);
        OkHttpClient httpClient = new HttpClientFactory().build(apiConfiguration);
        httpFinbourneTokenProvider = new HttpFinbourneTokenProvider(apiConfiguration, httpClient);
    }

    @Test
    public void get_OnRequestingAnInitialToken_ShouldReturnNewToken() throws FinbourneTokenException {
        FinbourneToken finbourneToken = httpFinbourneTokenProvider.get(Optional.empty());

        assertThat(finbourneToken.getAccessToken(), not(isEmptyOrNullString()));
        assertThat(finbourneToken.getRefreshToken(), not(isEmptyOrNullString()));
        assertThat(finbourneToken.getExpiresAt(), not(nullValue()));
    }

    @Test
    public void get_OnRequestingANewTokenWithRefreshing_ShouldReturnNewRefreshedToken() throws FinbourneTokenException {
        FinbourneToken initialToken = httpFinbourneTokenProvider.get(Optional.empty());
        // request a new access token based on the refresh parameter of our original token
        FinbourneToken refreshedToken = httpFinbourneTokenProvider.get(Optional.of(initialToken.getRefreshToken()));

        assertThat(refreshedToken.getAccessToken(), not(isEmptyOrNullString()));
        assertThat(refreshedToken.getRefreshToken(), not(isEmptyOrNullString()));
        assertThat(refreshedToken.getExpiresAt(), not(nullValue()));
        // ensure our new token is in fact a new and different token
        assertThat(refreshedToken, not(equalTo(initialToken)));
    }

    @Test
    public void get_OnRequestingANewTokenWithoutRefreshing_ShouldReturnNewToken() throws FinbourneTokenException {
        FinbourneToken initialToken = httpFinbourneTokenProvider.get(Optional.empty());
        // request a new access token by going through a full reauthentication (i.e. not a refresh)
        FinbourneToken aNewToken = httpFinbourneTokenProvider.get(Optional.empty());

        assertThat(aNewToken.getAccessToken(), not(isEmptyOrNullString()));
        assertThat(aNewToken.getRefreshToken(), not(isEmptyOrNullString()));
        assertThat(aNewToken.getExpiresAt(), not(nullValue()));
        // ensure our new token is in fact a new and different token
        assertThat(aNewToken, not(equalTo(initialToken)));
    }

    // Error cases
    @Test(expected = IllegalArgumentException.class)
    public void get_OnBadTokenUrl_ShouldThrowException() throws FinbourneTokenException, ApiConfigurationException {
        ApiConfiguration apiConfiguration = new ApiConfigurationBuilder().build(CredentialsSource.credentialsFile);
        OkHttpClient httpClient = new HttpClientFactory().build(apiConfiguration);
        apiConfiguration.setTokenUrl("invalidTokenUrl");

        HttpFinbourneTokenProvider httpFinbourneTokenProvider = new HttpFinbourneTokenProvider(apiConfiguration, httpClient);
        httpFinbourneTokenProvider.get(Optional.empty());
    }
}
