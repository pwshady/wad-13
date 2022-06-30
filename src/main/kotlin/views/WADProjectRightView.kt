package views

import javafx.scene.Parent
import javafx.scene.control.TextField
import org.jsoup.Jsoup
import tornadofx.*

class WADProjectRightView() : View() {
    override val root: Parent = hbox{
        var from : TextField by singleAssign()
        button("t1"){
            action {
                //val dao = WADProjectsDao()
                val doc = Jsoup.connect("https://web.archive.org/web/20121102132909id_/http://web.archive.org/screenshot/http://pozdravok.ru/").get()
                println(doc.outerHtml())
            }
        }
        from = textfield {  }

        }

    }
