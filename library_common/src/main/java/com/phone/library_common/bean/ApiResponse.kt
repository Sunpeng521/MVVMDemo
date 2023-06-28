package com.phone.library_common.bean

import com.phone.library_common.common.ApiException
import java.io.Serializable

/**
 * des Api描述类，用于承载业务信息以及基础业务逻辑判断
 * @date 2020/7/5
 * @author zs
 */
class ApiResponse<T> : Serializable {

    var data: T? = null

    /**
     * 业务信息
     */
    var errorMsg = ""

    /**
     * 业务code
     */
    var errorCode = -1

    var error: Throwable? = null


    /**
     * 如果服务端data肯定不为null，直接将data返回。
     * 假如data为null证明服务端出错,这种错误已经产生并且不可逆，
     * 客户端只需保证不闪退并给予提示即可
     */
    fun data(): T {
        when (errorCode) {
            //请求成功
            0, 200 -> {
                return data!!
            }
            //未登陆请求需要用户信息的接口
            -1001 -> {
                throw ApiException(errorMsg, errorCode)
            }
            //登录失败
            -1 -> {
                throw ApiException(errorMsg, errorCode)
            }
        }
        //其他错误
        throw ApiException(errorMsg, errorCode)
    }

}