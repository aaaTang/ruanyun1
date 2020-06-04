package cn.ruanyun.backInterface.common.utils;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;


/**
 * @Author: Yin BenLi
 * @Description: MultipartFile转成File工具类
 * @Date: Create in 2020/4/8 17:19
 */
public class MultipartFileToFile {

    /**
     * MultipartFile 转 File
     * @param file 文件
     * @throws Exception 异常
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (ObjectUtil.equal("", file) || file.getSize() <= 0) {

            file = null;
        } else {

            InputStream ins;
            ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 获取流文件
     * @param ins 输入流
     * @param file 文件
     */
    private static void inputStreamToFile(InputStream ins, File file) {

        try {

            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static String transferBase64(MultipartFile file) throws Exception{

        BASE64Encoder base64Encoder =new BASE64Encoder();
        return file.getOriginalFilename()+","+ base64Encoder.encode(file.getBytes());
    }


    /**
     * 删除本地临时文件
     * @param file 文件
     */
    public static void delteTempFile(File file) {

        if (file != null) {
            File del = new File(file.toURI());

            del.delete();
        }
    }
}
