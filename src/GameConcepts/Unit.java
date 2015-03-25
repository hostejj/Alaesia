package GameConcepts;

import java.io.Serializable;

public class Unit implements Serializable, Comparable<Unit> {
    private static String DEFAULTIMAGENAME = "Resources/UnitImages/WarriorFront.png";
    private static Integer DEFAULTMAXLEVEL = 4;

    //Standard characteristics
    private String imageName;
    private String charName;
    private String typeName;
    private Integer typeVal;
    private Integer ACC;
    private Integer APT;
    private Integer CPA;
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
    private String DESC;

    //Variable Characteristics
    private Integer curHP;
    private Integer curMP;
    private Integer curACT;
    private Integer curEXP;
    private Integer curMOV;
    private Integer curRET;

    //Maximums
    private Integer MaxLevel;

    public Unit(){
        this.imageName = DEFAULTIMAGENAME;
        this.charName = "John";
        this.typeName = "Warrior";
        this.typeVal = 2;
        this.ACC = 85;
        this.APT = 2;
        this.CPA = 2;
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
        this.DESC = "";

        this.curHP = this.HP;
        this.curMP = this.MP;
        this.curACT = this.APT;
        this.curEXP = 0;
        this.curMOV = this.MOVE;
        this.curRET = this.RET;

        this.MaxLevel = DEFAULTMAXLEVEL;
    }

    public Unit(String imageName, String charName, String typeName, Integer typeVal, Integer ACC, Integer APT,
                Integer CPA, Integer DEF, Integer EVA, Integer EXP, Integer HP, Integer LEVEL, Integer MOVE, Integer MP,
                Integer RANGE, Integer RET, Integer STR, String DESC) {
        this.imageName = imageName;
        this.charName = charName;
        this.typeName = typeName;
        this.typeVal = typeVal;
        this.ACC = ACC;
        this.APT = APT;
        this.CPA = CPA;
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
        this.DESC = DESC;

        this.curHP = this.HP;
        this.curMP = this.MP;
        this.curACT = this.APT;
        this.curEXP = 0;
        this.curMOV = this.MOVE;
        this.curRET = this.RET;

        this.MaxLevel = DEFAULTMAXLEVEL;
    }

    public Unit(Unit u) {
        this.imageName = u.imageName;
        this.charName = u.charName;
        this.typeName = u.typeName;
        this.typeVal = u.typeVal;
        this.ACC = u.ACC;
        this.APT = u.APT;
        this.CPA = u.CPA;
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
        this.DESC = u.DESC;

        this.curHP = u.HP;
        this.curMP = u.MP;
        this.curACT = u.APT;
        this.curEXP = u.EXP;
        this.curMOV = u.MOVE;
        this.curRET = u.RET;

        this.MaxLevel = u.MaxLevel;
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

    public Integer getCPA() {
        return CPA;
    }

    public void setCPA(Integer CPA) {
        this.CPA = CPA;
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

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public Integer getCurHP() {
        return curHP;
    }

    public void setCurHP(Integer curHP) {
        this.curHP = curHP;
    }

    public Integer getCurMP() {
        return curMP;
    }

    public void setCurMP(Integer curMP) {
        this.curMP = curMP;
    }

    public Integer getCurACT() {
        return curACT;
    }

    public void setCurACT(Integer curACT) {
        this.curACT = curACT;
    }

    public Integer getCurEXP() {
        return curEXP;
    }

    public void setCurEXP(Integer curEXP) {
        this.curEXP = curEXP;
    }

    public Integer getCurMOV() {
        return curMOV;
    }

    public void setCurMOV(Integer curMOV) {
        this.curMOV = curMOV;
    }

    public Integer getCurRET() {
        return curRET;
    }

    public void setCurRET(Integer curRET) {
        this.curRET = curRET;
    }

    public Integer getMaxLevel() {
        return MaxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        MaxLevel = maxLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;

        Unit unit = (Unit) o;

        if (!ACC.equals(unit.ACC)) return false;
        if (!APT.equals(unit.APT)) return false;
        if (!CPA.equals(unit.CPA)) return false;
        if (!DEF.equals(unit.DEF)) return false;
        if (!DESC.equals(unit.DESC)) return false;
        if (!EVA.equals(unit.EVA)) return false;
        if (!EXP.equals(unit.EXP)) return false;
        if (!HP.equals(unit.HP)) return false;
        if (!LEVEL.equals(unit.LEVEL)) return false;
        if (!MOVE.equals(unit.MOVE)) return false;
        if (!MP.equals(unit.MP)) return false;
        if (!MaxLevel.equals(unit.MaxLevel)) return false;
        if (!RANGE.equals(unit.RANGE)) return false;
        if (!RET.equals(unit.RET)) return false;
        if (!STR.equals(unit.STR)) return false;
        if (!charName.equals(unit.charName)) return false;
        if (!curACT.equals(unit.curACT)) return false;
        if (!curEXP.equals(unit.curEXP)) return false;
        if (!curHP.equals(unit.curHP)) return false;
        if (!curMOV.equals(unit.curMOV)) return false;
        if (!curMP.equals(unit.curMP)) return false;
        if (!curRET.equals(unit.curRET)) return false;
        if (!imageName.equals(unit.imageName)) return false;
        if (!typeName.equals(unit.typeName)) return false;
        if (!typeVal.equals(unit.typeVal)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageName.hashCode();
        result = 31 * result + charName.hashCode();
        result = 31 * result + typeName.hashCode();
        result = 31 * result + typeVal.hashCode();
        result = 31 * result + ACC.hashCode();
        result = 31 * result + APT.hashCode();
        result = 31 * result + CPA.hashCode();
        result = 31 * result + DEF.hashCode();
        result = 31 * result + EVA.hashCode();
        result = 31 * result + EXP.hashCode();
        result = 31 * result + HP.hashCode();
        result = 31 * result + LEVEL.hashCode();
        result = 31 * result + MOVE.hashCode();
        result = 31 * result + MP.hashCode();
        result = 31 * result + RANGE.hashCode();
        result = 31 * result + RET.hashCode();
        result = 31 * result + STR.hashCode();
        result = 31 * result + DESC.hashCode();
        result = 31 * result + curHP.hashCode();
        result = 31 * result + curMP.hashCode();
        result = 31 * result + curACT.hashCode();
        result = 31 * result + curEXP.hashCode();
        result = 31 * result + curMOV.hashCode();
        result = 31 * result + curRET.hashCode();
        result = 31 * result + MaxLevel.hashCode();
        return result;
    }

    @Override
    public int compareTo(Unit o) {
        String curString = this.getTypeName() + this.getCharName();
        String oString = o.getTypeName() + o.getCharName();
        return curString.compareTo(oString);
    }
}
