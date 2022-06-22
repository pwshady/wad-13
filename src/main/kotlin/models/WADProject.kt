package models

data class WADProject(
    var name: String = "",
    var domenName: String = "",
    var path: String = "",
    var status: String = "",
    var resumeKey: String = "",
    var projectSettings: ProjectSettings = ProjectSettings()
)