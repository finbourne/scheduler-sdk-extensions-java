package com.finbourne.scheduler.extensions;

import com.finbourne.scheduler.ApiException;
// UNCOMMENT BELOW TWO LINES TO IMPORT AN API AND THE TYPE A CALL FROM IT RETURNS
// import com.finbourne.scheduler.api.;
// import com.finbourne.scheduler.model.;
import com.finbourne.scheduler.extensions.auth.FinbourneTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

// UNCOMMENT BELOW TESTS AND USE IMPORTED API TO MAKE A VALID CALL - DRIVE FOLDERS API IS BEING USED AS AN EXAMPLE
// public class ApiFactoryBuilderTests {

//     @Rule
//     public ExpectedException thrown = ExpectedException.none();

//     @Test
//     public void build_WithExistingConfigurationFile_ShouldReturnFactory() throws ApiException, ApiConfigurationException, FinbourneTokenException {
//         ApiFactory apiFactory = ApiFactoryBuilder.build(CredentialsSource.credentialsFile);
//         assertThat(apiFactory, is(notNullValue()));
//         assertThatFactoryBuiltApiCanMakeApiCalls(apiFactory);
//     }

//     private static void assertThatFactoryBuiltApiCanMakeApiCalls(ApiFactory apiFactory) throws ApiException {
//         FoldersApi foldersApi = apiFactory.build(FoldersApi.class);
//         PagedResourceListOfStorageObject rootFolder = foldersApi.getRootFolder(null, null, null, null, null);
//         assertThat("Folders API created by factory should return root folder"
//                 , rootFolder, is(notNullValue()));
//         assertThat("Root folder contents types returned by the folders API should not be empty",
//                 rootFolder.getValues(), not(empty()));
//     }

// }
