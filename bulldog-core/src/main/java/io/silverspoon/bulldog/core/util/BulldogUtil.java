package io.silverspoon.bulldog.core.util;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public final class BulldogUtil {

   public static void sleepMs(final long ms) {
      try {
         Thread.sleep(ms);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   public static void sleepNs(final long ns) {
      final long start = System.nanoTime();
      final long end = start + ns;
      long now = 0;
      do {
         now = System.nanoTime();
      } while (now < end);
   }

   public static String bytesToString(byte[] bytes, String encoding) {
      if (bytes == null) {
         throw new IllegalArgumentException("bytes may not be null in string conversion");
      }

      if (bytes.length == 0) {
         return null;
      }

      try {
         return new String(bytes, encoding);
      } catch (Exception e) {
         throw new IllegalArgumentException("Unknown encoding");
      }
   }

   public static String bytesToString(byte[] bytes) {
      return bytesToString(bytes, "ASCII");
   }

   @SuppressWarnings("resource")
   public static String convertStreamToString(java.io.InputStream is) {
      Scanner s = new Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }

   public static String readFileAsString(String path) {
      try {
         byte[] encoded = Files.readAllBytes(Paths.get(path));
         return new String(encoded, Charset.defaultCharset());
      } catch (Exception ex) {
         return null;
      }
   }

   public static boolean isStringNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d+)?");
   }

   public static final int getUnsignedByte(byte b) {
      return b & 0xFF;
   }

   public static String printBinaryValue(byte value) {
      return String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
   }
}
