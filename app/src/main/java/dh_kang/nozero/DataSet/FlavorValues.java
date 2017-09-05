package dh_kang.nozero.DataSet;

/**
 * Created by dh93 on 2016-10-16.
 */
public class FlavorValues {
    private String flavor1 = null;
    private boolean selected1 = false;
    private String flavor2 = null;
    private boolean selected2 = false;
    private String flavor3 = null;
    private boolean selected3 = false;

    public FlavorValues(String flavor1, boolean selected1, String flavor2, boolean selected2, String flavor3, boolean selected3) {
        this.flavor1 = flavor1;
        this.selected1 = selected1;
        this.flavor2 = flavor2;
        this.selected2 = selected2;
        this.flavor3 = flavor3;
        this.selected3 = selected3;
    }

    public String getFlavor1() {
        return flavor1;
    }

    public String getFlavor2() {
        return flavor2;
    }

    public String getFlavor3() {
        return flavor3;
    }

    public boolean isSelected1() {
        return selected1;
    }

    public boolean isSelected2() {
        return selected2;
    }

    public boolean isSelected3() {
        return selected3;
    }
}
