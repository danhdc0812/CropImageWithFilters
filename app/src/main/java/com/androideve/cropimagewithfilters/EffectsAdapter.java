package com.androideve.cropimagewithfilters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androideve.cropimagewithfilters.model.ImageProcessingModel;
import com.androideve.cropimagewithfilters.utils.ImageFilters;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bytesbrick on 25/1/17.
 */

public class EffectsAdapter extends
        RecyclerView.Adapter<EffectsAdapter.ViewHolder>{

    private Context context;
    private Bitmap bitmap;
    private ArrayList<ImageProcessingModel> listaEfectos;
    ViewHolder holder;
    private OnItemClickListener listener;

    public EffectsAdapter(Context context,Bitmap bitmap, ArrayList<ImageProcessingModel> listaEfectos,OnItemClickListener listener) {

        this.listaEfectos = listaEfectos;
        this.context = context;
        this.bitmap = bitmap;
        this.listener = listener;
    }

    @Override
    public EffectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.effect_row_single_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return listaEfectos.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder viewholder, int position) {

        this.holder = viewholder;
        final ImageProcessingModel imagenProcesada = listaEfectos.get(position);
        holder.preimg.setTag(position);
        holder.bind(imagenProcesada, listener);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView preimg;
        protected TextView nameTV;

        public ViewHolder(View itemView) {
            super(itemView);

            preimg = (CircleImageView) itemView.findViewById(R.id.profile_image);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);

        }

        public void bind(final ImageProcessingModel imagenProcesada, final OnItemClickListener listener) {

            preimg.setImageBitmap(null);
            nameTV.setText(imagenProcesada.getProcessName());
            bitmap = imagenProcesada.getProcessedBitmap();

            Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 60, 60, true);

            switch (imagenProcesada.getProcessName()){

                case "Normal":
                    holder.preimg.setImageBitmap(bitmapScaled);
                    break;
                case "Black":
                    holder.preimg.setImageBitmap(ImageFilters.applyBlackFilter(bitmapScaled));
                    break;
                case "Snow":
                    holder.preimg.setImageBitmap(ImageFilters.applySnowEffect(bitmapScaled));
                    break;
                case "Sharp":
                    holder.preimg.setImageBitmap(ImageFilters.applySharpenEffect(bitmapScaled,1));
                    break;
                case "MeanRemoval":
                    holder.preimg.setImageBitmap(ImageFilters.applyMeanRemovalEffect(bitmapScaled));
                    break;
                case "Emboss":
                    holder.preimg.setImageBitmap(ImageFilters.applyEmbossEffect(bitmapScaled));
                    break;
                case "Engrave":
                    holder.preimg.setImageBitmap(ImageFilters.applyEngraveEffect(bitmapScaled));
                    break;
                case "Flea":
                    holder.preimg.setImageBitmap(ImageFilters.applyFleaEffect(bitmapScaled));
                    break;
                case "Blur":
                    holder.preimg.setImageBitmap(ImageFilters.applyGaussianBlurEffect(bitmapScaled));
                    break;
                case "RoundCorner":
                    holder.preimg.setImageBitmap(ImageFilters.applyRoundCornerEffect(bitmapScaled,50));
                    break;
                case "Saturation":
                    holder.preimg.setImageBitmap(ImageFilters.applySaturationFilter(bitmapScaled,1));
                    break;
                case "Boost":
                    holder.preimg.setImageBitmap(ImageFilters.applyBoostEffect(bitmapScaled,1,40));
                    break;
                case "Smooth":
                    holder.preimg.setImageBitmap(ImageFilters.applySmoothEffect(bitmapScaled,100));
                    break;
                case "Tint":
                    holder.preimg.setImageBitmap(ImageFilters.applyTintEffect(bitmapScaled,100));
                    break;
                case "Brightness":
                    holder.preimg.setImageBitmap(ImageFilters.applyBrightnessEffect(bitmapScaled,80));
                    break;
                case "Hue":
                    holder.preimg.setImageBitmap(ImageFilters.applyHueFilter(bitmapScaled,2));
                    break;
                case "Highlight":
                    holder.preimg.setImageBitmap(ImageFilters.applyHighlightEffect(bitmapScaled));
                    break;
                case "Invert":
                    holder.preimg.setImageBitmap(ImageFilters.applyInvertEffect(bitmapScaled));
                    break;
                case "Grey":
                    holder.preimg.setImageBitmap(ImageFilters.applyGreyscaleEffect(bitmapScaled));
                    break;
                case "Gamma":
                    holder.preimg.setImageBitmap(ImageFilters.applyGammaEffect(bitmapScaled,1.8,1.8,1.8));
                    break;
                case "ColorFilterRed":
                    holder.preimg.setImageBitmap(ImageFilters.applyColorFilterEffect(bitmapScaled,255,0,0));
                    break;
                case "ColorFilterBlue":
                    holder.preimg.setImageBitmap(ImageFilters.applyColorFilterEffect(bitmapScaled,0,255,0));
                    break;
                case "ColorFilterGreen":
                    holder.preimg.setImageBitmap(ImageFilters.applyColorFilterEffect(bitmapScaled,0,0,255));
                    break;
                case "ColorDepth":
                    holder.preimg.setImageBitmap(ImageFilters.applyDecreaseColorDepthEffect(bitmapScaled,32));
                    break;
                case "Contrast":
                    holder.preimg.setImageBitmap(ImageFilters.applyContrastEffect(bitmapScaled,70));
                    break;
                case "Sepia":
                    holder.preimg.setImageBitmap(ImageFilters.applySepiaToningEffect(bitmapScaled, 10, 1.5, 0.6, 0.12));
                    break;
                case "Shading":
                    holder.preimg.setImageBitmap(ImageFilters.applyShadingFilter(bitmapScaled, Color.CYAN));
                    break;
                case "ShadingYellow":
                    holder.preimg.setImageBitmap(ImageFilters.applyShadingFilter(bitmapScaled, Color.YELLOW));
                    break;
                case "ShadingGreen":
                    holder.preimg.setImageBitmap(ImageFilters.applyShadingFilter(bitmapScaled, Color.GREEN));
                    break;
                case "WaterMark":
                    holder.preimg.setImageBitmap(ImageFilters.applyWaterMarkEffect(bitmapScaled, "androideve.com", 200, 200, Color.GREEN, 80, 24, false));
                    break;
            }

            preimg.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    listener.onItemClick(pos);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
