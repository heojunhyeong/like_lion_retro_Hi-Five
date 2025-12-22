import java.util.Arrays;


public abstract class Beverage{
	private String description = "이름없는 음료";
	// 이 메소드의 문제점???
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	public abstract int cost();
	
	public Beverage removeCondiment() {
		//현재 클래스 타입이 Beverage의 경우 this를 반환한다. 
		return this;
	}
	
	public boolean equals(Beverage beverage) {
		//매개변수로 입력받은 beverage객체와 현재 객체를 비교하는 equals함수
		//매커니즘은 아래와 같다. 
		/*장식된 순서가 다르더라도 커피와 첨가물이 같으면 true를 얻으려면 커피와 첨가물이 들어있는
		  객체의 discription을 배열로 만들어 정렬, 둘을 비교해 같은지를 판별하면 된다. 
		  정렬을 한다면 순서와는 상관없이 자음 순으로 정렬될 것이기 때문이다. 
		  따라서 문자열의 ,를 제거한 뒤 공백으로 구분한 문자열을 sort하고 다시 합쳐 비교하는 방식으로 구현하였다. 
		*/
		
		String A=this.getDescription();//A 문자열에 현재 객체의 description을 저장
		String B=beverage.getDescription();//B 문자열에 입력받은 객체의 description을 저장

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
