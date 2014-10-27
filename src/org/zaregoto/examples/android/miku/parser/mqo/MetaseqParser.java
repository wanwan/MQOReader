package org.zaregoto.examples.android.miku.parser.mqo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zaregoto.examples.android.miku.parser.mqo.MetaseqMaterial.SHADER;
import org.zaregoto.examples.android.miku.util.LogUtil;

import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: waka
 * Date: 5/15/11
 * Time: 6:10 AM
 * To change this template use File | Settings | File Templates.
 */
// TODO: たぶん速度的に遅いはず。ループ部分は外に出してしまうことも考えるべきか.
// TODO: chunk 構造で再帰のほうがいいかも. 元の仕様からでは正直任意の chunk が組合せるとは読みとれなかったのでとりあえずこれで。
// TODO: これ automaton で C つかったほうがいいかも。
@Deprecated
public class MetaseqParser {

    enum MODE {
        MODE_METASEQUOIA,
        MODE_FORMAT,
        MODE_SCENE,
        MODE_MATERIAL,
        MODE_OBJECT,
        MODE_NONE,
    }
    

    private BufferedReader br = null;

    private boolean cancel = false;
    private MetaseqData metaseqData = null;

    private MODE mode = MODE.MODE_NONE;

    // pattern matcher for Material
    static private Pattern shaderMatcher = Pattern.compile("shader\\(([0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern vcolMatcher = Pattern.compile("vcol\\(([01])\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern colMatcher = Pattern.compile("col\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)");
    static private Pattern difMatcher = Pattern.compile("dif\\(([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern ambMatcher = Pattern.compile("amb\\(([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern emiMatcher = Pattern.compile("emi\\(([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern spcMatcher = Pattern.compile("spc\\(([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern powerMatcher = Pattern.compile("power\\(([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern texMatcher = Pattern.compile("tex\\((.*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern aplaneMatcher = Pattern.compile("aplane\\((.*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern bumpMatcher = Pattern.compile("bump\\((.*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern proj_typeMatcher = Pattern.compile("proj_type\\(([0-9])\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern proj_posMatcher = Pattern.compile("proj_pos\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern proj_scaleMatcher = Pattern.compile("proj_scale\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern proj_angleMatcher = Pattern.compile("proj_angle\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)", Pattern.CASE_INSENSITIVE);
    // pattern matcher for face
    static private Pattern vMatcher = Pattern.compile("V\\(([0-9\\s]*)\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern mMatcher = Pattern.compile("M\\(-?[0-9]*\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern uvMatcher = Pattern.compile("UV\\(.*\\)", Pattern.CASE_INSENSITIVE);
    static private Pattern col_faceMatcher = Pattern.compile("COL\\(.*\\)", Pattern.CASE_INSENSITIVE);
    

    @SuppressWarnings("unused")
	private MetaseqParser() {
        return;
    }

    public MetaseqParser(String filename) throws FileNotFoundException {

        File file = new File(filename);

        if (file.exists() && file.isFile()) {
            br = new BufferedReader(new FileReader(file));
        }

        metaseqData = new MetaseqData();
        
        return;
    }
    
    
    public MetaseqParser(InputStream is) {
    	br = new BufferedReader(new InputStreamReader(is));
    	
        metaseqData = new MetaseqData();
        
        return;    	
    }


    public MetaseqData parse() {

        String line = null;
        String[] elements = null;

        if (null == br) {
            return null;
        }

        if (null == metaseqData) {
            metaseqData = new MetaseqData();
        }

        try {
        	
        	MetaseqScene sceneData = null;
        	ArrayList<MetaseqMaterial> materialDatas = null;
        	MetaseqObject objectData = null;
        	
            while(!cancel
                  && null != (line = br.readLine()) ) {

                switch (mode) {
                case MODE_METASEQUOIA:
                	
                	mode = MODE.MODE_NONE;
                	break;
                case MODE_FORMAT:
                	
                	mode = MODE.MODE_NONE;
                	break;                	
                case MODE_SCENE:
                	parseScene(br, sceneData);
                	mode = MODE.MODE_NONE;
                	break;                	
                case MODE_MATERIAL:
                	parseMaterial(br, materialDatas);
                	metaseqData.addMaterials(materialDatas);
                	mode = MODE.MODE_NONE;
                	break;
                case MODE_OBJECT:
                	parseObject(br, objectData);
                	metaseqData.addObject(objectData);
                	mode = MODE.MODE_NONE;
                	break;
                case MODE_NONE:
            	default:                	
                    elements = splitChunk(line);
                    if (elements != null && elements.length > 0) {
                        if (elements[0].equalsIgnoreCase("Metasequoia")) {
                        	mode = MODE.MODE_METASEQUOIA;
                        	// 
                        	mode = MODE.MODE_NONE;
                        }
                        else if (elements[0].equalsIgnoreCase("Format")) {
                        	mode = MODE.MODE_FORMAT;
                        	// do something as format
                        	mode = MODE.MODE_NONE;
                        }
                        else if (elements[0].equalsIgnoreCase("Scene")) {
                        	mode = MODE.MODE_SCENE;
                        	sceneData = new MetaseqScene();                        	
                        }
                        else if (elements[0].equalsIgnoreCase("Material")) {
                        	mode = MODE.MODE_MATERIAL;
                        	materialDatas = new ArrayList<MetaseqMaterial>();
                        }
                        else if (elements[0].equalsIgnoreCase("Object")) {
                        	mode = MODE.MODE_OBJECT;                        	
                        	objectData = new MetaseqObject();
                        	String objectName = null;
                        	if (elements.length >= 2) {
                        		objectName = elements[1];
                        		objectData.setName(objectName);
                        	}

                        }
                    }
                    
                    continue;
                }
            }
        }
        catch (IOException e) {
            metaseqData = null;
        }
        finally {
            if (cancel) {
                metaseqData = null;
            }
            cancel = false;
        }

        return metaseqData;
    }


    private void parseScene (BufferedReader br, MetaseqScene sceneData) throws NumberFormatException, IOException {

        String line = null;
        String[] elements = null;

        while (br != null && null != (line = br.readLine())) {
            elements = splitScene(line);
            if (elements != null && elements.length > 0) {
                if (elements[0].equals("}")) {
                    break;
                }
                else if (elements[0].equalsIgnoreCase("pos") && 4 == elements.length) {
                    Double[] pos = new Double[] { Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), Double.parseDouble(elements[3]) };
                    sceneData.setPos(pos);
                }
                else if (elements[0].equalsIgnoreCase("lookat") && 4 == elements.length) {
                    Double[] lookat = new Double[] { Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), Double.parseDouble(elements[3]) };
                    sceneData.setLookat(lookat);
                }
                else if (elements[0].equalsIgnoreCase("head") && 2 == elements.length) {
                    Double head = Double.parseDouble(elements[1]);
                    sceneData.setHead(head);
                }
                else if (elements[0].equalsIgnoreCase("pich") && 2 == elements.length) {
                    Double pich = Double.parseDouble(elements[1]);
                    sceneData.setPich(pich);
                }
                else if (elements[0].equalsIgnoreCase("ortho") && 2 == elements.length) {
                    Integer ortho = Integer.parseInt(elements[1]);
                    sceneData.setOrtho(ortho);
                }
                else if (elements[0].equalsIgnoreCase("zoom2") && 2 == elements.length) {
                    Double zoom2 = Double.parseDouble(elements[1]);
                    sceneData.setZoom2(zoom2);
                }
                else if (elements[0].equalsIgnoreCase("amb") && 4 == elements.length) {
                    Double[] amb = new Double[] { Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), Double.parseDouble(elements[3]) };
                    sceneData.setAmb(amb);
                }
                else {
                    LogUtil.d("something unknown format: " + line);
                }
            }
        }

        return;
    }


    /**
     * 
     * @param br
     * @param materialData
     * @throws IOException
     */
    private void parseMaterial(BufferedReader br, ArrayList<MetaseqMaterial> materialDatas) throws IOException {

        String line = null;
        String[] elements = null;

        if (null == materialDatas) {
        	return;
        }
        
        while (br != null && null != (line = br.readLine())) {
            elements = splitMaterial(line);
            if (elements != null && elements.length > 0) {
                if (elements[0].equals("}")) {
                    break;
                }
                else {
                	
                	MetaseqMaterial material = new MetaseqMaterial();
                	
                    for (String element: elements) {
                        if (element.matches("shader\\(([0-9]*\\.?[0-9]*)\\)")) {
                        	Matcher matcher = shaderMatcher.matcher(element);
                        	if (matcher.find()) {
                        		int shader = Integer.parseInt(matcher.group(1));
                        		switch (shader) {
                        		case 0:
                        			material.setShader(SHADER.SHADER_CLASSIC);
                        			break;
                        		case 1:
                        			material.setShader(SHADER.SHADER_CONSTANT);                        			
                        			break;
                        		case 2:
                        			material.setShader(SHADER.SHADER_LAMBERT);                        			
                        			break;
                        		case 3:
                        			material.setShader(SHADER.SHADER_PHONG);                        			
                        			break;
                        		case 4:
                        			material.setShader(SHADER.SHADER_BLINN);                        			
                        			break;
                        		}                        		
                        	}
                        }
                        else if (element.matches("vcol\\(([01])\\)")) {
                        	
                        }
                        else if (element.matches("col\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("dif\\(([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("amb\\(([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("emi\\(([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("spc\\(([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("power\\(([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("tex\\((.*)\\)")) {
                        	
                        }
                        else if (element.matches("aplane\\((.*)\\)")) {
                        	
                        }
                        else if (element.matches("bump\\((.*)\\)")) {
                        	
                        }
                        else if (element.matches("proj_type\\(([0-9])\\)")) {
                        	
                        }
                        else if (element.matches("proj_pos\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("proj_scale\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                        else if (element.matches("proj_angle\\(([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\s([0-9]*\\.?[0-9]*)\\)")) {
                        	
                        }
                    }
                    
                    materialDatas.add(material);
                }
            }
        }        
        
        return;
    }

    
    private void parseVertex(BufferedReader br, MetaseqPolygon polygons) throws NumberFormatException, IOException {

        String line = null;
        String[] elements = null;    	
    	
    	if (null == polygons) {
    		return;
    	}
    	
    	while (br != null && null != (line = br.readLine())) {
            elements = splitVertex(line);
            if (elements != null && elements.length > 0) {
                if (elements[0].equals("}")) {
                    break;
                }
                else {

                	if (elements.length == 3) {
                		float x = Float.parseFloat(elements[0]);
                		float y = Float.parseFloat(elements[1]);
                		float z = Float.parseFloat(elements[2]);
                    	polygons.addVertex(x, y, z);         		
                	}

                }
            }
    	}
    	return;
    }

    
    private void parseFace(BufferedReader br, MetaseqPolygon polygons) throws IOException {
	
        String line = null;
        String[] elements = null;
        Short[] indexes = null;
    	
    	if (null == polygons) {
    		return;
    	}
    	
    	while (br != null && null != (line = br.readLine())) {
            elements = splitFace(line);
            if (elements != null && elements.length > 0) {
                if (elements[0].equals("}")) {
                    break;
                }
                else {

                	int size = Integer.parseInt(elements[0]);
                	indexes = new Short[size];

                	for (int i = 1; i < elements.length; i++) {
                		if (elements[i].equals("V")) {
                			i++;
                			for (int j = 0; j < size; j++ ) {
                				indexes[j] = Short.parseShort(elements[i+j]);
                			}
                			polygons.addPolygon(indexes);
                			i = i+size;
                		}
                		else if (elements[i].equals("M")) {
                			
                		}
                		else if (elements[i].equals("UV")) {
                			
                		}
                		else if (elements[i].equals("COL")) {
                			
                		}
                	}
                }
            }
    	}
    	return;    	
		
	}    
    

    // TODO: 階層をサポートする形のほうが望ましいような気がするが、どういうかたちになるのか仕様から読み取れないのでとりあえず、これで。
    private void parseObject(BufferedReader br, MetaseqObject objectData) throws IOException {

        String line = null;
        String[] elements = null;

        if (null == objectData) {
        	return;
        }
        
        while (br != null && null != (line = br.readLine())) {
            elements = splitObject(line);
            if (elements != null && elements.length > 0) {
                if (elements[0].equals("}")) {
                    break;
                }
                else {

                	if (elements[0].equalsIgnoreCase("depth")) {
                		
                	}
                	else if (elements[0].equalsIgnoreCase("folding")) {
                		
                	}
                	else if (elements[0].equalsIgnoreCase("scale")) {
                		
                	}
                	else if (elements[0].equalsIgnoreCase("rotation")) {
                		
                	}
                	else if (elements[0].equalsIgnoreCase("translation")) {
                		
                	}
                	else if (elements[0].equalsIgnoreCase("patch")) {
                		
                	}                		
                	else if (elements[0].equalsIgnoreCase("segment")) {
                		
                	}   
                	else if (elements[0].equalsIgnoreCase("visible")) {
                		
                	}                 		
                	else if (elements[0].equalsIgnoreCase("locking")) {
                		
                	}                 		
                	else if (elements[0].equalsIgnoreCase("shading")) {
                		
                	} 
                	else if (elements[0].equalsIgnoreCase("facet")) {
                		
                	} 
                	else if (elements[0].equalsIgnoreCase("color")) {
                		
                	}
                	else if (elements[0].equalsIgnoreCase("color_type")) {
                		
                	} 
                	else if (elements[0].equalsIgnoreCase("mirror")) {
                		
                	} 
                	else if (elements[0].equalsIgnoreCase("mirror_axis")) {
                		
                	}                 		
                	else if (elements[0].equalsIgnoreCase("mirror_dis")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("lathe")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("lathe_axis")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("lathe_seg")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("vertex")) {
                		if (elements.length >= 2) {
                			Integer num = Integer.parseInt(elements[1]);
                			objectData.setVertex(num);
                		}
                		MetaseqPolygon polygons = objectData.getPolygons();
                		parseVertex(br, polygons);
                	}  
                	else if (elements[0].equalsIgnoreCase("BVertex")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("face")) {
                		if (elements.length >= 2) {
                			Integer num = Integer.parseInt(elements[1]);
                			objectData.setFace(num);
                		}          
                		MetaseqPolygon polygons = objectData.getPolygons();
                		parseFace(br, polygons);                		
                	}  
                	else if (elements[0].equalsIgnoreCase("mirror_axis")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("mirror_axis")) {
                		
                	}  
                	else if (elements[0].equalsIgnoreCase("mirror_axis")) {
                		
                	}                  		
                	
                }
            }
        }        
    	
        return;
    }


	private String[] splitMaterial(String line) {

        String[] elements = null;

        if (null != line && line.trim().length() > 0) {
            elements = line.trim().split("\\s+.*\\(?.*\\)?\\s?");
        }

        return elements;
    }
    
    
    private String[] splitChunk(String line) {

        String[] elements = null;    	

        if (null != line && line.trim().length() > 0) {
            elements = line.trim().split("\\s+");
        }

        return elements;    	
    	
    }

    
    private String[] splitScene(String line) {

        String[] elements = null;    	

        if (null != line && line.trim().length() > 0) {
            elements = line.trim().split("\\s+");
        }
        
        return elements; 	
    }    
    
    
    private String[] splitObject(String line) {

        String[] elements = null;

        if (null != line && line.trim().length() > 0) {
            elements = line.trim().split("\\s+");
        }

        return elements;
    }

    
    private String[] splitVertex(String line) {

        String[] elements = null;

        if (null != line && line.trim().length() > 0) {
            elements = line.trim().split("\\s+");
        }

        return elements;
    }        

    
	private String[] splitFace(String line) {

        ArrayList<String> elements = null;
        StringTokenizer tokenizer = null;
        String delimiter = " \t()";

        if (null != line && line.trim().length() > 0) {
        	line = line.trim();
        	tokenizer = new StringTokenizer(line, delimiter);
        	elements = new ArrayList<String>();
        	while (tokenizer.hasMoreElements()) {
        		elements.add(tokenizer.nextToken());
        	}
        }

        return elements.toArray(new String[0]);
    }    
    
}
