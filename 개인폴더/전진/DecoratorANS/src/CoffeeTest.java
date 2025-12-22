
public class CoffeeTest {
	public static void main(String[] args) {


		Beverage beverage1 = new HouseBlend();
		beverage1 = new Mocha(beverage1);
		beverage1 = new Whip(beverage1);
		beverage1 = new Mocha(beverage1);
        System.out.println(beverage1.getDescription());


		System.out.printf("beverage1 = %s: %,d원%n",
			beverage1.getDescription(), beverage1.cost());

		Beverage beverage2 = new DarkRoast();
		beverage2 = new Mocha(beverage2);
		beverage2 = new Milk(beverage2);
		beverage2 = new Milk(beverage2);
		beverage2 = new Milk(beverage2);
		System.out.printf("beverage2 = %s: %,d원%n",
				beverage2.getDescription(), beverage2.cost());

		Beverage beverage3 = new HouseBlend();//1과는 순서만 다르고 첨가물은 같은 음료 객체
		beverage3 = new Whip(beverage3);
		beverage3 = new Mocha(beverage3);
		beverage3 = new Mocha(beverage3);
		System.out.printf("beverage3 = %s: %,d원%n",
				beverage3.getDescription(), beverage3.cost());


		System.out.println("beverage1 equals beverage2 : "+beverage1.equals(beverage2));
		System.out.println("beverage1 equals beverage3 : "+beverage1.equals(beverage3));
		//1과 2는 equal을 통해 다르다고 출력되고, 1과 3은 equal을 통해 같다고 출력된다.
	}
}
