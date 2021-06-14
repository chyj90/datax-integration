package com.cyj.arrange.util;

import com.cyj.arrange.bean.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by chengyajie on 2021/6/9.
 */
public class ResponseUtil {
    public static void writeResult(HttpServletResponse response, HttpStatus httpStatus,Object message)
    {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = null;
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result result = new Result();
        result.setCode(httpStatus.value());
        result.setMessage(message);
        try {
            writer = response.getWriter();
            mapper.writeValue(writer, result);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
