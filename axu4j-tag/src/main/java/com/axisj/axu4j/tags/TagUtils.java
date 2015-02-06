package com.axisj.axu4j.tags;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by HJ.Park on 2015-01-06.
 */
public class TagUtils {
    public static Logger logger = LoggerFactory.getLogger(TagUtils.class);

    public static final String EL_PREFIX = "${";
    public static final String EL_SUFFIX = "}";

    /**
     * JspFragment를 String으로 변환한다.
     *
     * @param jspFrag
     * @return
     */
    public static String toString(JspFragment jspFrag) {
        if (jspFrag == null) { return null; }

        StringWriter writer = new StringWriter();
        try {
            jspFrag.invoke(writer);
        } catch (Exception e) {
            logger.error("getJspContents is fail.", e);
        }

        return writer.toString();
    }

    /**
     * value가 EL값(${...})인지 확인한다.
     *
     * @param value
     * @return
     */
    public static boolean isELValue(Object value) {
        String elVal = ObjectUtils.toString(value);
        elVal = StringUtils.trim(elVal);
        return StringUtils.startsWith(elVal, EL_PREFIX);
    }

    /**
     * "${name}"에서 변수명(name)만 반환한다.
     *
     * @param value
     * @return
     */
    public static String getElName(Object value) {
        String elVal = ObjectUtils.toString(value);

        elVal = StringUtils.removeStart(elVal, EL_PREFIX);
        elVal = StringUtils.removeEnd(elVal, EL_SUFFIX);

        return elVal;
    }

    /**
     * "${...}" EL 명을 Object 값으로 변환한다.
     *
     * @param context
     * @param elName
     * @return
     */
    public static Object getElValue(JspContext context, String elName) {
        VariableResolver variableResolver = context.getVariableResolver();
        Object var = null;
        try {
            elName = TagUtils.getElName(elName);
            var    = variableResolver.resolveVariable(elName);
            logger.debug("ExpressionLanguage variable {} is [{}]", elName, var);
        } catch (ELException e) {
            logger.error("ExpressionLanguage Error", e);
        }

        return var;
    }

    /**
     * Object value를 Cookie 값으로 변환한다.
     *
     * @param value
     * @return
     */
    public static String getCookieValue(Object value) {
        String strVal = ObjectUtils.toString(value);
        try {
            strVal = URLEncoder.encode(strVal, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strVal;
    }
}
