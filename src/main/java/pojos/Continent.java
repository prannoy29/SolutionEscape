package pojos;

public enum Continent {
    SOUTH_AMERICA("south-america"),
    NORTH_AMERICA("north-america"),
    AFRICA("africa"),
    ASIA("asia"),
    OCENIA("ocenia"),
    EUROPE("europe");

    private String name;
    Continent(String name) {
        this.name = name;
    }
}
