package com.ctfstu.class17;

import com.ctfstu.common.BaseFilteringUploadServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

/**
 * class17 — CC4 链（CommonsCollections4）
 *
 * 入口: PriorityQueue.readObject() → TransformingComparator.compare()
 * 路径: ChainedTransformer → InstantiateTransformer → TrAXFilter → TemplatesImpl → bytecode exec
 *
 * 过滤策略：阻止其他 CC 链的关键入口/桥接类（CC4 不使用 LazyMap 和 InvokerTransformer）
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadHandler extends BaseFilteringUploadServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] FORBIDDEN = {
            // CC1 / CC3 / CC5 / CC6 / CC7: 使用 LazyMap（collections3）
            "org.apache.commons.collections.map.LazyMap",
            // CC1 / CC2 / CC5 / CC6 / CC7: 使用 InvokerTransformer
            "org.apache.commons.collections.functors.InvokerTransformer",
            "org.apache.commons.collections4.functors.InvokerTransformer",
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
