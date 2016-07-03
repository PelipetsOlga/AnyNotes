package anynotes.olyalya.pelipets.com.anynotes.models;

public class ColorMaterial {
    private int colorPrimary;
    private int colorDark;
    private int colorAccent;

    public ColorMaterial(int a, int b, int c){
        colorPrimary=a;
        colorDark=b;
        colorAccent=c;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public int getColorDark() {
        return colorDark;
    }

    public void setColorDark(int colorDark) {
        this.colorDark = colorDark;
    }

    public int getColorAccent() {
        return colorAccent;
    }

    public void setColorAccent(int colorAccent) {
        this.colorAccent = colorAccent;
    }
}
