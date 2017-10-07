package dh_kang.nozero.DataSet;

/**
 * Created by dh93 on 2016-11-21.
 */
public class BoardValues {
    private String writerIcon = null;
    private String basicId = null;
    private String basicTitle = null;
    private String basicDate = null;
    private String basicNumber =null;
    private String basicContent = null;

    public BoardValues(String writerIcon, String basicId, String basicTitle, String basicDate, String basicNumber, String basicContent) {
        this.writerIcon = writerIcon;
        this.basicId = basicId;
        this.basicTitle = basicTitle;
        this.basicDate = basicDate;
        this.basicNumber = basicNumber;
        this.basicContent = basicContent;
    }

    public String getWriterIcon() {
        return writerIcon;
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
