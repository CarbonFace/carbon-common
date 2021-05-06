package cn.carbonface.carboncommon.service.impl;

import cn.carbonface.carboncommon.dto.mongodb.MongoFile;
import cn.carbonface.carboncommon.exception.CarbonException;
import cn.carbonface.carboncommon.service.CommonService;
import cn.carbonface.carboncommon.tools.HttpUtil;
import cn.carbonface.carboncommon.tools.MongoGridFSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * classname CommonServiceImpl
 * Description TODO
 *
 * @author carbonface <553127022@qq.com>
 * Date 2021/4/28 14:02
 * @version v1.0
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Transactional
    @Override
    public String uploadFile(MultipartFile multipartFile, boolean temp) throws IOException, CarbonException {
        ObjectId objectId = MongoGridFSUtil.store(multipartFile, temp);
        return objectId.toString();
    }

    @Override
    public void downloadFile(String fileId) throws CarbonException, IOException {
        MongoFile mongoFile = MongoGridFSUtil.getMongoFile(fileId);
        String fileName = mongoFile.getFileName();
        String contentType = mongoFile.getContentType();
        if (contentType == null) {
            contentType = HttpUtil.DEFAULT_CONTENT_TYPE;
        }
        HttpServletResponse response = HttpUtil.getResponse();
        response.setHeader(HttpUtil.CONTENT_TYPE,contentType);
        response.addHeader(HttpUtil.CONTENT_DISPOSITION,HttpUtil.layContentDisposition(fileName));
        InputStream inputStream = MongoGridFSUtil.getFileInputStreamByMongoFile(mongoFile);
        IOUtils.copy(inputStream, HttpUtil.getResponse().getOutputStream());
    }
}
