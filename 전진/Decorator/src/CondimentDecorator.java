public abstract class CondimentDecorator extends Beverage {

    protected final Beverage beverage;

    protected CondimentDecorator(Beverage beverage) {
        super(beverage.getDescription());
        this.beverage = beverage;
    }

    @Override
    public abstract String getDescription();
}
