//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamsText {
    public static final int TYPE_URL = 0;
    public static final int TYPE_STRING = 1;
    private ArrayList<ParamsText.Param> strsList = new ArrayList();
    private HashMap<String, String> paramsMap = new HashMap();
    private int mType = 0;

    public ParamsText() {
    }

    public boolean setText(String str) {
        boolean bol = false;
        this.strsList.clear();
        Pattern pattern = Pattern.compile("[\\\\&\\?]{0,1}\\[([A-Za-z0-9=\\-_]*?)\\]");
        Matcher matcher = pattern.matcher(str);
        int start = 0;

        ParamsText.Param p;
        while(matcher.find()) {
            bol = true;
            if(!matcher.group().startsWith("\\")) {
                p = new ParamsText.Param();
                p.type = 0;
                p.value = str.substring(start, matcher.start());
                this.strsList.add(p);
                if(matcher.groupCount() > 0) {
                    ParamsText.Param mp = new ParamsText.Param();
                    if(matcher.group().startsWith("&")) {
                        mp.div = "&";
                    }

                    if(matcher.group().startsWith("?")) {
                        mp.div = "?";
                    }

                    mp.type = 1;
                    mp.value = matcher.group(1);
                    if(mp.value.indexOf("=") >= 0) {
                        String[] ss = mp.value.split("=");
                        mp.key = ss[0];
                        mp.value = ss[1];
                    }

                    this.strsList.add(mp);
                }

                start = matcher.end();
            }
        }

        p = new ParamsText.Param();
        p.type = 0;
        p.value = str.substring(start, str.length());
        this.strsList.add(p);
        return bol;
    }

    public void putParam(String key, String value) {
        this.paramsMap.put(key, value);
    }

    public void clearParam() {
        this.paramsMap.clear();
    }

    public String toString() {
        int toolsize = 0;
        String str = this.toOneString();

        while(this.setText(str)) {
            str = this.toOneString();
            ++toolsize;
            if(toolsize >= 4) {
                break;
            }
        }

        return str;
    }

    private String toOneString() {
        StringBuffer sb = new StringBuffer();
        Iterator var2 = this.strsList.iterator();

        while(true) {
            while(var2.hasNext()) {
                ParamsText.Param p = (ParamsText.Param)var2.next();
                if(p.type == 0) {
                    sb.append(p.value);
                } else if(!this.paramsMap.containsKey(p.value)) {
                    sb.append("");
                } else {
                    if(this.mType == 0 && p.div != null && (p.div.equals("&") || p.div.equals("?"))) {
                        if(sb.toString().indexOf("?") > 0) {
                            sb.append("&");
                        } else {
                            sb.append("?");
                        }
                    } else if(p.div != null) {
                        sb.append(p.div);
                    }

                    sb.append(p.toString((String)this.paramsMap.get(p.value)));
                }
            }

            return sb.toString();
        }
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    private class Param {
        public int type;
        public String key;
        public String div;
        public String value;

        private Param() {
        }

        public String toString(String value) {
            return this.key != null?this.key + "=" + value:value;
        }

        public String toString() {
            return this.value;
        }
    }
}
