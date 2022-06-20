package br.com.android.gitrepos.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by Carlos Souza on 17,junho,2022
 */
object Permissions {

    const val INTERNET_PERMISSION_CODE = 0
    private const val INTERNET_PERMISSION = Manifest.permission.INTERNET

    fun hasInternetPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, INTERNET_PERMISSION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestInternetPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(INTERNET_PERMISSION), INTERNET_PERMISSION_CODE)
    }
}