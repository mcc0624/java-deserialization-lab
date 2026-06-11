package com.ctfstu.class13;

import com.ctfstu.common.BaseUploadServlet;
import org.apache.commons.collections.map.TransformedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseUploadServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) throws Exception {
        if (obj instanceof TransformedMap) {
            Map<?, ?> transformedMap = (Map<?, ?>) obj;
            Method checkSetValue = transformedMap.getClass()
                    .getDeclaredMethod("checkSetValue", Object.class);
            checkSetValue.setAccessible(true);
            checkSetValue.invoke(transformedMap, Runtime.getRuntime());

            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ TransformedMap.checkSetValue 已触发</div>")
                .append("</div>");
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象内容:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");
    }
}
