package com.mukatalab.scopeActions.settings

import com.intellij.psi.search.SearchScope
import com.mukatalab.scopeActions.services.ScopeActionsProjectConfigState
import javax.swing.table.AbstractTableModel

class ScopeActionsTableModel(
    val selectedScope: SearchScope,
    val scopeActionsProjectConfigState: ScopeActionsProjectConfigState,
    val scopeActionTypes: List<String>,
) : AbstractTableModel() {
    private val selectedScopeName: String
        get() = selectedScope.displayName


    private val _columnNames = arrayOf("Scope Action Type", "Enabled", "Action ID")
    private val _columnClasses = arrayOf(String::class.java, Boolean::class.java, String::class.java)


    override fun getColumnClass(columnIndex: Int): Class<*> {
        return _columnClasses[columnIndex]
    }

    override fun getColumnName(column: Int): String {
        return _columnNames[column]
    }

    override fun getRowCount(): Int {
        return scopeActionTypes.size
    }

    override fun getColumnCount(): Int {
        return _columnNames.size
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val scopeActionType = scopeActionTypes[rowIndex]

        return when (columnIndex) {
            0 -> scopeActionType
            1 -> scopeActionsProjectConfigState.getEnabled(selectedScopeName, scopeActionType)
            2 -> scopeActionsProjectConfigState.getActionId(selectedScopeName, scopeActionType)
            else -> throw IndexOutOfBoundsException()
        }
    }


    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        val scopeActionType = scopeActionTypes[rowIndex]

        when (columnIndex) {
            1 -> {
                scopeActionsProjectConfigState.setEnabled(selectedScopeName, scopeActionType, aValue as Boolean)
            }
            2 -> {
                scopeActionsProjectConfigState.setActionId(selectedScopeName, scopeActionType, aValue as String)
            }
            else -> return
        }
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return columnIndex != 0
    }
}