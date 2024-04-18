package org.utl.integradora;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonObject;

import org.utl.integradora.api.Config;
import org.utl.integradora.api.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class iniciarSesion extends AppCompatActivity {

    Button btnInicio;
    EditText txtCorreo, txtContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_sesion);

        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        btnInicio = findViewById(R.id.btnInicio);

        btnInicio.setOnClickListener(view->{
            validar();
        });

    }
    public void validar(){
        String user,password;
        user = String.valueOf(txtCorreo.getText());
        password = String.valueOf(txtContrasenia.getText());

        btnInicio.setOnClickListener(view -> {
            validar();
        });
        //Declaramos el Objeto retrofit

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService api = retrofit.create(LoginService.class);
        Call<JsonObject> validarLogin = api.validar(user,password);
        validarLogin.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Verificar si la respuesta fue exitosa
                if (response.isSuccessful()) {
                    JsonObject respuesta = response.body();
                    if (respuesta != null && respuesta.has("message")) {
                        String message = respuesta.get("message").getAsString();
                        if (message.equals("Usuario encontrado.")) {
                            // Si el usuario está autenticado correctamente
                            // Mostrar mensaje de éxito y token
                            String token = respuesta.get("token").getAsString();
                            Toast.makeText(iniciarSesion.this, "Correcto\nToken de acceso generado: " + token, Toast.LENGTH_SHORT).show();
                            Intent abrirVistaWelcome = new Intent(iniciarSesion.this, Home.class);
                            abrirVistaWelcome.putExtra("username", user);
                            startActivity(abrirVistaWelcome);
                        } else {
                            // Si el usuario no está autenticado correctamente
                            // Mostrar mensaje de error
                            Toast.makeText(iniciarSesion.this, "Incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Si la respuesta no fue exitosa
                    // Mostrar mensaje de error basado en el código de respuesta
                    Toast.makeText(iniciarSesion.this, "Error: " , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Manejar errores de conexión
                Toast.makeText(iniciarSesion.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Imprimir el stack trace para depurar
            }
        });
    }
}