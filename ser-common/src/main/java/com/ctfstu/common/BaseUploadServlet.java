package com.ctfstu.common;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件上传 + 反序列化 模板方法基类。
 *
 * 子类只需实现 handleObject() 方法，定义反序列化后如何处理对象。
 *
 * 模板流程：
 *   1. 创建 ./upload 目录
 *   2. 保存上传文件到 ./upload/test{nanoTime}
 *   3. 反序列化文件 → Object obj
 *   4. 调用 handleObject(obj, req, resp) 由子类处理
 *   5. 返回 HTML 响应
 *
 * 注意：子类上必须标注 @WebServlet("/upload") 和 @MultipartConfig，
 *       因为 Java 注解不会被继承，且抽象类自身不可实例化。
 */
public abstract class BaseUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dirPath = "./upload";
        Path directoryPath = Paths.get(dirPath);

        // 确保上传目录存在
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // 保存上传文件
        String filename = dirPath + "/test" + System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            Part part = req.getPart("uploadFile");
            if (part == null) {
                resp.getWriter().write("错误: 未找到 uploadFile 参数");
                return;
            }
            IOUtils.copy(part.getInputStream(), fos);
        }

        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">✅ 文件上传成功</div>")
            .append("<div class=\"result-value\">文件路径: ").append(filename).append("</div>")
            .append("</div>");

        try {
            Object obj = deserialize(filename);
            handleObject(obj, req, resp, html);
        } catch (ClassNotFoundException e) {
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">❌ 反序列化错误</div>")
                .append("<div class=\"result-value\" style=\"color: #e74c3c;\">")
                .append(e.getMessage()).append("</div></div>");
            resp.getWriter().write(html.toString());
            return;
        } catch (Exception e) {
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚠️ 异常</div>")
                .append("<div class=\"result-value\" style=\"color: #e74c3c;\">")
                .append(e.getMessage()).append("</div></div>");
            resp.getWriter().write(html.toString());
            return;
        }

        resp.getWriter().write(html.toString());
    }

    /**
     * 反序列化方法 — 子类可覆盖以实现过滤逻辑
     */
    protected Object deserialize(String filename) throws IOException, ClassNotFoundException {
        return UnSerLiz.unser(filename);
    }

    /**
     * 模板方法 — 子类实现具体的对象处理逻辑
     */
    protected abstract void handleObject(Object obj, HttpServletRequest req,
                                         HttpServletResponse resp, StringBuilder html)
            throws Exception;
}
