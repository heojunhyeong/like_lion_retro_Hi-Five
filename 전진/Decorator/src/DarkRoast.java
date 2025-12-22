/**
 * 다크로스트 커피를 나타내는 음료 클래스
 *
 * Decorator 패턴에서 ConcreteComponent 역할을 수행하며,
 * 기본 음료 비용을 반환한다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-22
 * @DateOfEdit 2025-12-22
 */
public class DarkRoast extends Beverage {

    private static final int COST = 1200;

    public DarkRoast(){
        super("다크로스트 커피");
    }


    @Override
    public int cost() {
        return COST;
    }
}
