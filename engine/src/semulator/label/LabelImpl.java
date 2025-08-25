package semulator.label;

public class LabelImpl implements Label {
    private final String label;

    public LabelImpl(int number) {
        this.label = "L" + number;
    }

    @Override
    public String getLabelRepresentation() {return this.label;}
}

