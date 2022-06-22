package file

import staticWAD.WADStatic
import java.io.File
import java.nio.charset.Charset

class IOFileData {

    fun errorsLoad (path: String): Int
    {
        var resultCode = 0
        var fileLabels = File("src/main/resources${path}")
        if(fileLabels.exists()){
            val result = loadFileStringToMapPairIntString(fileLabels)
            resultCode = result.first
            WADStatic.WADconst.errors = result.second
        } else {
            fileLabels = File("src/main/resources/language/en/labels.txt")
            if(fileLabels.exists()){
                val result = loadFileStringToMapPairIntString(fileLabels)
                if(result.first == -1){
                    resultCode = 1001
                } else {
                    resultCode = result.first
                }
                WADStatic.WADconst.errors = result.second
            }
        }
        return resultCode
    }

    fun labelsLoad(path: String): Int
    {
        var resultCode = 0
        var fileLabels = File("src/main/resources${path}")
        if(fileLabels.exists()){
            val result = loadFileStringToMapPairStringString(fileLabels)
            resultCode = result.first
            WADStatic.WADconst.labels = result.second
        } else {
            fileLabels = File("src/main/resources/language/en/labels.txt")
            if(fileLabels.exists()){
                val result = loadFileStringToMapPairStringString(fileLabels)
                if(result.first == -1){
                    resultCode = 1001
                } else {
                    resultCode = result.first
                }
                WADStatic.WADconst.labels = result.second
            }
        }
        return resultCode
    }

    private fun loadFileStringToMapPairStringString(file: File): Pair<Int, Map<String, String>>
    {
        var resultCode = -1
        var mapResult = mutableMapOf<String, String>()
        var listResult = mutableListOf<String>()
        try {
            file.forEachLine(Charset.defaultCharset()) { listResult.add(it) }
        }
        catch (e: Exception){
            resultCode = 101
        }
        try {
            listResult.map {
                if (it.contains("=")){
                    var arrayStr = it.split("=")
                    mapResult.put(arrayStr[0], arrayStr[1])
                }
            }
        }
        catch (e: Exception){
            resultCode = 111
        }
        return Pair(resultCode, mapResult)
    }

    private fun loadFileStringToMapPairIntString(file: File): Pair<Int, Map<Int, String>>
    {
        var resultCode = -1
        var mapResult = mutableMapOf<Int, String>()
        var listResult = mutableListOf<String>()
        try {
            file.forEachLine { listResult.add(it) }
        }
        catch (e: Exception){
            resultCode = 101
        }
        try {
            listResult.map {
                var arrayStr = it.split("=")
                mapResult.put(arrayStr[0].toInt(), arrayStr[1])
            }
        }
        catch (e: Exception){
            resultCode = 111
        }
        return Pair(resultCode, mapResult)
    }
}