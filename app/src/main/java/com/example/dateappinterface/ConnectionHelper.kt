package com.example.dateappinterface

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager

class ConnectionHelper {
    var con: Connection? = null
    var uname: String? = null
    var pass: String? = null
    var ip: String? = null
    var port: String? = null
    var database: String? = null
    fun connectionclass(): Connection? {
        ip = "192.168.0.107"
        database = "DatingAppAccounts"
        port = "1433"
        uname = "user"
        pass = "123"
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var connection: Connection? = null
        var ConnectionURL: String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            ConnectionURL = "jdbc:jtds:sqlserver://$ip:$port;databasename=$database;user=$uname;password=$pass"
            connection = DriverManager.getConnection(ConnectionURL)
        } catch (ex: Exception) {
            Log.e("Error ", ex.message!!)
        }
        return connection
    }
}