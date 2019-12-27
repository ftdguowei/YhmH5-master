package com.sigmatrix.yhmh5.yhmh5.dummy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sigmatrix.yhmh5.yhmh5.R;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends Activity
        implements EMDKManager.EMDKListener,
        Scanner.StatusListener, Scanner.DataListener
{

  private EMDKManager emdkManager = null;
  private BarcodeManager barcodeManager = null;
  private List<ScannerInfo> deviceList = null;
  private Scanner scanner = null;
  private boolean bSoftTriggerSelected = false;
  private boolean bDecoderSettingsChanged = false;
  private boolean bExtScannerDisconnected = false;
  private final Object lock = new Object();


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);

    EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);
    if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
      return;
    }
  }


  @Override
  public void onResume() {
    super.onResume();
    if (emdkManager != null) {
      // Acquire the barcode manager resources
      initBarcodeManager();
      // Enumerate scanner devices
      enumerateScannerDevices();
      initScanner();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    deInitScanner();
    deInitBarcodeManager();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (emdkManager != null) {
      emdkManager.release();
      emdkManager = null;
    }
  }

  private void deInitBarcodeManager(){
    if (emdkManager != null) {
      emdkManager.release(EMDKManager.FEATURE_TYPE.BARCODE);
    }
  }
  @Override
  public void onClosed() {
    if (emdkManager != null) {
      emdkManager.release();
      emdkManager = null;
    }
  }
//  @Override
//  public void onConnectionChange(ScannerInfo scannerInfo, BarcodeManager.ConnectionState connectionState) {
//    switch(connectionState) {
//      case CONNECTED:
//        bSoftTriggerSelected = false;
//        synchronized (lock) {
//          initScanner();
//          bExtScannerDisconnected = false;
//        }
//        break;
//      case DISCONNECTED:
//        bExtScannerDisconnected = true;
//        synchronized (lock) {
//          deInitScanner();
//        }
//        break;
//    }
//  }
  @Override
  public void onData(ScanDataCollection scanDataCollection) {
    if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
      ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
      for(final ScanDataCollection.ScanData data : scanData) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            String barcode = data.getData();
            Intent intent = new Intent();
            intent.putExtra("code",barcode);
            setResult(1,intent);
            finish();
          }
        });
      }
    }
  }
  @Override
  public void onStatus(StatusData statusData) {
    StatusData.ScannerStates state = statusData.getState();
    switch(state) {
      case IDLE:
        // set trigger type
        if(bSoftTriggerSelected) {
          scanner.triggerType = Scanner.TriggerType.SOFT_ONCE;
          bSoftTriggerSelected = false;
        } else {
          scanner.triggerType = Scanner.TriggerType.HARD;
        }
        // set decoders
        if(bDecoderSettingsChanged) {
          bDecoderSettingsChanged = false;
        }
        // submit read
        if(!scanner.isReadPending() && !bExtScannerDisconnected) {
          try {
            scanner.read();
          } catch (ScannerException e) {
          }
        }
        break;
      case WAITING:
        break;
      case SCANNING:
        break;
      case DISABLED:
        break;
      case ERROR:
        break;
      default:
        break;
    }
  }
  @Override
  public void onOpened(EMDKManager emdkManager) {
    this.emdkManager = emdkManager;
    // Acquire the barcode manager resources
    initBarcodeManager();
    // Enumerate scanner devices
    enumerateScannerDevices();

    deInitScanner();
    initScanner();
  }
  private void initScanner() {
    if (scanner == null) {
      if (barcodeManager != null)
        scanner = barcodeManager.getDevice(deviceList.get(0));
    }
    if (scanner != null) {
      scanner.addDataListener(this);
      scanner.addStatusListener(this);
      try {
        scanner.enable();
      } catch (ScannerException e) {
        deInitScanner();
      }
    }else{
    }
  }
  private void deInitScanner() {
    if (scanner != null) {
      try{
        scanner.disable();
      } catch (Exception e) {
      }

      try {
        scanner.removeDataListener(this);
        scanner.removeStatusListener(this);
      } catch (Exception e) {
      }

      try{
        scanner.release();
      } catch (Exception e) {
      }
      scanner = null;
    }
  }
  private void initBarcodeManager(){
    barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
    // Add connection listener
//    if (barcodeManager != null) {
//      barcodeManager.addConnectionListener(this);
//    }
  }
  private void enumerateScannerDevices() {
    if (barcodeManager != null) {
      deviceList = barcodeManager.getSupportedDevicesInfo();
    }
  }

}

