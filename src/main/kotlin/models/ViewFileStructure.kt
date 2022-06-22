package models

import javafx.collections.ObservableList

data class ViewFileStructure (
    var nameFile : String,
    var partFile : Int,
    var structureFile : ObservableList<WADVersionFileData>
)