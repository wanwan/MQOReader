package org.zaregoto.examples.android.miku.parser.mqo;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: waka
 * Date: 5/15/11
 * Time: 6:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetaseqData {

    private String version = null;
    private MetaseqScene scene = null;
    private ArrayList<MetaseqMaterial> materials = null;
    private ArrayList<MetaseqObject> objects = null;


    public MetaseqData() {

        version = new String();

        scene = new MetaseqScene();
        materials = new ArrayList<MetaseqMaterial>();
        objects = new ArrayList<MetaseqObject>();

        return;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MetaseqScene getScene() {
        return scene;
    }

    public void setScene(MetaseqScene scene) {
        this.scene = scene;
    }

    public ArrayList<MetaseqMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<MetaseqMaterial> materials) {
        this.materials = materials;
    }

    public ArrayList<MetaseqObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<MetaseqObject> objects) {
        this.objects = objects;
    }


	public void addMaterials(ArrayList<MetaseqMaterial> materialDatas) {
		if (null != materials && null != materialDatas) {
			materials.addAll(materialDatas);
		}
		return;
	}


	public void addObject(MetaseqObject objectData) {
		if (null != objects && null != objectData) {
			objects.add(objectData);
		}
		
	}


}
