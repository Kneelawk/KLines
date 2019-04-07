package org.kneelawk.klines.buffers

import java.nio._

/**
 * Created by Kneelawk on 4/6/19.
 */
trait ReadableBufferObject extends BufferObject {

  /**
   * Gets a chunk of bytes starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in bytes of the chunk to get.
   *
   * @return a ByteBuffer containing the chunk of data.
   *         This ByteBuffer may or may not need to be free'd in a particular
   *         fashion depending on implementation.
   */
  def getBytes(offset: Long, chunkLen: Int): ByteBuffer

  /**
   * Gets a single byte at offset.
   *
   * @param offset the offset in bytes of the byte to get.
   *
   * @return a single byte representing the data.
   */
  def getByte(offset: Long): Byte

  /**
   * Gets a chunk of shorts starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in shorts of the chunk to get.
   *
   * @return a ShortBuffer containing the chunk of data.
   *         This ShortBuffer may or may not need to be free'd in a particular
   *         fashion depending on implementation.
   */
  def getShorts(offset: Long, chunkLen: Int): ShortBuffer

  /**
   * Gets a single short at offset.
   *
   * @param offset the offset in bytes of the short to get.
   *
   * @return a single short representing the data.
   */
  def getShort(offset: Long): Short

  /**
   * Gets a chunk of ints starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in ints of the chunk to get.
   *
   * @return an IntBuffer containing the chunk of data.
   *         This IntBuffer may or may not need to be free'd in a particular
   *         fashion depending on implementation.
   */
  def getInts(offset: Long, chunkLen: Int): IntBuffer

  /**
   * Gets a single int at offset.
   *
   * @param offset the offset in bytes of the int to get.
   *
   * @return a single int representing the data.
   */
  def getInt(offset: Long): Int

  /**
   * Gets a chunk of longs starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in longs of the chunk to get.
   *
   * @return a LongBuffer containing the chunk of data.
   *         This LongBuffer may or may not need to be free'd in a particular
   *         fashion depending on implementation.
   */
  def getLongs(offset: Long, chunkLen: Int): LongBuffer

  /**
   * Gets a single long at offset.
   *
   * @param offset the offset in bytes of the long to get.
   *
   * @return a single long representing the data.
   */
  def getLong(offset: Long): Long

  /**
   * Gets a chunk of floats starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in floats of the chunk to get.
   *
   * @return a FloatBuffer containing the chunk of data.
   *         This ByteBuffer may or may not need to be free'd in a particular
   *         fashion depending on implementation.
   */
  def getFloats(offset: Long, chunkLen: Int): FloatBuffer

  /**
   * Gets a single float at offset.
   *
   * @param offset the offset in bytes of the float to get.
   *
   * @return a single float representing the data.
   */
  def getFloat(offset: Long): Float

  /**
   * Gets a chunk of doubles starting at offset and with a length of chunkLen.
   *
   * @param offset   the offset in bytes of the chunk to get.
   * @param chunkLen the length in doubles of the chunk to get.
   *
   * @return a DoubleBuffer containing the chunk of data.
   *         This DoubleBuffer may or may not need to be free'd in a particular
   *         fashion depending on implementation.
   */
  def getDoubles(offset: Long, chunkLen: Int): DoubleBuffer

  /**
   * Gets a single double at offset.
   *
   * @param offset the offset in bytes of the double to get.
   *
   * @return a single double representing the data.
   */
  def getDouble(offset: Long): Double

  /**
   * Reads a chunk of bytes starting at offset and with a length of
   * buf.remaining() into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of bytes into.
   */
  def readTo(offset: Long, buf: ByteBuffer)

  /**
   * Reads a chunk of shorts starting at offset and with a length of
   * buf.remaining() * the length of a short into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of shorts into.
   */
  def readTo(offset: Long, buf: ShortBuffer)

  /**
   * Reads a chunk of ints starting at offset and with a length of
   * buf.remaining() * the length of an int into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of ints into.
   */
  def readTo(offset: Long, buf: IntBuffer)

  /**
   * Reads a chunk of longs starting at offset and with a length of
   * buf.remaining() * the length of a long into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of longs into.
   */
  def readTo(offset: Long, buf: LongBuffer)

  /**
   * Reads a chunk of floats starting at offset and with a length of
   * buf.remaining() * the length of a float into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of floats into.
   */
  def readTo(offset: Long, buf: FloatBuffer)

  /**
   * Reads a chunk of doubles starting at offset and with a length of
   * buf.remaining() * the length of a double into the buf.
   *
   * @param offset the offset in bytes of the chunk to get.
   * @param buf    the buffer to read the chunk of doubles into.
   */
  def readTo(offset: Long, buf: DoubleBuffer)
}
