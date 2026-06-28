package mx.edu.utez.libriflow.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CSV {
    private static final String FILE_PATH = "Usuario_BD.csv";

    public static void addToCSV(String nuevosDatos) {
        File archivo = new File(FILE_PATH);
        try {
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo CSV creado por primera vez en: " + archivo.getAbsolutePath());
            }

            String linea = String.join(",", nuevosDatos) + "\n";

            try (FileWriter writer = new FileWriter(archivo, StandardCharsets.UTF_8, true)) {
                writer.write(linea);
                writer.flush();
                System.out.println("Registro agregado correctamente.");
            }
        } catch (IOException e) {
            System.err.println("Error crítico al manejar el archivo CSV: " + e.getMessage());
        }
    }
}