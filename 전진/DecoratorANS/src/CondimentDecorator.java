import java.util.Arrays;


public abstract class CondimentDecorator extends Beverage {
	protected Beverage beverage;
	protected CondimentDecorator(Beverage beverage){
		this.beverage = beverage;
	}
	
	public Beverage removeCondiment(){
		//현재 클래스 타입이 CondimentDecorator의 경우 장식한 beverage를 반환한다. 
		return beverage;
	}
	
	@Override
	public abstract String getDescription();

    public boolean hasCondiment(Class<? extends CondimentDecorator> type, Beverage beverage) {
        Beverage current = beverage;
        while (current instanceof CondimentDecorator) {
            if (type.isInstance(current))
                return true;
            current = ((CondimentDecorator) current).beverage;
        }
        return false;
    }
	
	public boolean equals(CondimentDecorator decorator) {
		//매개변수로 입력받은 CondimentDecorator객체와 현재 객체를 비교하는 equals함수
		//Beverage클래스의 equals함수와 같은 매커니즘으로 설계되었다. 
		
		String A=this.getDescription();//A 문자열에 현재 객체의 description을 저장
		String B=decorator.getDescription();//B 문자열에 입력받은 객체의 description을 저장

		A=A.replace(",", "");//replace를 이용해 각 문자열의 ,를 없애줌
		B=B.replace(",", "");
		String[] AS=A.split(" ");//각 문자열을 split을 이용해 공백으로 구분
		String[] BS=B.split(" ");

		Arrays.sort(AS);//각 문자열 배열을 정렬
		Arrays.sort(BS);
		String NA=Arrays.toString(AS);//정렬한 문자열 배열을 다시 문자열로 변환
		String NB=Arrays.toString(BS);

		if(NA.equals(NB))//2개 문자열을 비교
			return true;
		else
			return false;
	}
}
