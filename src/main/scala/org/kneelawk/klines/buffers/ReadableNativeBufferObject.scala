package org.kneelawk.klines.buffers

import java.nio._

import org.kneelawk.klines.util.TryUtil._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

/**
 * Created by Kneelawk on 4/6/19.
 */
trait ReadableNativeBufferObject extends ReadableBufferObject {

  /**
   * Reads a chunk of data starting at offset and with a length of len into the
   * buffer represented by len and address.
   *
   * @param offset  the offset in bytes of the data within this buffer to read.
   * @param len     the length of the data to read.
   * @param address the address of the buffer to write to.
   */
  def readToNative(offset: Long, len: Long, address: Long)

  /**
   * Gets a chunk of bytes starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in bytes of the chunk to get.
   *
   * @return a [[org.lwjgl.system.MemoryUtil.memAlloc()]]'d ByteBuffer containing the chunk of data.
   */
  override def getBytes(offset: Long, chunkLen: Int): ByteBuffer = {
    val buffer = memAlloc(chunkLen)
    readTo(offset, buffer)
    buffer
  }

  /**
   * Gets a single byte at offset.
   *
   * @param offset the offset in bytes of the byte to get.
   *
   * @return a single byte representing the data.
   */
  override def getByte(offset: Long): Byte = {
    tryWith(stackPush()) { stack =>
      val buffer = stack.malloc(1)
      readTo(offset, buffer)
      buffer.get(0)
    }
  }

  /**
   * Gets a chunk of shorts starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in shorts of the chunk to get.
   *
   * @return a [[org.lwjgl.system.MemoryUtil.memAllocShort()]]'d ShortBuffer containing the chunk of data.
   */
  override def getShorts(offset: Long, chunkLen: Int): ShortBuffer = {
    val buffer = memAllocShort(1)
    readTo(offset, buffer)
    buffer
  }

  /**
   * Gets a single short at offset.
   *
   * @param offset the offset in bytes of the short to get.
   *
   * @return a single short representing the data.
   */
  override def getShort(offset: Long): Short = {
    tryWith(stackPush()) { stack =>
      val buffer = stack.mallocShort(1)
      readTo(offset, buffer)
      buffer.get(0)
    }
  }

  /**
   * Gets a chunk of ints starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in ints of the chunk to get.
   *
   * @return a [[org.lwjgl.system.MemoryUtil.memAllocInt()]]'d IntBuffer containing the chunk of data.
   */
  override def getInts(offset: Long, chunkLen: Int): IntBuffer = {
    val buffer = memAllocInt(chunkLen)
    readTo(offset, buffer)
    buffer
  }

  /**
   * Gets a single int at offset.
   *
   * @param offset the offset in bytes of the int to get.
   *
   * @return a single int representing the data.
   */
  override def getInt(offset: Long): Int = {
    tryWith(stackPush()) { stack =>
      val buffer = stack.mallocInt(1)
      readTo(offset, buffer)
      buffer.get(0)
    }
  }

  /**
   * Gets a chunk of longs starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in longs of the chunk to get.
   *
   * @return a [[org.lwjgl.system.MemoryUtil.memAllocLong()]]'d LongBuffer containing the chunk of data.
   */
  override def getLongs(offset: Long, chunkLen: Int): LongBuffer = {
    val buffer = memAllocLong(chunkLen)
    readTo(offset, buffer)
    buffer
  }

  /**
   * Gets a single long at offset.
   *
   * @param offset the offset in bytes of the long to get.
   *
   * @return a single long representing the data.
   */
  override def getLong(offset: Long): Long = {
    tryWith(stackPush()) { stack =>
      val buffer = stack.mallocLong(1)
      readTo(offset, buffer)
      buffer.get(0)
    }
  }

  /**
   * Gets a chunk of floats starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in floats of the chunk to get.
   *
   * @return a [[org.lwjgl.system.MemoryUtil.memAllocFloat()]]'d FloatBuffer containing the chunk of data.
   */
  override def getFloats(offset: Long, chunkLen: Int): FloatBuffer = {
    val buffer = memAllocFloat(chunkLen)
    readTo(offset, buffer)
    buffer
  }

  /**
   * Gets a single float at offset.
   *
   * @param offset the offset in bytes of the float to get.
   *
   * @return a single float representing the data.
   */
  override def getFloat(offset: Long): Float = {
    tryWith(stackPush()) { stack =>
      val buffer = stack.mallocFloat(1)
      readTo(offset, buffer)
      buffer.get(0)
    }
  }

  /**
   * Gets a chunk of doubles starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in doubles of the chunk to get.
   *
   * @return a [[org.lwjgl.system.MemoryUtil.memAllocDouble()]]'d DoubleBuffer containing the chunk of data.
   */
  override def getDoubles(offset: Long, chunkLen: Int): DoubleBuffer = {
    val buffer = memAllocDouble(chunkLen)
    readTo(offset, buffer)
    buffer
  }

  /**
   * Gets a single double at offset.
   *
   * @param offset the offset in bytes of the double to get.
   *
   * @return a single double representing the data.
   */
  override def getDouble(offset: Long): Double = {
    tryWith(stackPush()) { stack =>
      val buffer = stack.mallocDouble(1)
      readTo(offset, buffer)
      buffer.get(0)
    }
  }

  /**
   * Reads a chunk of bytes starting at offset and with a length of
   * buf.remaining() into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of bytes into.
   */
  override def readTo(offset: Long, buf: ByteBuffer): Unit = {
    readToNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Reads a chunk of shorts starting at offset and with a length of
   * buf.remaining() * the length of a short into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of shorts into.
   */
  override def readTo(offset: Long, buf: ShortBuffer): Unit = {
    readToNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Reads a chunk of ints starting at offset and with a length of
   * buf.remaining() * the length of an int into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of ints into.
   */
  override def readTo(offset: Long, buf: IntBuffer): Unit = {
    readToNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Reads a chunk of longs starting at offset and with a length of
   * buf.remaining() * the length of a long into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of longs into.
   */
  override def readTo(offset: Long, buf: LongBuffer): Unit = {
    readToNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Reads a chunk of floats starting at offset and with a length of
   * buf.remaining() * the length of a float into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of floats into.
   */
  override def readTo(offset: Long, buf: FloatBuffer): Unit = {
    readToNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Reads a chunk of doubles starting at offset and with a length of
   * buf.remaining() * the length of a double into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of doubles into.
   */
  override def readTo(offset: Long, buf: DoubleBuffer): Unit = {
    readToNative(offset, buf.remaining(), memAddress(buf))
  }
}
