package com.ctfstu.class18;

import com.ctfstu.common.BaseFilteringUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseFilteringUploadServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] FORBIDDEN = {
            "org.apache.commons.collections4.",
            "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
            "com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter",
            "com.mchange.v2.c3p0.PoolBackedDataSource",
            "org.codehaus.groovy.runtime.MethodClosure",
            "com.sun.rowset.JdbcRowSetImpl",
            "org.springframework.aop.framework.AdvisedSupport",
            "java.net.URL",
            "bsh.Interpreter",
            "clojure.lang.PersistentArrayMap",
            "org.mozilla.javascript.NativeError",
            "sun.rmi.server.UnicastRef",
            "java.rmi.server.RemoteObject"
    };

    @Override
    protected String[] getForbiddenPatterns() {
        return FORBIDDEN;
    }

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) throws Exception {
        
        // 🔒 顶层类型安全检查: 只允许 javax.management.BadAttributeValueExpException
        if (!(obj instanceof javax.management.BadAttributeValueExpException)) {
            throw new SecurityException(
                "拒绝反序列化: 仅允许 javax.management.BadAttributeValueExpException, 实际类型: " + obj.getClass().getName());
        }
        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 反序列化成功</div>")
            .append("<div class=\"result-value\">类型: ").append(obj.getClass().getName()).append("</div>")
            .append("</div>");
    }
}
