package semulator.variable;

import java.io.Serializable;
import java.util.Objects;

public class VariableImpl implements Variable
{
    private final VariableType type;
    private final int number;

    public VariableImpl(VariableType type, int number) {
        this.type = type;
        this.number = number;
    }

    @Override
    public VariableType getType() {
        return type;
    }

    @Override
    public String getRepresentation() {
        return type.getVariableRepresentation(number);
    }

    @Override
    public int hashCode() {//implement hashcode based on variable representation
        return Objects.hash(getRepresentation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(getRepresentation(), ((VariableImpl) o).getRepresentation());
    }

  @Override
  public Variable myClone() {
        return  new VariableImpl(type, number);
    }

}
