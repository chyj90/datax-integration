package com.cyj.arrange.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * Created by chengyajie on 2021/6/9.
 */
@Getter
@Accessors(chain = true)
public class Result {
    @Setter
    int code;

    Object message;

    public Result()
    {
        this.code = HttpStatus.OK.value();
    }

    public Result setMessage(Object message) {
        if (message instanceof IPage)
        {
            this.message = new Pager((IPage) message);
        }else
        {
            this.message = message;
        }
        return this;
    }
}
