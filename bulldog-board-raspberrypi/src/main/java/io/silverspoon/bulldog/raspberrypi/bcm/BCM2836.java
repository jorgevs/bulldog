package io.silverspoon.bulldog.raspberrypi.bcm;

/**
 * BCM2836 implementation of {@link io.silverspoon.bulldog.raspberrypi.bcm.AbstractBCM}
 * Used by RaspberryPi 2
 */
public class BCM2836 extends AbstractBCM {

   public static final int BCM_PERI_BASE = 0x3F000000;
   public static final int GPIO_BASE = (BCM_PERI_BASE + 0x200000);
   public static final int PWM_BASE = (BCM_PERI_BASE + 0x20C000);
   public static final int CLOCK_BASE = (BCM_PERI_BASE + 0x101000);

   public static final int PWMCLK_CNTL = 40 * 4;
   public static final int PWMCLK_DIV = 41 * 4;

   public static final int PWM_CTL = 0;
   public static final int PWM_RNG1 = 4 * 4;
   public static final int PWM_DAT1 = 5 * 4;

   public static final int GPIO_SET = 7 * 4;
   public static final int GPIO_CLEAR = 10 * 4;

   @Override
   public int getBCMPeriBase() {
      return BCM_PERI_BASE;
   }

   @Override
   public int getGPIOBase() {
      return GPIO_BASE;
   }

   @Override
   public int getPWMBase() {
      return PWM_BASE;
   }

   @Override
   public int getClockBase() {
      return CLOCK_BASE;
   }

   @Override
   public int getPWMClkCntl() {
      return PWMCLK_CNTL;
   }

   @Override
   public int getPWMClkDiv() {
      return PWMCLK_DIV;
   }

   @Override
   public int getPWMCtl() {
      return PWM_CTL;
   }

   @Override
   public int getPWMRng1() {
      return PWM_RNG1;
   }

   @Override
   public int getPWMDat1() {
      return PWM_DAT1;
   }

   @Override
   public int getGPIOSet() {
      return GPIO_SET;
   }

   @Override
   public int getGPIOClear() {
      return GPIO_CLEAR;
   }
}
