package json

import kotlinx.serialization.Serializable

@Serializable
data class WADProjectJson (
    val name: String,
    val domenName: String,
    val path: String,
    val status: String,
    val resumeKey: String,
    val projectSettings: ProjectSettingsJson
) {
    @Serializable
    data class ProjectSettingsJson (
        var from : String,
        var to : String,
        var timestamp : Int,
        var fileType : String,
        var fileLimit : Int,
    )
}