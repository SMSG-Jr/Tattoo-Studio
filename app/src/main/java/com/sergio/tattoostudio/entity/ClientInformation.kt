package com.sergio.tattoostudio.entity

import java.io.Serializable

class ClientInformation(
    val id:String = "",
    val cname:String = "",
    val cphoto:String = "",
    val cphoneNumber:String = "",
    val cemail:String = "",
    val cbirthDate:String = "",
    val cdateOfLastTattoo:String = "",
    val cinsta:String = "",
    val ctattooCount:String = "",
    val ctattooTotalCost:String ="",
    val cdateOfFirstTattoo:String =""


) : Serializable

