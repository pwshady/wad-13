package staticWAD

import models.ProjectStatus
import models.RunStatus
import models.WADProject
import tornadofx.observable

class WADStatic {
    object WADstat{
        var createProjectStatusCode : Int = 0
        var openProjectStatusCode : Int = 0
        var errorList = mutableListOf<Pair<Int, Int>>()
        var openProjectList = mutableListOf<WADProject>().observable()
        var openProjectListName = mutableListOf<String>().observable()
        var closeProjectListName = mutableListOf<String>().observable()
        var allProjectList = mutableListOf<WADProject>().observable()
        var allProjectListName = mutableListOf<String>().observable()
        var wadProjectList = mutableListOf<ProjectStatus>().observable()
        var wadProjectRunList = mutableListOf<RunStatus>().observable()
    }
    object WADconst{
        var labels = mapOf<String, String>()
        var errors = mapOf<Int, String>()
    }
}