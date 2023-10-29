package com.example.fitnessapp.db.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExecicioIlustracaoDao {


    public static boolean salvarIlustracao(Context context, Bitmap img, String fileName) {

        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            img.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Bitmap buscarIlustracao(Context context, String imgRefer) {

        // Carregue a imagem do diretório privado da aplicação.
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(imgRefer);
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean removerIlustracao(Context context, String fileName) {
        try {
            return context.deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
