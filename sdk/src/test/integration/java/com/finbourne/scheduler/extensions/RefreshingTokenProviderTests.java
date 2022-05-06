package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.extensions.auth.*;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class RefreshingTokenProviderTests {

    private RefreshingTokenProvider tokenProvider;
    private HttpFinbourneTokenProvider httpFinbourneTokenProvider;

    @Before
    public void setUp() throws ApiConfigurationException {
        ApiConfiguration apiConfiguration = new ApiConfigurationBuilder().build(CredentialsSource.credentialsFile);
        OkHttpClient httpClient = new HttpClientFactory().build(apiConfiguration);
        httpFinbourneTokenProvider = new HttpFinbourneTokenProvider(apiConfiguration, httpClient);
        RefreshingTokenProvider instanceToSpy = new RefreshingTokenProvider(httpFinbourneTokenProvider);
        tokenProvider = spy(instanceToSpy);
    }

    @Test
    public void get_OnNoCurrentToken_ShouldReturnNewToken() throws FinbourneTokenException {
        FinbourneToken finbourneToken = tokenProvider.get();
        assertThat(finbourneToken.getAccessToken(), not(isEmptyOrNullString()));
        assertThat(finbourneToken.getRefreshToken(), not(isEmptyOrNullString()));
        assertThat(finbourneToken.getExpiresAt(), not(nullValue()));
    }

    @Test
    public void get_OnNonExpiredCurrentToken_ShouldReturnSameToken() throws FinbourneTokenException {
        // first call should create a token
        FinbourneToken finbourneToken = tokenProvider.get();

        // mock token not expired
        doReturn(false).when(tokenProvider).isTokenExpired(finbourneToken);

        // second call return same token as it has not expired
        FinbourneToken nextFinbourneToken = tokenProvider.get();

        assertThat(nextFinbourneToken, sameInstance(finbourneToken));
    }

    @Test
    public void get_OnExpiredCurrentToken_ShouldReturnNewToken() throws FinbourneTokenException {
        // first call should create a token
        FinbourneToken finbourneToken = tokenProvider.get();

        // mock token expired
        doReturn(true).when(tokenProvider).isTokenExpired(finbourneToken);

        // second call should return a new token as the current one has expired
        FinbourneToken nextFinbourneToken = tokenProvider.get();

        assertThat(nextFinbourneToken.getAccessToken(), not(isEmptyOrNullString()));
        assertThat(nextFinbourneToken.getRefreshToken(), not(isEmptyOrNullString()));
        assertThat(nextFinbourneToken.getExpiresAt(), not(nullValue()));

        assertThat(nextFinbourneToken, not(equalTo(finbourneToken)));
        assertThat(nextFinbourneToken.getAccessToken(), not(equalTo(finbourneToken.getAccessToken())));
        assertThat(nextFinbourneToken.getExpiresAt(), not(equalTo(finbourneToken.getExpiresAt())));
        // although a new token the refresh token parameter should remain constant
        assertThat(nextFinbourneToken.getRefreshToken(), equalTo(finbourneToken.getRefreshToken()));
    }



}
