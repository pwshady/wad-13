package models

data class DownloadFileStructure (
    var index : Int,
    var cildrenIndex : Int,
    var nameFile : String,
    var partFile : Int,
    var typeFile: String,
    var codeFile: Int,
    var lengthFile: Long,
    var dateFile: Long,
    var statusFile: Int,
)