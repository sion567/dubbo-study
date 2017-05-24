package cc.sion567.praser;


import cc.sion567.bean.Foo;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;


public class FooSingleBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return Foo.class;
    }
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String remark = element.getAttribute("remark");
        String id = element.getAttribute("id");
        if (StringUtils.hasText(id)) {
            bean.addPropertyValue("id", id);
        }
        if (StringUtils.hasText(remark)) {
            bean.addPropertyValue("remark", remark);
        }
    }

}
