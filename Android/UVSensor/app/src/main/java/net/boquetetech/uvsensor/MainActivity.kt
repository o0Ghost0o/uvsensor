package net.boquetetech.uvsensor

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    companion object {
        var bt_device: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      view items
        val bt_status = findViewById<ImageView>(R.id.bt_status)
        val bt_on = findViewById<Button>(R.id.bt_on)
        val bt_off = findViewById<Button>(R.id.bt_off)
        val bt_paired = findViewById<Button>(R.id.bt_paired)

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(applicationContext, "Dispositivo no soporta Bluetooth!", Toast.LENGTH_SHORT).show()
        }

        if (bluetoothAdapter?.isEnabled == true) {
            bt_status.setImageResource(R.drawable.ic_bluetooth_on)
        } else {
            bt_status.setImageResource(R.drawable.ic_bluetooth_off)
        }

        val bt_on_call =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    bt_status.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth encendido", Toast.LENGTH_SHORT).show()
                } else {
                    bt_status.setImageResource(R.drawable.ic_bluetooth_off)
                    Toast.makeText(this, "No se pudo encender el bluetooth", Toast.LENGTH_SHORT).show()
                }
            }

        bt_on.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                bt_on_call.launch(enableBtIntent)
                bt_status.setImageResource(R.drawable.ic_bluetooth_on)
            } else {
                bt_status.setImageResource(R.drawable.ic_bluetooth_on)
                Toast.makeText(this, "Bluetooth se encuentra encendido", Toast.LENGTH_SHORT).show()
            }
        }

        bt_off.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true) {
                bluetoothAdapter.disable()
                bt_status.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth apagado", Toast.LENGTH_SHORT).show()
            } else {
                bt_status.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth se encuentra apagado", Toast.LENGTH_SHORT).show()
            }
        }

        bt_paired.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true) {
                val devices = bluetoothAdapter.bondedDevices
                var hc_05 = false
                for (device in devices) {
                    var deviceName = device.name
                    var deviceAddress = device
                    if (deviceName == "HC-05") {
                        hc_05 = true
                        Toast.makeText(this, "Dispositivo emparejado. Name: ${deviceName} UUID: ${deviceAddress}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ControlActivity::class.java)
                        intent.putExtra(bt_device, deviceAddress.toString())
                        startActivity(intent)
                    }
                }
                if (hc_05) {
//                    Toast.makeText(this, "Dispositivo emparejado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No se ha emparejado con el HC-05 todavia, por favor empareje el dispositivo", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Bluetooth se encuentra apagado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}