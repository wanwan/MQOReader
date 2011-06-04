package org.zaregoto.examples.android.miku.parser.mqo;

/**
 * Created by IntelliJ IDEA.
 * User: waka
 * Date: 5/15/11
 * Time: 6:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetaseqScene {

    private Double[] pos = null;
    private Double[] lookat = null;
    private Double head = null;
    private Double pich = null;
    private Integer ortho = null;
    private Double zoom2 = null;
    private Double[] amb = null;

    public MetaseqScene() {
        return;
    }


    public Double[] getPos() {
        return pos;
    }

    public void setPos(Double[] pos) {
        this.pos = pos;
    }

    public Double[] getLookat() {
        return lookat;
    }

    public void setLookat(Double[] lookat) {
        this.lookat = lookat;
    }

    public Double getHead() {
        return head;
    }

    public void setHead(Double head) {
        this.head = head;
    }

    public Double getPich() {
        return pich;
    }

    public void setPich(Double pich) {
        this.pich = pich;
    }

    public Integer getOrtho() {
        return ortho;
    }

    public void setOrtho(Integer ortho) {
        this.ortho = ortho;
    }

    public Double getZoom2() {
        return zoom2;
    }

    public void setZoom2(Double zoom2) {
        this.zoom2 = zoom2;
    }

    public Double[] getAmb() {
        return amb;
    }

    public void setAmb(Double[] amb) {
        this.amb = amb;
    }

}
