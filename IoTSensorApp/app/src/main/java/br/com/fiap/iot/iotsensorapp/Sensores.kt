package br.com.fiap.iot.iotsensorapp

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Sensores{
    var S0001: String?=null
    var S0002: String?=null
    var S0003: String?=null

    constructor(){

    }

    constructor(ultrasonico:String?,som:String?,luminosidade:String?){
        this.S0001 = ultrasonico
        this.S0002 = som
        this.S0003 = luminosidade
    }
}