package org.utl.integradora;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class iluminacion extends AppCompatActivity {

    ImageButton btnFoco1;
    ImageButton btnFoco2;
    ImageButton btnFoco3;
    ImageButton btnFoco4;
    String comando="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iluminacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnFoco1 = findViewById(R.id.btnFoco1);
        btnFoco2 = findViewById(R.id.btnFoco2);
        btnFoco3 = findViewById(R.id.btnFoco3);
        btnFoco4 = findViewById(R.id.btnFoco4);

        btnFoco1.setOnClickListener(v->{
            comando = "i";
            Dispositivos.enviarDatosBluetooth(comando);
        });

        btnFoco2.setOnClickListener(v->{
            comando = "s";
            Dispositivos.enviarDatosBluetooth(comando);
        });

        btnFoco3.setOnClickListener(v->{
            comando = "p";
            Dispositivos.enviarDatosBluetooth(comando);
        });

        btnFoco4.setOnClickListener(v->{
            comando = "y";
            Dispositivos.enviarDatosBluetooth(comando);
        });
    }
}