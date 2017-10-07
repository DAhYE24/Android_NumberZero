package dh_kang.nozero.DataSet;

/**
 * Created by dh93 on 2016-11-17.
 */
public class PerfumeValues {
    private int perfumeIdx = 0;
    private String perfumeName = null;
    private String perfumeEnglishName = null;
    private String perfumeImageUrl = null;
    private String perfumeBrand = null;
    private String perfumePrice = null;
    private String perfumeCapacity = null;

    public PerfumeValues(int perfumeIdx, String perfumeName, String perfumeEnglishName, String perfumeImageUrl, String perfumeBrand, String perfumePrice, String perfumeCapacity) {
        this.perfumeIdx = perfumeIdx;
        this.perfumeName = perfumeName;
        this.perfumeEnglishName = perfumeEnglishName;
        this.perfumeImageUrl = perfumeImageUrl;
        this.perfumeBrand = perfumeBrand;
        this.perfumePrice = perfumePrice;
        this.perfumeCapacity = perfumeCapacity;
    }

    public int getPerfumeIdx() {
        return perfumeIdx;
    }

    public String getPerfumeName() {
        return perfumeName;
    }

    public String getPerfumeEnglishName() {
        return perfumeEnglishName;
    }

    public String getPerfumeImageUrl() {
        return perfumeImageUrl;
    }

    public String getPerfumeBrand() {
        return perfumeBrand;
    }

    public String getPerfumePrice() {
        return perfumePrice;
    }

    public String getPerfumeCapacity() {
        return perfumeCapacity;
    }
}
