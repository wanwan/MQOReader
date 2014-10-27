package org.zaregoto.examples.android.miku.parser.mqo;

import org.zaregoto.examples.android.miku.util.LogUtil;

import java.io.*;

/**
 * Created by waka on 14/10/23.
 */
public class Parser {

    private File mqofile = null;
    private BufferedInputStream bis = null;

    private Lexer lexer = null;

    public Parser() {
    }

    public void open(String filename) throws FileNotFoundException {

        mqofile = new File(filename);
        bis = new BufferedInputStream(new FileInputStream(mqofile));
        lexer = new Lexer(bis);

        return;
    }

    public void parse() throws IOException {

        MQOElement e;

        while (null != (e = lexer.next())) {
            e.dump();
        }

        return;
    }


    public void close() throws IOException {

        if (null != bis) {
            bis.close();
            bis = null;
        }
        lexer = null;
        mqofile = null;

        return;
    }

    enum MQOElement {
        HEADER_METASEQUOIA("Metasequoia"),
        HEADER_KEYWORD_DOCUMENT("Document"),
        HEADER_FORMAT("Format"),
        HEADER_KEYWORD_VER("Ver"),
        CHUNK_TRIAL_NOISE("TrialNoise"),
        CHUNK_INCLUDE_XML("IncludeXml"),
        CHUNK_SCENE("Scene"),
        CHUNK_BACKIMAGE("BackImage"),
        CHUNK_MATERIAL("Material"),
        CHUNK_OBJECT("Object"),
        CHUNK_OBJECT_VERTEX("vertex"),
        CHUNK_OBJECT_VERTEX_ATTR("vertexattr"),
        CHUNK_OBJECT_VERTEX_ATTR_UID("uid"),
        CHUNK_OBJECT_VERTEX_ATTR_WEIT("weit"),
        CHUNK_OBJECT_VERTEX_ATTR_COLOR("color"),
        CHUNK_OBJECT_BVERTEX("BVertex"),
        CHUNK_OBJECT_BVERTEX_VECTOR("vector"),
        CHUNK_OBJECT_BVERTEX_WEIT("weit"),
        CHUNK_OBJECT_BVERTEX_COLOR("color"),
        CHUNK_OBJECT_FACE("face"),
        CHUNK_BLOB("BLOB"),
        CHUNK_BEGIN("{"),
        CHUNK_END("}"),
        INDEX_ATTR_BEGIN("("),
        INDEX_ATTR_END(")"),
        STRING(null),
        INT(null),
        FLOAT(null),
        BYTE_ARRAY(null);

        String word;
        String value;

        MQOElement(String word) {
            this.word = word;
        }

        public boolean equals(String _word) {
            boolean ret = false;

            if (null == this.word || null == _word) {
                ret = false;
            }
            else {
                if (0 == this.word.toUpperCase().compareTo(_word.trim().toUpperCase())) {
                    ret = true;
                }
                else {
                    ret = false;
                }
            }

            return ret;
        }

        public void setValue(String value) {
            this.value = value;
            return;
        }

        public String getValue() {
            return this.value;
        }

        public void dump() {
            StringBuffer b = new StringBuffer();
            b.append(this.name());
            b.append(" word: " + word);
            b.append(" value: " + value);

            LogUtil.d(b.toString());
            return;
        }
    }
}
