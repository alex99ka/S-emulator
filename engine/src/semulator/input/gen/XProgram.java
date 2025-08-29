package semulator.input.gen;
import jakarta.xml.bind.annotation.*;
import semulator.impl.api.skeleton.Op;

import java.util.ArrayList;
import java.util.List;
@XmlRootElement(name = "S-Program")
@XmlAccessorType(XmlAccessType.FIELD)
public class XProgram {
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlElement(name = "S-Instructions", required = true)
    private ArrayList<XOp> xOpArrayList;  // contains the list of SInstruction

    public String getName() { return name; }
    public ArrayList<XOp> getInstructions() { return xOpArrayList; }
}


