package views

import controller.WADProjectsController
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import javafx.scene.control.DatePicker
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import models.ProjectSettings
import models.WADProject
import staticWAD.WADStatic
import tornadofx.*
import validation.ValidationProject
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class WADCreateProjectView() : Fragment() {
    private val  wadProjectsController : WADProjectsController by inject()
    override val root: Parent = form {
        var dirFlag = true
        var creat = true
        var name : TextField by singleAssign()
        var domenName : TextField by singleAssign()
        var from : TextField by singleAssign()
        var dateFrom : DatePicker by singleAssign()
        var to : TextField by singleAssign()
        var dateTo : DatePicker by singleAssign()
        var directory : TextField by singleAssign()
        var errorList : TextArea by singleAssign()
        var errorText = MutableList(6,{Pair("",0)})
        val dateFromValue = SimpleObjectProperty<LocalDate>()
        val dateToValue = SimpleObjectProperty<LocalDate>()
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val min = "19970101000000"
        var max = sdf.format(Date())

        fun statusUpdating (errorText : List<Pair<String,Int>>):Unit {
            errorList.textProperty().set(errorText.map { task -> task.first }.joinToString(separator =""))
            when (errorText.map { task -> task.second }.maxOrNull()){
                0 -> errorList.style{
                    backgroundColor += c("green")
                    creat = true
                }
                1 -> errorList.style{
                    backgroundColor += c("yellow")
                    creat = true
                }
                2 -> errorList.style{
                    backgroundColor += c("red")
                    creat = false
                }
            }
            println(creat)
        }

        fieldset(WADStatic.WADconst.labels["WADCreateProjectView__formname"]){
            field(WADStatic.WADconst.labels["WADCreateProjectView__projectname"]) {
                name = textfield()
                errorText.set(0, Pair(ValidationProject.nameValidation(name.text).first,ValidationProject.nameValidation(name.text).second))
                name.textProperty().onChange {
                    errorText.set(0, Pair(ValidationProject.nameValidation(name.text).first,ValidationProject.nameValidation(name.text).second))
                    if (dirFlag){
                        directory.text = "c:\\wad\\${name.text}"
                    }
                    if (WADStatic.WADstat.allProjectListName != null) {
                        for (i in 0 until WADStatic.WADstat.allProjectListName.size){
                            if(name.text == WADStatic.WADstat.allProjectListName[i]){
                                Pair(WADStatic.WADconst.labels["WADCreateProjectView__projectname__error1"], 2)?.let { it1 ->
                                    errorText.set(0, it1 as Pair<String, Int>
                                    )
                                }
                            }
                        }
                    }
                    statusUpdating (errorText);
                }
            }
            field(WADStatic.WADconst.labels["WADCreateProjectView__domenname"]){
                domenName = textfield()
                errorText.set(1, Pair(ValidationProject.domenNameValidation(domenName.text).first,ValidationProject.domenNameValidation(domenName.text).second))
                domenName.textProperty().onChange {
                    errorText.set(1, Pair(ValidationProject.domenNameValidation(domenName.text).first,ValidationProject.domenNameValidation(domenName.text).second))
                    statusUpdating(errorText);
                }
            }
            field(WADStatic.WADconst.labels["WADCreateProjectView__datefrom"]){
                hbox {
                    from = textfield()
                    dateFrom = datepicker(dateFromValue){
                        value = LocalDate.now()
                    }
                    WADStatic.WADconst.labels["WADCreateProjectView__button__minimum"]?.let {
                        button(it) {
                            action {
                                from.text = min
                            }
                        }
                    }
                    dateFrom.valueProperty().onChange {
                        from.text = "${dateFrom.value.toString().replace("-","")}000000"
                    }
                    errorText.set(2, Pair(ValidationProject.dateTimeValidation(from.text,min,"",
                        WADStatic.WADconst.labels["WADCreateProjectView__datefrom"]
                    )
                        .first,ValidationProject.dateTimeValidation(from.text,min,"",
                        WADStatic.WADconst.labels["WADCreateProjectView__datefrom"]
                    ).second))
                    from.textProperty().onChange {
                        errorText.set(2, Pair(ValidationProject.dateTimeValidation(from.text,min,"",
                            WADStatic.WADconst.labels["WADCreateProjectView__datefrom"]
                        )
                            .first,ValidationProject.dateTimeValidation(from.text,min,"",
                            WADStatic.WADconst.labels["WADCreateProjectView__datefrom"]
                        ).second))
                        statusUpdating(errorText);
                    }
                }
            }
            field(WADStatic.WADconst.labels["WADCreateProjectView__dateto"]){
                hbox{
                    to = textfield()
                    dateTo = datepicker(dateToValue){
                        value = LocalDate.now()
                    }
                    WADStatic.WADconst.labels["WADCreateProjectView__button__maximum"]?.let {
                        button(it) {
                            action {
                                max = sdf.format(Date())
                                to.text = max
                            }
                        }
                    }
                    dateTo.valueProperty().onChange {
                        to.text = "${dateTo.value.toString().replace("-","")}000000"
                        max = sdf.format(Date())
                    }
                    errorText.set(3, Pair(ValidationProject.dateTimeValidation(to.text,"",max,
                        WADStatic.WADconst.labels["WADCreateProjectView__dateto"]
                    )
                        .first,ValidationProject.dateTimeValidation(to.text,"",max,
                        WADStatic.WADconst.labels["WADCreateProjectView__dateto"]
                    ).second))
                    to.textProperty().onChange {
                        max = sdf.format(Date())
                        errorText.set(3, Pair(ValidationProject.dateTimeValidation(to.text,"",max,
                            WADStatic.WADconst.labels["WADCreateProjectView__dateto"]
                        )
                            .first,ValidationProject.dateTimeValidation(to.text,"",max,
                            WADStatic.WADconst.labels["WADCreateProjectView__dateto"]
                        ).second))
                        statusUpdating(errorText);
                    }
                }
            }
            field(WADStatic.WADconst.labels["WADCreateProjectView__Files_directory"]){
                hbox {
                    directory = textfield()
                    WADStatic.WADconst.labels["WADCreateProjectView__button__path"]?.let {
                        button(it){
                            action {

                            }
                        }
                    }
                    errorText.set(4, Pair(ValidationProject.directotyValidation(directory.text)
                        .first,ValidationProject.directotyValidation(directory.text).second))
                    directory.focusedProperty().onChange {
                        dirFlag = false
                    }
                    directory.textProperty().onChange {
                        errorText.set(4, Pair(ValidationProject.directotyValidation(directory.text)
                            .first,ValidationProject.directotyValidation(directory.text).second))
                        val dir = File(directory.text)
                        if(dir.isDirectory){
                            Pair(WADStatic.WADconst.labels["WADCreateProjectView__projectname__error2"],1)?.let { it1 ->
                                errorText.set(4,
                                    it1 as Pair<String, Int>
                                )
                            }
                        }
                        statusUpdating(errorText);
                    }
                }
            }
            errorList = textarea(){
            }
            errorList.disableProperty().set(true)
            statusUpdating(errorText);
        }

        hbox{
            WADStatic.WADconst.labels["WADCreateProjectView__button__creat"]?.let{
                button(it) {
                    setOnAction {
                        if (creat) {
                            var result: Int
                            var wadProject = WADProject(
                                name.text, domenName.text, directory.text, "", "",
                                ProjectSettings(from.text, to.text)
                            )
                            result = wadProjectsController.createProject(wadProject)
                            //result = wadProjectsController.openProject(wadProject.name)
                            WADStatic.WADstat.createProjectStatusCode = 2
                            wadProjectsController.createProjectView()
                            close()
                        }
                    }
                }
            }
            WADStatic.WADconst.labels["WADCreateProjectView__button__cancel"]?.let {
                button(it) {
                    setOnAction {
                        WADStatic.WADstat.createProjectStatusCode = 5
                        wadProjectsController.createProjectView()
                        close()
                    }
                }
            }
        }

    }

    override fun onUndock() {
        WADStatic.WADstat.createProjectStatusCode = 0
    }

}