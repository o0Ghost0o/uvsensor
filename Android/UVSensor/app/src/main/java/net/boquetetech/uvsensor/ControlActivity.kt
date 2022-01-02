package net.boquetetech.uvsensor

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class ControlActivity: AppCompatActivity() {

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        var keepReading: Boolean = false
        lateinit var m_address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_layout)

//      view items
        val bt_disconect = findViewById<Button>(R.id.bt_disconnect)
        val bt_read = findViewById<Button>(R.id.bt_read)
        val rData = findViewById<TextView>(R.id.rData)

        m_address = intent.getStringExtra(MainActivity.bt_device).toString()

        ConnectToDevice(this).execute()

        bt_read.setOnClickListener {
            if (keepReading) {
                bt_read.text = "Leer Datos"
                keepReading = false
            } else {
                keepReading = true
                bt_read.text = "Detener Lectura"
                rData.text = "Enviando datos..."
                var data: String = ""
                AsyncTask.execute {
                    while (keepReading) {
                        sendCommand("conn_on")
//                        var data = readData().split(":")
                        data = readData()
//                        sendData(data[0], data[1])
                        sendDump(data)
                    }
                    rData.text = "Se ha detenido el envio. " + data
                }
            }
        }

        bt_disconect.setOnClickListener {
            keepReading = false
            sendCommand("conn_off")
            disconnect()
        }
    }


    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun readData(): String {
//            Log.i(LOGTAG, Thread.currentThread().name)

        val bluetoothSocketInputStream = m_bluetoothSocket?.inputStream
        val buffer = ByteArray(1024)
        var bytes: Int
        var readMessage = ""
        //Loop to listen for received bluetooth messages
        while (true) {
            try {
                if (bluetoothSocketInputStream != null) {
                    bytes = bluetoothSocketInputStream.read(buffer)
                    readMessage = String(buffer, 0, bytes)
                    Log.e("read", readMessage);
                }
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }

        return readMessage
    }

    private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private fun sendDump(data: String) {
        val URL = "https://vm3163.eosdns.net/functions/functions.php"
        val apikey = "uvsensor-as5df-asf8asf-80"
        val action = "dump"
        // url to post our data

        // creating a new variable for our request queue

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(this@ControlActivity)

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        val request: StringRequest = object : StringRequest(
            Method.POST, URL,
            Response.Listener { response ->
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty

                // on below line we are displaying a success toast message.
                Toast.makeText(this@ControlActivity, "Data added to API", Toast.LENGTH_SHORT)
                    .show()
                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.
                    val status = respObj.getString("status")

                    // on below line we are setting this string s to our text view.
//                        responseTV.setText("Name : $name\nJob : $job")
                    Toast.makeText(
                        this@ControlActivity,
                        "Status: " + status,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> // method to handle errors.
                Toast.makeText(
                    this@ControlActivity,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): Map<String, String>? {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["apikey"] = apikey
                params["action"] = action
                params["data"] = data

                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        // below line is to make
        // a json object request.
        queue.add(request)
    }

    private fun sendData(reading: String, uvindex: String) {
        try {
            val requestQueue = Volley.newRequestQueue(this)
            val URL = "https://vm3163.eosdns.net/functions/functions.php"
            val apikey = "uvsensor-as5df-asf8asf-80"
            val jsonBody = JSONObject()
            jsonBody.put("apikey", apikey)
            jsonBody.put("action", "writeData")
            jsonBody.put("reading", reading)
            jsonBody.put("uvindex", uvindex)
            val requestBody = jsonBody.toString()
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, URL,
                Response.Listener { response -> Log.i("VOLLEY", response!!) },
                Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val data: ByteArray = requestBody.toByteArray(charset("utf-8"))
                    return data
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    var responseString = ""
                    if (response != null) {
                        responseString = response.statusCode.toString()
                        // can get more details such as response.headers
                    }
                    return Response.success(
                        responseString,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
                Toast.makeText(context, "No se ha podido conectar, intente nuevamente", Toast.LENGTH_SHORT)
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
}