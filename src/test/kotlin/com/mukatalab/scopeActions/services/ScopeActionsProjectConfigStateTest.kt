package com.mukatalab.scopeActions.services

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class ScopeActionsProjectConfigStateTest {

    @Test
    fun testCopiedElementIsEqualToOriginal() {
        val state = ScopeActionsProjectConfigState(
            mutableListOf(
                ScopeActionConfigOverride(
                    scopeName = "DummyScope1",
                    scopeActionType = "DummyActionType1"
                ),
                ScopeActionConfigOverride(
                    scopeName = "DummyScope2",
                    scopeActionType = "DummyActionType2",
                    enabled = true,
                    customActionId = "CustomAction2"
                ),
                ScopeActionConfigOverride(
                    scopeName = "DummyScope3",
                    scopeActionType = "DummyActionType3",
                    enabled = false,
                ),
            ),
        )

        val copiedState = state.copy()
        assertTrue(copiedState == state)
    }
}