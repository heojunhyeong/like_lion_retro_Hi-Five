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
}