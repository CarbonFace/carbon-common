package cn.carbonface.carboncommon.dto.mongodb;

import org.bson.BSON;
import org.bson.BsonBinary;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * classname MongoTempFileBlock
 * Description TODO
 *
 * @author carbonface <553127022@qq.com>
 * Date 2021/4/26 14:50
 * @version v1.0
 */
@Document("file")
public class MongoFile implements Serializable {

    private static final long   serialVersionUID    = -1977734149662640654L;

    public static final String  MONGO_ID            = "_id";
    public static final String EXPIRATION = "expiration";

    public static final long    FILE_EXPIRE_TIME    = 120 * 1000;

    public static final long    BIG_FILE_SIZE       = 15 * 1024 * 1024;         //15MB

    @MongoId
    private ObjectId id;

    private ObjectId fileId;

    private String fileName;

    private Date uploadDate;

    private String contentType;

    private Date expiration;

    private Boolean bigFile;

    private Binary binary;

    public MongoFile(MultipartFile file) throws IOException {
        this(file,false);
    }

    public MongoFile(MultipartFile file, boolean temp) throws IOException {
        long fileLength = file.getSize();
        long l = file.getResource().contentLength();
        contentType = file.getContentType();
        fileName = file.getOriginalFilename();
        long currentTimeMillis = System.currentTimeMillis();
        uploadDate = new Date(currentTimeMillis);
        if (temp){
            expiration = new Date(currentTimeMillis + FILE_EXPIRE_TIME);
        }
        if (fileLength > BIG_FILE_SIZE) {
            bigFile = true;
        } else {
            bigFile = false;
            binary = new Binary(file.getBytes());
        }
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Boolean isBigFile() {
        return bigFile;
    }

    public void setBigFile(Boolean bigFile) {
        this.bigFile = bigFile;
    }

    public Binary getBinary() {
        return binary;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }
}
