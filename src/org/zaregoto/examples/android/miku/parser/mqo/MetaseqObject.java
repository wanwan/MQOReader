package org.zaregoto.examples.android.miku.parser.mqo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: waka
 * Date: 5/15/11
 * Time: 6:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetaseqObject {

   enum SHADING {
        FLAT_SHADING,
        GLOW_SHADING,
    }

    enum COLOR_TYPE {
        USE_ENVIRONMENT,
        USE_OBJECT_SPECIFIED,
    }

    enum MIRROR_TYPE {
        MIRROR_TYPE_NONE,
            MIRROR_TYPE_SEPARATE_LEFT_RIGHT,
            MIRROR_TYPE_NOT_SEPARATE,
            MIRROR_TYPE_UNDEFINED,
    }

    enum MIRROR_AXIS {
        MIRROR_AXIS_X,
            MIRROR_AXIS_Y,
            MIRROR_AXIS_Z,
            MIRROR_AXIS_UNDEFINED,
    }

    enum LATHE_TYPE {
        LATHE_TYPE_NONE,
            LATHE_TYPE_BOTH_FACE,
            LATHE_TYPE_UNDEFINED,
    }

    enum LATHE_AXIS {
        LATHE_AXIS_X,
            LATHE_AXIS_Y,
            LATHE_AXIS_Z,
            LATHE_AXIS_UNDEFINED,
    }


    private String name = null;
    private Integer depth = null;
    private Integer foldeing = null;
    private Double[] scale = null;
    private Double[] rotation = null;
    private Double[] translation = null;
    private Double patch = null;
    private Double segment = null;
    private Boolean visible = null;
    private Boolean locking = null;
    private SHADING shading = null;
    private Integer facet = null;
    private Double[] color;
    private COLOR_TYPE colorType = null;
    private MIRROR_TYPE mirrorType = null;
    private MIRROR_AXIS mirrorAxis = null;
    private Double mirrorDis = null;
    private LATHE_TYPE latheType = null;
    private LATHE_AXIS latheAxis = null;
    private Integer latheSig = null;
    private Integer vertex = null;
    private Integer bVertex = null;
    private Integer face = null;

    private MetaseqPolygon polygons = null;

    public MetaseqObject() {
    	polygons = new MetaseqPolygon();
        return;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getFoldeing() {
        return foldeing;
    }

    public void setFoldeing(Integer foldeing) {
        this.foldeing = foldeing;
    }

    public Double[] getScale() {
        return scale;
    }

    public void setScale(Double[] scale) {
        this.scale = scale;
    }

    public Double[] getRotation() {
        return rotation;
    }

    public void setRotation(Double[] rotation) {
        this.rotation = rotation;
    }

    public Double[] getTranslation() {
        return translation;
    }

    public void setTranslation(Double[] translation) {
        this.translation = translation;
    }

    public Double getPatch() {
        return patch;
    }

    public void setPatch(Double patch) {
        this.patch = patch;
    }

    public Double getSegment() {
        return segment;
    }

    public void setSegment(Double segment) {
        this.segment = segment;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getLocking() {
        return locking;
    }

    public void setLocking(Boolean locking) {
        this.locking = locking;
    }

    public SHADING getShading() {
        return shading;
    }

    public void setShading(SHADING shading) {
        this.shading = shading;
    }

    public Integer getFacet() {
        return facet;
    }

    public void setFacet(Integer facet) {
        this.facet = facet;
    }

    public Double[] getColor() {
        return color;
    }

    public void setColor(Double[] color) {
        this.color = color;
    }

    public COLOR_TYPE getColorType() {
        return colorType;
    }

    public void setColorType(COLOR_TYPE colorType) {
        this.colorType = colorType;
    }

    public MIRROR_TYPE getMirrorType() {
        return mirrorType;
    }

    public void setMirrorType(MIRROR_TYPE mirrorType) {
        this.mirrorType = mirrorType;
    }

    public MIRROR_AXIS getMirrorAxis() {
        return mirrorAxis;
    }

    public void setMirrorAxis(MIRROR_AXIS mirrorAxis) {
        this.mirrorAxis = mirrorAxis;
    }

    public Double getMirrorDis() {
        return mirrorDis;
    }

    public void setMirrorDis(Double mirrorDis) {
        this.mirrorDis = mirrorDis;
    }

    public LATHE_TYPE getLatheType() {
        return latheType;
    }

    public void setLatheType(LATHE_TYPE latheType) {
        this.latheType = latheType;
    }

    public LATHE_AXIS getLatheAxis() {
        return latheAxis;
    }

    public void setLatheAxis(LATHE_AXIS latheAxis) {
        this.latheAxis = latheAxis;
    }

    public Integer getLatheSig() {
        return latheSig;
    }

    public void setLatheSig(Integer latheSig) {
        this.latheSig = latheSig;
    }

    public Integer getVertex() {
        return vertex;
    }

    public void setVertex(Integer vertex) {
        this.vertex = vertex;
    }

    public Integer getbVertex() {
        return bVertex;
    }

    public void setbVertex(Integer bVertex) {
        this.bVertex = bVertex;
    }

    public Integer getFace() {
        return face;
    }

    public void setFace(Integer face) {
        this.face = face;
    }


	public void setPolygons(MetaseqPolygon polygons) {
		this.polygons = polygons;
	}


	public MetaseqPolygon getPolygons() {
		return polygons;
	}
	
	
	// TODO: これなんとかならんかな. format 的に vertex size { 形式なんで, polygon を Array で覚えるのやめるべきかも.
	public FloatBuffer makeVertexFloatBuffer() {

		int size = -1;
		float[] expands = null;
		ByteBuffer bb = null;
		FloatBuffer fb = null;
		MetaseqPolygon.Vertex vertex = null;
		int i = 0;

		if (null != polygons ) {
			size = polygons.getVertexes().size() * 3;
			expands = new float[size];
		
			Iterator<MetaseqPolygon.Vertex> it = polygons.getVertexes().iterator();
			while (it.hasNext()) {
				vertex = it.next();
				expands[i+0] = vertex.getX();
				expands[i+1] = vertex.getY(); 
				expands[i+2] = vertex.getZ();
				i += 3;
			}
			
			bb = ByteBuffer.allocateDirect(4*size); // sizeof(float) * num of points
			bb.order(ByteOrder.nativeOrder());
			fb = bb.asFloatBuffer();
			fb.put(expands);
			fb.position(0);
		}
		
		return fb;
	}
	

	// TODO: なんとかならんかな、これ。
	public ShortBuffer makeTrianglePolygonShortBuffer() {

		int size = -1;
		ArrayList<Short> expands = null;
		Short[] _expands = null;
		short[] __expands = null;
		ByteBuffer bb = null;
		ShortBuffer sb = null;
		MetaseqPolygon.Polygon polygon = null;

		if (null != polygons ) {
			expands = new ArrayList<Short>();
		
			Iterator<MetaseqPolygon.TrianglePolygon> it = polygons.getTriangles().iterator();
			while (it.hasNext()) {
				polygon = it.next();
				expands.addAll(Arrays.asList(polygon.getIndexes()));
			}
			
			_expands = expands.toArray(new Short[0]);
			if (null != _expands) {
				__expands = new short[_expands.length];
				for (int i = 0; i < _expands.length; i++) {
					__expands[i] = _expands[i]; 
				}
			}
			size = expands.size();
			bb = ByteBuffer.allocateDirect(2*size); // sizeof(short) * num of points
			bb.order(ByteOrder.nativeOrder());
			sb = bb.asShortBuffer();
			sb.put(__expands);
			sb.position(0);
		}
		
		return sb;
	}


	public ShortBuffer makeQuadPolygonShortBuffer() {

		int size = -1;
		ArrayList<Short> expands = null;
		Short[] _expands = null;
		short[] __expands = null;
		ByteBuffer bb = null;
		ShortBuffer sb = null;
		MetaseqPolygon.Polygon polygon = null;

		if (null != polygons ) {
			expands = new ArrayList<Short>();
		
			Iterator<MetaseqPolygon.QuadPolygon> it = polygons.getQuads().iterator();
			while (it.hasNext()) {
				polygon = it.next();
				expands.addAll(Arrays.asList(polygon.getIndexes()));
			}
			
			_expands = expands.toArray(new Short[0]);
			if (null != _expands) {
				__expands = new short[_expands.length];
				for (int i = 0; i < _expands.length; i++) {
					__expands[i] = _expands[i]; 
				}
			}
			size = expands.size();			
			bb = ByteBuffer.allocateDirect(2*size); // sizeof(short) * num of points
			bb.order(ByteOrder.nativeOrder());
			sb = bb.asShortBuffer();
			sb.put(__expands);
			sb.position(0);
		}
		
		return sb;
		
	}	
	
}
