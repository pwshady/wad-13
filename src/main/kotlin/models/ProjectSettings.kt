package models

data class ProjectSettings (
    var from : String = "",
    var to : String = "",
    var timestamp : Int = 0,
    var fileType : String = "",
    var fileLimit : Int = 10000,
)