package semulator.variable;

public interface Variable  {
    //myClone method for deep clone
    Variable myClone();
    VariableType getType();
    String getRepresentation();


    Variable RESULT = new VariableImpl(VariableType.RESULT, 0);

    //implent deep clone method for variable withthe super.clone() method
}
