package net.boquetetech.uvsensor

import android.bluetooth.BluetoothDevice

class BluetoothUtils {
    companion object {
        val ACTION_DISCOVERY_STARTED = "ACTION_DISCOVERY_STARTED"
        val ACTION_DISCOVERY_STOPPED = "ACTION_DISCOVERY_STOPPED"
        val ACTION_DEVICE_FOUND = "ACTION_DEVICE_FOUND"
        val ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED"
        val ACTION_DEVICE_DISCONNECTED = "ACTION_DEVICE_DISCONNECTED"
        val ACTION_MESSAGE_RECEIVED = "ACTION_MESSAGE_RECEIVED"
        val ACTION_MESSAGE_SENT = "ACTION_MESSAGE_SENT"
        val ACTION_CONNECTION_ERROR = "ACTION_CONNECTION_ERROR"
        val EXTRA_DEVICE = "EXTRA_DEVICE"
        val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    }

    interface IBluetoothSDKListener {
        /**
         * from action BluetoothUtils.ACTION_DISCOVERY_STARTED
         */
        fun onDiscoveryStarted()

        /**
         * from action BluetoothUtils.ACTION_DISCOVERY_STOPPED
         */
        fun onDiscoveryStopped()

        /**
         * from action BluetoothUtils.ACTION_DEVICE_FOUND
         */
        fun onDeviceDiscovered(device: BluetoothDevice?)

        /**
         * from action BluetoothUtils.ACTION_DEVICE_CONNECTED
         */
        fun onDeviceConnected(device: BluetoothDevice?)

        /**
         * from action BluetoothUtils.ACTION_MESSAGE_RECEIVED
         */
        fun onMessageReceived(device: BluetoothDevice?, message: String?)

        /**
         * from action BluetoothUtils.ACTION_MESSAGE_SENT
         */
        fun onMessageSent(device: BluetoothDevice?)

        /**
         * from action BluetoothUtils.ACTION_CONNECTION_ERROR
         */
        fun onError(message: String?)

        /**
         * from action BluetoothUtils.ACTION_DEVICE_DISCONNECTED
         */
        fun onDeviceDisconnected()
    }
}