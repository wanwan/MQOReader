package org.zaregoto.examples.android.miku;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import org.zaregoto.mqoparser.model.MQOData;
import org.zaregoto.mqoparser.model.MQOObject;

public class MQORenderer implements GLSurfaceView.Renderer {

	private Context mContext = null;
	private MQOData data = null;


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }


//	private int mWidth = -1;
//	private int mHeight = -1;
//	private int vbo[];
//
//	private static final int baseOrtho = 120;
//
//	public MQORenderer(Context mContext, MQOData data) {
//		this.setmContext(mContext);
//		this.setData(data);
//		return;
//	}
//
//	public void onDrawFrame(GL10 gl10) {
//
//		GL11 gl = (GL11)gl10;
//		gl.glClearColor (0, 0, 1, 1);
//		gl.glClear      (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//
//		// カメラの設定(デフォルト)
//		gl.glMatrixMode(GL11.GL_MODELVIEW);
//		gl.glLoadIdentity();
//
//		// 板ポリゴンの描画
////		gl.glBindBuffer   (GL11.GL_ARRAY_BUFFER, vbo[0]);
////		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
////		gl.glBindBuffer   (GL11.GL_ELEMENT_ARRAY_BUFFER, vbo[1]);
//
////		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo[2]);
////		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
////		gl.glDrawElements (GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_SHORT, 0 );
//
//		gl.glBindBuffer   (GL11.GL_ARRAY_BUFFER, vbo[0]);
//		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
//		gl.glBindBuffer   (GL11.GL_ELEMENT_ARRAY_BUFFER, vbo[1]);
//		gl.glDrawElements (GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_SHORT, 0);
//		//gl.glBindBuffer   (GL11.GL_ELEMENT_ARRAY_BUFFER, vbo[2]);
//		//gl.glDrawElements (GL11.GL_QUADS 3, GL11.GL_UNSIGNED_SHORT, 0 );
//
//
////		renderMain(gl);
//
//		return;
//	}
//
//	private void renderMain(GL10 gl10) {
//
//		GL11 gl = (GL11)gl10;
//
//		ArrayList<MQOObject> objects = null;
//		Iterator<MQOObject> it = null;
//		MQOObject object = null;
//		FloatBuffer fb = null;
//
////		if (data != null && null != gl) {
////			objects = data.getObjects();
////			if (objects != null && objects.size() > 0) {
////				it = objects.iterator();
////				while (it.hasNext()) {
////					object = it.next();
////					fb = object.makeFloatBuffer();
////					gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
////					gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
////					gl.glDrawArrays(GL10.GL_TRIANGLES, 0, object.getVertex());
////				}
////			}
////		}
//
////		if (data != null && null != gl) {
////		objects = data.getObjects();
////		object = objects.get(0);
////		fb = object.makeFloatBuffer();
////		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo[2]);
////		gl.glBufferData(GL11.GL_ARRAY_BUFFER, 4*object.getVertex()*3, fb, GL11.GL_STATIC_DRAW);
////		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
////		gl.glDrawElements (GL11.GL_TRIANGLES, object.getVertex(), GL11.GL_UNSIGNED_SHORT, 0 );
////	}
//
//		return;
//	}
//
//
//	public void onSurfaceChanged(GL10 gl10, int width, int height) {
//
//		this.mWidth = width;
//		this.mHeight = height;
//
//		GL11 gl = (GL11)gl10;
//		float ratio = (float)height / (float)width;
//
//		gl.glViewport (0, 0, width, height);
//		gl.glMatrixMode(GL11.GL_PROJECTION);
//		gl.glLoadIdentity();
//		//gl.glOrthof(-2, 2, -2*ratio , 2*ratio, -2, 2);
//		gl.glOrthof(-baseOrtho, baseOrtho, -baseOrtho*ratio, baseOrtho*ratio, -baseOrtho, baseOrtho);
//
//		return;
//	}
//
//
//	public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
//
//		GL11 gl = (GL11)gl10;
//
//		// 全ての携帯でGL_FLOATがサポートされている(common profile)とは限らない.
//		// 不安ならGL_FIXEDを使うべき.
//		//float[] positions = {1,-1,0, 1,1,0, -1,-1,0, -1,1,0};
//		//FloatBuffer positionsBuffer = FloatBuffer.wrap (positions);
//		int fsize = data.getObjects().get(0).getVertex() * 4;
//		FloatBuffer positionsBuffer = data.getObjects().get(0).makeVertexFloatBuffer();
//
//		//short[] indices = {0,1,2,3};
//		//ShortBuffer indicesBuffer = ShortBuffer.wrap (indices);
//		ShortBuffer triangleIndecisBuffer = data.getObjects().get(0).makeTrianglePolygonShortBuffer();
//		int ssize1 = triangleIndecisBuffer.capacity();
//
//		ShortBuffer quadIndeciesBuffer = data.getObjects().get(0).makeQuadPolygonShortBuffer();
//		int ssize2 = quadIndeciesBuffer.capacity();
//
//		//float[] triangles = {100,-100,0, 100,100,0, -100,-100,0};
//		//FloatBuffer trianglesBuffer = FloatBuffer.wrap(triangles);
//
//		// VertexBufferのサポートはGL11から.
//		vbo = new int[3];
//		gl.glGenBuffers (3, vbo, 0);
//
//		gl.glBindBuffer (GL11.GL_ARRAY_BUFFER, vbo[0]);
//		gl.glBufferData (GL11.GL_ARRAY_BUFFER, fsize, positionsBuffer, GL11.GL_STATIC_DRAW);
//
//		gl.glBindBuffer (GL11.GL_ELEMENT_ARRAY_BUFFER, vbo[1]);
//		gl.glBufferData (GL11.GL_ELEMENT_ARRAY_BUFFER, ssize1, triangleIndecisBuffer, GL11.GL_STATIC_DRAW);
//
//		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, vbo[2]);
//		gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, ssize2, quadIndeciesBuffer, GL11.GL_STATIC_DRAW);
//
//		//gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo[2]);
//		//gl.glBufferData(GL11.GL_ARRAY_BUFFER, 4*9, trianglesBuffer, GL11.GL_STATIC_DRAW);
//
//		gl.glEnableClientState (GL11.GL_VERTEX_ARRAY);
//
//		return;
//	}
//
//	public void setmContext(Context mContext) {
//		this.mContext = mContext;
//	}
//
//	public Context getmContext() {
//		return mContext;
//	}
//
//	public void setData(MQOData data) {
//		this.data = data;
//	}
//
//	public MQOData getData() {
//		return data;
//	}

}
