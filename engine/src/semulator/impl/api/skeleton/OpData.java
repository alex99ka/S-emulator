package semulator.impl.api.skeleton;


public enum OpData {

    INCREASE("INCREASE" , 1,0, OpType.BASIC),
    DECREASE("DECREASE",1,0,OpType.BASIC),
    JUMP_NOT_ZERO("JUMP_NOT_ZERO",2,0,OpType.BASIC),
    NEUTRAL("NEUTRAL",0,0,OpType.BASIC),
    ZERO_VARIABLE("ZERO_VARIABLE",1,1,OpType.SYNTHETIC),
    GOTO_LABEL("GOTO_LABEL",1,1,OpType.SYNTHETIC),
    ASSIGNMENT("ASSIGNMENT",4,2,OpType.SYNTHETIC),
    CONSTANT_ASSIGNMENT("CONSTANT_ASSIGNMENT",2,2,OpType.SYNTHETIC),
    JUMP_ZERO("JUMP_ZERO",2,2,OpType.SYNTHETIC),
    JUMP_EQUAL_CONSTANT("JUMP_EQUAL_CONSTANT",2,3,OpType.SYNTHETIC),
    JUMP_EQUAL_VARIABLE("JUMP_EQUAL_VARIABLE",2,2,OpType.SYNTHETIC)

    ;


    private final String name;
    private final int cycles;

    public int getDegree() {
        return degree;
    }

    private final int degree;
    private final OpType type;

    OpData(String name, int cycles, int degree, OpType type) {
        this.name = name;
        this.cycles = cycles;
        this.type = type;
        this.degree = degree;
    }

    public String getName() {
        return name;
    }
    public int getCycles() {
        return cycles;
    }

    public OpType getType() {
        return type;
    }
}
