import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import file.IOFileJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.ProjectStatus
import models.WADProject
import models.WADVersionFileData
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import services.WADGetFileListService
import staticWAD.WADStatic
import tornadofx.onChange
import java.io.File

class WADJob(private val wadProject: WADProject) {
    val name = wadProject.name
    var flag = false
    var filesStructure = mutableMapOf<String, MutableList<Pair<Int,WADVersionFileData>>>()
    var filesStructureList = mutableListOf<Pair<String, MutableList<Pair<Int, WADVersionFileData>>>>()
    var allFiles = 0
    var unicFiles = 0
    fun main() {
        WADStatic.WADstat.wadProjectRunList.onChange {
            WADStatic.WADstat.wadProjectRunList.forEach{ it ->
                if (it.projectName == name && it.statusMessage){
                    if(it.codeMessage == 1 && !WADStatic.WADstat.wadProjectList.last{it.projectName == name}.run){
                        job(wadProject)
                        flag = true
                    }
                    if (it.codeMessage == 0){
                        flag = false
                    }
                    //println(WADStatus.stat.wadProjectRunList)
                    it.statusMessage = false
                }
            }
        }
    }

    fun job(wadProject: WADProject){
        CoroutineScope(Dispatchers.IO).launch {
            WADStatic.WADstat.wadProjectList.last{it.projectName == name}.run = true
            val retrofit : Retrofit = Retrofit.Builder().baseUrl("https://web.archive.org")
                .addConverterFactory(ScalarsConverterFactory.create()).addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
            val service = retrofit.create(WADGetFileListService::class.java)
            val iofj = IOFileJson()
            var wadProjectGet = Pair(0,wadProject)
            while (flag) {
                if (File("${wadProject.path}\\${wadProject.name}.wadproject").exists())
                {
                    wadProjectGet = iofj.loadProject("${wadProject.path}\\${wadProject.name}.wadproject")
                }
                var wadProject = wadProjectGet.second
                if (wadProject.status == "") {
                    allFiles = wadProject.projectSettings.fileLimit * wadProject.projectSettings.timestamp
                    WADStatic.WADstat.wadProjectList.add(ProjectStatus(wadProject.name, true, 0, "Load file list: part${wadProject.projectSettings.timestamp}",allFiles, unicFiles, 0))

                    val result = service.getFileList(
                        "${wadProject.domenName}*",
                        wadProject.projectSettings.fileLimit,
                        wadProject.resumeKey,
                        wadProject.projectSettings.from,
                        wadProject.projectSettings.to,
                        wadProject.projectSettings.fileType
                    ).await()
                    var fileList = result.split("\n").toMutableList()
                    wadProject.resumeKey = fileList[fileList.size - 2]
                    if (fileList[fileList.size - 3] == "") {
                        fileList = fileList.subList(0, fileList.size - 3)
                        allFiles += fileList.size
                        fileList.replaceAll { "$it 0" }
                        val resultSaveFile =
                            iofj.saveFileList("${wadProject.path}\\part${wadProject.projectSettings.timestamp}", fileList)
                        if (resultSaveFile != -1) {
                            flag = false
                        }
                        wadProject.projectSettings.timestamp += 1
                    } else {
                        fileList = fileList.subList(0, fileList.size - 1)
                        wadProject.resumeKey = ""
                        flag = false
                        allFiles += fileList.size
                        fileList.replaceAll { "$it 0" }
                        val resultSaveFile =
                            iofj.saveFileList("${wadProject.path}\\part${wadProject.projectSettings.timestamp}", fileList)
                        if (resultSaveFile != -1) {
                            flag = false
                        }
                        wadProject.status = "1"
                        wadProject.projectSettings.timestamp = 0
                    }
                    iofj.saveProject(wadProject)
                }
                if (wadProject.status == "1"){
                    while (File("${wadProject.path}\\part${wadProject.projectSettings.timestamp}").exists()){
                        WADStatic.WADstat.wadProjectList.add(ProjectStatus(wadProject.name, true, 0, "Create file list: part${wadProject.projectSettings.timestamp}", allFiles, unicFiles, 0))
                        var result = iofj.loadFileList("${wadProject.path}\\part${wadProject.projectSettings.timestamp}")
                        println(result.first)
                        if (result.first == -1){
                            addFileList(result.second as MutableList<String>, wadProject.projectSettings.timestamp)
                        }
                        wadProject.projectSettings.timestamp += 1
                        println("Unique file: ${filesStructure.size}")
                        println(wadProject.projectSettings.timestamp)
                        println(File("${wadProject.path}\\part${wadProject.projectSettings.timestamp}").exists())
                    }
                    wadProject.projectSettings.timestamp = 0
                    filesStructureList = filesStructure.toList().toMutableList()
                    WADStatic.WADstat.wadProjectList.add(ProjectStatus(wadProject.name, false, 1, "File structure complite", allFiles, unicFiles,0))
                    break
                }
            }
        WADStatic.WADstat.wadProjectList.last{it.projectName == name}.run = false
        }
    }

    private fun addFileList(fileList : MutableList<String>, part: Int = 0): Int
    {
        var resultCode = 0
        val longChars = '0'..'9'
        fileList.map {
            val fileStr = it.split(" ").toMutableList()
            if (!fileStr[0].all { it in longChars }){
                fileStr[0] = "0"
            }
            if (!fileStr[3].all { it in longChars }){
                fileStr[3] = "0"
            }
            if (!fileStr[4].all { it in longChars }){
                fileStr[4] = "0"
            }
            var fileData = Pair(fileStr[1], Pair(part, WADVersionFileData(fileStr[2], fileStr[3].toInt(), fileStr[4].toLong(), fileStr[0].toLong(),fileStr[5].toInt())))
            if (filesStructure.containsKey(fileData.first)){
                filesStructure[fileData.first]?.add(fileData.second)
            } else {
                filesStructure.put(fileData.first, mutableListOf(fileData.second))
            }
        }
        unicFiles = filesStructure.size
        return resultCode
    }
}


