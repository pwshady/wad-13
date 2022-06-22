package views

import controller.WADProjectsController
import javafx.scene.Parent
import staticWAD.WADStatic
import tornadofx.*

class WADProjectTopView() : View() {
    val  projectsController : WADProjectsController by inject()
    override val root: Parent = menubar {
        menu(WADStatic.WADconst.labels["WADProjectTopView__menu__file"]) {
            WADStatic.WADconst.labels["WADProjectTopView__menu__file__new_project"]?.let { item(it).action { projectsController.createProjectView() } }
            item("")
            WADStatic.WADconst.labels["WADProjectTopView__menu__file__open_project"]?.let { item(it).action { projectsController.openProjectView() } }
            menu ("Стоп"){  }
            menu ("Stop and close"){  }
            item("New project")
        }

        menu("Viev"){
        }

        menu ("Help"){
            item("About")
        }

    }
}