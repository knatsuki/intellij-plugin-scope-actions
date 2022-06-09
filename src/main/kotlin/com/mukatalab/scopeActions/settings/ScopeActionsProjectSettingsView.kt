package com.mukatalab.scopeActions.settings

import com.intellij.openapi.ui.DialogPanel
import com.intellij.psi.search.SearchScope
import com.intellij.ui.BooleanTableCellEditor
import com.intellij.ui.BooleanTableCellRenderer
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.ui.scale.JBUIScale
import com.intellij.ui.table.JBTable
import com.mukatalab.scopeActions.services.ScopeActionsProjectConfigState
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListModel
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class ScopeActionsProjectSettingsView(private val scopes: List<SearchScope>, val scopeActionTypes: List<String>) {
    private var _scopeActionsProjectConfigState: ScopeActionsProjectConfigState

    var scopeActionsProjectConfigState: ScopeActionsProjectConfigState
        get() = _scopeActionsProjectConfigState
        set(value) {
            _scopeActionsProjectConfigState = value
            resetScopeActionsTableComponent(value)
        }

    //    Managed JComponents
    private val mainSplitterComponent: OnePixelSplitter
    private val scopeListComponent: JBList<SearchScope>
    val mainPanelComponent: DialogPanel

    init {
        _scopeActionsProjectConfigState = ScopeActionsProjectConfigState()
        val selectedScope: SearchScope = scopes.first()

        scopeListComponent = buildScopeListComponent(JBList.createDefaultListModel(scopes)).apply {
            selectionMode = ListSelectionModel.SINGLE_SELECTION
            setSelectedValue(selectedScope, true)
            addListSelectionListener(object : ListSelectionListener {
                override fun valueChanged(e: ListSelectionEvent?) {
                    if (e?.valueIsAdjusting != false) return
                    resetScopeActionsTableComponent()
                }

            })
        }
        val scopeActionsDetailTableComponent = buildScopeActionsDetailTableComponent(
            selectedScope,
            scopeActionsProjectConfigState,
            scopeActionTypes
        )
        mainSplitterComponent = OnePixelSplitter(false, 0.2f).apply {
            firstComponent = scopeListComponent
            secondComponent = scopeActionsDetailTableComponent
        }
        mainPanelComponent = buildMainDialogComponent(mainSplitterComponent)
    }

    val preferredFocusedComponent: JComponent get() = mainSplitterComponent.firstComponent

    fun resetScopeActionsTableComponent(state: ScopeActionsProjectConfigState? = null) {
        mainSplitterComponent.secondComponent =
            buildScopeActionsDetailTableComponent(
                scopeListComponent.selectedValue,
                state ?: scopeActionsProjectConfigState,
                scopeActionTypes
            )
    }
}

/**
 * Build the scope list component on the left panel
 */
private fun buildScopeListComponent(listModel: ListModel<SearchScope>): JBList<SearchScope> {
    return JBList(listModel).apply {
        setCellRenderer(object : ColoredListCellRenderer<SearchScope>() {
            override fun customizeCellRenderer(
                list: JList<out SearchScope>,
                value: SearchScope,
                index: Int,
                selected: Boolean,
                hasFocus: Boolean
            ) {
                append(value.displayName)
            }
        })
    }
}


/**
 * Build the scope table component on the right panel
 */
private fun buildScopeActionsDetailTableComponent(
    selectedScope: SearchScope,
    scopeActionsProjectConfigState: ScopeActionsProjectConfigState,
    scopeActionTypes: List<String>
): JComponent {
    val table = JBTable(
        ScopeActionsTableModel(
            selectedScope,
            scopeActionsProjectConfigState,
            scopeActionTypes
        )
    ).apply {
        setShowGrid(false)
        emptyText.text = "No Actions configured"
        val headerFontMetrics = tableHeader.getFontMetrics(tableHeader.font)

        val enabledColumn = columnModel.getColumn(1)
        enabledColumn.cellRenderer = BooleanTableCellRenderer()
        enabledColumn.cellEditor = BooleanTableCellEditor()
        enabledColumn.preferredWidth = headerFontMetrics.stringWidth(getColumnName(1)) + JBUIScale.scale(20)
        enabledColumn.minWidth = enabledColumn.preferredWidth
    }

    return panel {
        row {
            scrollCell(table).apply {
                resizableColumn()
                horizontalAlign(HorizontalAlign.FILL)
                verticalAlign(VerticalAlign.FILL)
            }
        }.apply {
            resizableRow()
        }
    }
}

/**
 * Build the main dialog component view
 */
private fun buildMainDialogComponent(
    mainContent: JComponent
): DialogPanel {
    return panel {
        row {
            cell(mainContent).apply {
                resizableColumn()
                horizontalAlign(HorizontalAlign.FILL)
                verticalAlign(VerticalAlign.FILL)
            }
        }.apply { resizableRow() }
    }

}