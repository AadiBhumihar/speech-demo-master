package edu.cmu.pocketsphinx.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class Speech extends Activity implements RecognitionListener {

    private static final String DIGITS_SEARCH = "direc";
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 20;
    private SpeechRecognizer recognizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.speech);

        ((TextView) findViewById(R.id.caption_text)).setText("Preparing the recognizer");

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        try
        {
            Assets assets = new Assets(Speech.this);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        }
        catch (IOException e)
        {
            ((TextView) findViewById(R.id.caption_text))
                    .setText("Failed to init recognizer " + e);
        }

        ((TextView) findViewById(R.id.caption_text)).setText("Say Yes,no,accept,reject,up, down, left, right, forwards, backwards");

        reset();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                finish();
            }
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis)
    {
    }

    @Override
    public void onResult(Hypothesis hypothesis)
    {
        ((TextView) findViewById(R.id.result_text)).setText("");

        if (hypothesis != null)
        {
            String text = hypothesis.getHypstr();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            if(TextUtils.equals(text,"yes") || TextUtils.equals(text,"accept")){
                Intent intent = new Intent(Speech.this ,Test_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("text",text);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBeginningOfSpeech()
    {
    }

    @Override
    public void onEndOfSpeech()
    {
        reset();
    }

    private void setupRecognizer(File assetsDir)
    {
        File modelsDir = new File(assetsDir, "models");

        try {
            recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                    .setRawLogDir(assetsDir)
                     .setKeywordThreshold(1e-20f) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                    .getRecognizer();
//            recognizer = SpeechRecognizerSetup.defaultSetup().setAcousticModel(new File(assetsDir, "en-us-ptm")).
//            defaultSetup().setAcousticModel(new File(modelsDir, "hmm/en-us-ptm"))
//                    .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
//                    .setRawLogDir(assetsDir).setKeywordThreshold(1e-20f)
//                    .getRecognizer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recognizer.addListener(this);

        File digitsGrammar = new File(assetsDir, "direc.gram");
        recognizer.addKeywordSearch(DIGITS_SEARCH, digitsGrammar);
    }

    private void reset()
    {
        recognizer.stop();
        recognizer.startListening(DIGITS_SEARCH);
    }

    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        //switchSearch(KWS_SEARCH);
    }
}
