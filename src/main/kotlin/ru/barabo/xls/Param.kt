package ru.barabo.xls

data class Param(val componentType: ComponentType,
                 val label: String,
                 val varParam: Var,
                 val cursor: CursorData? = null)

enum class ComponentType {
    TEXTFIELD,
    TEXTFIELDINT,
    TEXTFIELDAMOUNT,
    DATEPICKER,
    CHECKBOX,
    COMBOBOX,
    TABLEBOX
}

