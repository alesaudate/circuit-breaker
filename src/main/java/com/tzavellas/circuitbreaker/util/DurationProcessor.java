package com.tzavellas.circuitbreaker.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * A {@link BeanFactoryPostProcessor} that registers a property editor
 * for {@link Duration} objects.
 * 
 * @deprecated Use SpringXmlConfigurationSupport instead, since it also registers a PropertyEditor for
 *             <code>java.lang.Class</code>.
 * @author spiros
 */
@Deprecated
public class DurationProcessor implements BeanFactoryPostProcessor {
	
	private static final PropertyEditorRegistrar REGISTRAR = new PropertyEditorRegistrar() {
		public void registerCustomEditors(PropertyEditorRegistry registry) {
			registry.registerCustomEditor(Duration.class, new Duration.Editor());
		}
	};
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
		bf.addPropertyEditorRegistrar(REGISTRAR);
	}
}