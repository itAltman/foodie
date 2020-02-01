package me.atm.foodie.interceptor;

import me.atm.common.utils.JsonResult;
import me.atm.common.utils.JsonUtils;
import me.atm.common.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户token拦截
 *
 * @author Altman
 * @date 2020/01/15
 **/
public class UserTokenInterceptor implements HandlerInterceptor {
    @Resource
    private RedisOperator redisOperator;

    private static final String REDIS_USER_TOKEN = "redis_user_token";

    /**
     * 进入controller之前拦截
     *
     * @date 2020/1/15
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(userToken)) {
            returnErrorResponse(response,JsonResult.errorMsg("请登录..."));
            return false;
        } else {
            String redisUserToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(redisUserToken)) {
                returnErrorResponse(response,JsonResult.errorMsg("请登录..."));
                return false;
            } else {
                if (!redisUserToken.equals(userToken)) {
                    returnErrorResponse(response,JsonResult.errorMsg("信息被篡改..."));
                    return false;
                }
            }
        }
        return true;
    }
    
    public void returnErrorResponse(HttpServletResponse response, JsonResult result) {
        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成controller之后，渲染视图前拦截。
     *
     * @date 2020/1/15
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完成controller之后，渲染视图后拦截。
     *
     * @date 2020/1/15
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
