package com.androideve.cropimagewithfilters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androideve.cropimagewithfilters.model.ImageProcessingModel;
import com.androideve.cropimagewithfilters.utils.ImageFilters;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/**
 * Created by bytesbrick on 25/1/17.
 */

public class CropImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private RecyclerView reclist;
    Bitmap bmOriginal;
    ArrayList<ImageProcessingModel> effecList = new ArrayList<>();
    Uri imageUri;
    int position1;
    Bitmap bitmapScaled = null;
    Button saveImageBT;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropimage);

        effecList.clear();
        imageView = ((ImageView) findViewById(R.id.resultImageView));
        reclist = (RecyclerView) findViewById(R.id.reclist);
        saveImageBT = (Button) findViewById(R.id.saveImageBT);

        reclist.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        reclist.setLayoutManager(llm);


        Intent intent = getIntent();

        imageUri = intent.getParcelableExtra("imageUri");


        if (imageUri != null) {
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "No image is set to show", Toast.LENGTH_LONG).show();
        }

        try {
            String path = imageUri.getPath();
            bmOriginal = getThumbnailBitmap(path, 80);


            String[] filterList = getResources().getStringArray(R.array.filters);
            for (int i = 0; i < filterList.length; i++) {

                String processName = filterList[i];
                Bitmap bitmap = bmOriginal;
                ImageProcessingModel model = new ImageProcessingModel(i, processName, bitmap);
                effecList.add(model);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        reclist.setAdapter(new EffectsAdapter(CropImageActivity.this, bmOriginal, effecList, new EffectsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                position1 = position;

                new AsyncTaskRunner().execute();
            }
        }));

        saveImageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncTaskImageSaveRunner().execute();
            }
        });

    }


    private class AsyncTaskImageSaveRunner extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CropImageActivity.this,
                    "", "Please wait..");
        }

        @Override
        protected Void doInBackground(Void... voids) {


            saveBitmap(bitmapScaled,"androideve"+System.currentTimeMillis());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Toast.makeText(CropImageActivity.this,"Image saved successfully!",Toast.LENGTH_LONG).show();

        }
    }

    private void scanSdCard(){

        String folderPath = Environment.getExternalStorageDirectory() + "/Androideve";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ folderPath)));
        }
    }

    private void saveBitmap(Bitmap bmp,String fileName){
        try {

            String folderPath = Environment.getExternalStorageDirectory() + "/Androideve";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                File wallpaperDirectory = new File(folderPath);
                wallpaperDirectory.mkdirs();
            }

            file = new File(folderPath,fileName+".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,90,fos);
            scanSdCard();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }


    }


    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CropImageActivity.this,
                    "", "Take some time...");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            final ImageProcessingModel imagenProcesada = effecList.get(position1);
            bitmapScaled = null;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(CropImageActivity.this.getContentResolver(), imageUri);

                switch (imagenProcesada.getProcessName()) {

                    case "Normal":
                        bitmapScaled = bitmap;

                        break;
                    case "Black":
                        bitmapScaled = ImageFilters.applyBlackFilter(bitmap);

                        break;
                    case "MeanRemoval":
                        bitmapScaled = ImageFilters.applyMeanRemovalEffect(bitmap);

                        break;
                    case "Emboss":
                        bitmapScaled = ImageFilters.applyEmbossEffect(bitmap);

                        break;
                    case "Engrave":
                        bitmapScaled = ImageFilters.applyEngraveEffect(bitmap);
                        break;
                    case "Flea":
                        bitmapScaled = ImageFilters.applyFleaEffect(bitmap);
                        break;
                    case "Blur":
                        bitmapScaled = ImageFilters.applyGaussianBlurEffect(bitmap);
                        break;
                    case "RoundCorner":
                        bitmapScaled = ImageFilters.applyRoundCornerEffect(bitmap, 50);
                        break;
                    case "Saturation":
                        bitmapScaled = ImageFilters.applySaturationFilter(bitmap, 1);
                        break;
                    case "Boost":
                        bitmapScaled = ImageFilters.applyBoostEffect(bitmap, 1, 40);
                        break;
                    case "Smooth":
                        bitmapScaled = ImageFilters.applySmoothEffect(bitmap, 100);
                        break;
                    case "Tint":
                        bitmapScaled = ImageFilters.applyTintEffect(bitmap, 100);
                        break;
                    case "Brightness":
                        bitmapScaled = ImageFilters.applyBrightnessEffect(bitmap, 80);
                        break;
                    case "Hue":
                        bitmapScaled = ImageFilters.applyHueFilter(bitmap, 2);
                        break;
                    case "Highlight":
                        bitmapScaled = ImageFilters.applyHighlightEffect(bitmap);
                        break;
                    case "Invert":
                        bitmapScaled = ImageFilters.applyInvertEffect(bitmap);
                        break;
                    case "Grey":
                        bitmapScaled = ImageFilters.applyGreyscaleEffect(bitmap);
                        break;
                    case "Gamma":
                        bitmapScaled = ImageFilters.applyGammaEffect(bitmap, 1.8, 1.8, 1.8);
                        break;
                    case "ColorFilterRed":
                        bitmapScaled = ImageFilters.applyColorFilterEffect(bitmap, 255, 0, 0);
                        break;
                    case "ColorFilterBlue":
                        bitmapScaled = ImageFilters.applyColorFilterEffect(bitmap, 0, 255, 0);
                        break;
                    case "ColorFilterGreen":
                        bitmapScaled = ImageFilters.applyColorFilterEffect(bitmap, 0, 0, 255);
                        break;
                    case "ColorDepth":
                        bitmapScaled = ImageFilters.applyDecreaseColorDepthEffect(bitmap, 32);
                        break;
                    case "Contrast":
                        bitmapScaled = ImageFilters.applyContrastEffect(bitmap, 70);
                        break;
                    case "Sepia":
                        bitmapScaled = ImageFilters.applySepiaToningEffect(bitmap, 10, 1.5, 0.6, 0.12);
                        break;
                    case "Shading":
                        bitmapScaled = ImageFilters.applyShadingFilter(bitmap, Color.CYAN);
                        break;
                    case "ShadingYellow":
                        bitmapScaled = ImageFilters.applyShadingFilter(bitmap, Color.YELLOW);
                        break;
                    case "ShadingGreen":
                        bitmapScaled = ImageFilters.applyShadingFilter(bitmap, Color.GREEN);
                        break;
                    case "WaterMark":
                        bitmapScaled = ImageFilters.applyWaterMarkEffect(bitmap, "androideve.com", 200, 200, Color.GREEN, 80, 24, false);
                        break;
                    case "Snow":
                        bitmapScaled = ImageFilters.applySnowEffect(bitmap);
                        break;
                    case "Sharp":
                        bitmapScaled = ImageFilters.applySharpenEffect(bitmap,1);
                        break;

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            imageView.setImageBitmap(bitmapScaled);
        }
    }


    //Create Thumbnail from big images
    public Bitmap getThumbnailBitmap(String path, int thumbnailSize) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / thumbnailSize;
        return BitmapFactory.decodeFile(path, opts);
    }

}

