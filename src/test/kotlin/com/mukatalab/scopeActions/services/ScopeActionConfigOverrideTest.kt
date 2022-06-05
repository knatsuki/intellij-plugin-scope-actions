package com.mukatalab.scopeActions.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * These are mostly just sanity checks to validate basic properties of equal() methods
 * for both data classes and collection of data classes.
 */
internal class ScopeActionConfigOverrideTest {

    @ParameterizedTest
    @MethodSource("configOverrideInstances")
    fun testCopiedElementIsEqualToOriginal(original: ScopeActionConfigOverride) {
        assertTrue(original == original.copy())
    }

    @Test
    fun testListOfCopiedElementsAreEqual() {

        val originalList = listOf(
            ScopeActionConfigOverride(
                scopeName = "DummyScope1",
                scopeActionType = "DummyActionType1"
            ),
            ScopeActionConfigOverride(
                scopeName = "DummyScope2",
                scopeActionType = "DummyActionType2",
                enabled = true,
            )
        )

        val copiedList = originalList.map { it.copy() }

        assertTrue(originalList == copiedList)

//        Same when immutable
        assertTrue(originalList.toMutableList() == copiedList.toMutableList())
    }

    private companion object {
        @JvmStatic
        fun configOverrideInstances() = Stream.of(
            Arguments.of(
                ScopeActionConfigOverride(
                    scopeName = "DummyScope1",
                    scopeActionType = "DummyActionType1"
                )
            ),
            Arguments.of(
                ScopeActionConfigOverride(
                    scopeName = "DummyScope2",
                    scopeActionType = "DummyActionType2",
                    enabled = true,
                )
            ),
            Arguments.of(
                ScopeActionConfigOverride(
                    scopeName = "DummyScope3",
                    scopeActionType = "DummyActionType3",
                    customActionId = "CustomActionId"
                )
            ),
            Arguments.of(
                ScopeActionConfigOverride(
                    scopeName = "DummyScope4",
                    scopeActionType = "DummyActionType4",
                    enabled = false,
                    customActionId = "CustomActionId"
                )
            ),
        )
    }
}