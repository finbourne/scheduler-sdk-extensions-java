package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiException;
import com.finbourne.scheduler.api.JobsApi;
import com.finbourne.scheduler.model.ResourceListOfJobDefinition;
import com.finbourne.scheduler.extensions.auth.FinbourneTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class ApiFactoryBuilderTests {

     @Rule
     public ExpectedException thrown = ExpectedException.none();

     @Test
     public void build_WithExistingConfigurationFile_ShouldReturnFactory() throws ApiException, ApiConfigurationException, FinbourneTokenException {
         ApiFactory apiFactory = ApiFactoryBuilder.build(CredentialsSource.credentialsFile);
         assertThat(apiFactory, is(notNullValue()));
         assertThatFactoryBuiltApiCanMakeApiCalls(apiFactory);
     }

     private static void assertThatFactoryBuiltApiCanMakeApiCalls(ApiFactory apiFactory) throws ApiException {
         JobsApi jobsApi = apiFactory.build(JobsApi.class);
         ResourceListOfJobDefinition listOfJobDefinition = jobsApi.listJobs(null, null, null, null, null);
         assertThat("Jobs API created by factory should return list of jobs"
                 , listOfJobDefinition, is(notNullValue()));
         assertThat("list of jobs contents types returned by the jobs API should not be empty",
                 listOfJobDefinition.getValues(), not(empty()));
     }

}
