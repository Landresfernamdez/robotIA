package com.example.voiceapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Button;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech myTTs;
    private SpeechRecognizer mySpeechRecognizer;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    BluetoothSPP bluetooth;
    final String ADELANTE = "1";
    final String ATRAS = "3";
    final String IZQ = "2";
    final String DER = "4";
    final String DEF="5";
    final  String CATCH="6";
    Button connect;
    Button adelante;
    Button atras;
    Button izq;
    Button der;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        connect = (Button) findViewById(R.id.connect);
        adelante = (Button) findViewById(R.id.adelante);
        atras = (Button) findViewById(R.id.atras);
        izq = (Button) findViewById(R.id.izq);
        der = (Button) findViewById(R.id.der);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        bluetooth = new BluetoothSPP(this);
        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                connect.setText("Connected to " + name);
            }

            public void onDeviceDisconnected() {
                connect.setText("Connection lost");
            }

            public void onDeviceConnectionFailed() {
                connect.setText("Unable to connect");
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
        adelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(ADELANTE, true);
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(ATRAS, true);
            }
        });
        izq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(IZQ, true);
            }
        });
        der.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(DER, true);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( MainActivity.this  ,new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                }*/
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                mySpeechRecognizer.startListening(intent);
            }
        });
        initializeTextToSpeach();
        initializeSpeechRecognizer();
    }
    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }
    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void processResult(String command){
        command = command.toLowerCase();
        if(command.indexOf("move")!= -1){
            if(command.indexOf("forward")!=-1){
                speak("Moving forward");
                bluetooth.send(ADELANTE, true);
            }
            if(command.indexOf("backward")!= -1){
                speak("Moving backwards");
                bluetooth.send(ATRAS, true);
            }
            if(command.indexOf("left")!= -1){
                speak("Moving to the left");
                bluetooth.send(IZQ, true);
            }
            if(command.indexOf("right")!= -1){
                speak("Moving to the right");
                bluetooth.send(DER, true);
            }
        }
        if(command.indexOf("defend")!= -1){
            speak("Going to defend");
            //bluetooth.send(DEF, true);

        }
        if(command.indexOf("fetch")!= -1){
            speak("Fetching the ball");
            //bluetooth.send(CATCH, true);
        }
    }
    private void initializeSpeechRecognizer(){
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }
                @Override
                public void onBeginningOfSpeech() {

                }
                @Override
                public void onRmsChanged(float rmsdB) {

                }
                @Override
                public void onBufferReceived(byte[] buffer) {

                }
                @Override
                public void onEndOfSpeech() {

                }
                @Override
                public void onError(int error) {

                }
                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                }
                @Override
                public void onPartialResults(Bundle partialResults) {

                }
                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }
    private void initializeTextToSpeach(){
        myTTs = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTs.getEngines().size()==0){
                    Toast.makeText(MainActivity.this, "There is not TTs engine on your device", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    myTTs.setLanguage(Locale.US);
                    speak("Hello! I am ready, wo-hoo");
                }
            }
        });
    }
    private void speak(String message){
        if(Build.VERSION.SDK_INT >= 21)
        {
            myTTs.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            myTTs.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        myTTs.shutdown();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}