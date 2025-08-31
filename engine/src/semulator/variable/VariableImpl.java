package semulator.variable;

import java.util.Objects;

public class VariableImpl implements Variable {
    private final VariableType type;
    private final int number;

    public VariableImpl(VariableType type, int number) {
        this.type = type;
        this.number = number;
    }
    //add Ctor without number so its 0 by default

    @Override
    public VariableType getType() {return type;}

    @Override
    public String getRepresntation() {return type.getVariableRepresentation(number);
    }

    @Override
    public int hashCode() {//implement hashcode based on variable representation}
        return Objects.hash(getRepresntation());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(getRepresntation(), ((VariableImpl) o).getRepresntation());
    }
}
