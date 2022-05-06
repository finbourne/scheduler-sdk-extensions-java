package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiClient;
import com.finbourne.scheduler.extensions.auth.FinbourneToken;
import com.finbourne.scheduler.extensions.auth.FinbourneTokenException;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

public class ApiClientBuilderTest {

    private ApiClientBuilder apiClientBuilder;

    //mock dependencies
    private ApiClient apiClient;
    private OkHttpClient httpClient;
    private ApiConfiguration apiConfiguration;
    private FinbourneToken finbourneToken;

    // test helpers
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        httpClient = mock(OkHttpClient.class);
        apiConfiguration = mock(ApiConfiguration.class);
        finbourneToken = mock(FinbourneToken.class);
        apiClient = mock(ApiClient.class);
        apiClientBuilder = spy(new ApiClientBuilder());

        // mock creation of default api client
        doReturn(apiClient).when(apiClientBuilder).createApiClient();
        // mock default well formed finbourne token
        doReturn("access_token_01").when(finbourneToken).getAccessToken();
    }

    @Test
    public void createApiClient_OnProxyAddress_ShouldSetHttpClient() throws FinbourneTokenException {
        doReturn("http://proxy.address").when(apiConfiguration).getProxyAddress();
        apiClientBuilder.createDefaultApiClient(apiConfiguration, httpClient, finbourneToken);
        verify(apiClient).setHttpClient(httpClient);
    }

    @Test
    public void createApiClient_OnProxyAddress_ShouldNotSetAndOverrideDefaultHttpClient() throws FinbourneTokenException {
        doReturn(null).when(apiConfiguration).getProxyAddress();
        apiClientBuilder.createDefaultApiClient(apiConfiguration, httpClient, finbourneToken);
        verify(apiClient, times(0)).setHttpClient(httpClient);
    }

    @Test
    public void createApiClient_OnNoApplicationName_ShouldNotSetApplicationHeader() throws FinbourneTokenException {
        doReturn(null).when(apiConfiguration).getApplicationName();
        apiClientBuilder.createDefaultApiClient(apiConfiguration, httpClient, finbourneToken);
        verify(apiClient,times(0)).addDefaultHeader(eq("X-LUSID-Application"), any());
    }

    @Test
    public void createApiClient_OnNullAccessToken_ShouldThrowLusidTokenException() throws FinbourneTokenException {
        doReturn(null).when(finbourneToken).getAccessToken();
        thrown.expect(FinbourneTokenException.class);
        thrown.expectMessage("Cannot construct an API client with a null authorisation header. Ensure " +
                "finbourne token generated is valid");
        apiClientBuilder.createDefaultApiClient(apiConfiguration, httpClient, finbourneToken);
    }
}
