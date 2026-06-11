package com.ctfstu.class09;

import com.ctfstu.common.BaseUploadServlet;
import org.apache.commons.collections.Transformer;

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
        if (obj instanceof Transformer) {
            Transformer t = (Transformer) obj;
            t.transform(null);
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ ChainedTransformer 已触发</div>")
                .append("</div>");
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象内容:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");
    }
}
