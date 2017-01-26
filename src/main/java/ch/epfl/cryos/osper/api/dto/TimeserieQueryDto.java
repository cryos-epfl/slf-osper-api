package ch.epfl.cryos.osper.api.dto;

/**
 * Created by kryvych on 03/10/16.
 */
public class TimeserieQueryDto {

//
    private String from;
    private String until;


    private Integer limit;


    public TimeserieQueryDto() {
    }



    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    @Override
    public String toString() {
        return "TimeserieQueryDto{" +
                "from='" + from + '\'' +
                ", until='" + until + '\'' +
                ", limit=" + limit +
                '}';
    }
}
