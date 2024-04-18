package org.utl.integradora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class Temperatura extends AppCompatActivity {

    Button btnPrender;
    TextView txtTemperatura;
    String comando;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_temperatura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPrender = findViewById(R.id.btnPrender);
        txtTemperatura = findViewById(R.id.txtTemperatura);
        btnPrender.setOnClickListener(v->{
            comando = "h";
            Dispositivos.enviarDatosBluetooth(comando);
        });

        // Iniciar hilo para recibir datos Bluetooth

        new Thread(this::recibirDatosBluetooth).start();

    }



    @SuppressLint("SetTextI18n")
    public void recibirDatosBluetooth() {
        try {
            while (true) {
                if (Dispositivos.bluetoothSocket != null && Dispositivos.bluetoothSocket.isConnected()) {
                    StringBuilder receivedDataBuilder = new StringBuilder();

                    while (true) {
                        byte[] buffer = new byte[1024];
                        int bytes = Dispositivos.bluetoothSocket.getInputStream().read(buffer);
                        if (bytes > 0) {
                            String partialData = new String(buffer, 0, bytes);
                            receivedDataBuilder.append(partialData);

                            // Check for a newline character (or other suitable delimiter)
                            int delimiterIndex = receivedDataBuilder.indexOf("\n");
                            if (delimiterIndex >= 0) {
                                String completeData = receivedDataBuilder.substring(0, delimiterIndex);
                                runOnUiThread(() -> txtTemperatura.setText(completeData));
                                System.out.println(completeData);
                                receivedDataBuilder.delete(0, delimiterIndex + 1); // Clear for next string
                                break;  // Exit inner loop to wait for a new string
                            }
                        } else {
                            break;  // No more data available, wait for more
                        }
                    }
                } else {
                    Thread.sleep(500); // Wait 1 second before trying again
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            runOnUiThread(() -> txtTemperatura.setText("Error"));
        }
    }
}