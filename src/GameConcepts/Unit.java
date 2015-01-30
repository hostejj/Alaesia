package GameConcepts;

public class Unit {
    private String charName;
    private String typeName;
    private Integer typeVal;
    private Integer ACC;
    private Integer APT;
    private Integer DEF;
    private Integer EVA;
    private Integer EXP;
    private Integer HP;
    private Integer LEVEL;
    private Integer MOVE;
    private Integer MP;
    private Integer RANGE;
    private Integer RET;
    private Integer STR;
    private Integer VALUE;

    public Unit(String charName, String typeName, Integer typeVal, Integer ACC, Integer APT, Integer DEF, Integer EVA, Integer EXP,
                Integer HP, Integer LEVEL, Integer MOVE, Integer MP, Integer RANGE, Integer RET, Integer STR, Integer VALUE) {
        this.charName = charName;
        this.typeName = typeName;
        this.typeVal = typeVal;
        this.ACC = ACC;
        this.APT = APT;
        this.DEF = DEF;
        this.EVA = EVA;
        this.EXP = EXP;
        this.HP = HP;
        this.LEVEL = LEVEL;
        this.MOVE = MOVE;
        this.MP = MP;
        this.RANGE = RANGE;
        this.RET = RET;
        this.STR = STR;
        this.VALUE = VALUE;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(Integer typeVal) {
        this.typeVal = typeVal;
    }

    public Integer getACC() {
        return ACC;
    }

    public void setACC(Integer ACC) {
        this.ACC = ACC;
    }

    public Integer getAPT() {
        return APT;
    }

    public void setAPT(Integer APT) {
        this.APT = APT;
    }

    public Integer getDEF() {
        return DEF;
    }

    public void setDEF(Integer DEF) {
        this.DEF = DEF;
    }

    public Integer getEVA() {
        return EVA;
    }

    public void setEVA(Integer EVA) {
        this.EVA = EVA;
    }

    public Integer getEXP() {
        return EXP;
    }

    public void setEXP(Integer EXP) {
        this.EXP = EXP;
    }

    public Integer getHP() {
        return HP;
    }

    public void setHP(Integer HP) {
        this.HP = HP;
    }

    public Integer getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(Integer LEVEL) {
        this.LEVEL = LEVEL;
    }

    public Integer getMOVE() {
        return MOVE;
    }

    public void setMOVE(Integer MOVE) {
        this.MOVE = MOVE;
    }

    public Integer getMP() {
        return MP;
    }

    public void setMP(Integer MP) {
        this.MP = MP;
    }

    public Integer getRANGE() {
        return RANGE;
    }

    public void setRANGE(Integer RANGE) {
        this.RANGE = RANGE;
    }

    public Integer getRET() {
        return RET;
    }

    public void setRET(Integer RET) {
        this.RET = RET;
    }

    public Integer getSTR() {
        return STR;
    }

    public void setSTR(Integer STR) {
        this.STR = STR;
    }

    public Integer getVALUE() {
        return VALUE;
    }

    public void setVALUE(Integer VALUE) {
        this.VALUE = VALUE;
    }
}
