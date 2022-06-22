package views

import controller.WADProjectsController
import javafx.scene.Parent
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import staticWAD.WADStatic
import tornadofx.*

class WADOpenProjectView : Fragment() {
    private val  wadProjectsController : WADProjectsController by inject()
    override val root: Parent = vbox {
        var listViev : ListView<String> by singleAssign()
        listViev = listview(WADStatic.WADstat.closeProjectListName){
            selectionModel.selectionMode = SelectionMode.SINGLE
            contextmenu {
                item("Open").action {
                    if (listViev.selectedItem != null){
                        if (wadProjectsController.openProject(listViev.selectedItem!!) == -1){
                            WADStatic.WADstat.closeProjectListName
                        }
                    }
                }
                item("Delete").action {
                    if (listViev.selectedItem != null){
                        if (wadProjectsController.deleteProject(listViev.selectedItem!!) == 0){
                            WADStatic.WADstat.closeProjectListName
                        }
                    }
                }
            }
        }
        listViev.onDoubleClick {
            if (listViev.selectedItem != null){
                if (wadProjectsController.openProject(listViev.selectedItem!!) == 0){
                    WADStatic.WADstat.openProjectListName
                }
            }
        }

    }
    override fun onUndock() {
        WADStatic.WADstat.openProjectStatusCode = 0
    }
}