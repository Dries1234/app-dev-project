package com.example.aoopproject.classes

import android.net.Uri

class Contract {
    companion object {
        var AUTHORITY = "com.example.aoopproject"
        var BASE_CONTENT_URI = Uri.parse("content://$AUTHORITY")
    }

}