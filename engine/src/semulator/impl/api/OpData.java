package semulator.impl.api;

public enum OpData {
    INCREASE("Increase" , 1),
    DECREASE("Decrease",1),
    NEUTRAL("Neutral",0),
    JUMP_NOT_ZERO("JumpNotZero",2)

    ;

    private final String name;
    private final int cycles;

    OpData(String name, int cycles) {
        this.name = name;
        this.cycles = cycles;
    }

    public String getName() {
        return name;
    }
    public int getCycles() {
        return cycles;
    }
}
