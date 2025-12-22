import java.util.Objects;

public abstract class CondimentDecorator extends Beverage {
    protected Beverage beverage;

    protected CondimentDecorator(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public abstract String getDescription();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CondimentDecorator other = (CondimentDecorator) obj;
        return Objects.equals(beverage, other.beverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), beverage);
    }
}

