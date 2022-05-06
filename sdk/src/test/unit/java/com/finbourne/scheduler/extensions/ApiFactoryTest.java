package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiClient;
import com.finbourne.scheduler.api.JobsApi;
import com.finbourne.scheduler.model.ArgumentDefinition;
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

     @Test
     public void build_ForJobsApi_ReturnJobsApi(){
         JobsApi jobsApi = apiFactory.build(JobsApi.class);
         assertThat(jobsApi, instanceOf(JobsApi.class));
     }

     @Test
     public void build_ForAnyApi_SetsTheApiFactoryClientAndNotTheDefault(){
         JobsApi jobsApi = apiFactory.build(JobsApi.class);
         assertThat(jobsApi.getApiClient(), equalTo(apiClient));
     }

     // Singleton Check Cases

     @Test
     public void build_ForSameApiBuiltAgainWithSameFactory_ReturnTheSameSingletonInstanceOfApi(){
         JobsApi jobsApi = apiFactory.build(JobsApi.class);
         JobsApi jobsApiSecond = apiFactory.build(JobsApi.class);
         assertThat(jobsApi, sameInstance(jobsApiSecond));
     }

     @Test
     public void build_ForSameApiBuiltWithDifferentFactories_ReturnAUniqueInstanceOfApi(){
         JobsApi jobsApi = apiFactory.build(JobsApi.class);
         JobsApi jobsApiSecond = new ApiFactory(mock(ApiClient.class)).build(JobsApi.class);
         assertThat(jobsApi, not(sameInstance(jobsApiSecond)));
     }

     // Error Cases

     @Test
     public void build_ForNonApiPackageClass_ShouldThrowException(){
         thrown.expect(UnsupportedOperationException.class);
         thrown.expectMessage("com.finbourne.scheduler.model.ArgumentDefinition class is not a supported API class. " +
                 "Supported API classes live in the " + ApiFactory.API_PACKAGE + " package.");
         apiFactory.build(ArgumentDefinition.class);
     }



}
