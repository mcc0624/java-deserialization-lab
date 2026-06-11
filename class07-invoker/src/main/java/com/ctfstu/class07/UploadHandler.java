package com.ctfstu.class07;

import com.ctfstu.common.BaseUploadServlet;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseUploadServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) {
        if (obj instanceof InvokerTransformer) {
            Transformer t = (Transformer) obj;
            t.transform(Runtime.getRuntime());
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ InvokerTransformer 已触发</div>")
                .append("</div>");
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象内容:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");
    }
}
