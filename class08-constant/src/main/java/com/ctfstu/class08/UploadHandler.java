package com.ctfstu.class08;

import com.ctfstu.common.BaseUploadServlet;
import org.apache.commons.collections.functors.ConstantTransformer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseUploadServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) throws Exception {
        String cmd = req.getParameter("cmd");
        if (obj instanceof ConstantTransformer) {
            Class<?> clazz = (Class<?>) ((ConstantTransformer) obj).transform("ignored");
            Method getRuntimeMethod = clazz.getMethod("getRuntime");
            Object runtimeInstance = getRuntimeMethod.invoke(null);
            Method execMethod = runtimeInstance.getClass().getMethod("exec", String.class);
            execMethod.invoke(runtimeInstance, cmd != null ? cmd : "id");

            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ ConstantTransformer 已触发</div>")
                .append("<div class=\"result-value\">命令: ").append(cmd != null ? cmd : "id").append("</div>")
                .append("</div>");
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象内容:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");
    }
}
