public class CoffeeTest {
    public static void main(String[] args) {
        Beverage beverage = new HouseBlend();
        beverage = new Mocha(beverage);
        beverage = new Whip(beverage);
        beverage = new Mocha(beverage);
        System.out.printf("%s: %,d원%n",
                beverage.getDescription(), beverage.cost());

        beverage = new DarkRoast();
        beverage = new Mocha(beverage);
        beverage = new Milk(beverage);
        try {
            beverage = new Milk(beverage);
        } catch (IllegalArgumentException e) {
            System.out.println("두 번째 우유 추가 실패: " + e.getMessage());
        }
        System.out.printf("%s: %,d원%n",
                beverage.getDescription(), beverage.cost());
    }
}

