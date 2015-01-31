package GameConcepts;

public class Unit {
    private String imageName;
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

    public Unit(){
        this.imageName = "Resources/UnitImages/WarriorFront.png";
        this.charName = "John";
        this.typeName = "Warrior";
        this.typeVal = 2;
        this.ACC = 85;
        this.APT = 2;
        this.DEF = 3;
        this.EVA = 10;
        this.EXP = 0;
        this.HP = 15;
        this.LEVEL = 1;
        this.MOVE = 5;
        this.MP = 0;
        this.RANGE = 1;
        this.RET = 1;
        this.STR = 6;
        this.VALUE = calcVALUE();
    }

    public Unit(String imageName, String charName, String typeName, Integer typeVal, Integer ACC, Integer APT, Integer DEF, Integer EVA,
                Integer EXP, Integer HP, Integer LEVEL, Integer MOVE, Integer MP, Integer RANGE, Integer RET, Integer STR) {
        this.imageName = imageName;
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
        this.VALUE = typeVal;
    }

    public Unit(Unit u) {
        this.imageName = u.imageName;
        this.charName = u.charName;
        this.typeName = u.typeName;
        this.typeVal = u.typeVal;
        this.ACC = u.ACC;
        this.APT = u.APT;
        this.DEF = u.DEF;
        this.EVA = u.EVA;
        this.EXP = u.EXP;
        this.HP = u.HP;
        this.LEVEL = u.LEVEL;
        this.MOVE = u.MOVE;
        this.MP = u.MP;
        this.RANGE = u.RANGE;
        this.RET = u.RET;
        this.STR = u.STR;
        this.VALUE = u.VALUE;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public Integer calcVALUE(){
        //calculate current worth of the unit
        return 5;
    }

    public Integer getVALUE() {
        return VALUE;
    }

    public void setVALUE(Integer VALUE) {
        this.VALUE = VALUE;
    }
}
