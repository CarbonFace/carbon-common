package cn.carbonface.carboncommon.service;

import cn.carbonface.carboncommon.exception.CarbonException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * classname CommonService
 * Description TODO
 *
 * @author carbonface <553127022@qq.com>
 * Date 2021/4/28 14:02
 * @version v1.0
 */
public interface CommonService {

    String uploadFile(MultipartFile multipartFile,boolean temp) throws IOException, CarbonException;

    void downloadFile(String fileId) throws CarbonException, IOException;
}
