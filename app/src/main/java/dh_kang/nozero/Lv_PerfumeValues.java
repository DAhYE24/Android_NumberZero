package dh_kang.nozero;

/**
 * Created by dh93 on 2016-11-17.
 */
public class Lv_PerfumeValues {
    String searchName = null;
    String searchCapacity = null;
    String perfImg = null;
    String perfName = null;
    String perfNameEng = null;
    String perfBrand = null;
    String perfCnP = null;

    public Lv_PerfumeValues(String searchName, String searchCapacity, String perfImg, String perfName, String perfNameEng, String perfBrand, String perfCnP) {
        this.searchName = searchName;
        this.searchCapacity = searchCapacity;
        this.perfImg = perfImg;
        this.perfName = perfName;
        this.perfNameEng = perfNameEng;
        this.perfBrand = perfBrand;
        this.perfCnP = perfCnP;
    }

    public String getSearchName() {
        return searchName;
    }

    public String getSearchCapacity() {
        return searchCapacity;
    }

    public String getPerfImg() {
        return perfImg;
    }

    public String getPerfName() {
        return perfName;
    }

    public String getPerfNameEng() {
        return perfNameEng;
    }

    public String getPerfBrand() {
        return perfBrand;
    }

    public String getPerfCnP() {
        return perfCnP;
    }
}
