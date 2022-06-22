package models

data class ProjectStatus (
    var projectName : String,
    var run : Boolean = false,
    var statusCode : Int = 0,
    var statusText : String = "",
    var allFiles : Int = 0,
    var unicFiles : Int = 0,
    var downloadFiles : Int = 0
)