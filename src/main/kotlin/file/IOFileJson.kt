package file

import com.google.gson.Gson
import json.WADProjectJson
import models.ProjectSettings
import models.WADProject
import staticWAD.WADStatic
import java.io.File
import java.io.FileReader

class IOFileJson {
    fun loadAllProjects(): Int
    {
        var resultCode = 0
        try {
            var file = File("src/main/resources/allproject.json")
            if(file.exists()){
                val gson = Gson()
                var allWADProjectJson = mutableListOf<WADProjectJson>()
                allWADProjectJson = gson.fromJson(FileReader(file), allWADProjectJson::class.java)
                for (i in 0 until allWADProjectJson.size){
                    val wadProject = wadProjectJsonToWadProject(gson.fromJson(gson.toJson(allWADProjectJson[i]),WADProjectJson::class.java))
                    WADStatic.WADstat.allProjectList.add(wadProject)
                    WADStatic.WADstat.allProjectListName.add(wadProject.name)
                }
            }
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 101
        }
        return resultCode
    }

    fun saveAllProjects(): Int
    {
        var resultCode = 0
        try {
            var file = File("src/main/resources/allproject.json")
            val gson = Gson()
            var allWADProjectJson = mutableListOf<WADProjectJson>()
            for (i in 0 until WADStatic.WADstat.allProjectList.size) {
                allWADProjectJson.add(wadProjectToWadProjectJson(WADStatic.WADstat.allProjectList[i]))
            }
            file.writeText(gson.toJson(allWADProjectJson))
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 102
        }
        return resultCode
    }

    fun loadOpenProjects(): Int
    {
        var resultCode = 0
        try {
            var file = File("src/main/resources/openproject.json")
            if(file.exists()){
                val gson = Gson()
                var openWADProjectJson = mutableListOf<WADProjectJson>()
                openWADProjectJson = gson.fromJson(FileReader(file), openWADProjectJson::class.java)
                for (i in 0 until openWADProjectJson.size){
                    val wadProject = wadProjectJsonToWadProject(gson.fromJson(gson.toJson(openWADProjectJson[i]),WADProjectJson::class.java))
                    WADStatic.WADstat.openProjectList.add(wadProject)
                    WADStatic.WADstat.openProjectListName.add(wadProject.name)
                }
            }
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 101
        }
        return resultCode
    }

    fun saveOpenProjects(): Int
    {
        var resultCode = 0
        try {
            var file = File("src/main/resources/openproject.json")
            val gson = Gson()
            var openWADProjectJson = mutableListOf<WADProjectJson>()
            for (i in 0 until WADStatic.WADstat.openProjectList.size) {
                openWADProjectJson.add(wadProjectToWadProjectJson(WADStatic.WADstat.openProjectList[i]))
            }
            file.writeText(gson.toJson(openWADProjectJson))
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 102
        }
        return resultCode
    }

    fun loadProject(path: String): Pair<Int, WADProject>
    {
        var resultCode = 0
        var wadProject = WADProject()
        try {
            var file = File(path)
            if(file.exists()){
                val gson = Gson()
                wadProject = wadProjectJsonToWadProject(gson.fromJson(FileReader(file), WADProjectJson::class.java))
            }
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 101
        }
        return Pair(resultCode, wadProject)
    }

    fun saveProject(wadProject: WADProject): Int
    {
        var resultCode = 0
        try {
            var file = File("${wadProject.path}\\${wadProject.name}.wadproject")
            val gson = Gson()
            file.writeText(gson.toJson(wadProject))

            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 102
        }
        return resultCode
    }

    fun saveFileList(path: String, fileList: List<String>): Int
    {
        var resultCode = 0
        try {
            val gson = Gson()
            if (!File(path).exists()){
                if(File(path).mkdirs()){
                    val file = File("${path}\\filelist.wadflj")
                    file.writeText(gson.toJson(fileList))
                }
            }else{
                val file = File("${path}\\filelist.wadflj")
                file.writeText(gson.toJson(fileList))
            }
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 102
        }
        return resultCode
    }

    fun loadFileList(path: String): Pair<Int, List<String>>
    {
        var resultCode = 0
        var fileList = mutableListOf<String>()
        try {
            val gson = Gson()
            val file = File("${path}\\filelist.wadflj")
            fileList = gson.fromJson(file.readText(), List::class.java) as MutableList<String>
            resultCode = -1
        }
        catch (e: Exception){
            resultCode = 102
        }
        return Pair(resultCode, fileList)
    }

    fun wadProjectToWadProjectJson(wadProject: WADProject) : WADProjectJson
    {
        return WADProjectJson(
            wadProject.name,
            wadProject.domenName,
            wadProject.path,
            wadProject.status,
            wadProject.resumeKey,
            WADProjectJson.ProjectSettingsJson(
                wadProject.projectSettings.from,
                wadProject.projectSettings.to,
                wadProject.projectSettings.timestamp,
                wadProject.projectSettings.fileType,
                wadProject.projectSettings.fileLimit
            )
        )
    }

    private fun wadProjectJsonToWadProject(wadProjectJson: WADProjectJson): WADProject
    {
        var wadProjectSettings = ProjectSettings(
            wadProjectJson.projectSettings.from,
            wadProjectJson.projectSettings.to,
            wadProjectJson.projectSettings.timestamp,
            wadProjectJson.projectSettings.fileType,
            wadProjectJson.projectSettings.fileLimit
        )
        return WADProject(
            wadProjectJson.name,
            wadProjectJson.domenName,
            wadProjectJson.path,
            wadProjectJson.status,
            wadProjectJson.resumeKey,
            wadProjectSettings
        )
    }
}