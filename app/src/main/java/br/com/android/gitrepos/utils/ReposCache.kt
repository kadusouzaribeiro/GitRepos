package br.com.android.gitrepos

import android.content.Context
import android.util.Log
import java.io.*


/**
 * Created by Carlos Souza on 16,junho,2022
 */
object RepoCache {

    private const val CACHEFILE = "repo_cache"
    private const val LASTPAGE = "page_cache"

    fun save(context: Context, json: String, page: Boolean = false) {
        try {
            var fileName = CACHEFILE
            if (page) {
                fileName = LASTPAGE
            }
            val file = File(context.cacheDir, fileName)
            val fileOutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(json)
            bufferedWriter.flush()
            bufferedWriter.close()
            outputStreamWriter.close()
            fileOutputStream.close()
        } catch (ex: Exception) {
            Log.e("CACHE", "Erro ao salvar cache: ${ex.message}")
        }
    }

    fun read(context: Context, page: Boolean = false): String {
        val retBuf = StringBuffer()
        try {
            var fileName = CACHEFILE
            if (page) {
                fileName = LASTPAGE
            }
            val file = File(context.cacheDir, fileName)
            val fileInputStream = FileInputStream(file)

            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var lineData = bufferedReader.readLine()
            while (lineData != null) {
                retBuf.append(lineData)
                lineData = bufferedReader.readLine()
            }

        } catch (ex: java.lang.Exception) {
            Log.e("CACHE", "Erro ao recuperar cache: ${ex.message}")
        }
        return retBuf.toString()
    }
}