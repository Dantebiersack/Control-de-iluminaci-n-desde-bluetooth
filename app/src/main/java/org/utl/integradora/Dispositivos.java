package org.utl.integradora;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Dispositivos extends AppCompatActivity {

    Spinner SpinnerDispositivos;

    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothSocket bluetoothSocket;
    public static OutputStream outputStream;

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dispositivos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SpinnerDispositivos = findViewById(R.id.SpinnerDispositivos);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Solicitar permiso de Bluetooth
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_PRIVILEGED,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH}, 1001);
            }
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no está disponible en este dispositivo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setupBluetoothSpinner();


    }


    public static void enviarDatosBluetooth(String data) {
        if (outputStream != null) {
            try {
                outputStream.write(data.getBytes());
                outputStream.flush();
                //Toast.makeText(this, "Datos enviados: " + data, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(this, "Error al enviar datos por Bluetooth", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(this, "No hay conexión Bluetooth establecida", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, ahora puedes intentar nuevamente conectar al dispositivo Bluetooth
                // Aquí puedes llamar nuevamente a conectarDispositivoBluetooth() con el nombre del dispositivo
                Toast.makeText(this, "Conexion exitosa", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado, manejar esta situación (por ejemplo, mostrando un mensaje al usuario)
                Toast.makeText(this, "Permiso de Bluetooth Connect denegado", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, String.valueOf(grantResults.length), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, String.valueOf(grantResults[0]), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void setupBluetoothSpinner() {
        // Obtener los nombres de los dispositivos vinculados como una lista
        List<String> nombresDispositivos = new ArrayList<>(obtenerNombresDispositivosVinculados());
        Toast.makeText(this, String.valueOf(nombresDispositivos.size()), Toast.LENGTH_SHORT).show();
        // Agregar un elemento predeterminado al principio de la lista
        nombresDispositivos.add(0, "Selecciona un dispositivo");

        // Crear el adaptador con la lista de nombres de dispositivos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, nombresDispositivos);

        // Configurar el adaptador para el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerDispositivos.setAdapter(adapter);

        SpinnerDispositivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el nombre del dispositivo seleccionado
                String deviceName = parent.getItemAtPosition(position).toString();
                // Verificar si se seleccionó un dispositivo válido
                if (!deviceName.equals("Selecciona un dispositivo")) {
                    conectarDispositivoBluetooth(deviceName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona ningún dispositivo
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private Set<String> obtenerNombresDispositivosVinculados() {
        // Verificar si tienes el permiso ACCESS_FINE_LOCATION o ACCESS_COARSE_LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no tienes el permiso, solicítalo al usuario
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_BLUETOOTH_PERMISSION);
            // Devuelve un conjunto vacío mientras se espera la respuesta del usuario
            return new HashSet<>();
        }

        // Si tienes el permiso, procede a obtener los dispositivos vinculados
        Set<BluetoothDevice> dispositivosVinculados = bluetoothAdapter.getBondedDevices();
        Set<String> nombresDispositivos = new HashSet<>();
        for (BluetoothDevice dispositivo : dispositivosVinculados) {
            nombresDispositivos.add(dispositivo.getName());
        }
        return nombresDispositivos;
    }

    private void desconectarDispositivoBluetooth() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void conectarDispositivoBluetooth(String deviceName) {
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth está desactivado, solicitar al usuario que lo active
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            return;
        }

        // Obtener la dirección MAC del dispositivo seleccionado
        BluetoothDevice selectedDevice = null;
        @SuppressLint("MissingPermission") Set<BluetoothDevice> dispositivosVinculados = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice dispositivo : dispositivosVinculados) {
            if (dispositivo.getName().equals(deviceName)) {
                selectedDevice = dispositivo;
                break;
            }
        }

        if (selectedDevice == null) {
            // No se pudo encontrar el dispositivo seleccionado
            Toast.makeText(this, "No se pudo encontrar el dispositivo seleccionado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el UUID para SPP (Serial Port Profile)
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        try {
            Log.i("info","preparado para conectar");
            // Crear un socket Bluetooth para la conexión
            bluetoothSocket = selectedDevice.createRfcommSocketToServiceRecord(uuid);
            // Establecer la conexión
            bluetoothSocket.connect();
            // Obtener el OutputStream para enviar datos
            outputStream = bluetoothSocket.getOutputStream();

            // Mostrar el nombre, MAC y distancia en el txtEstado
            String deviceInfo = "Nombre: " + selectedDevice.getName() + "\n"
                    + "MAC: " + selectedDevice.getAddress();

            Toast.makeText(this, "Conexión establecida con éxito", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al conectar con el dispositivo Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
}