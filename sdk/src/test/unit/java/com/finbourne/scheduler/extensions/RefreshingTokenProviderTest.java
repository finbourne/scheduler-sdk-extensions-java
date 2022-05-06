package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.extensions.auth.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class RefreshingTokenProviderTest {

    private RefreshingTokenProvider tokenProvider;

    // dependency mocks
    private HttpFinbourneTokenProvider httpFinbourneTokenProvider;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        httpFinbourneTokenProvider = mock(HttpFinbourneTokenProvider.class);
        tokenProvider = new RefreshingTokenProvider(httpFinbourneTokenProvider);
    }

    @Test
    public void get_OnNoCurrentToken_ShouldCallFinbourneForFullAuthentication() throws FinbourneTokenException {
        // mock retrieving initial token when calling scheduler API
        FinbourneToken authenticatedToken = new FinbourneToken("access_01", "refresh_01", LocalDateTime.MAX);
        doReturn(authenticatedToken).when(httpFinbourneTokenProvider).get(Optional.empty());

        FinbourneToken currentToken = tokenProvider.get();

        assertThat(currentToken, equalTo(authenticatedToken));
        // ensure retrieved token using api call and did so via full authentication route
        verify(httpFinbourneTokenProvider, times(1)).get(Optional.empty());
    }

    @Test
    public void get_OnNonExpiredCurrentToken_ShouldDoNothing() throws FinbourneTokenException {
        FinbourneToken nonExpiredToken = new FinbourneToken("access_01", "refresh_01", LocalDateTime.MAX);
        doReturn(nonExpiredToken).when(httpFinbourneTokenProvider).get(Optional.empty());

        // first call should create a token (not expired)
        FinbourneToken currentToken = tokenProvider.get();

        // second call return same token as it has not expired
        FinbourneToken nextToken = tokenProvider.get();

        assertThat(nextToken, sameInstance(currentToken));
        // ensure only ever made one remote call via full authentication
        verify(httpFinbourneTokenProvider, times(1)).get(Optional.empty());
        verify(httpFinbourneTokenProvider, times(0)).get(Optional.of(anyString()));
    }

    @Test
    public void get_OnExpiredCurrentToken_ShouldCallFinbourneForNewRefreshedToken() throws FinbourneTokenException {
        // mock retrieving initial token that then expires
        FinbourneToken expiredToken = new FinbourneToken("access_01", "refresh_01", LocalDateTime.MIN);
        doReturn(expiredToken).when(httpFinbourneTokenProvider).get(Optional.empty());

        // mock retrieving a refreshed token when we attempt to call api with a valid refresh parameter
        FinbourneToken refreshedToken = new FinbourneToken("access_02", "refresh_01", LocalDateTime.MAX);
        doReturn(refreshedToken).when(httpFinbourneTokenProvider).get(Optional.of("refresh_01"));

        // first call should create a token (expired)
        FinbourneToken currentToken = tokenProvider.get();

        // second call should return a new token as the current one has expired
        FinbourneToken nextToken = tokenProvider.get();

        assertThat(nextToken, not(equalTo(currentToken)));
        assertThat(nextToken, equalTo(refreshedToken));
        // ensure make one call for full authentication and subsequent call via refresh
        verify(httpFinbourneTokenProvider, times(1)).get(Optional.empty());
        verify(httpFinbourneTokenProvider, times(1)).get(Optional.of("refresh_01"));
    }

    @Test
    public void get_OnExpiredRefreshToken_ShouldCallFinbourneProviderForFullReauthentication() throws FinbourneTokenException {
        // Test the less often case where the REFRESH token itself has expired and attempting to refresh is not
        // possible. A full authentication is required

        // setup initial expired access token
        FinbourneToken expiredToken = new FinbourneToken("access_01", "refresh_01", LocalDateTime.MIN);
        doReturn(expiredToken).when(httpFinbourneTokenProvider).get(Optional.empty());
        // first call should create a token (expired)
        FinbourneToken currentToken = tokenProvider.get();

        // throw exception on attempting to refresh with an expired REFRESH token
        doThrow(new FinbourneTokenException("Refresh token has expired")).when(httpFinbourneTokenProvider).get(Optional.of("refresh_01"));
        // setup a new access token via full authentication route
        FinbourneToken newTokenWithNewRefresh = new FinbourneToken("access_02", "refresh_02", LocalDateTime.MAX);
        doReturn(newTokenWithNewRefresh).when(httpFinbourneTokenProvider).get(Optional.empty());

        // fetch token again. as both the access and refresh token have expired expect an exception and a full
        // reauthentication attempt
        FinbourneToken nextToken = tokenProvider.get();

        assertThat(nextToken, not(equalTo(currentToken)));
        assertThat(nextToken, equalTo(newTokenWithNewRefresh));
        // ensure make initial full auth call, a refresh attempt and another full auth call
        verify(httpFinbourneTokenProvider, times(2)).get(Optional.empty());
        verify(httpFinbourneTokenProvider, times(1)).get(Optional.of("refresh_01"));
    }

    // Error Cases

    @Test
    public void get_OnFailedRemoteAuthentication_ShouldReThrowFinbourneTokenException() throws FinbourneTokenException {
        FinbourneTokenException finbourneTokenException = new FinbourneTokenException("Failed to retrieve token");
        doThrow(finbourneTokenException).when(httpFinbourneTokenProvider).get(Optional.empty());

        thrown.expect(FinbourneTokenException.class);
        tokenProvider.get();
    }

    @Test
    public void get_OnFailedRefreshAndFailedReAuthAttempt_ShouldReThrowFinbourneTokenException() throws FinbourneTokenException {
        FinbourneToken expiredToken = new FinbourneToken("access_01", "refresh_01", LocalDateTime.MIN);
        doReturn(expiredToken).when(httpFinbourneTokenProvider).get(Optional.empty());

        // get the first token (expired)
        FinbourneToken currentToken = tokenProvider.get();

        // mock the refresh token expiring
        doThrow(new FinbourneTokenException("Refresh token has expired")).when(httpFinbourneTokenProvider).get(Optional.of("refresh_01"));
        // mock full authentication failing on a retry
        doThrow(new FinbourneTokenException("Reauthentication attempt has failed")).when(httpFinbourneTokenProvider).get(Optional.empty());

        thrown.expect(FinbourneTokenException.class);
        tokenProvider.get();
    }

}
