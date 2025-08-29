package semulator.variable;

public interface Variable {
    VariableType getType();
    String getRepresntation();

    Variable RESULT = new VariableImpl(VariableType.RESULT, 0);

}
