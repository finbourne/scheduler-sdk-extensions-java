package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiClient;
// UNCOMMENT BELOW LINES IMPORTING THE API(S) YOU WANT TO TEST, AND AN ARBITRARY OBJECT FOR AN EXCEPTION TEST
// import com.finbourne.scheduler.api.;
// import com.finbourne.scheduler.model.;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ApiFactoryTest {

    private ApiFactory apiFactory;
    private ApiClient apiClient;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        apiClient = mock(ApiClient.class);
        apiFactory = new ApiFactory(apiClient);
    }

    // UNCOMMENT BELOW TESTS AND MODIFY THEM FOR THE DESIRED SDK - DRIVE EXAMPLES BEING SHOWN HERE
    // General Cases

    // @Test
    // public void build_ForFilesApi_ReturnFilesApi(){
    //     FilesApi filesApi = apiFactory.build(FilesApi.class);
    //     assertThat(filesApi, instanceOf(FilesApi.class));
    // }

    // @Test
    // public void build_ForAnyApi_SetsTheApiFactoryClientAndNotTheDefault(){
    //     FilesApi filesApi = apiFactory.build(FilesApi.class);
    //     assertThat(filesApi.getApiClient(), equalTo(apiClient));
    // }

    // // Singleton Check Cases

    // @Test
    // public void build_ForSameApiBuiltAgainWithSameFactory_ReturnTheSameSingletonInstanceOfApi(){
    //     FilesApi filesApi = apiFactory.build(FilesApi.class);
    //     FilesApi filesApiSecond = apiFactory.build(FilesApi.class);
    //     assertThat(filesApi, sameInstance(filesApiSecond));
    // }

    // @Test
    // public void build_ForSameApiBuiltWithDifferentFactories_ReturnAUniqueInstanceOfApi(){
    //     FilesApi filesApi = apiFactory.build(FilesApi.class);
    //     FilesApi filesApiSecond = new ApiFactory(mock(ApiClient.class)).build(FilesApi.class);
    //     assertThat(filesApi, not(sameInstance(filesApiSecond)));
    // }

    // // Error Cases

    // @Test
    // public void build_ForNonApiPackageClass_ShouldThrowException(){
    //     thrown.expect(UnsupportedOperationException.class);
    //     thrown.expectMessage("com.finbourne.drive.model.StorageObject class is not a supported API class. " +
    //             "Supported API classes live in the " + ApiFactory.API_PACKAGE + " package.");
    //     apiFactory.build(StorageObject.class);
    // }



}
