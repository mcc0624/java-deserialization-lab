package com.ctfstu.class03;

import com.ctfstu.common.BaseUploadServlet;
import com.ctfstu.common.Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseUploadServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) throws Exception {
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象反序列化结果:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");

        String cmd = null;
        if (obj instanceof Student) {
            cmd = ((Student) obj).getName();
        }

        if (cmd != null) {
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ 尝试执行命令:</div>")
                .append("<div class=\"result-value\">").append(cmd).append("</div>")
                .append("</div>");

            Process process = Runtime.getRuntime().exec(cmd);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    html.append("<br>后台: ").append(line);
                }
            }
            process.waitFor();
        }
    }
}
