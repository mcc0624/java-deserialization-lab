package com.ctfstu.class16;

import com.ctfstu.common.BaseFilteringUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

/**
 * class16 — CC3 链（CommonsCollections3）
 *
 * 入口: AnnotationInvocationHandler.readObject()
 * 路径: LazyMap → ChainedTransformer → InstantiateTransformer → TrAXFilter → TemplatesImpl → bytecode exec
 *
 * 过滤策略：阻止其他 CC 链的关键入口/桥接类
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseFilteringUploadServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] FORBIDDEN = {
            // CC1 / CC5 / CC6 / CC7: 使用 InvokerTransformer（CC3 改用 InstantiateTransformer）
            "org.apache.commons.collections.functors.InvokerTransformer",
            // CC2 / CC4: 使用 collections4
            "org.apache.commons.collections4.",
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
