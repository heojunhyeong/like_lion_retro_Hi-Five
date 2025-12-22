public class Milk extends CondimentDecorator {

    public Milk(Beverage beverage){
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 우유";
    }

    @Override
    public int cost() {
        return beverage.cost() + 500;
    }
}
