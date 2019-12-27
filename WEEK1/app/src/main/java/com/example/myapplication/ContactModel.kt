package com.example.myapplication

/**
 * Created by Parsania Hardik on 20-Jan-18.
 */
class ContactModel {

    var name: String? = null
    var number: String? = null

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

}