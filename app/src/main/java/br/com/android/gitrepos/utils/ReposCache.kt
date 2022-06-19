package br.com.android.gitrepos.utils

import android.content.Context
import android.util.Log
import java.io.*


/**
 * Created by Carlos Souza on 16,junho,2022
 */
object RepoCache {

    private const val CACHEFILE = "repo_cache"

    fun save(context: Context, json: String) {
        try {
            val file = File(context.cacheDir, CACHEFILE)
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

    fun read(context: Context): String {
        val retBuf = StringBuffer()
        try {
            val file = File(context.cacheDir, CACHEFILE)
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