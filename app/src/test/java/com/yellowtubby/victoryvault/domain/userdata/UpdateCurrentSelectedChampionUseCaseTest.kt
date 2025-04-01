package com.yellowtubby.victoryvault.domain.userdata

import org.junit.Test

class UpdateCurrentSelectedChampionUseCaseTest {

    @Test
    fun `Successful champion update`() {
        // Verify that the `updateSelectedChampion` method of the `userRepository` 
        // is called with the correct `Champion` object when `invoke` is called.
        // TODO implement test
    }

    @Test
    fun `Champion object is null`() {
        // Test what happens if a null champion is passed in, and if an exception is 
        // thrown in that scenario.
        // TODO implement test
    }

    @Test
    fun `User repository throws exception`() {
        // Check if an exception thrown by `userRepository.updateSelectedChampion` 
        // is properly propagated or handled by the `invoke` method. Mock the User 
        // Repository to throw an error
        // TODO implement test
    }

    @Test
    fun `Verify userRepository is called`() {
        // Ensure that when invoke is called, that the userRepository is correctly 
        // invoked. Use Mockito to verify the User Repository was invoked
        // TODO implement test
    }

    @Test
    fun `Empty Champion object`() {
        // Test the scenario where an empty Champion object is passed to `invoke` 
        // which has no attributes set. Check if this creates an error
        // TODO implement test
    }

    @Test
    fun `Large Champion Name`() {
        // Test the scenario where a Champion object with an extremely long name is 
        // passed to `invoke`. This will simulate a bad database object passed as 
        // parameter
        // TODO implement test
    }

    @Test
    fun `Correct coroutine context`() {
        // Ensure the invoke suspend function is running on the correct coroutine 
        // context. Simulate the user repo running on a background context, 
        // and verify that invoke is also running on that context
        // TODO implement test
    }

    @Test
    fun `Multiple calls to invoke`() {
        // Verify that the `invoke` method can be called multiple times with different 
        // Champion objects without any issues. Check that the calls do not overwrite 
        // other data and that they are all correct
        // TODO implement test
    }

    @Test
    fun `Concurrent calls to invoke`() {
        // Test if concurrent calls to `invoke` with different Champion objects are 
        // handled correctly. Check to see that no error or data inconsistency 
        // is encountered
        // TODO implement test
    }

    @Test
    fun `Check Default Injection`() {
        // Test the scenario where we check if the correct default implementation is 
        // injected with userRepository. This test will help ensure that dependency 
        // injection is working as expected.
        // TODO implement test
    }

}