package icu.zxb996.mp.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 统一返回处理
 *
 * @author Gavin Zhang
 * @date 2023/1/10 19:33
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "统一返回处理")
public class ResultVO<T> implements Serializable {

    private static final int CODE_SUCCESS = 200;

    private static final int CODE_ERROR = 500;

    private Integer code;

    private String message;

    private T data;

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.code = CODE_SUCCESS;
        result.data = data;
        result.message = "成功";
        return result;
    }

    public static <T> ResultVO<T> success(String message, T data) {
        ResultVO<T> result = new ResultVO<>();
        result.code = CODE_SUCCESS;
        result.data = data;
        result.message = message;
        return result;
    }

    public static <T> ResultVO<T> error(Integer code, String message) {
        ResultVO<T> result = new ResultVO<>();
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> ResultVO<T> error(Integer code, String message, T data) {
        ResultVO<T> result = new ResultVO<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> ResultVO<T> error(String message) {
        ResultVO<T> result = new ResultVO<>();
        result.code = CODE_ERROR;
        result.message = message;
        return result;
    }

    public static <T> ResultVO<T> error(ResultVO<T> resultVO) {
        return error(resultVO.getCode(), resultVO.getMessage());
    }
}
