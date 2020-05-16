package com.nuang.myfirstapp

import android.net.Uri

class ContactModel {
    var name: String? = null
    var number: String? = null
    var mail: String? = null
    var photoName: String? = null
    var id: String? = null

    fun setNames(name: String) {
        this.name = name
    }

    fun getNumbers(): String {
        return number.toString()
    }

    fun setid(name: String) {
        this.id = name
    }

    fun getid(): String {
        return id.toString()
    }

    fun setNumbers(number: String) {
        this.number = number
    }

    fun getNames(): String {
        return name.toString()
    }

    fun setMails(mail: String) {
        this.mail = mail
    }

    fun getMails(): String {
        return mail.toString()
    }
    fun setPhoto(photo: String) {
        this.photoName = photo
    }

    fun getPhoto(): String? {
        return photoName
    }
}