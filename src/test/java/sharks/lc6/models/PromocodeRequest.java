package sharks.lc6.models;

public class PromocodeRequest {
    private final String promocode;

    public PromocodeRequest(String promocode) {
        this.promocode = promocode;
    }

    public String getPromocode() { return promocode; }
}