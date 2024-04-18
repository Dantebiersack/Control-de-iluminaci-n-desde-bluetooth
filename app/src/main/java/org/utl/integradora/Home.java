package org.utl.integradora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class  Home extends AppCompatActivity {

    Button btnConnect, btnIluminacion, btnTemperatura, btnApagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        btnApagar= findViewById(R.id.btnApagar);
        btnConnect= findViewById(R.id.btnConnect);
        btnIluminacion = findViewById(R.id.btnIluminacion);
        btnTemperatura = findViewById(R.id.btnTemperatura);

        btnApagar.setOnClickListener(view->{
            apagar();
        });

        btnConnect.setOnClickListener(view->{
            Intent abrirConnect = new Intent(Home.this, Dispositivos.class);
            startActivity(abrirConnect);
        });

        btnTemperatura.setOnClickListener(view->{
            Intent abrirTempe = new Intent(Home.this, Temperatura.class);
            startActivity(abrirTempe);
        });

        btnIluminacion.setOnClickListener(view->{
            Intent abrirFocos = new Intent(Home.this, iluminacion.class);
            startActivity(abrirFocos);
        });
    }

    public void apagar(){


    }
}