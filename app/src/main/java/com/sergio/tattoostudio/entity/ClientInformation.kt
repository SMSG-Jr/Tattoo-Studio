package com.sergio.tattoostudio.entity

import java.io.Serializable

class ClientInformation(
    val cname:String = "Nome",
    val cphoneNumber:String = "(XX)XXXXX-XXXX",
    val cemail:String = "XXX@XXXX.com",
    val cbirthDate:String = "XX/XX/XXXX",
    val cdateOfLastTattoo:String = "XX/XX/XXXX",
    val cinsta:String = "XXXX",
    val ctattooCount:String = "XX",
    val ctattooTotalCost:String ="R$ XX,XX",
    val cdateOfFirstTattoo:String ="XX/XX/XXXX",

) : Serializable

