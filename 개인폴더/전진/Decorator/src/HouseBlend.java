public class HouseBlend extends Beverage {
    private static final int COST = 1000;

    public HouseBlend(){
        super("하우스블렌드 커피");
    }

    @Override
    public int cost() {
        return COST;
    }
}
