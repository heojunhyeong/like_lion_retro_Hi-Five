public abstract class Beverage {

    protected final String description;

    protected Beverage(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public abstract int cost();
}
