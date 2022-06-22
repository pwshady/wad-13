package controller

import models.RunStatus
import staticWAD.WADStatic
import tornadofx.Controller

class WADProjectController : Controller(){
    fun sendMessageProject(projectName: String, statusMessage : Boolean, codeMessage : Int) : Int{
        WADStatic.WADstat.wadProjectRunList.add(RunStatus(projectName,statusMessage,codeMessage))
        return 0
    }

    fun deleteMessageProject(text: String) : Int{
        WADStatic.WADstat.wadProjectRunList.removeAll{ it.projectName == text}
        return 0
    }

    fun getStatus(name: String): String
    {
        return "${WADStatic.WADstat.wadProjectList.filter { it.projectName == name }[0].statusText}"
    }
}