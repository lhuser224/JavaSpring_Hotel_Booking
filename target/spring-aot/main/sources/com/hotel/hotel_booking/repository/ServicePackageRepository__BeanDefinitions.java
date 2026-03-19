package com.hotel.hotel_booking.repository;

import com.hotel.hotel_booking.entity.ServicePackage;
import jakarta.persistence.EntityManager;
import java.lang.Class;
import java.lang.Integer;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean__Autowiring5;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * Bean definitions for {@link ServicePackageRepository}.
 */
@Generated
public class ServicePackageRepository__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'servicePackageRepository'.
   */
  private static BeanInstanceSupplier<JpaRepositoryFactoryBean> getServicePackageRepositoryInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<JpaRepositoryFactoryBean>forConstructor(Class.class)
            .withGenerator((registeredBean, args) -> new JpaRepositoryFactoryBean(args.get(0)));
  }

  /**
   * Get the bean definition for 'servicePackageRepository'.
   */
  public static BeanDefinition getServicePackageRepositoryBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JpaRepositoryFactoryBean.class);
    beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(JpaRepositoryFactoryBean.class, ServicePackageRepository.class, ServicePackage.class, Integer.class));
    beanDefinition.setLazyInit(false);
    beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "com.hotel.hotel_booking.repository.ServicePackageRepository");
    beanDefinition.getPropertyValues().addPropertyValue("queryLookupStrategyKey", QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
    beanDefinition.getPropertyValues().addPropertyValue("lazyInit", false);
    beanDefinition.getPropertyValues().addPropertyValue("namedQueries", new RuntimeBeanReference("jpa.named-queries#5"));
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragments", new RuntimeBeanReference("jpa.ServicePackageRepository.fragments#0"));
    beanDefinition.getPropertyValues().addPropertyValue("transactionManager", "transactionManager");
    beanDefinition.getPropertyValues().addPropertyValue("entityManager", new RuntimeBeanReference("jpaSharedEM_entityManagerFactory", EntityManager.class));
    beanDefinition.getPropertyValues().addPropertyValue("escapeCharacter", '\\');
    beanDefinition.getPropertyValues().addPropertyValue("mappingContext", new RuntimeBeanReference("jpaMappingContext"));
    beanDefinition.getPropertyValues().addPropertyValue("queryEnhancerSelector", QueryEnhancerSelector.DefaultQueryEnhancerSelector.class);
    beanDefinition.getPropertyValues().addPropertyValue("enableDefaultTransactions", true);
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragmentsFunction", new RepositoryFactoryBeanSupport.RepositoryFragmentsFunction() {
      public RepositoryComposition.RepositoryFragments getRepositoryFragments(
          BeanFactory beanFactory, RepositoryFactoryBeanSupport.FragmentCreationContext context) {
        EntityManager entityManager = beanFactory.getBean(EntityManager.class);
        return RepositoryComposition.RepositoryFragments.just(new com.hotel.hotel_booking.repository.ServicePackageRepositoryImpl__AotRepository(entityManager, context));
      }
    });
    InstanceSupplier<JpaRepositoryFactoryBean> instanceSupplier = getServicePackageRepositoryInstanceSupplier();
    instanceSupplier = instanceSupplier.andThen(JpaRepositoryFactoryBean__Autowiring5::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
