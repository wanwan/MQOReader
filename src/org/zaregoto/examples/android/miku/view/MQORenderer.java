package org.zaregoto.examples.android.miku.view;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import org.zaregoto.mqoparser.model.MQOData;
import org.zaregoto.mqoparser.model.MQOObject;

public class MQORenderer implements GLSurfaceView.Renderer {

    private final String VSHADER_SRC =
            "attribute vec4 aPosition;\n" +
                    "attribute vec2 aUV;\n" +
                    "uniform mat4 uProjection;\n" +
                    "uniform mat4 uView;\n" +
                    "uniform mat4 uModel;\n" +
                    "varying vec2 vUV;\n" +
                    "void main() {\n" +
                    "  gl_Position = uProjection * uView * uModel * aPosition;\n" +
                    "  vUV = aUV;\n" +
                    "}\n";

    private final String FSHADER_SRC =
            "precision mediump float;\n" +
                    "varying vec2 vUV;\n" +
                    "uniform sampler2D uTex;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(uTex, vUV);\n" +
                    "}\n";


	private Context mContext = null;
	private MQOData data = null;

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;
    private float mScreenAspectRatio;


    private float mCameraPosX = 0.0f;
    private float mCameraPosY = 0.0f;
    private float mCameraPosZ = 0.0f;

    private float mCameraDirectionX = 0.0f;
    private float mCameraDirectionY = 0.0f;
    private float mCameraDirectionZ = 1.0f;

    private float mCameraFovDegree = 45;


    private int mPositionHandle;
    private int mProjectionMatrixHandle;
    private int mViewMatrixHandle;
    private int mUVHandle;
    private int mTexHandle;
    private int mModelMatrixHandle;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        int vShader;
        int fShader;
        int program;

        vShader = loadShader(GLES20.GL_VERTEX_SHADER, VSHADER_SRC);
        fShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FSHADER_SRC);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        GLES20.glLinkProgram(program);

        GLES20.glUseProgram(program);

        mPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        mUVHandle = GLES20.glGetAttribLocation(program, "aUV");
        mProjectionMatrixHandle = GLES20.glGetUniformLocation(program, "uProjection");
        mViewMatrixHandle = GLES20.glGetUniformLocation(program, "uView");
        mTexHandle = GLES20.glGetUniformLocation(program, "uTex");
        mModelMatrixHandle = GLES20.glGetUniformLocation(program, "uModel");

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        int _height = height/2;
        mScreenAspectRatio = (float) width / (float) (_height == 0 ? 1 : _height);

        GLES20.glViewport(0, _height / 2, width, _height);

        Matrix.setLookAtM(mViewMatrix, 0, mCameraPosX, mCameraPosY, mCameraPosZ, mCameraDirectionX, mCameraDirectionY, mCameraDirectionZ, 0.0f, 1.0f, 0.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, mCameraFovDegree, mScreenAspectRatio, Z_NEAR, Z_FAR);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

//        mCameraDirectionX = (float) (Math.cos(mRotationAngleXZ)*Math.cos(mRotationAngleY));
//        mCameraDirectionZ = (float) (Math.sin(mRotationAngleXZ)*Math.cos(mRotationAngleY));
//        mCameraDirectionY = (float) Math.sin(mRotationAngleY);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);

//        if (mTextureUpdate && null != mTexture && !mTexture.getPhoto().isRecycled()) {
//            Log.d("", "load texture1");
//            loadTexture(mTexture.getPhoto());
//            mTexture.getPhoto().recycle();
//            mTextureUpdate = false;
//        }

        Matrix.setLookAtM(mViewMatrix, 0, mCameraPosX, mCameraPosY, mCameraPosZ, mCameraDirectionX, mCameraDirectionY, mCameraDirectionZ, 0.0f, 1.0f, 0.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, mCameraFovDegree, mScreenAspectRatio, Z_NEAR, Z_FAR);


//        if (null != mTexture.getElevetionAngle()) {
//            float elevationAngle = mTexture.getElevetionAngle().floatValue();
//            Matrix.rotateM(mModelMatrix, 0, (float) elevationAngle, 0, 0, 1);
//        }
//        if (null != mTexture.getHorizontalAngle()) {
//            float horizontalAngle = mTexture.getHorizontalAngle().floatValue();
//            Matrix.rotateM(mModelMatrix, 0, horizontalAngle, 1, 0, 0);
//        }

        GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, mProjectionMatrix, 0);
        GLES20.glUniformMatrix4fv(mViewMatrixHandle, 1, false, mViewMatrix, 0);

//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mTextures[0]);
//        GLES20.glUniform1i(mTexHandle,0);

//        mShell.draw(mPositionHandle, mUVHandle);


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


    /**
     * デバッグ用 GL エラー判定メソッド
     * @param TAG TAG 出力文字列
     * @param glOperation メッセージ出力文字列
     */
    private static void checkGlError(String TAG, String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
        return;
    }


    private int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


}
