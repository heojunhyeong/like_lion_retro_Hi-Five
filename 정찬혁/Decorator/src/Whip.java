public class Whip extends CondimentDecorator {
    public Whip(Beverage beverage){
        super(beverage);
    }
    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 크림";
    }
    @Override
    public int cost() {
        return beverage.cost() + 500;
    }
}
