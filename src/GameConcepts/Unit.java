package GameConcepts;

import java.io.Serializable;

public class Unit implements Serializable, Comparable<Unit> {
    private final static String DEFAULTIMAGENAME = "Resources/UnitImages/WarriorFront.png";
    private final static Integer DEFAULTMAXLEVEL = 4;
    private final static Integer DEFAULTOVERLEVELBONUS = 2;

    //Standard characteristics
    private String imageName;
    private String charName;
    private String typeName;
    private Integer typeVal;
    private Integer ACC;
    private Integer APT;
    private Integer CMP;
    private Integer CPA;
    private Integer DEF;
    private Integer EVA;
    private Integer EXP;
    private Integer HP;
    private Integer LEVEL;
    private Integer MOVE;
    private Integer MP;
    private Integer minRANGE;
    private Integer maxRANGE;
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

    //Level up Characteristics
    private Integer lvlACC;
    private Integer lvlDEF;
    private Integer lvlEVA;
    private Integer lvlEXP;
    private Integer lvlHP;
    private Integer lvlMP;
    private Integer lvlSTR;

    //Maximums
    private Integer MaxLevel;

    public Unit(){
        this.imageName = DEFAULTIMAGENAME;
        this.charName = "John";
        this.typeName = "Warrior";
        this.typeVal = 2;
        this.ACC = 85;
        this.APT = 2;
        this.CMP = 0;
        this.CPA = 2;
        this.DEF = 3;
        this.EVA = 10;
        this.EXP = 30;
        this.HP = 15;
        this.LEVEL = 1;
        this.MOVE = 5;
        this.MP = 0;
        this.minRANGE = 1;
        this.maxRANGE = 1;
        this.RET = 1;
        this.STR = 6;
        this.DESC = "";

        this.curHP = this.HP;
        this.curMP = this.MP;
        this.curACT = this.APT;
        this.curEXP = 0;
        this.curMOV = this.MOVE;
        this.curRET = this.RET;

        this.lvlACC = 5;
        this.lvlDEF = 1;
        this.lvlEVA = 5;
        this.lvlEXP = 10;
        this.lvlHP = 3;
        this.lvlMP = 0;
        this.lvlSTR = 1;

        this.MaxLevel = DEFAULTMAXLEVEL;
    }

    public Unit(String imageName, String charName, String typeName, Integer typeVal, Integer ACC, Integer APT, Integer CMP,
                Integer CPA, Integer DEF, Integer EVA, Integer EXP, Integer HP, Integer maxLEVEL, Integer MOVE, Integer MP,
                Integer minRANGE, Integer maxRANGE, Integer RET, Integer STR, String DESC, Integer lvlACC, Integer lvlDEF,
                Integer lvlEVA, Integer lvlEXP, Integer lvlHP, Integer lvlMP, Integer lvlSTR) {
        this.imageName = imageName;
        this.charName = charName;
        this.typeName = typeName;
        this.typeVal = typeVal;
        this.ACC = ACC;
        this.APT = APT;
        this.CMP = CMP;
        this.CPA = CPA;
        this.DEF = DEF;
        this.EVA = EVA;
        this.EXP = EXP;
        this.HP = HP;
        this.LEVEL = 1;
        this.MOVE = MOVE;
        this.MP = MP;
        this.minRANGE = minRANGE;
        this.maxRANGE = maxRANGE;
        this.RET = RET;
        this.STR = STR;
        this.DESC = DESC;

        this.curHP = this.HP;
        this.curMP = this.MP;
        this.curACT = this.APT;
        this.curEXP = 0;
        this.curMOV = this.MOVE;
        this.curRET = this.RET;

        this.lvlACC = lvlACC;
        this.lvlDEF = lvlDEF;
        this.lvlEVA = lvlEVA;
        this.lvlEXP = lvlEXP;
        this.lvlHP = lvlHP;
        this.lvlMP = lvlMP;
        this.lvlSTR = lvlSTR;

        this.MaxLevel = maxLEVEL;
    }

    public Unit(Unit u) {
        this.imageName = u.imageName;
        this.charName = u.charName;
        this.typeName = u.typeName;
        this.typeVal = u.typeVal;
        this.ACC = u.ACC;
        this.APT = u.APT;
        this.CMP = u.CMP;
        this.CPA = u.CPA;
        this.DEF = u.DEF;
        this.EVA = u.EVA;
        this.EXP = u.EXP;
        this.HP = u.HP;
        this.LEVEL = u.LEVEL;
        this.MOVE = u.MOVE;
        this.MP = u.MP;
        this.minRANGE = u.minRANGE;
        this.maxRANGE = u.maxRANGE;
        this.RET = u.RET;
        this.STR = u.STR;
        this.DESC = u.DESC;

        this.curHP = u.HP;
        this.curMP = u.MP;
        this.curACT = u.APT;
        this.curEXP = u.curEXP;
        this.curMOV = u.MOVE;
        this.curRET = u.RET;

        this.lvlACC = u.lvlACC;
        this.lvlDEF = u.lvlDEF;
        this.lvlEVA = u.lvlEVA;
        this.lvlEXP = u.lvlEXP;
        this.lvlHP = u.lvlHP;
        this.lvlMP = u.lvlMP;
        this.lvlSTR = u.lvlSTR;

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

    public Integer getCMP() {
        return CMP;
    }

    public void setCMP(Integer CMP) {
        this.CMP = CMP;
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

    public Integer getMaxRANGE() {
        return maxRANGE;
    }

    public void setMaxRANGE(Integer maxRANGE) {
        this.maxRANGE = maxRANGE;
    }

    public Integer getMinRANGE() {
        return minRANGE;
    }

    public void setMinRANGE(Integer minRANGE) {
        this.minRANGE = minRANGE;
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

    public void refreshUnit(){
        if(curACT == APT){ // heal if the unit rested this round
            if(curHP < HP){
                curHP++;
            }
            if(curMP < MP){
                curMP++;
            }
        }
        if(curMP < MP){
            curMP++;
        }
        this.curACT = APT;
        this.curMOV = MOVE;
        this.curRET = RET;
    }

    public void addExperience(Integer addExp){
        curEXP += addExp;
        if(curEXP >= EXP){
            curEXP -= EXP;
            levelUp();
        }
    }

    public void levelUp(){
        if(LEVEL < MaxLevel){ //increase the level counter
            LEVEL++;
            ACC += lvlACC;
            DEF += lvlDEF;
            EVA += lvlEVA;
            EXP += lvlEXP;
            HP += lvlHP;
            curHP += lvlHP;
            MP += lvlMP;
            STR += lvlSTR;
        } else { // some bonus even if already the max level.
            curHP += DEFAULTOVERLEVELBONUS;
            HP += DEFAULTOVERLEVELBONUS;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;

        Unit unit = (Unit) o;

        if (ACC != null ? !ACC.equals(unit.ACC) : unit.ACC != null) return false;
        if (APT != null ? !APT.equals(unit.APT) : unit.APT != null) return false;
        if (CPA != null ? !CPA.equals(unit.CPA) : unit.CPA != null) return false;
        if (DEF != null ? !DEF.equals(unit.DEF) : unit.DEF != null) return false;
        if (DESC != null ? !DESC.equals(unit.DESC) : unit.DESC != null) return false;
        if (EVA != null ? !EVA.equals(unit.EVA) : unit.EVA != null) return false;
        if (EXP != null ? !EXP.equals(unit.EXP) : unit.EXP != null) return false;
        if (HP != null ? !HP.equals(unit.HP) : unit.HP != null) return false;
        if (LEVEL != null ? !LEVEL.equals(unit.LEVEL) : unit.LEVEL != null) return false;
        if (MOVE != null ? !MOVE.equals(unit.MOVE) : unit.MOVE != null) return false;
        if (MP != null ? !MP.equals(unit.MP) : unit.MP != null) return false;
        if (MaxLevel != null ? !MaxLevel.equals(unit.MaxLevel) : unit.MaxLevel != null) return false;
        if (RET != null ? !RET.equals(unit.RET) : unit.RET != null) return false;
        if (STR != null ? !STR.equals(unit.STR) : unit.STR != null) return false;
        if (charName != null ? !charName.equals(unit.charName) : unit.charName != null) return false;
        if (curACT != null ? !curACT.equals(unit.curACT) : unit.curACT != null) return false;
        if (curEXP != null ? !curEXP.equals(unit.curEXP) : unit.curEXP != null) return false;
        if (curHP != null ? !curHP.equals(unit.curHP) : unit.curHP != null) return false;
        if (curMOV != null ? !curMOV.equals(unit.curMOV) : unit.curMOV != null) return false;
        if (curMP != null ? !curMP.equals(unit.curMP) : unit.curMP != null) return false;
        if (curRET != null ? !curRET.equals(unit.curRET) : unit.curRET != null) return false;
        if (imageName != null ? !imageName.equals(unit.imageName) : unit.imageName != null) return false;
        if (lvlACC != null ? !lvlACC.equals(unit.lvlACC) : unit.lvlACC != null) return false;
        if (lvlDEF != null ? !lvlDEF.equals(unit.lvlDEF) : unit.lvlDEF != null) return false;
        if (lvlEVA != null ? !lvlEVA.equals(unit.lvlEVA) : unit.lvlEVA != null) return false;
        if (lvlEXP != null ? !lvlEXP.equals(unit.lvlEXP) : unit.lvlEXP != null) return false;
        if (lvlHP != null ? !lvlHP.equals(unit.lvlHP) : unit.lvlHP != null) return false;
        if (lvlMP != null ? !lvlMP.equals(unit.lvlMP) : unit.lvlMP != null) return false;
        if (lvlSTR != null ? !lvlSTR.equals(unit.lvlSTR) : unit.lvlSTR != null) return false;
        if (maxRANGE != null ? !maxRANGE.equals(unit.maxRANGE) : unit.maxRANGE != null) return false;
        if (minRANGE != null ? !minRANGE.equals(unit.minRANGE) : unit.minRANGE != null) return false;
        if (typeName != null ? !typeName.equals(unit.typeName) : unit.typeName != null) return false;
        if (typeVal != null ? !typeVal.equals(unit.typeVal) : unit.typeVal != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageName != null ? imageName.hashCode() : 0;
        result = 31 * result + (charName != null ? charName.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (typeVal != null ? typeVal.hashCode() : 0);
        result = 31 * result + (ACC != null ? ACC.hashCode() : 0);
        result = 31 * result + (APT != null ? APT.hashCode() : 0);
        result = 31 * result + (CPA != null ? CPA.hashCode() : 0);
        result = 31 * result + (DEF != null ? DEF.hashCode() : 0);
        result = 31 * result + (EVA != null ? EVA.hashCode() : 0);
        result = 31 * result + (EXP != null ? EXP.hashCode() : 0);
        result = 31 * result + (HP != null ? HP.hashCode() : 0);
        result = 31 * result + (LEVEL != null ? LEVEL.hashCode() : 0);
        result = 31 * result + (MOVE != null ? MOVE.hashCode() : 0);
        result = 31 * result + (MP != null ? MP.hashCode() : 0);
        result = 31 * result + (minRANGE != null ? minRANGE.hashCode() : 0);
        result = 31 * result + (maxRANGE != null ? maxRANGE.hashCode() : 0);
        result = 31 * result + (RET != null ? RET.hashCode() : 0);
        result = 31 * result + (STR != null ? STR.hashCode() : 0);
        result = 31 * result + (DESC != null ? DESC.hashCode() : 0);
        result = 31 * result + (curHP != null ? curHP.hashCode() : 0);
        result = 31 * result + (curMP != null ? curMP.hashCode() : 0);
        result = 31 * result + (curACT != null ? curACT.hashCode() : 0);
        result = 31 * result + (curEXP != null ? curEXP.hashCode() : 0);
        result = 31 * result + (curMOV != null ? curMOV.hashCode() : 0);
        result = 31 * result + (curRET != null ? curRET.hashCode() : 0);
        result = 31 * result + (lvlACC != null ? lvlACC.hashCode() : 0);
        result = 31 * result + (lvlDEF != null ? lvlDEF.hashCode() : 0);
        result = 31 * result + (lvlEVA != null ? lvlEVA.hashCode() : 0);
        result = 31 * result + (lvlEXP != null ? lvlEXP.hashCode() : 0);
        result = 31 * result + (lvlHP != null ? lvlHP.hashCode() : 0);
        result = 31 * result + (lvlMP != null ? lvlMP.hashCode() : 0);
        result = 31 * result + (lvlSTR != null ? lvlSTR.hashCode() : 0);
        result = 31 * result + (MaxLevel != null ? MaxLevel.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Unit o) {
        String curString = this.getTypeName() + this.getCharName();
        String oString = o.getTypeName() + o.getCharName();
        return curString.compareTo(oString);
    }

    @Override
    public String toString() {
        return typeName + " --- " + charName;
    }
}
