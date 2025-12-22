public class Mocha extends CondimentDecorator {
    public Mocha(Beverage beverage) {
        super(beverage);
    }
    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 모카";
    }
    @Override
    public int cost() {
        return beverage.cost() + 200;
    }
}
