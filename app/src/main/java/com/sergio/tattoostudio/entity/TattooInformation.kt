package com.sergio.tattoostudio.entity

import java.io.Serializable

data class TattooInformation(
        val id:String = "",
        val tattooPhoto:String = "",
        val tattooName:String = "",
        val tattooDescription:String = "",
        val tattooDate:String = "",
        val tattooPrice:String =""
): Serializable
