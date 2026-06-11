package com.ctfstu.class12;

import com.ctfstu.common.BaseUploadServlet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

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
                                 HttpServletResponse resp, StringBuilder html) throws Exception {
        if (obj instanceof TemplatesImpl) {
            TemplatesImpl templates = (TemplatesImpl) obj;
            templates.newTransformer();
            html.append("<div class=\"result-item\">")
                .append("<div class=\"result-label\">⚡ TemplatesImpl.newTransformer() 已触发</div>")
                .append("</div>");
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象类型:</div>")
            .append("<div class=\"result-value\">").append(obj.getClass().getName()).append("</div>")
            .append("</div>");
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 对象内容:</div>")
            .append("<div class=\"result-value\">").append(obj.toString()).append("</div>")
            .append("</div>");
    }
}
