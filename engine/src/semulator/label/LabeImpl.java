package semulator.label;

public class LabeImpl implements Label {
    private String label;

    public LabeImpl(int number) {
        this.label = "L" + number;
    }

    @Override
    public String getLabelRepresentation() {return this.label;}
}

