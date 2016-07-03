package anynotes.olyalya.pelipets.com.anynotes.models;

import android.content.Context;

public class ThemeMaterial {
    private ColorMaterial primary;
    private ColorMaterial accent;
    private Context ctx;

    public  ThemeMaterial(Context ctx){
        this.ctx=ctx;
    }

    public ThemeMaterial(Context ctx, ColorMaterial c1, ColorMaterial c2){
        primary=c1;
        accent=c2;
        this.ctx=ctx;
    }

    public ThemeMaterial(Context ctx, String c1, String c2){
        primary=new ColorMaterial(c1);
        accent=new ColorMaterial(c2);
        this.ctx=ctx;
    }

    public ColorMaterial getPrimary() {
        return primary;
    }

    public void setPrimary(ColorMaterial primary) {
        this.primary = primary;
    }

    public ColorMaterial getAccent() {
        return accent;
    }

    public void setAccent(ColorMaterial accent) {
        this.accent = accent;
    }
}
