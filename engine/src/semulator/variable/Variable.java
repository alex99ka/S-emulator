package semulator.variable;

public interface Variable  {
    //myClone method for deep clone
    Variable myClone();
    VariableType getType();
    String getRepresentation();


    Variable RESULT = new VariableImpl(VariableType.RESULT, 0);

    //implement deep clone method for variable with the super.clone() method
}
