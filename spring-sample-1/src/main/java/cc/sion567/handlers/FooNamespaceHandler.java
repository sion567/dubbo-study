package cc.sion567.handlers;

import cc.sion567.praser.FooBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class FooNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("cc", new FooBeanDefinitionParser());
    }

}
