package cn.sowell.datacenter.model.basepeople.dto;

public class  FieldDataDto  {

    /**
     *          json.put("id", "2");
     json.put("title", "身份证号");
     json.put("title_en", "idcode");
     json.put("type", "1");
     */
    private  String  id;
    private  String title;
    private  String title_en;
    private  String type;
    private  String check_rule;

    public FieldDataDto() {
    }

    public FieldDataDto(String id, String title, String title_en, String type,String check_rule) {
        this.id = id;
        this.title = title;
        this.title_en = title_en;
        this.type = type;
        this.check_rule =check_rule;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", title_en='" + title_en + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheck_rule() {
        return check_rule;
    }

    public void setCheck_rule(String check_rule) {
        this.check_rule = check_rule;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldDataDto that = (FieldDataDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (title_en != null ? !title_en.equals(that.title_en) : that.title_en != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return check_rule != null ? check_rule.equals(that.check_rule) : that.check_rule == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (title_en != null ? title_en.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (check_rule != null ? check_rule.hashCode() : 0);
        return result;
    }
}
