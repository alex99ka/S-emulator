package semulator.execution;

import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

public interface ExpandContext {
    Label newUniqueLabel();

    /** מחזיר משתנה עבודה חדש בפורמט z<number> (ייחודי), מאופס ומוסף ל-currSnap ולסט המשתנים. */
    Variable newWorkVar();

    void addOpWithNewLabel(Op op);


}

