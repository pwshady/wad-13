package views

import javafx.scene.Parent
import tornadofx.*


class WADProjectsView() : View() {
    init {

    }
    override val root: Parent = borderpane {
        minWidth = 1200.0
        minHeight = 800.0
        var topViev = WADProjectTopView()
        top(topViev::class)

        button("t1"){
            action {

            }
        }
        center(WADProjectCenterView::class)
        var rightViev = WADProjectRightView()
        right(rightViev::class)

    }
}