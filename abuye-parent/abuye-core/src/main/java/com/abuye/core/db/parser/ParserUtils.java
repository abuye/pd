package com.abuye.core.db.parser;

public class ParserUtils {
    public static String removeChar(String s, char ignore){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);
            if(ignore != x){
                sb.append(x);
            }
        }
        return sb.toString();
    }
}
