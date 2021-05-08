package cn.carbonface.carboncommon.tools;

import cn.carbonface.carboncommon.dto.RetCode;
import cn.carbonface.carboncommon.dto.mongodb.MongoFile;
import cn.carbonface.carboncommon.exception.CarbonException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * classname MongoGridFSUtil
 * Description MongoGridFSUtil for GridFS operation of service
 *
 * @author carbonface <553127022@qq.com>
 * Date 2021/4/23 15:48
 * @version v1.0
 */
@Component
@Slf4j
public class MongoGridFSUtil {

    public static final String mongoFileDatabase = "mongoFile";
    public static final String CONTENT_TYPE = "_contentType";
    public static final String FS_FILES = "fs.files";
    public static final String FS_CHUNKS = "fs.chunks";
    public static final String FILES_ID = "files_id";

    public static final String LENGTH = "length";
    public static final String CHUNK_SIZE = "chunkSize";



    public static MongoTemplate mongoTemplate;
    public static GridFsTemplate gridFsTemplate;
    public static MongoDbFactory mongoDbFactory;

    public MongoGridFSUtil(MongoProperties mongoProperties, MongoConverter mongoConverter) {
        initGridFsTemplate(mongoProperties, mongoConverter);
    }

    private void initGridFsTemplate(MongoProperties mongoProperties, MongoConverter mongoConverter) {
        String host = mongoProperties.getHost();
        Integer port = mongoProperties.getPort();
        String username = mongoProperties.getUsername();
        String dbName = mongoProperties.getDatabase();
        String gridFsDatabase = mongoProperties.getGridFsDatabase();
        char[] password = mongoProperties.getPassword();
        ServerAddress serverTempAddress = new ServerAddress(host, port);
        List<ServerAddress> addrs = new ArrayList<>();
        addrs.add(serverTempAddress);
        // username password and dbName
        MongoCredential mongoCredential = MongoCredential.createCredential(username, dbName, password);
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        MongoClientOptions mongoClientOptions = builder.build();
        //get authorized connection with mongoDB
        MongoClient mongoClient = new MongoClient(addrs, mongoCredential, mongoClientOptions);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, gridFsDatabase == null ? mongoFileDatabase : gridFsDatabase);
        MongoGridFSUtil.mongoDbFactory = mongoDbFactory;
        gridFsTemplate = new GridFsTemplate(mongoDbFactory, mongoConverter);
        mongoTemplate = new MongoTemplate(mongoDbFactory,mongoConverter);
    }

    @Scheduled(cron = "* * 2 * * ?")
    @Transactional
    public void clearExpiration(){
        log.info("clearExpiration started");
        Date now = new Date(System.currentTimeMillis());
        Query query = Query.query(Criteria.where(MongoFile.EXPIRATION).exists(true).
                andOperator(Criteria.where(MongoFile.EXPIRATION).lt(now)));
        List<MongoFile> mongoFiles = mongoTemplate.find(query, MongoFile.class);
        for (MongoFile mongoFile : mongoFiles) {
            DeleteResult fileRemoveResult = mongoTemplate.remove(mongoFile);
            if (fileRemoveResult.getDeletedCount()==1) {
                gridFsTemplate.delete(Query.query(Criteria.where(MongoFile.MONGO_ID).is(mongoFile.getFileId())));
            }else {
                log.error("mongoDB file clean task failed!");
                log.error("MongoFile ObjectId is {} ",mongoFile.getId().toString());
            }
        }
        log.info("clearExpiration ended");
    }

    public static ObjectId store(MultipartFile multipartFile) throws IOException, CarbonException {
        return store(multipartFile, false);
    }

    public static ObjectId store(MultipartFile multipartFile, boolean temp) throws IOException, CarbonException {
        MongoFile mongoFile = new MongoFile(multipartFile,temp);
        if (mongoFile.isBigFile()){
            ObjectId objectId = gridFsTemplate.store(multipartFile.getInputStream(), mongoFile.getFileName(), mongoFile.getContentType());
            mongoFile.setFileId(objectId);
        }
        mongoFile =  mongoTemplate.save(mongoFile);
        return mongoFile.getId();
    }

    private static void setFileExpiration(ObjectId objectId) throws CarbonException {
        MongoDatabase gridFsDatabase = mongoDbFactory.getDb(mongoFileDatabase);
        long currentTimeMillis = System.currentTimeMillis();
        Date fileExpiration = new Date(currentTimeMillis + MongoFile.FILE_EXPIRE_TIME);
        Document update = new Document();
        update.append("$set", new Document(MongoFile.EXPIRATION, fileExpiration));
        Document files = gridFsDatabase.getCollection(FS_FILES).findOneAndUpdate(Filters.eq(MongoFile.MONGO_ID, objectId), update);
//        if (files == null){
//            log.error("Mongo存储异常，找不到id{}的存储的文件",objectId.toString());
//            throw new CarbonException(RetCode.INTERNAL_ERROR);
//        }
//        long length = files.getLong(LENGTH);
//        int chunkSize = files.getInteger(CHUNK_SIZE);
//        int chunkNumber = (int)Math.ceil((float)length/(double)chunkSize);
//        Date chunksExpiration = new Date(currentTimeMillis + CHUNKS_EXPIRE_TIME);
//        Document update2 = new Document();
//        update2.append("$set", new Document(EXPIRATION,chunksExpiration));
//        UpdateResult chunkUpdateResult = gridFsDatabase.getCollection(FS_CHUNKS).updateMany(Filters.eq(FILES_ID, objectId), update2);
//        long matchedCount = chunkUpdateResult.getMatchedCount();
//        long modifiedCount = chunkUpdateResult.getModifiedCount();
//        if (matchedCount != chunkNumber){
//            log.error("Mongo存储异常，临时文件切片数{}与计算切片数{}不相同",matchedCount,chunkNumber);
//            throw new CarbonException(RetCode.INTERNAL_ERROR);
//        }
//        if (matchedCount != modifiedCount){
//            log.error("Mongo存储异常，临时文件切片更新过期时间失败，更新数量{}与切片数{}不一致",modifiedCount,matchedCount);
//            throw new CarbonException(RetCode.INTERNAL_ERROR);
//        }
    }

    public static MongoFile getMongoFile(String fileId) throws CarbonException {

        MongoFile mongoFile = mongoTemplate.findOne(Query.query(Criteria.where(MongoFile.MONGO_ID).is(new ObjectId(fileId))),MongoFile.class);
        if (mongoFile == null){
            throw new CarbonException(RetCode.FILE_NOT_FIND);
        }
        return mongoFile;
    }

    public static InputStream getFileInputStreamByMongoFile(MongoFile mongoFile) throws IOException, CarbonException {
        if (mongoFile.isBigFile()) {
            ObjectId fileId = mongoFile.getFileId();
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where(MongoFile.MONGO_ID).is(fileId)));
            if (gridFSFile == null) {
                throw new CarbonException(RetCode.FILE_NOT_FIND);
            }
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            return resource.getInputStream();
        }else{
            byte[] data = mongoFile.getBinary().getData();
            return new ByteArrayInputStream(data);
        }
    }

    public static void downloadFileByFileId(String fileId) throws CarbonException, IOException {
        MongoFile mongoFile = getMongoFile(fileId);
        String fileName = mongoFile.getFileName();
        String contentType = mongoFile.getContentType();
        if (contentType == null) {
            contentType = HttpUtil.DEFAULT_CONTENT_TYPE;
        }
        HttpServletResponse response = HttpUtil.getResponse();
        response.setHeader(HttpUtil.CONTENT_TYPE,contentType);
        response.addHeader(HttpUtil.CONTENT_DISPOSITION,HttpUtil.layContentDisposition(fileName));
        InputStream inputStream = getFileInputStreamByMongoFile(mongoFile);
        IOUtils.copy(inputStream, HttpUtil.getResponse().getOutputStream());
    }

}
