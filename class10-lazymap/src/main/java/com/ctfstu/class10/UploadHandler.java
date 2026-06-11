package com.ctfstu.class10;

import com.ctfstu.common.BaseUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseUploadServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) {
        if (obj instanceof Map) {
            Map<?, ?> lazyMap = (Map<?, ?>) obj;
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ 触发 LazyMap.get()</div>")
                .append("</div>");
            Object result = lazyMap.get("any_nonexistent_key");
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">LazyMap.get 返回:</div>")
                .append("<div class=\"result-value\">").append(result).append("</div>")
                .append("</div>");
        } else {
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚠️ 反序列化对象不是 Map 类型</div>")
                .append("</div>");
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象内容:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");
    }
}
