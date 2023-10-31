package com.example.fitnessapp.db.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

    public static void buscarIlustracao(Context context, String imgRefer, ImageView imageView) {
        if(imgRefer.endsWith(".png")) {
            FileInputStream fileInputStream;
            try {
                fileInputStream = context.openFileInput(imgRefer);
                imageView.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
                return ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(imgRefer.endsWith(".gif")) {
            try {
                // Use o Glide para carregar o GIF animado e exibi-lo na ImageView.
                Glide.with(context)
                        .asGif() // Indique que estamos carregando um GIF animado.
                        .load(context.openFileInput(imgRefer))
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
