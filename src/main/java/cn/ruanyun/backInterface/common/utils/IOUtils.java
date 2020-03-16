package cn.ruanyun.backInterface.common.utils;

import org.apache.commons.codec.Charsets;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @program: ruanyun
 * @description: io工具类
 * @author: fei
 * @create: 2020-02-13 16:43
 **/
public abstract class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * closeQuietly
     * @param closeable 自动关闭
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static String toString(InputStream input) throws IOException {
        return toString(input, Charsets.UTF_8);
    }

    /**
     * 读取输入流转化为string
     * @param input
     * @param charset
     * @return
     * @throws IOException
     */
    public static String toString(InputStream input, Charset charset) throws IOException {
        InputStreamReader in = new InputStreamReader(input, charset);
        StringBuilder out = new StringBuilder();
        char[] c = new char[DEFAULT_BUFFER_SIZE];
        for (int n; (n = in.read(c)) != -1;) {
            out.append(new String(c, 0, n));
        }
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(input);
        return out.toString();
    }

    /**
     * 读取输入流为文件
     * @param input
     * @param file
     * @throws IOException
     */
    public static void toFile(InputStream input, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while ((bytesRead = input.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(input);
    }

}
