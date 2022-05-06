package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiClient;
import com.finbourne.scheduler.ApiException;
import com.finbourne.scheduler.api.SchedulesApi;
import com.finbourne.scheduler.extensions.auth.FinbourneTokenException;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

// UNCOMMENT TEST BELOW TO USE THE IMPORTED API. HERE WE ARE USING DRIVE FOLDERS API AS AN EXAMPLE
 public class ApiExceptionTests {

     @Test
     public void thrown_exception_tostring_contains_requestid() throws ApiConfigurationException, FinbourneTokenException {

         ApiConfiguration apiConfiguration = new ApiConfigurationBuilder().build(CredentialsSource.credentialsFile);
         ApiClient apiClient = new ApiClientBuilder().build(apiConfiguration);

         SchedulesApi schedulesApi = new SchedulesApi(apiClient);

         try {
             schedulesApi.getSchedule("doesntExist", "shouldFail");
         }
         catch (ApiException e) {

             String message = e.toString();

             assertNotNull("Null exception message", message);

             String[] parts = message.split("\\r?\\n");

             assertThat(parts.length, is(greaterThanOrEqualTo(1)));

             //  of the format 'LUSID request id = 000000000:AAAAAAA'
             String[] idParts = parts[0].split(" = ");

             assertThat("missing requestId", idParts.length, is(equalTo(2)));
         }
         catch (Exception e) {
             fail("Unexpected exception of type " + e.getClass());
         }


     }
 }
