package com.ctfstu.class15;

import com.ctfstu.common.BaseFilteringUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

/**
 * class15 — CC2 链（CommonsCollections2）
 *
 * 入口: PriorityQueue.readObject() → TransformingComparator.compare()
 * 路径: InvokerTransformer → TemplatesImpl.newTransformer() → bytecode exec
 *
 * 过滤策略：阻止其他 CC 链的关键入口/桥接类
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseFilteringUploadServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] FORBIDDEN = {
            // CC1 / CC3 / CC5 / CC6 / CC7: 均使用 collections3（LazyMap、InvokerTransformer 等）
            "org.apache.commons.collections.",
            // CC3 / CC4: InstantiateTransformer + TrAXFilter 路径
            "org.apache.commons.collections4.functors.InstantiateTransformer",
            "com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter",
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
