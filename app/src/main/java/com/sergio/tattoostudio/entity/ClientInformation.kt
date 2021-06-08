package com.sergio.tattoostudio.entity

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class ClientInformation(
    var id:String = "",
    var cname:String = "",
    var cphoto:String = "",
    var cphoneNumber:String = "",
    var cemail:String = "",
    var cbirthDate:String = "",
    var cdateOfLastTattoo:String = "",
    var cinsta:String = "",
    var ctattooCount:String = "0",
    var ctattooTotalCost:String ="00,00",
    var cdateOfFirstTattoo:String =""


) : Serializable{

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun isNewFirstDate(dateChosenString: String, format: SimpleDateFormat):Boolean{
        return if (cdateOfFirstTattoo == "") {
            true
        }else{
            val dateChosen = Calendar.getInstance()
            val dateFirst = Calendar.getInstance()
            dateChosen.time = format.parse(dateChosenString)
            dateFirst.time = format.parse(cdateOfFirstTattoo)
            dateChosen.before(dateFirst)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun isNewLastDate(dateChosenString: String, format: SimpleDateFormat):Boolean{
        return if (cdateOfLastTattoo == "") {
            true
        }else{
            val dateChosen = Calendar.getInstance()
            val dateLast = Calendar.getInstance()
            dateChosen.time = format.parse(dateChosenString)
            dateLast.time = format.parse(cdateOfLastTattoo)
            dateChosen.after(dateLast)
        }
    }
}

