package views

import Singleton.FileDownload
import javafx.stage.Stage
import tornadofx.App

class WADApp() : App(WADProjectsView::class)
{
    override fun start(stage: Stage){
        with(stage){
            isResizable = false
            super.start(this)
        }
    }
}