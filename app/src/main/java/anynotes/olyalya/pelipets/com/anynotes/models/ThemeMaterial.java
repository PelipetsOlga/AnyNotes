package anynotes.olyalya.pelipets.com.anynotes.models;

import android.content.Context;

public class ThemeMaterial {
    private int primary;
    private int accent;
    private Context ctx;

    public  ThemeMaterial(Context ctx){
        this.ctx=ctx;
    }

    public ThemeMaterial(Context ctx, int c1, int c2){
        primary=c1;
        accent=c2;
        this.ctx=ctx;
    }

    public ThemeMaterial(Context ctx, ColorMaterial c1, ColorMaterial c2){
        primary=c1.getColorPrimary();
        accent=c2.getColorAccent();
        this.ctx=ctx;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public void setAccent(int accent) {
        this.accent = accent;
    }

    public void setPrimary(ColorMaterial primary) {
        this.primary = primary.getColorPrimary();
    }

    public void setAccent(ColorMaterial accent) {
        this.accent = accent.getColorAccent();
    }
}
