package dh_kang.nozero.DataSet;

/**
 * Created by dh93 on 2016-11-17.
 */
public class PerfumeValues {
    private String perfumeName = null;
    private String perfumeCapacity = null;
    private String perfumeImage = null;
    private String perfumeEngName = null;
    private String perfumeBrand = null;
    private String perfumePrice = null;

    public PerfumeValues(String perfumeName, String perfumeCapacity, String perfumeImage, String perfumeEngName, String perfumeBrand, String perfumePrice) {
        this.perfumeName = perfumeName;
        this.perfumeCapacity = perfumeCapacity;
        this.perfumeImage = perfumeImage;
        this.perfumeEngName = perfumeEngName;
        this.perfumeBrand = perfumeBrand;
        this.perfumePrice = perfumePrice;
    }

    public String getPerfumeName() {
        return perfumeName;
    }

    public String getPerfumeCapacity() {
        return perfumeCapacity;
    }

    public String getPerfumeImage() {
        return perfumeImage;
    }

    public String getPerfumeEngName() {
        return perfumeEngName;
    }

    public String getPerfumeBrand() {
        return perfumeBrand;
    }

    public String getPerfumePrice() {
        return perfumePrice;
    }
}
