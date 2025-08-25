package semulator.variable;

public class VariableImpl implements Variable {
    private final VariableType type;
    private final int number;

    public VariableImpl(VariableType type, int number) {
        this.type = type;
        this.number = number;
    }

    @Override
    public VariableType getType() {return type;}

    @Override
    public String getRepresntation() {return type.getVariableRepresentation(number);
    }
}
