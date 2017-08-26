package dh_kang.nozero;

/**
 * Created by dh93 on 2016-10-16.
 */
public class Lv_FlaValues {
    String flavOne = null; // 향료명
    boolean selOne = false; // 칸이 선택되었는지 아닌지
    String flavTwo = null;
    boolean selTwo = false;
    String flavThree = null;
    boolean selThree = false;

    public Lv_FlaValues(String flavOne, boolean selOne, String flavTwo, boolean selTwo, String flavThree, boolean selThree) {
        this.flavOne = flavOne;
        this.selOne = selOne;
        this.flavTwo = flavTwo;
        this.selTwo = selTwo;
        this.flavThree = flavThree;
        this.selThree = selThree;
    }

    public boolean isSelThree() {
        return selThree;
    }

    public void setSelThree(boolean selThree) {
        this.selThree = selThree;
    }

    public String getFlavOne() {
        return flavOne;
    }

    public void setFlavOne(String flavOne) {
        this.flavOne = flavOne;
    }

    public boolean isSelOne() {
        return selOne;
    }

    public void setSelOne(boolean selOne) {
        this.selOne = selOne;
    }

    public String getFlavTwo() {
        return flavTwo;
    }

    public void setFlavTwo(String flavTwo) {
        this.flavTwo = flavTwo;
    }

    public boolean isSelTwo() {
        return selTwo;
    }

    public void setSelTwo(boolean selTwo) {
        this.selTwo = selTwo;
    }

    public String getFlavThree() {
        return flavThree;
    }

    public void setFlavThree(String flavThree) {
        this.flavThree = flavThree;
    }
}
