package com.hotel.hotel_booking;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link HotelBookingApplication}.
 */
@Generated
public class HotelBookingApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'hotelBookingApplication'.
   */
  public static BeanDefinition getHotelBookingApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(HotelBookingApplication.class);
    beanDefinition.setInstanceSupplier(HotelBookingApplication::new);
    return beanDefinition;
  }
}
