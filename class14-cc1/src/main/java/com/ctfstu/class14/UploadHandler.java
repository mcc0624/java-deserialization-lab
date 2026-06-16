package com.ctfstu.class14;

import com.ctfstu.common.BaseFilteringUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

/**
 * class14 — CC1 链（CommonsCollections1）
 *
 * 入口: AnnotationInvocationHandler.readObject()
 * 路径: LazyMap → ChainedTransformer → InvokerTransformer → Runtime.exec()
 *
 * 过滤策略：阻止其他 CC 链的关键入口/桥接类
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseFilteringUploadServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] FORBIDDEN = {
            // CC2 / CC4: collections4 路径
            "org.apache.commons.collections4.comparators.TransformingComparator",
            // CC3: InstantiateTransformer + TrAXFilter 路径
            "org.apache.commons.collections.functors.InstantiateTransformer",
            // CC3 / CC4: TemplatesImpl 字节码路径
            "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
            // CC5: BadAttributeValueExpException 入口
            "javax.management.BadAttributeValueExpException",
            // CC5 / CC6: TiedMapEntry 桥接
            "org.apache.commons.collections.keyvalue.TiedMapEntry",
            // CC7: Hashtable 入口
            "java.util.Hashtable",
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
