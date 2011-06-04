package org.zaregoto.examples.android.miku.parser.mqo;

/**
 * Created by IntelliJ IDEA.
 * User: waka
 * Date: 5/15/11
 * Time: 6:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetaseqMaterial {

    enum SHADER {
        SHADER_CLASSIC,    // 0
        SHADER_CONSTANT,   // 1
        SHADER_LAMBERT,    // 2
        SHADER_PHONG,      // 3
        SHADER_BLINN,      // 4
    }

    enum PROJ_TYPE {
        PROJ_TYPE_UV,       // 0
        PROJ_TYPE_PLANE,    // 1
        PROJ_TYPE_CYLINDER, // 2
        PROJ_TYPE_SPHERE,   // 3
    }

    private String name = null;
    private SHADER shader = null;
    private Boolean vcol = null;  // 0: false 1: true
    private Double[] color = null;
    private Double dif = null;
    private Double amb = null;
    private Double emi = null;
    private Double spc = null;
    private Double power = null;
    private String tex = null;
    private String aplane = null;
    private String bump = null;
    private PROJ_TYPE projType = null;
    private Double[] projPos = null;
    private Double[] projScale = null;
    private Double[] projAngle = null;


    public MetaseqMaterial() {
        return;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SHADER getShader() {
        return shader;
    }

    public void setShader(SHADER shader) {
        this.shader = shader;
    }

    public Boolean getVcol() {
        return vcol;
    }

    public void setVcol(Boolean vcol) {
        this.vcol = vcol;
    }

    public Double[] getColor() {
        return color;
    }

    public void setColor(Double[] color) {
        this.color = color;
    }

    public Double getDif() {
        return dif;
    }

    public void setDif(Double dif) {
        this.dif = dif;
    }

    public Double getAmb() {
        return amb;
    }

    public void setAmb(Double amb) {
        this.amb = amb;
    }

    public Double getEmi() {
        return emi;
    }

    public void setEmi(Double emi) {
        this.emi = emi;
    }

    public Double getSpc() {
        return spc;
    }

    public void setSpc(Double spc) {
        this.spc = spc;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public String getTex() {
        return tex;
    }

    public void setTex(String tex) {
        this.tex = tex;
    }

    public String getAplane() {
        return aplane;
    }

    public void setAplane(String aplane) {
        this.aplane = aplane;
    }

    public String getBump() {
        return bump;
    }

    public void setBump(String bump) {
        this.bump = bump;
    }

    public PROJ_TYPE getProjType() {
        return projType;
    }

    public void setProjType(PROJ_TYPE projType) {
        this.projType = projType;
    }

    public Double[] getProjPos() {
        return projPos;
    }

    public void setProjPos(Double[] projPos) {
        this.projPos = projPos;
    }

    public Double[] getProjScale() {
        return projScale;
    }

    public void setProjScale(Double[] projScale) {
        this.projScale = projScale;
    }

    public Double[] getProjAngle() {
        return projAngle;
    }

    public void setProjAngle(Double[] projAngle) {
        this.projAngle = projAngle;
    }
}
