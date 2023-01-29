package com.example.aoopproject.classes

import com.example.aoopproject.R

class ImageProvider {

    /*
    * @returns {number}
    * @param {String} nutriscore
    *
    * Returns the drawable for the provided nutriscore, or 0 if no nutri score was found
    * */
    fun getNutriScore(nutri: String?): Int{
        var img : Int = 0
        when(nutri?.lowercase()){
            "a" -> img = R.drawable.ic_nutriscore_a
            "b" -> img = R.drawable.ic_nutriscore_b
            "d" -> img = R.drawable.ic_nutriscore_d
            "c" -> img = R.drawable.ic_nutriscore_c
            "e" -> img = R.drawable.ic_nutriscore_e
            "unknown" -> img = R.drawable.ic_nutriscore_unknown
        }
        return img
    }

}