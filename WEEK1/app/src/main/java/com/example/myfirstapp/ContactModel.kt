package com.example.myfirstapp

class ContactModel {
    var name: String? = null
    var number: String? = null
    var mail: String? = null

    fun setNames(name: String) {
        this.name = name
    }

    fun getNumbers(): String {
        return number.toString()
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
}