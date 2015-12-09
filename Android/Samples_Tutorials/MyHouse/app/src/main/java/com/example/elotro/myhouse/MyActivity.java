package com.example.elotro.myhouse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import Dynatac.Bus.DynatacBusClientSocket;
import Dynatac.Bus.IDynatacBus;
import Dynatac.Protocol.IDynatacProtocolMaster;
import Dynatac.Protocol.IDynatacProtocolMaster.IDynatacProtocolMasterSuscriptor;
import Dynatac.Protocol.DynatacProtocol;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MyActivity extends AppCompatActivity {
    // Declare object pointers
    //
    TextView textStatus, textTemperature, textLed;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, buttonOn, buttonOff;

    // Dynatac objects
    //
    IDynatacProtocolMaster master_ = null;
    EventsReceiver eventsReceiver_ = null;
    BusListener busListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Create dynatac objects
        //
        eventsReceiver_ = new EventsReceiver();
        busListener = new BusListener();

        // Retrieve all objects
        //
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonOn = (Button) findViewById(R.id.btnHeaterOn);
        buttonOff = (Button) findViewById(R.id.btnHeaterOff);
        textStatus = (TextView) findViewById(R.id.status);
        textTemperature = (TextView) findViewById(R.id.temperature);
        textLed = (TextView) findViewById(R.id.ledStatus);

        // Initialize objects. Same as disconnect.
        //
        textStatus.setText("DISCONNECTED.");
        textLed.setText("Heater status: UNKNOWN");
        textTemperature.setText("Received temperature: UNKNOWN");

        // connect button events
        //
        buttonConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textStatus.setText("CONNECTING to ..."+editTextAddress.getText().toString());
                String addr = editTextAddress.getText().toString();
                int port =  Integer.parseInt(editTextPort.getText().toString());

                DynatacBusClientSocket dBus;

                if (addr == null || addr == "" || port == 0) {
                    dBus = new DynatacBusClientSocket("192.168.1.7", 9090);
                }
                else
                {
                    dBus = new DynatacBusClientSocket(addr, port);
                }
                dBus.installListener(busListener);
                master_ = new DynatacProtocol(dBus);
                master_.setOnEvent(eventsReceiver_);
            }
        });

        // clear button events
        //
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textStatus.setText("DISCONNECTED.");
                textLed.setText("Heater status: UNKNOWN");
                textTemperature.setText("Received temperature: UNKNOWN");
                master_ = null;
            }
        });

        // led button events
        //
        buttonOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (master_ != null) {
                    master_.sendCommand(0, 1);
                }
            }
        });

        buttonOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (master_ != null) {
                    master_.sendCommand(0, 0);
                }
            }
        });
    }

    class EventsReceiver implements IDynatacProtocolMaster.IDynatacProtocolMasterSuscriptor {

        final static int DYNATAC_EVENT_LED_STATUS = 0;
        final static int DYNATAC_EVENT_TEMPERATURE = 1;

        @Override
        public void remoteEvent(int i, int i1) {

            //textStatus.setText(i1);

            if (i == DYNATAC_EVENT_LED_STATUS) {
                if (i1 == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textLed.setText("Heater status: OFF");
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textLed.setText("Heater status: ON");
                        }
                    });
                }
            } else if (i == DYNATAC_EVENT_TEMPERATURE) {

                final int temperatura = i1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textTemperature.setText("Received temperature: " + temperatura);
                    }
                });
            }

        }
    }

    class BusListener implements IDynatacBus.IDynatacBusListener {
        @Override
        public void dataAvailable(String s, IDynatacBus iDynatacBus) {
            // Nothing to do here
            //
        }

        @Override
        public void onStatusChange(int i) {
            boolean busIsUsable_ = false;

            busIsUsable_ = (i & IDynatacBus.DYNATAC_BUS_STATUS_UNAVAILABLE)==0;

            if (busIsUsable_)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText("CONNECTED to " + editTextAddress.getText().toString() + ".");
                    }
                });

            }
            else
            {
                final int errornum = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText("ERROR ("+errornum+") connecting to " + editTextAddress.getText().toString() + "...");
                    }
                });

            }
        }
    }
}