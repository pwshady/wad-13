package models

data class RunStatus (
    var projectName : String,
    var statusMessage : Boolean = false,
    var codeMessage : Int
    )