package ind.xyz.mywebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class  Result<T> implements Serializable {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private boolean status;


    private Result(ResultCode resultCode,T data,boolean status){
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
        this.status=status;
    }

    /**
     * 无数据成功返回
     *
     * @return
     */
    public static  <T>Result success(){
        return new Result<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),null,true);
    }
    /**
     * 带数据返回
     */
    public static <T> Result success(T data){
        return new Result<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),data,true);
    }

    /**
     * 失败
     */
    public static  <T>Result fail(){
        return new Result<T>(ResultCode.FAIL.getCode(),ResultCode.FAIL.getMessage(), null,false);
    }
    /**
     * 失败
     */
    public static <T> Result fail(T data){
        return new Result<T>(ResultCode.FAIL.getCode(),ResultCode.FAIL.getMessage(), data,false);
    }


    @Override
    public String toString() {
        return "Result [code=" + code + ", message=" + message + ", data=" + data + "]";
    }
}