package com.ctfstu.common;

import java.io.IOException;

/**
 * 带黑名单过滤的上传 Servlet 基类。
 * 子类通过 getForbiddenPatterns() 返回黑名单数组。
 */
public abstract class BaseFilteringUploadServlet extends BaseUploadServlet {

    private static final long serialVersionUID = 1L;

    /** 子类返回黑名单规则 */
    protected abstract String[] getForbiddenPatterns();

    @Override
    protected Object deserialize(String filename) throws IOException, ClassNotFoundException {
        return FilteringUnSerLiz.unser(filename, getForbiddenPatterns());
    }
}
