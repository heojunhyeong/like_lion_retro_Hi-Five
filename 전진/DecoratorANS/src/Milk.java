
public class Milk extends CondimentDecorator {
    /*
	public Milk(Beverage beverage){
		super(beverage);
		String str= beverage.getDescription();
		if(str.contains("우유"))//음료의 discription을 확인해 우유가 이미 들어있으면
		{
			super.beverage=beverage.removeCondiment();
			//super의 beverage객체에 대해 CondimentDecorator클래스에 재정의한 removeCondiment 작동
		}
	}*/


    public Milk(Beverage beverage){
		super(beverage);

        if (hasCondiment(Milk.class, beverage)) {
            throw new IllegalStateException("우유는 중복 추가할 수 없습니다.");
        }
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
