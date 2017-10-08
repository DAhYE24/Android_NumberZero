package dh_kang.nozero.DataSet;

/**
 * Created by dh93 on 2016-11-21.
 */
public class BoardValues {
    private int boardIdx = 0;
    private String boardTitle = null;
    private String userNickname = null;
    private String profileThumbnailUrl = null;
    private String updateAt = null;

//    final long rvId = 0;
    private boolean pinned;

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public BoardValues(int boardIdx, String boardTitle, String userNickname, String profileThumbnailUrl, String updateAt) {
        this.boardIdx = boardIdx;
        this.boardTitle = boardTitle;
        this.userNickname = userNickname;
        this.profileThumbnailUrl = profileThumbnailUrl;
        this.updateAt = updateAt;
    }

    public int getBoardIdx() {
        return boardIdx;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getProfileThumbnailUrl() {
        return profileThumbnailUrl;
    }

    public String getUpdateAt() {
        return updateAt;
    }
}
