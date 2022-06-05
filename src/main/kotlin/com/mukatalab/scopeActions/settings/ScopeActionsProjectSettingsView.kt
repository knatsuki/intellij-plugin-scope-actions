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
import javax.swing.DefaultListModel
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListModel
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class ScopeActionsProjectSettingsView(private var scopes: List<SearchScope>, scopeActionTypes: List<String>) {
    private val scopeListModel: DefaultListModel<SearchScope>
    private val scopeActionsTableModel: ScopeActionsTableModel

    var scopeActionsProjectConfigState: ScopeActionsProjectConfigState
        get() {
            return scopeActionsTableModel.scopeActionsProjectConfigState
        }
        set(value) {
            scopeActionsTableModel.scopeActionsProjectConfigState = value
        }

    //    Managed JComponents
    private val scopeActionsDetailTableComponent: JBTable
    private val scopeListComponent: JBList<SearchScope>
    val mainPanelComponent: DialogPanel

    init {
        val selectedScope: SearchScope = scopes.first()

        scopeListModel = JBList.createDefaultListModel(scopes)
        scopeActionsTableModel = ScopeActionsTableModel(selectedScope, scopeActionTypes)

        scopeListComponent = buildScopeListComponent(scopeListModel).apply {
            selectionMode = ListSelectionModel.SINGLE_SELECTION
            setSelectedValue(selectedScope, true)
            addListSelectionListener(object : ListSelectionListener {
                override fun valueChanged(e: ListSelectionEvent?) {
                    val selectedIndex = e?.lastIndex ?: return
                    scopeActionsTableModel.selectedScope = scopes[selectedIndex]
                }

            })
        }
        scopeActionsDetailTableComponent = buildScopeActionsDetailTableComponent(scopeActionsTableModel)
        mainPanelComponent = buildMainDialogComponent(scopeListComponent, scopeActionsDetailTableComponent)
    }

    val preferredFocusedComponent: JComponent get() = scopeListComponent
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
private fun buildScopeActionsDetailTableComponent(tableModel: ScopeActionsTableModel): JBTable {
    return JBTable(tableModel).apply {
        setShowGrid(false)
        emptyText.text = "No Actions configured"
        val headerFontMetrics = tableHeader.getFontMetrics(tableHeader.font)

        val enabledColumn = columnModel.getColumn(1)
        enabledColumn.cellRenderer = BooleanTableCellRenderer()
        enabledColumn.cellEditor = BooleanTableCellEditor()
        enabledColumn.preferredWidth = headerFontMetrics.stringWidth(getColumnName(1)) + JBUIScale.scale(20)
        enabledColumn.minWidth = enabledColumn.preferredWidth
    }
}

/**
 * Build the main dialog component view
 */
private fun buildMainDialogComponent(
    scopeListComponent: JBList<SearchScope>,
    scopeActionsTableComponent: JBTable
): DialogPanel {
    val mainContent = OnePixelSplitter(false, 0.2f).apply {
        firstComponent = scopeListComponent
        secondComponent = panel {
            row {
                scrollCell(scopeActionsTableComponent).apply {
                    resizableColumn()
                    horizontalAlign(HorizontalAlign.FILL)
                    verticalAlign(VerticalAlign.FILL)
                }
            }.apply {
                resizableRow()
            }
        }
    }

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