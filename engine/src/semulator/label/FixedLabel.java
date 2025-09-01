package semulator.label;

public enum FixedLabel implements Label {

    EXIT{
        @Override
        public  String getLabelRepresentation() {return "EXIT";}

        @Override
        public Label myClone() {
            return null;
        }
    },
    EMPTY{
        @Override
        public String getLabelRepresentation() {return "";}
        public   Label myClone() {
            return null;
        }
    };

    @Override
    public abstract String getLabelRepresentation();

}
