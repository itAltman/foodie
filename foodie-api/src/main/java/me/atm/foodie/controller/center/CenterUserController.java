package me.atm.foodie.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.atm.common.utils.CookieUtils;
import me.atm.common.utils.DateUtils;
import me.atm.common.utils.JsonResult;
import me.atm.common.utils.JsonUtils;
import me.atm.pojo.Users;
import me.atm.pojo.bo.center.CenterUserBO;
import me.atm.service.center.CenterUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息-个人信息相关接口"})
@RestController
@RequestMapping("/userInfo")
public class CenterUserController {

    private static final String SYSTEM_FACE_URL = "/Users/admin/Documents/test";
    private static final String SYSTEM_FACE_DOMAIN = "http://localhost:8088/";

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "上传用户头像", notes = "上传一个图片文件。格式为png/jpg/jpeg,且大小不能超过500KB", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JsonResult uploadFace(
            @ApiParam(value = "用户id", required = true) @RequestParam String userId,
            @ApiParam(value = "用户头像文件", required = true) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取到文件图片 并检验正确性
        if (file == null) {
            return JsonResult.errorMsg("上传头像文件未空，请重新选择");
        }

        // 2. 判断目录、保存图片到本地
        // face.png -> face  .png
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // /Users/admin/Documents/test/1911064TAGPGNZ9P/xxx-20191119191440.png
        String faceFilePath = SYSTEM_FACE_URL + File.separator + userId + File.separator + fileName + "-" + DateUtils.getCurrentDateString(DateUtils.DATE_PATTERN) + suffix;
        File faceFile = new File(faceFilePath);
        if (faceFile.getParentFile() != null) {
            faceFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(faceFile);
             InputStream inputStream = file.getInputStream()) {
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JsonResult.errorException("文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
            JsonResult.errorException("文件流读取失败");
        }

        // 3. 更新数据库头像url http://localhost:8088/1911064TAGPGNZ9P/xxx-20191119191440.png
        String url = SYSTEM_FACE_DOMAIN + userId + File.separator + fileName + "-" + DateUtils.getCurrentDateString(DateUtils.DATE_PATTERN) + suffix;
        Users userResult = centerUserService.updateUserFace(userId, url);

        // 4. 更新cookie信息
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        return JsonResult.ok();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JsonResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        System.out.println(centerUserBO);

        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return JsonResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某一个属性
            String errorField = error.getField();
            // 验证错误的信息
            String errorMsg = error.getDefaultMessage();

            map.put(errorField, errorMsg);
        }
        return map;
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
