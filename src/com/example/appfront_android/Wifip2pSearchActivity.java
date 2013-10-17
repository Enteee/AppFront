package com.example.appfront_android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


public abstract class Wifip2pSearchActivity extends Activity{
	
	/**
	 * A unique string identifying AppFront service.
	 */
	final public static String				SERVICE_IDENTIFYER		= "appfront-tags";
	/** 
	 * The hashmap key for the tags list.
	 */
	final public static String				SERVICE_TAG_KEY			= "Tags";
	final public static int					REQUEST_ENABLE_WIFI		= 1;
	
	private WifiP2pManager 					wifip2pManager;
	private	Channel							wifip2pChannel;
	private WiFiDirectBroadcastReceiver		wifip2pReceiver;
	private IntentFilter 					wifip2pIntentFilter;
	private	HashMap<String, String> 		wifip2pServiceRecord;
	private WifiP2pServiceInfo				wifip2pServiceInfo;
	private WifiP2pDnsSdServiceRequest		wifip2pServiceRequest;
	private boolean							wifip2pRunning			= false;
	/**
	 * Setup the record listener
	 */
	private DnsSdTxtRecordListener 			wifip2pRecordListener 	= new DnsSdTxtRecordListener() {
		/* Callback includes:
    	 * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
    	 * record: TXT record dta as a map of key/value pairs.
    	 * device: The device running the advertised service.
    	 */
		@Override
    	public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
			Log.v("wifip2p","New domain:"+fullDomain);
			if(fullDomain.compareTo(SERVICE_IDENTIFYER+"."+SERVICE_IDENTIFYER+".local.") == 0 ){
				final String foundTag = (String) record.get(SERVICE_TAG_KEY);
				final String searchTag = (String) wifip2pServiceRecord.get(SERVICE_TAG_KEY);
				Log.v("wifip2p","TAG found:"+foundTag);
				if(searchTag.compareTo(foundTag)==0){
					toogleCheck(true);
				}
				
			}
		}
    };
    /**
     * Define the response listener
     */
	private DnsSdServiceResponseListener	wifip2pResponseListener = new DnsSdServiceResponseListener() {
        @Override
        public void onDnsSdServiceAvailable(String instanceName, String registrationType,WifiP2pDevice resourceType) {
        	// Do nothing..
        }
    };
    
    abstract protected void toogleCheck(boolean enable);
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initialize wifip2p
		wifip2pManager		= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		wifip2pChannel		= wifip2pManager.initialize(this, getMainLooper(), null);
		wifip2pReceiver		= new WiFiDirectBroadcastReceiver(wifip2pManager, wifip2pChannel, this);
		// setup reponse listeners
        wifip2pManager.setDnsSdResponseListeners(wifip2pChannel, wifip2pResponseListener, wifip2pRecordListener);
		// setup intent filter
		wifip2pIntentFilter = new IntentFilter();
		wifip2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		wifip2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		wifip2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		wifip2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    registerReceiver(wifip2pReceiver, wifip2pIntentFilter);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    unregisterReceiver(wifip2pReceiver);
	}
	
	/**
	 * Enables Wifi
	 */
    private void enableWifi(){
    	// TODO: enable wifi dialog
    }
	
	/**
	 * Accepts activity results
	 */
	protected void onActivityResult(final int requestCode, final int resultCode,final Intent data){
		switch(requestCode){
			case REQUEST_ENABLE_WIFI:
				// did the user say 'yes'?
				if(resultCode == RESULT_OK){
					wifip2pRunning = true;
				}
				break;
		}
	}
	
	public void startSearch(final String searchTag){
		// put service tag into serviceRecord
		wifip2pServiceRecord = new HashMap<String, String>();
		wifip2pServiceRecord.put(SERVICE_IDENTIFYER, SERVICE_IDENTIFYER);
		wifip2pServiceRecord.put(SERVICE_TAG_KEY, searchTag);
		// check if we are already advertising a tag
        if(wifip2pRunning == true){
	        wifip2pManager.removeLocalService(wifip2pChannel, wifip2pServiceInfo,  new ActionListener() {
	            @Override
	            public void onSuccess() {
	                // Command successful! Code isn't necessarily needed here,
	                // Unless you want to update the UI or add logging statements.
	            }
	
	            @Override
	            public void onFailure(int arg0) {
	                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
	            }
	        });
        }
	    // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        wifip2pServiceInfo = WifiP2pDnsSdServiceInfo.newInstance(SERVICE_IDENTIFYER,SERVICE_IDENTIFYER, wifip2pServiceRecord);
        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        wifip2pManager.addLocalService(wifip2pChannel, wifip2pServiceInfo, new ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
            	Log.v("wifip2p","STARTED!");
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
        
        wifip2pServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        wifip2pManager.addServiceRequest(wifip2pChannel,wifip2pServiceRequest,
        		new ActionListener() {
                	@Override
                    public void onSuccess() {
                        // Success!
                    }

                    @Override
                    public void onFailure(int code) {
                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    }
                });
        
        // start discovering services
        wifip2pManager.discoverServices(wifip2pChannel, new ActionListener() {
            @Override
            public void onSuccess() {
                // Success!
            }

            @Override
            public void onFailure(int code) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
        
        // wifip2p is running now!
        wifip2pRunning = true;
	}
}
