package jun.moviecommunity.service;

import lombok.Data;

import java.util.Date;

@Data
public class MovieResponseDto {
    private int display;
    private Item[] items;

    @Data
    static class Item {
        public String title;
        public String link;
        public String image;
        public String subtitle;
        public Date pubDate;
        public String director;
        public String actor;
        public float userRating;
    }
}
