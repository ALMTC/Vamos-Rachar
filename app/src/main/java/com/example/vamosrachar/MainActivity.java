package com.example.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText texValor,texPessoas;
    TextView res;
    FloatingActionButton share, tocar;
    TextToSpeech ttsPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        texValor = findViewById(R.id.valor);
        texValor.addTextChangedListener(this);

        texPessoas = findViewById(R.id.pessoas);
        texPessoas.addTextChangedListener(this);

        res = findViewById(R.id.resultado);

        share = findViewById(R.id.compartilhar);
        share.setOnClickListener(this);

        tocar = findViewById(R.id.falar);
        tocar.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlay = new TextToSpeech(this,this);
            }else{
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
        try {
            double resultado = Double.parseDouble(texValor.getText().toString());
            float p = Float.parseFloat(texPessoas.getText().toString());
            resultado = resultado/p;
            DecimalFormat df = new DecimalFormat("#.00");
            res.setText("R$"+df.format(resultado));
        }catch (Exception e){
            res.setText("R$ 0.00");
        }
    }

    @Override
    public void onClick(View v) {
        if(v == share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"O valor da conta por pessoa foi de "+ res.getText().toString());
            startActivity(intent);
        }
        if(v == tocar){
            if(ttsPlay != null){
                ttsPlay.speak("O valor da conta por pessoa foi de "+ res.getText().toString(), TextToSpeech.QUEUE_FLUSH,null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            Toast.makeText(this, "TTS ativado...",Toast.LENGTH_LONG).show();
        }
        else if (status == TextToSpeech.ERROR){
            Toast.makeText(this, "Sem TTS habilitado...",Toast.LENGTH_LONG).show();
        }
    }
}