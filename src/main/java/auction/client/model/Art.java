package auction.client.model;

public class Art extends Item {
    private String artist;
    private String medium;      //chất liệu
    private int year;

    public Art(String name, String description, String artist,
               String medium, int year) {
        super(name, description);
        this.artist = artist;
        this.medium = medium;
        this.year = year;
    }

    @Override
    public String getItemType() {
        return "ART";
    }

    @Override
    public String getSpecifications() {
        return String.format(
                "Artist: %s | Medium: %s | Year: %d", //%s là string, %d là số nguyên
                artist, medium, year //map theo thứ tự các biến
        );
    }

    //getters
    public String getArtist() { return artist; }
    public String getMedium() { return medium; }
    public int getYear() { return year; }
}

