package com.example.elotro.myhouse;

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
    TextView  textStatus, textTemperature, textLed;
    EditText  editTextAddress, editTextPort;
    Button    buttonConnect, buttonClear, buttonLed;

    // Dynatac objects
    //
    IDynatacProtocolMaster master_ = null;
    EventsReceiver eventsReceiver_ = null;
    BusListener    busListener = null;

    int ledStatus = 0;
    boolean busIsUsable_ = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Create dynatac objects
        //
        eventsReceiver_ = new EventsReceiver();
        busListener     = new BusListener();

        // Retrieve all objects
        //
        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort    = (EditText)findViewById(R.id.port);
        buttonConnect   = (Button)findViewById(R.id.connect);
        buttonClear     = (Button)findViewById(R.id.clear);
        buttonLed       = (Button)findViewById(R.id.btnLed);
        textStatus      = (TextView)findViewById(R.id.status);
        textTemperature = (TextView)findViewById(R.id.temperature);
        textLed    = (TextView)findViewById(R.id.ledStatus);


        // connect button events
        //
        buttonConnect.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                IDynatacBus dBus = new DynatacBusClientSocket(editTextAddress.getText().toString(),  Integer.parseInt(editTextPort.getText().toString()));

                dBus.installListener(busListener);

                master_ = new DynatacProtocol(dBus);

                master_.setOnEvent(eventsReceiver_);
        }});

        // clear button events
        //
        buttonClear.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textStatus.setText("DISCONNECTED.");
                textTemperature.setText("");
                master_ = null;
        }});

        // led button events
        //
        buttonLed.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if (master_ != null) {
                    ledStatus = ledStatus==0?1:0;
                    master_.sendCommand(0, ledStatus);
                }
            }});
    }

    class EventsReceiver implements IDynatacProtocolMaster.IDynatacProtocolMasterSuscriptor {

        final static int DYNATAC_EVENT_LED_STATUS  = 0;
        final static int DYNATAC_EVENT_TEMPERATURE = 1;

        @Override
        public void remoteEvent(int i, int i1) {
            //textStatus.setText(i1);

            if (i == DYNATAC_EVENT_LED_STATUS) {
                if (i1 == 0) {
                    textLed.setText("Led status: OFF");
                } else {
                    textLed.setText("Led status: ON");
                }
            } else {
                if (i == DYNATAC_EVENT_TEMPERATURE) {
                    textTemperature.setText("Received temperature: " + i1);
                }
            }
        }
    }

    class BusListener implements IDynatacBus.IDynatacBusListener
    {
        @Override
        public void dataAvailable(String s, IDynatacBus iDynatacBus) {
            // Nothing to do here
            //
        }

        @Override
        public void onStatusChange(int i) {
            busIsUsable_ = (i & IDynatacBus.DYNATAC_BUS_STATUS_UNAVAILABLE)==0;

            if (busIsUsable_)
            {
                textStatus.setText("CONNECTED to "+editTextAddress.getText().toString() + "...");
            }
            else
            {
                textStatus.setText("ERROR connecting to "+editTextAddress.getText().toString() + "...");
            }
        }
    }
}
