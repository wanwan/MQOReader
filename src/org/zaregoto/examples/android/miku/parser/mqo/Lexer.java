package org.zaregoto.examples.android.miku.parser.mqo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by waka on 14/10/26.
 */
public class Lexer {

    enum LEXER_STS {
        CURRENT_NOT_DEFINED,
        CURRENT_IN_WORD,;
    }

    BufferedReader br = null;

    public Lexer(InputStream is) {
        this.br = new BufferedReader(new InputStreamReader(is));
    }

    public Parser.MQOElement next() throws IOException {

        Parser.MQOElement next = null;
        int c;
        StringBuffer buf = null;
        LEXER_STS sts = LEXER_STS.CURRENT_NOT_DEFINED;

        determine_element:
        while (true) {

            br.mark(1);
            if (0 < (c = br.read())) {
                switch (sts) {
                    case CURRENT_NOT_DEFINED:
                        if (isSeparater(c)) {
                            //skip
                        }
                        else if (isChunkSeparaterIn(c)) {
                            buf = new StringBuffer();
                            buf.append(Character.toChars(c));
                            next = createMQOElement(buf);
                            break determine_element;
                        }
                        else if (isChunkSeparaterOut(c)) {
                            buf = new StringBuffer();
                            buf.append(Character.toChars(c));
                            next = createMQOElement(buf);
                            break determine_element;
                        }
                        else if (isIndexSeparaterIn(c)) {
                            buf = new StringBuffer();
                            buf.append(Character.toChars(c));
                            next = createMQOElement(buf);
                            break determine_element;
                        }
                        else if (isIndexSeparaterOut(c)) {
                            buf = new StringBuffer();
                            buf.append(Character.toChars(c));
                            next = createMQOElement(buf);
                            break determine_element;
                        }
                        else {
                            sts = LEXER_STS.CURRENT_IN_WORD;
                            buf = new StringBuffer();
                            buf.append(Character.toChars(c));
                        }
                        break;
                    case CURRENT_IN_WORD:
                        if (isWordChar(c)) {
                            buf.append(Character.toChars(c));
                        }
                        else {
                            br.reset();
                            break determine_element;
                        }
                        break;
                    default:
                        break;
                }
            } else {
                break;
            }
        }

        if (LEXER_STS.CURRENT_IN_WORD == sts) {
            next = createMQOElement(buf);
        }

        return next;
    }


    private Parser.MQOElement createMQOElement(StringBuffer buf) {

        String word = buf.toString().trim();
        Parser.MQOElement element = null;

        search:
        for (Parser.MQOElement e : Parser.MQOElement.values()) {
            if (e.equals(word)) {
                element = e;
                break search;
            }
        }

        if (null == element) {
            String regex = "\".*\"";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(word);
            if (m.find()) {
                element = Parser.MQOElement.STRING;
                element.setValue(word);
            }
        }

        if (null == element) {
            String regex = "^[-]?[0-9]+$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(word);
            if (m.find()) {
                element = Parser.MQOElement.INT;
                element.setValue(word);
            }
        }

        if (null == element) {
            String regex = "^[-]?[0-9]*.[0-9]*$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(word);
            if (m.find()) {
                element = Parser.MQOElement.FLOAT;
                element.setValue(word);
            }
        }

        if (null == element) {
            element = Parser.MQOElement.BYTE_ARRAY;
            element.setValue(word);
        }

        return element;
    }

    private boolean isWordChar(int c) {

        boolean ret = false;

        if (isSeparater(c)) {
            ret = false;
        }
        else {
            ret = true;
        }
        return ret;
    }

    private boolean isSeparater(int c) {

        boolean ret = false;

        switch (c) {
            case ' ':
            case '\n':
            case '\r':
                ret = true;
                break;
            default:
                break;
        }
        return ret;
    }

    private boolean isChunkSeparaterIn(int c) {

        boolean ret = false;

        switch (c) {
            case '{':
                ret = true;
                break;
            default:
                break;
        }
        return ret;
    }

    private boolean isChunkSeparaterOut(int c) {

        boolean ret = false;

        switch (c) {
            case '}':
                ret = true;
                break;
            default:
                break;
        }
        return ret;
    }

    private boolean isIndexSeparaterIn(int c) {

        boolean ret = false;

        switch (c) {
            case '(':
                ret = true;
                break;
            default:
                break;
        }
        return ret;
    }

    private boolean isIndexSeparaterOut(int c) {

        boolean ret = false;

        switch (c) {
            case ')':
                ret = true;
                break;
            default:
                break;
        }
        return ret;
    }

}
