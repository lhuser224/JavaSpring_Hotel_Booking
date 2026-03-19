package com.hotel.hotel_booking.repository;

import com.hotel.hotel_booking.entity.Reservation;
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
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean__Autowiring7;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * Bean definitions for {@link ReservationRepository}.
 */
@Generated
public class ReservationRepository__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'reservationRepository'.
   */
  private static BeanInstanceSupplier<JpaRepositoryFactoryBean> getReservationRepositoryInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<JpaRepositoryFactoryBean>forConstructor(Class.class)
            .withGenerator((registeredBean, args) -> new JpaRepositoryFactoryBean(args.get(0)));
  }

  /**
   * Get the bean definition for 'reservationRepository'.
   */
  public static BeanDefinition getReservationRepositoryBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JpaRepositoryFactoryBean.class);
    beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(JpaRepositoryFactoryBean.class, ReservationRepository.class, Reservation.class, Integer.class));
    beanDefinition.setLazyInit(false);
    beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "com.hotel.hotel_booking.repository.ReservationRepository");
    beanDefinition.getPropertyValues().addPropertyValue("queryLookupStrategyKey", QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
    beanDefinition.getPropertyValues().addPropertyValue("lazyInit", false);
    beanDefinition.getPropertyValues().addPropertyValue("namedQueries", new RuntimeBeanReference("jpa.named-queries#7"));
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragments", new RuntimeBeanReference("jpa.ReservationRepository.fragments#0"));
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
        return RepositoryComposition.RepositoryFragments.just(new com.hotel.hotel_booking.repository.ReservationRepositoryImpl__AotRepository(entityManager, context));
      }
    });
    InstanceSupplier<JpaRepositoryFactoryBean> instanceSupplier = getReservationRepositoryInstanceSupplier();
    instanceSupplier = instanceSupplier.andThen(JpaRepositoryFactoryBean__Autowiring7::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
