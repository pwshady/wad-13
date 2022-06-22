package controller

import file.IOFileJson
import models.WADProject
import staticWAD.WADStatic
import tornadofx.Controller
import tornadofx.observable
import views.WADCreateProjectView
import views.WADOpenProjectView

import java.io.File

class WADProjectsController : Controller() {
    fun createProjectView(): Int
    {
        when (WADStatic.WADstat.createProjectStatusCode){
            0 -> {
                find<WADCreateProjectView>().openWindow(owner = null)
                WADStatic.WADstat.createProjectStatusCode = 1
            }
            1 -> println("ocp")
            2 -> println("create")
            3 -> println("error db")
            4 -> println("error dir")
            5 -> {
                println("cancel")
                WADStatic.WADstat.createProjectStatusCode = 0
            }
        }
        return -1
    }


    fun createProject(wadProject: WADProject): Int
    {
        var resultCode = 0
        val iofj = IOFileJson()
        WADStatic.WADstat.allProjectList.add(wadProject)
        resultCode = iofj.saveAllProjects()
        if (resultCode == -1){
            WADStatic.WADstat.openProjectList.add(wadProject)
            resultCode = iofj.saveOpenProjects()
        }
        if (resultCode == -1){
            if(File(wadProject.path).mkdirs()){
                resultCode = iofj.saveProject(wadProject)
            }
        }
        if (resultCode == -1) {
            WADStatic.WADstat.allProjectListName.add(wadProject.name)
            WADStatic.WADstat.openProjectListName.add(wadProject.name)
        }
        return resultCode
    }

    fun openProjectView(): Int
    {
        WADStatic.WADstat.closeProjectListName = WADStatic.WADstat.allProjectListName.minus(WADStatic.WADstat.openProjectListName).observable()
        when (WADStatic.WADstat.openProjectStatusCode){
            0 -> {
                find<WADOpenProjectView>().openWindow(owner = null)
                WADStatic.WADstat.openProjectStatusCode = 1
            }
            1 -> println("oop")
        }
        return -1
    }

    fun openProject(name: String): Int
    {
        var resultCode = -1
        var iofj = IOFileJson()
        try {
            WADStatic.WADstat.allProjectList.map {
                if (it.name == name) {
                    WADStatic.WADstat.closeProjectListName.remove(name)
                    WADStatic.WADstat.openProjectListName.add(it.name)
                    WADStatic.WADstat.openProjectList.add(it)
                    resultCode = iofj.saveOpenProjects()
                }
            }
        }
        catch (e: Exception){

        }
        return resultCode
    }

    fun closeProject(name: String): Int
    {
        var resultCode = 0
        var iofj = IOFileJson()
        try {
            WADStatic.WADstat.openProjectListName.remove(name)
            WADStatic.WADstat.closeProjectListName.add(name)
            WADStatic.WADstat.openProjectList.map {
                if (it.name == name) {
                    WADStatic.WADstat.openProjectList.remove(it)
                    resultCode = iofj.saveOpenProjects()
                }
            }
        }
        catch (e: Exception) {

        }
        println("uuuuuuuuuuuuu")
        return resultCode
    }

    fun deleteProject(name: String): Int
    {
        var resultCode = 0
        var iofj = IOFileJson()
        try {
            WADStatic.WADstat.allProjectListName.remove(name)
            WADStatic.WADstat.closeProjectListName.remove(name)
            WADStatic.WADstat.allProjectList.map {
                if (it.name == name) {
                    WADStatic.WADstat.allProjectList.remove(it)
                    resultCode = iofj.saveAllProjects()
                }
            }
        }
        catch (e: Exception){

        }
        return resultCode
    }

}