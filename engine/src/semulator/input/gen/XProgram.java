package semulator.input.gen;
import jakarta.xml.bind.annotation.*;
import semulator.impl.api.skeleton.Op;

import java.util.List;
@XmlRootElement(name = "S-Program")
@XmlAccessorType(XmlAccessType.FIELD)
public class XProgram {
    @XmlAttribute(name = "name")
    private String name;

    @XmlElementWrapper(name = "S-Instructions")
    @XmlElement(name = "S-Instruction")
    private List<Op> op;

    public String getName() { return name; }
    public List<Op> getOp() { return op; }
}
