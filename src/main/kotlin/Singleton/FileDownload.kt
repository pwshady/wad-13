package Singleton

import models.DownloadFileStructure
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class FileDownload {
    object Download{
        val status = AtomicBoolean(true)
        fun fileDownload(downloadFileStructure: DownloadFileStructure) : Int
        {
            var resultCode = -1
            thread {
                Thread.sleep(1000)
                FileDownload.Download.status.set(true)
            }
            return resultCode
        }
    }
}