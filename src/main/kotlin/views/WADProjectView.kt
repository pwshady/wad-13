package views

import Singleton.FileDownload
import WADJob
import controller.WADProjectController
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Side
import javafx.scene.Parent
import javafx.scene.control.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.DownloadFileStructure
import models.ViewFileStructure
import models.WADProject
import models.WADVersionFileData
import staticWAD.WADStatic
import tornadofx.*
import kotlin.concurrent.thread

class WADProjectView(wadProject: WADProject) : Fragment() {
    val  wadProjectController : WADProjectController by inject()

    override val root: Parent = vbox {
        minWidth = 1200.0
        minHeight = 800.0
        var flagDownload = false
        var textFieldDomenName : TextField by singleAssign()
        var statusLabel : Label by singleAssign()
        var buttonStart : Button by singleAssign()
        var buttonStop : Button by singleAssign()
        var buttonDownload : Button by singleAssign()
        var comboboxFileType : ComboBox<String> by singleAssign()
        var typeFile = FXCollections.observableArrayList("All types","Html")
        var wadJob = WADJob(wadProject)
        wadJob.main()
        var viewFileStructure = mutableListOf<ViewFileStructure>().observable()
        var downloadFileStructure = mutableListOf<DownloadFileStructure>()

        fun createLocalFileStructure(startIndex : Int, total : Int){
            viewFileStructure.clear()
            val endIndex = if (wadJob.filesStructureList.size< startIndex + total){
                wadJob.filesStructureList.size - 1
            } else {
                startIndex + total
            }
            for (i in startIndex..endIndex){
                var fileDataList = mutableListOf<WADVersionFileData>().observable()
                for (j in 0 until wadJob.filesStructureList[i].second.size){
                    var tecFileData = wadJob.filesStructureList[i].second[j].second
                    fileDataList.add(tecFileData)
                }
                viewFileStructure.add(ViewFileStructure(wadJob.filesStructureList[i].first, wadJob.filesStructureList[i].second[0].first, fileDataList))
            }
        }

        fun createDownloadFileStructure(typeFile : String, allFile : Boolean){
            downloadFileStructure.clear()
            for (i in wadJob.filesStructureList.size-1 downTo 0){
                if (allFile) {
                    for (j in 0 until wadJob.filesStructureList[i].second.size) {
                        var tecFileData = DownloadFileStructure(
                            i,
                            j,
                            wadJob.filesStructureList[i].first,
                            wadJob.filesStructureList[i].second[j].first,
                            wadJob.filesStructureList[i].second[j].second.typeFile,
                            wadJob.filesStructureList[i].second[j].second.codeFile,
                            wadJob.filesStructureList[i].second[j].second.lengthFile,
                            wadJob.filesStructureList[i].second[j].second.dateFile,
                            wadJob.filesStructureList[i].second[j].second.statusFile
                        )
                        if (tecFileData.typeFile == typeFile || typeFile == "") {
                            if (tecFileData.statusFile == 0) {
                                downloadFileStructure.add(tecFileData)
                            }
                        }
                    }
                } else {
                    var tecFileData = DownloadFileStructure(
                        i,
                        wadJob.filesStructureList[i].second.size-1,
                        wadJob.filesStructureList[i].first,
                        wadJob.filesStructureList[i].second[wadJob.filesStructureList[i].second.size - 1].first,
                        wadJob.filesStructureList[i].second[wadJob.filesStructureList[i].second.size - 1].second.typeFile,
                        wadJob.filesStructureList[i].second[wadJob.filesStructureList[i].second.size - 1].second.codeFile,
                        wadJob.filesStructureList[i].second[wadJob.filesStructureList[i].second.size - 1].second.lengthFile,
                        wadJob.filesStructureList[i].second[wadJob.filesStructureList[i].second.size - 1].second.dateFile,
                        wadJob.filesStructureList[i].second[wadJob.filesStructureList[i].second.size - 1].second.statusFile
                    )
                    if (tecFileData.typeFile == typeFile || typeFile == "") {
                        if (tecFileData.statusFile == 0) {
                            downloadFileStructure.add(tecFileData)
                        }
                    }
                }
            }
        }

        fun downloadFile(){
            createDownloadFileStructure("", true)
            println(downloadFileStructure)
            println(downloadFileStructure.size)
            thread {
                while (flagDownload) {
                    if (FileDownload.Download.status.get()) {
                        FileDownload.Download.status.set(false)
                        if (FileDownload.Download.fileDownload(downloadFileStructure[downloadFileStructure.size-1]) == -1){
                            println("Ok ${downloadFileStructure[downloadFileStructure.size-1].nameFile}")
                            wadJob.filesStructureList[downloadFileStructure[downloadFileStructure.size-1].index].second[downloadFileStructure[downloadFileStructure.size-1].cildrenIndex].second.statusFile = 1
                            downloadFileStructure.removeAt(downloadFileStructure.size-1)
                            createLocalFileStructure(0, 9)
                            if (downloadFileStructure.size == 0){
                                break
                            }
                        }
                    }
                    Thread.sleep(100)
                }
                println("end")
            }
        }

        hbox {
            label("Domen name: "){
                this.style{
                    //this.backgroundColor += c("red")
                }
            }

            textFieldDomenName = textfield {
                this.disableProperty().set(true)
            }

            textFieldDomenName.text = wadProject.domenName

            //checkbox("Only url") {  }

            buttonStart = button("Start"){
                action {
                    wadProjectController.sendMessageProject(wadProject.name,true,1)
                    buttonStart.disableProperty().set(true)
                    buttonStop.disableProperty().set(false)
                }
            }

            buttonStop = button("Stop"){
                action {
                    wadProjectController.sendMessageProject(wadProject.name,true,0)
                    buttonStart.disableProperty().set(false)
                    buttonStop.disableProperty().set(true)
                }
            }

            progressbar {
                thread {
                    var i = 0
                    while (true) {
                        Platform.runLater { progress = i.toDouble() / 10 }
                        Thread.sleep(1000)
                        if (WADStatic.WADstat.wadProjectList.lastOrNull { it.projectName == wadProject.name }?.run == true) {
                            i++
                        }
                        if (i == 10) {
                            i = 0
                        }
                    }
                }
            }

            statusLabel = label {
                text = wadProjectController.getStatus(wadProject.name)
            }

            comboboxFileType = combobox(){
                items = typeFile
                this.selectionModel.select(0)
            }

            buttonDownload = button("Download") {
                action {
                    when(comboboxFileType.selectionModel.selectedIndex){
                        0 -> println("all")
                        1 -> println("html")
                    }
                    if (flagDownload){
                        flagDownload = false
                    } else {
                        flagDownload = true
                        downloadFile()
                    }
                }
            }

            WADStatic.WADstat.wadProjectList.onChange {
                if (WADStatic.WADstat.wadProjectList.lastOrNull { it.projectName == wadProject.name } != null){
                    var statusStr = WADStatic.WADstat.wadProjectList.lastOrNull { it.projectName == wadProject.name }!!.statusText
                    Platform.runLater { statusLabel.text = "Status: $statusStr" }
                    var statusCode = WADStatic.WADstat.wadProjectList.lastOrNull { it.projectName == wadProject.name }!!.statusCode
                    println(WADStatic.WADstat.wadProjectList.lastOrNull { it.projectName == wadProject.name })
                    if (statusCode == 1){
                        createLocalFileStructure(0, 9)
                        buttonDownload.disableProperty().set(false)
                        buttonStart.disableProperty().set(false)
                        buttonStop.disableProperty().set(true)
                    }
                }
            }

            buttonDownload.disableProperty().set(true)
            buttonStop.disableProperty().set(true)
        }

        drawer (side = Side.BOTTOM, multiselect = false) {
            item("files", expanded = true){
                tableview(viewFileStructure) {
                    readonlyColumn("Name", ViewFileStructure::nameFile)
                    rowExpander(expandOnDoubleClick = true){
                        paddingLeft = expanderColumn.width
                        tableview(it.structureFile){
                            readonlyColumn("TypeFile", WADVersionFileData::typeFile)
                            readonlyColumn("CodeFile", WADVersionFileData::codeFile)
                            readonlyColumn("DadeFile", WADVersionFileData::dateFile)
                            readonlyColumn("LengthFile", WADVersionFileData::lengthFile)
                            readonlyColumn("StatusFile", WADVersionFileData::statusFile)
                        }
                    }
                }

            }

            item("parsing"){

            }
            item("test") {

                    vbox {
                        class Branch(val id : Int, val code : String, val city : String, val state : String)
                        class Region(val id : Int, val name : String, val country : String, val branches : ObservableList<Branch>)

                        val  regions = listOf(Region(1,"Pac Nor", "USA", listOf(Branch(1,"d", "seatl","WA")).observable())).observable()
                        tableview(regions) {

                        }
                    }

            }
        }
    }
}


