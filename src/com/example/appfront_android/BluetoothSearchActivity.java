package com.example.appfront_android;

import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.widget.CheckBox;


public class BluetoothSearchActivity extends Activity{
	
	final public static int					REQUEST_ENABLE_BT	= 1;
    // Stops scanning after 10 seconds.
    private static final long 				SCAN_PERIOD 		= 10000;
	
	private BluetoothManager 				bluetoothManager;
	private BluetoothAdapter				bluetoothAdapter;
	private boolean							bluetoothRunning	= false;
	
	private boolean							leScanning			= false;
	
	private TagLeScanCallback 				tagLeScanCallback	= new TagLeScanCallback();
	
	private UUID[] 							searchUUIDs			= new UUID[1];
	    
	private class TagLeScanCallback implements BluetoothAdapter.LeScanCallback {
	    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
	    	ParcelUuid[] uuids = device.getUuids();
	    	for(int i=0;i<uuids.length;i++){
	    		UUID uuid = uuids[i].getUuid();
	    		// check if matching uuid
	    		for(int j=0;j<searchUUIDs.length;j++)
	    		if(uuid.compareTo(searchUUIDs[j]) == 0){
	    			// found match!
	    			CheckBox chkFound = (CheckBox)findViewById(R.id.chkFound);
	    			chkFound.setChecked(true);
	    		}
	    	}
	    }
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get the Bluetooth manager
		bluetoothManager	= (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		// and the Bluetooth adapter
		bluetoothAdapter	= bluetoothManager.getAdapter();
		enableBluetooth();
	}
	
	private void enableBluetooth(){
		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			bluetoothRunning = false;
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
		}else{
			bluetoothRunning = true;
		}
	}
	
	/**
	 * Accepts activity results
	 */
	protected void onActivityResult(final int requestCode, final int resultCode,final Intent data){
		switch(requestCode){
			case REQUEST_ENABLE_BT:
				// did the user say 'yes'?
				if(resultCode == RESULT_OK){
					bluetoothRunning = true;
				}
				break;
		}
	}
	
	public void startSearch(final String searchTag){
		// build search UUID
		String uuidString = String.format("%1$16.16s", searchTag);
		// TODO: better string -> hash
		long lowBytes = uuidString.substring(0, 9).hashCode();
		long hiBytes = uuidString.substring(8).hashCode();
		searchUUIDs[0] = new UUID(lowBytes,hiBytes);
		scanLeDevice(true);
	}
	
	private void scanLeDevice(final boolean enable) {
		if (enable) {
            leScanning = true;
            bluetoothAdapter.startLeScan(searchUUIDs,tagLeScanCallback);
        } else {
            leScanning = false;
            bluetoothAdapter.stopLeScan(tagLeScanCallback);
        }
    }
}
