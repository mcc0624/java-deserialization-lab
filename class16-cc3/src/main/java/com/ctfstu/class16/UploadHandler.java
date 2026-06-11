package com.ctfstu.class16;

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
            "javax.management.BadAttributeValueExpException",
            "org.apache.commons.collections.keyvalue.TiedMapEntry",
            "org.apache.commons.collections4.comparators.TransformingComparator",
            "org.apache.commons.collections4.functors.ChainedTransformer",
            "org.apache.commons.collections4.functors.ConstantTransformer",
            "org.apache.commons.collections4.functors.InvokerTransformer",
            "org.apache.commons.collections4.functors.InstantiateTransformer",
            "org.apache.commons.collections4.map.LazyMap",
            "com.mchange.v2.c3p0.PoolBackedDataSource",
            "org.codehaus.groovy.runtime.MethodClosure",
            "com.sun.rowset.JdbcRowSetImpl",
            "org.apache.commons.beanutils.BeanComparator",
            "org.springframework.aop.framework.AdvisedSupport"
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
