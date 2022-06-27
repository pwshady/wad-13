
import Singleton.FileDownload
import file.IOFileData
import file.IOFileJson
import staticWAD.WADStatic
import tornadofx.launch
import views.WADApp

import java.util.*

fun main(){
    var resultCode = -1
    val properties = Properties()
    properties.load(Any::class.java.getResourceAsStream("/WAD.properties"))
    val iof = IOFileData()
    resultCode = iof.errorsLoad("/language/${properties["language"]}/errors.txt")
    if(resultCode != -1){
        WADStatic.WADstat.errorList.plus(Pair(0, resultCode))
    }
    resultCode = iof.labelsLoad("/language/${properties["language"]}/labels.txt")
    if(resultCode != -1){
        WADStatic.WADstat.errorList.plus(Pair(0, resultCode))
    }
    val iofj = IOFileJson()
    resultCode = iofj.loadAllProjects()
    if(resultCode != -1){
        WADStatic.WADstat.errorList.plus(Pair(0, resultCode))
    }
    resultCode = iofj.loadOpenProjects()
    if(resultCode != -1){
        WADStatic.WADstat.errorList.plus(Pair(0, resultCode))
    }
    println(resultCode)
    launch<WADApp>()
}