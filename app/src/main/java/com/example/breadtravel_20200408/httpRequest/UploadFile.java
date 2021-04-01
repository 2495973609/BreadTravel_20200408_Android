package com.example.breadtravel_20200408.httpRequest;

import com.example.breadtravel_20200408.util.SendNotification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class UploadFile {
    static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
    static final String METHOD = "POST";
    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";

    public UploadFile(final File file, final Map<String, String> params, final String url1) {
        final SendNotification sendNotification = new SendNotification();
        try {
            String sendUrl = HOST + url1;
            String newFileName = file.getName();
            StringBuilder sb = new StringBuilder();
            /**
             * 普通的表单数据
             */
            for (String key : params.keySet()) {
                sb.append("--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n");
                sb.append("\r\n");
                sb.append(params.get(key) + "\r\n");
            }
            /**
             * 上传文件的头
             */
            sb.append("--" + BOUNDARY + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + "img" + "\"; filename=\"" + newFileName + "\"" + "\r\n");
            sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
            sb.append("\r\n");
            byte[] headerInfo = sb.toString().getBytes("UTF-8");
            byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
            System.out.println(sb.toString());
            URL url = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(METHOD);
            conn.setReadTimeout(10000);//设置读取超时的毫秒数
            conn.setConnectTimeout(10000);//设置连接超时的毫秒数
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("Content-Length", String.valueOf(headerInfo.length + file.length() + endInfo.length));
            conn.setRequestProperty("Accept-Encoding", "identity"); // 添加这行代码
            conn.setDoOutput(true);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            InputStream in = new FileInputStream(file);
            out.write(headerInfo);
            byte[] buf = new byte[1024*1024];
            int len;
            int pro1 = 0, pro2 = 0;
            int totalLength = 0;
            long fileLength = file.length();
            while ((len = in.read(buf)) != -1) {
                totalLength += len;
                if (fileLength > 0) {
                    pro1 = (int) ((totalLength / (float) fileLength) * 100);
                    System.out.println(pro1+"!!!!!!!!!!!!!");
                }
                if (pro1 != pro2) {
                    System.out.println(pro2+"!!!!!!!!!!!!!!!");
                    sendNotification.setNotification(pro2 = pro1, file);
                }
                out.write(buf, 0, len);
            }
            out.write(endInfo);
            in.close();
            out.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in2 = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in2));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
