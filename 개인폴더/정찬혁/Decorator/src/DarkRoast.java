public class DarkRoast extends Beverage {
    private static final int COST = 1200;
    public DarkRoast(){
        setDescription("다크로스트 커피");
    }
    @Override
    public int cost() {
        return COST;
    }
}
