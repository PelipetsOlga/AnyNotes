package anynotes.olyalya.pelipets.com.anynotes.models;


import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.R;

public class ColorPallete {
    private List<ColorMaterial> pallete;
    private Context ctx;
    private Resources res;
 /*   private ColorMaterial materialRed;
    private ColorMaterial materialPink;
    private ColorMaterial materialPurple;
    private ColorMaterial materialDeepPurple;
    private ColorMaterial materialIndigo;
    private ColorMaterial materialBlue;
    private ColorMaterial materialLightBlue;
    private ColorMaterial materialCyan;
    private ColorMaterial materialTeal;
    private ColorMaterial materialGreen;
    private ColorMaterial materialLightGreen;
    private ColorMaterial materialLime;
    private ColorMaterial materialYellow;
    private ColorMaterial materialAmber;
    private ColorMaterial materialOrange;
    private ColorMaterial materialDeepOrange;
    private ColorMaterial materialBrown;
    private ColorMaterial materialGrey;
    private ColorMaterial materialBlueGrey;*/


    public List<ColorMaterial> getPallete() {
        return pallete;
    }

    public Context getCtx() {
        return ctx;
    }

    public Resources getRes() {
        return res;
    }

 /*   public ColorMaterial getMaterialRed() {
        return materialRed;
    }

    public ColorMaterial getMaterialPink() {
        return materialPink;
    }

    public ColorMaterial getMaterialPurple() {
        return materialPurple;
    }

    public ColorMaterial getMaterialDeepPurple() {
        return materialDeepPurple;
    }

    public ColorMaterial getMaterialIndigo() {
        return materialIndigo;
    }

    public ColorMaterial getMaterialBlue() {
        return materialBlue;
    }

    public ColorMaterial getMaterialLightBlue() {
        return materialLightBlue;
    }

    public ColorMaterial getMaterialCyan() {
        return materialCyan;
    }

    public ColorMaterial getMaterialTeal() {
        return materialTeal;
    }

    public ColorMaterial getMaterialGreen() {
        return materialGreen;
    }

    public ColorMaterial getMaterialLightGreen() {
        return materialLightGreen;
    }

    public ColorMaterial getMaterialLime() {
        return materialLime;
    }

    public ColorMaterial getMaterialYellow() {
        return materialYellow;
    }

    public ColorMaterial getMaterialAmber() {
        return materialAmber;
    }

    public ColorMaterial getMaterialOrange() {
        return materialOrange;
    }

    public ColorMaterial getMaterialDeepOrange() {
        return materialDeepOrange;
    }

    public ColorMaterial getMaterialBrown() {
        return materialBrown;
    }

    public ColorMaterial getMaterialGrey() {
        return materialGrey;
    }

    public ColorMaterial getMaterialBlueGrey() {
        return materialBlueGrey;
    }*/

    public ColorPallete(Context ctx) {
        this.ctx = ctx;
        res = this.ctx.getResources();
        pallete = new ArrayList<>();

        pallete.add(new ColorMaterial("red"));
        pallete.add(new ColorMaterial("pink"));
        pallete.add(new ColorMaterial("purple"));
        pallete.add(new ColorMaterial("deep_purple"));
        pallete.add(new ColorMaterial("indigo"));
        pallete.add(new ColorMaterial("blue"));
        pallete.add(new ColorMaterial("light_blue"));
        pallete.add(new ColorMaterial("cyan"));
        pallete.add(new ColorMaterial("teal"));
        pallete.add(new ColorMaterial("green"));
        pallete.add(new ColorMaterial("light_green"));
        pallete.add(new ColorMaterial("lime"));
        pallete.add(new ColorMaterial("yellow"));
        pallete.add(new ColorMaterial("amber"));
        pallete.add(new ColorMaterial("orange"));
        pallete.add(new ColorMaterial("deep_orange"));
        pallete.add(new ColorMaterial("brown"));
        pallete.add(new ColorMaterial("grey"));
        pallete.add(new ColorMaterial("blue_grey"));

        /*pallete.add(materialRed = new ColorMaterial(res.getColor(R.color.red), res.getColor(R.color.red_dark), res.getColor(R.color.red_accent)));
        pallete.add(materialPink=new ColorMaterial(res.getColor(R.color.pink), res.getColor(R.color.pink_dark), res.getColor(R.color.pink_accent)));
        pallete.add(materialPurple=new ColorMaterial(res.getColor(R.color.purple), res.getColor(R.color.purple_dark), res.getColor(R.color.purple_accent)));
        pallete.add(materialDeepPurple=new ColorMaterial(res.getColor(R.color.deep_purple), res.getColor(R.color.deep_purple_dark), res.getColor(R.color.deep_purple_accent)));
        pallete.add(materialIndigo=new ColorMaterial(res.getColor(R.color.indigo), res.getColor(R.color.indigo_dark), res.getColor(R.color.indigo_accent)));
        pallete.add(materialBlue=new ColorMaterial(res.getColor(R.color.blue), res.getColor(R.color.blue_dark), res.getColor(R.color.blue_accent)));
        pallete.add(materialLightBlue=new ColorMaterial(res.getColor(R.color.light_blue), res.getColor(R.color.light_blue_dark), res.getColor(R.color.light_blue_accent)));
        pallete.add(materialCyan=new ColorMaterial(res.getColor(R.color.cyan), res.getColor(R.color.cyan_dark), res.getColor(R.color.cyan_accent)));
        pallete.add(materialTeal=new ColorMaterial(res.getColor(R.color.teal), res.getColor(R.color.teal_dark), res.getColor(R.color.teal_accent)));
        pallete.add(materialGreen=new ColorMaterial(res.getColor(R.color.green), res.getColor(R.color.green_dark), res.getColor(R.color.green_accent)));
        pallete.add(materialLightGreen=new ColorMaterial(res.getColor(R.color.light_green), res.getColor(R.color.light_green_dark), res.getColor(R.color.light_green_accent)));
        pallete.add(materialLime=new ColorMaterial(res.getColor(R.color.lime), res.getColor(R.color.lime_dark), res.getColor(R.color.lime_accent)));
        pallete.add(materialYellow=new ColorMaterial(res.getColor(R.color.yellow), res.getColor(R.color.yellow_dark), res.getColor(R.color.yellow_accent)));
        pallete.add(materialAmber=new ColorMaterial(res.getColor(R.color.amber), res.getColor(R.color.amber_dark), res.getColor(R.color.amber_accent)));
        pallete.add(materialOrange=new ColorMaterial(res.getColor(R.color.orange), res.getColor(R.color.orange_dark), res.getColor(R.color.orange_accent)));
        pallete.add(materialDeepOrange=new ColorMaterial(res.getColor(R.color.deep_orange), res.getColor(R.color.deep_orange_dark), res.getColor(R.color.deep_orange_accent)));
        pallete.add(materialBrown=new ColorMaterial(res.getColor(R.color.brown), res.getColor(R.color.brown_dark), res.getColor(R.color.brown_accent)));
        pallete.add(materialGrey=new ColorMaterial(res.getColor(R.color.grey), res.getColor(R.color.grey_dark), res.getColor(R.color.grey_accent)));
        pallete.add(materialBlueGrey=new ColorMaterial(res.getColor(R.color.blue_grey), res.getColor(R.color.blue_grey_dark), res.getColor(R.color.blue_grey_accent)));*/
    }


}
