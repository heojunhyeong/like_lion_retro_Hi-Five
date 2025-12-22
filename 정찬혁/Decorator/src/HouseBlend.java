public class HouseBlend extends Beverage {
    public HouseBlend(){
        setDescription("하우스블랜드 커피");
    }
    @Override
    public int cost() {
        return 1000;
    } // magic number
}
