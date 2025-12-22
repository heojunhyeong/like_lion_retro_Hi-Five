public class Milk extends CondimentDecorator {
    public Milk(Beverage beverage){
        super(validateNoDuplicateMilk(beverage));
    }

    private static Beverage validateNoDuplicateMilk(Beverage beverage) {
        if (beverage == null) {
            throw new IllegalArgumentException("음료가 없습니다.");
        }
        Beverage current = beverage;
        while (current instanceof CondimentDecorator) {
            if (current instanceof Milk) {
                throw new IllegalArgumentException("우유는 한 번만 추가할 수 있습니다.");
            }
            current = ((CondimentDecorator) current).beverage;
        }
        return beverage;
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

