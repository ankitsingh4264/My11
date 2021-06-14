package com.example.my11

import com.example.my11.beans.Matche
import com.example.my11.beans.Predicted

object  Utils {
    var FutureMatchtoPlay:Matche? =null
    var CountForNullPlayer:Int? = 0
    var latitude:String? = ""
    var longitude:String? = ""
    var prediction:Predicted?=null
    var SetofplayedMatches:HashSet<String>  = HashSet()
}