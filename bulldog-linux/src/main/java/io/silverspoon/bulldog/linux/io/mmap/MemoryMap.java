package io.silverspoon.bulldog.linux.io.mmap;

import io.silverspoon.bulldog.linux.jni.NativeMmap;
import io.silverspoon.bulldog.linux.jni.NativeTools;
import io.silverspoon.bulldog.linux.util.MMapFailedException;

public class MemoryMap {

   private int fileDescriptor = 0;
   private long mmapPointer = 0;

   public MemoryMap(String filename, long offset, long size) {
      this(filename, offset, size, 0L);
   }

   public MemoryMap(String filename, long offset, long size, long address) {
      fileDescriptor = NativeTools.open(filename, NativeTools.OPEN_READ_WRITE);
      if (fileDescriptor == -1) {
         throw new MMapFailedException("Unable to open file " + filename);
      }
      mmapPointer = NativeMmap.createMap(0, size, NativeMmap.READ | NativeMmap.WRITE, NativeMmap.SHARED, fileDescriptor, offset);
      if (mmapPointer == 0x0) {
         throw new MMapFailedException("Unable to map memory");
      }
   }

   public void closeMap() {
      if (fileDescriptor == 0) {
         return;
      }
      NativeTools.close(fileDescriptor);
   }

   public void setLongValue(long offset, long value) {
      NativeMmap.setLongValueAt(mmapPointer + offset, value);
   }

   public long getLongValueAt(long offset) {
      return NativeMmap.getLongValueAt(mmapPointer + offset);
   }

   public void setIntValue(long offset, int value) {
      NativeMmap.setIntValueAt(mmapPointer + offset, value);
   }

   public int getIntValueAt(long offset) {
      return NativeMmap.getIntValueAt(mmapPointer + offset);
   }

   public void setShortValue(long offset, int value) {
      NativeMmap.setIntValueAt(mmapPointer + offset, value);
   }

   public short getShortValueAt(long offset) {
      return NativeMmap.getShortValueAt(mmapPointer + offset);
   }

   public void setByteValue(long offset, byte value) {
      NativeMmap.setIntValueAt(mmapPointer + offset, value);
   }

   public byte getByteValueAt(long offset) {
      return NativeMmap.getByteValueAt(mmapPointer + offset * 2);
   }
}
