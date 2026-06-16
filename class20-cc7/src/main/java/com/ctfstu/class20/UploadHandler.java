package com.ctfstu.class20;

import com.ctfstu.common.BaseFilteringUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

/**
 * class20 — CC7 链（CommonsCollections7）
 *
 * 入口: Hashtable.readObject() → reconstitutionPut() → LazyMap.equals()
 * 路径: LazyMap.get() → ChainedTransformer → InvokerTransformer → Runtime.exec()
 *
 * 过滤策略：阻止其他 CC 链的关键入口/桥接类
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseFilteringUploadServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] FORBIDDEN = {
            // CC1 / CC3: AnnotationInvocationHandler 入口
            "sun.reflect.annotation.AnnotationInvocationHandler",
            // CC5: BadAttributeValueExpException 入口
            "javax.management.BadAttributeValueExpException",
            // CC5 / CC6: TiedMapEntry 桥接
            "org.apache.commons.collections.keyvalue.TiedMapEntry",
            // CC3 / CC4: InstantiateTransformer 路径
            "org.apache.commons.collections.functors.InstantiateTransformer",
            // CC2 / CC3 / CC4: TemplatesImpl 字节码路径
            "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
            "com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter",
            // CC2 / CC4: 使用 collections4
            "org.apache.commons.collections4.",
    };

    @Override
    protected String[] getForbiddenPatterns() {
        return FORBIDDEN;
    }

    @Override
    protected void handleObject(Object obj, HttpServletRequest req,
                                 HttpServletResponse resp, StringBuilder html) throws Exception {

        html.append("<div class=\"result-item\">")
            .append("<div class=\"result-label\">🎯 反序列化成功</div>")
            .append("<div class=\"result-value\">类型: ").append(obj.getClass().getName()).append("</div>")
            .append("</div>");
    }
}
