package anynotes.olyalya.pelipets.com.anynotes.models;

public class ColorMaterial {
    private String tag;
    //  private int colorPrimary;
    //private int colorDark;
    //private int colorAccent;

    public ColorMaterial(String tag
                         //  , int a, int b, int c
    ) {
        this.tag = tag;
        //  colorPrimary=a;
        // colorDark=b;
        //colorAccent=c;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
