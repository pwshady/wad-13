package views

import controller.WADProjectsController
import javafx.scene.Parent
import javafx.scene.control.TabPane
import models.ProjectStatus
import staticWAD.WADStatic
import tornadofx.*

class WADProjectCenterView() : View() {
    val  wadProjectsController : WADProjectsController by inject()
    override val root: Parent = hbox {
        minWidth = 1200.0
        minHeight = 800.0
        var tp: TabPane by singleAssign()
        tp = tabpane() {}
        fun setTab(){
            for (i in 0 until WADStatic.WADstat.openProjectList.size){
                var flag = false
                for (j in 0 until tp.tabs.size){
                    if (WADStatic.WADstat.openProjectList[i].name == tp.tabs[j].text){
                        flag = true
                    }
                }
                if(!flag){
                    tp.tab(WADStatic.WADstat.openProjectList[i].name) {
                        WADStatic.WADstat.wadProjectList.add(ProjectStatus(WADStatic.WADstat.openProjectList[i].name))
                        this += WADProjectView(WADStatic.WADstat.openProjectList[i])
                        this.setOnClosed {
                            wadProjectsController.closeProject(this.text)
                        }
                    }
                }
            }
        }
        setTab()
        WADStatic.WADstat.openProjectList.onChange {
            setTab()
            println(WADStatic.WADstat.closeProjectListName)
        }

    }
}


