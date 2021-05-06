package cn.carbonface.carboncommon.controller;

import cn.carbonface.carboncommon.dto.ApiResult;
import cn.carbonface.carboncommon.exception.CarbonException;
import cn.carbonface.carboncommon.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


/**
 * classname CommonController
 * Description TODO
 *
 * @author carbonface <553127022@qq.com>
 * Date 2021/4/23 15:35
 * @version v1.0
 */
@RestController
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @PostMapping("file/upload")
    public ApiResult<String> uploadFile(MultipartFile file,boolean temp) throws IOException, CarbonException {
        String objectId = commonService.uploadFile(file, temp);
        return ApiResult.ok(objectId, "上传成功!");
    }

    @GetMapping("file/download")
    public void downloadFile(String fileId) throws IOException, CarbonException {
        commonService.downloadFile(fileId);
    }
}
