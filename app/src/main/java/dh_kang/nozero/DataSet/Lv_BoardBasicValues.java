package dh_kang.nozero.DataSet;

/**
 * Created by dh93 on 2016-11-21.
 */
public class Lv_BoardBasicValues {
    String basicIcon = null;
    String basicId = null;
    String basicTitle = null;
    String basicDate = null;
    String basicNumber =null;
    String basicContent = null;

    public Lv_BoardBasicValues(String basicIcon, String basicId, String basicTitle, String basicDate, String basicNumber, String basicContent) {
        this.basicIcon = basicIcon;
        this.basicId = basicId;
        this.basicTitle = basicTitle;
        this.basicDate = basicDate;
        this.basicNumber = basicNumber;
        this.basicContent = basicContent;
    }

    public String getBasicIcon() {
        return basicIcon;
    }

    public String getBasicId() {
        return basicId;
    }

    public String getBasicTitle() {
        return basicTitle;
    }

    public String getBasicDate() {
        return basicDate;
    }

    public String getBasicNumber() {
        return basicNumber;
    }

    public String getBasicContent() {
        return basicContent;
    }
}
