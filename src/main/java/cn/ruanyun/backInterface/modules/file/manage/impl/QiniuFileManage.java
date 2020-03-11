package cn.ruanyun.backInterface.modules.file.manage.impl;



import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.SettingConstant;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.modules.base.pojo.Setting;
import cn.ruanyun.backInterface.modules.base.service.SettingService;
import cn.ruanyun.backInterface.modules.base.vo.OssSetting;
import cn.ruanyun.backInterface.modules.file.manage.FileManage;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author fei
 */
@Component
public class QiniuFileManage implements FileManage {

    @Autowired
    private SettingService settingService;

    @Override
    public OssSetting getOssSetting() {

        Setting setting = settingService.get(SettingConstant.QINIU_OSS);
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            throw new RuanyunException("您还未配置七牛云对象存储");
        }
        return new Gson().fromJson(setting.getValue(), OssSetting.class);
    }

    public Configuration getConfiguration(Integer zone){

        Configuration cfg = null;
        if(zone.equals(SettingConstant.ZONE_ZERO)){
            cfg = new Configuration(Zone.zone0());
        }else if(zone.equals(SettingConstant.ZONE_ONE)){
            cfg = new Configuration(Zone.zone1());
        }else if(zone.equals(SettingConstant.ZONE_TWO)){
            cfg = new Configuration(Zone.zone2());
        }else if(zone.equals(SettingConstant.ZONE_THREE)){
            cfg = new Configuration(Zone.zoneNa0());
        }else if(zone.equals(SettingConstant.ZONE_FOUR)){
            cfg = new Configuration(Zone.zoneAs0());
        }else {
            cfg = new Configuration(Zone.autoZone());
        }
        return cfg;
    }

    public UploadManager getUploadManager(Configuration cfg){

        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager;
    }

    @Override
    public String pathUpload(String filePath, String key) {

        OssSetting os = getOssSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        String upToken = auth.uploadToken(os.getBucket());
        try {
            Response response = getUploadManager(getConfiguration(os.getZone())).put(filePath, key, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return os.getHttp() + os.getEndpoint() + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            throw new RuanyunException("上传文件出错，请检查七牛云配置，" + r.toString());
        }
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String key, MultipartFile file) {

        OssSetting os = getOssSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        String upToken = auth.uploadToken(os.getBucket());
        try {
            Response response = getUploadManager(getConfiguration(os.getZone())).put(inputStream, key, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return os.getHttp() + os.getEndpoint() + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            throw new RuanyunException("上传文件出错，请检查七牛云配置，" + r.toString());
        }
    }

    @Override
    public String renameFile(String fromKey, String toKey) {

        OssSetting os = getOssSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));
        try {
            bucketManager.move(os.getBucket(), fromKey, os.getBucket(), toKey);
            return os.getHttp() + os.getEndpoint() + "/" + toKey;
        } catch (QiniuException ex) {
            throw new RuanyunException("重命名文件失败，" + ex.response.toString());
        }
    }

    @Override
    public String copyFile(String fromKey, String toKey) {

        OssSetting os = getOssSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));
        try {
            bucketManager.copy(os.getBucket(), fromKey, os.getBucket(), toKey);
            return os.getHttp() + os.getEndpoint() + "/" + toKey;
        } catch (QiniuException ex) {
            throw new RuanyunException("复制文件失败，" + ex.response.toString());
        }
    }

    @Override
    public void deleteFile(String key) {

        OssSetting os = getOssSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));
        try {
            bucketManager.delete(os.getBucket(), key);
        } catch (QiniuException ex) {
            throw new RuanyunException("删除文件失败，" + ex.response.toString());
        }
    }
}
