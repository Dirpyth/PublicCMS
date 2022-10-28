package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getSha512 获取sha512
 * <p>
 * 参数列表
 * <ol>
 * <li>字符串
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>string</code> sha512值
 * </ul>
 * 使用示例
 * <p>
 * ${getSha512('aaa')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getSha512?parameters=aaa', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetSha512Method extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return VerificationUtils.sha512Encode(string);
        }
        return null;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
