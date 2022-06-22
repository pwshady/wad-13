package views

import javafx.scene.Parent
import javafx.scene.control.TextField
import tornadofx.*

class WADProjectRightView() : View() {
    override val root: Parent = hbox{
        var from : TextField by singleAssign()
        button("t1"){
            action {
                //val dao = WADProjectsDao()
                println( )
            }
        }
        from = textfield {  }

        }

    }
