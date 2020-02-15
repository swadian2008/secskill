package secskill.dataobject;

public class SequenceDO {
    private String name;

    private Integer crrentValue;

    private Integer step;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCrrentValue() {
        return crrentValue;
    }

    public void setCrrentValue(Integer crrentValue) {
        this.crrentValue = crrentValue;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}