package radhakrishnan.familytracker.traking.model;

/**
 * Created by radhakrishnan on 5/9/17.
 */

class TrackingCode {
    private String id;
    private String code;

    public TrackingCode() {
    }

    TrackingCode(String id, String code) {
        this.id = id;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
