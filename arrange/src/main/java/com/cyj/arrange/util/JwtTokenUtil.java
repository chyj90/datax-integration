package com.cyj.arrange.util;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by chengyajie on 2021/6/9.
 */
public class JwtTokenUtil {
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "123456";
    private static String userRoleKey = "userRole";
    private static Gson gson;
    public static final String TOKEN_KEY="Access-Token";

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new TypeAdapter<Number>() {
                    @Override
                    public void write(JsonWriter out, Number value) throws IOException {
                        out.value(value);
                    }

                    @Override
                    public Number read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        try {
                            String result = in.nextString();
                            if ("".equals(result)) {
                                return null;
                            }
                            return Integer.parseInt(result);
                        } catch (NumberFormatException e) {
                            throw new JsonSyntaxException(e);
                        }
                    }
                }).create();
    }
    public static Gson gson()
    {
        return gson;
    }

    public String createToken(String userName) {
        String token = Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    public static String createToken(String userName, String role) {
        String token = Jwts.builder().setSubject(userName)
                .claim(userRoleKey, role)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    public static String getUserNameFromToken(String token) {
        String userName = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }

    public static String getUserRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody();
        return claims.get(userRoleKey).toString();
    }

    public static boolean validate(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr))
        {
            return false;
        }
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }
}
