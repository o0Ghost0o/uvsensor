package net.boquetetech.uvsensor

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BluetoothSDKService : Service() {

    // Service Binder
    private val binder = LocalBinder()

    // Bluetooth stuff
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var pairedDevices: MutableSet<BluetoothDevice>
    private var connectedDevice: BluetoothDevice? = null
    private val MY_UUID = "..."
    private val RESULT_INTENT = 15

    // Bluetooth connections
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null
    private var mAcceptThread: AcceptThread? = null

    // Invoked only first time
    override fun onCreate() {
        super.onCreate()
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    // Invoked every service star
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    /**
     * Class used for the client Binder.
     */
    inner class LocalBinder : Binder() {
        /*
        Function that can be called from Activity or Fragment
        */
    }


    /**
     * Broadcast Receiver for catching ACTION_FOUND aka new device discovered
     */
    private val discoveryBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            /*
              Our broadcast receiver for manage Bluetooth actions
            */
        }
    }

    private inner class AcceptThread : Thread() {
        // Body
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        // Body
    }

    @Synchronized
    private fun startConnectedThread(
        bluetoothSocket: BluetoothSocket?,
    ) {
        connectedThread = ConnectedThread(bluetoothSocket!!)
        connectedThread!!.start()
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        // Body
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(discoveryBroadcastReceiver)
        } catch (e: Exception) {
            // already unregistered
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun pushBroadcastMessage(action: String, device: BluetoothDevice?, message: String?) {
        val intent = Intent(action)
        if (device != null) {
            intent.putExtra(BluetoothUtils.EXTRA_DEVICE, device)
        }
        if (message != null) {
            intent.putExtra(BluetoothUtils.EXTRA_MESSAGE, message)
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

}