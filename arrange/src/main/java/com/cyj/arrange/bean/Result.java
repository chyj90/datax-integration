package com.cyj.arrange.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * Created by chengyajie on 2021/6/9.
 */
@Getter
@Setter
@Accessors(chain = true)
public class Result {
    int code;

    Object message;

    public Result()
    {
        this.code = HttpStatus.OK.value();
    }
}
