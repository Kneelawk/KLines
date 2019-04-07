package org.kneelawk.klines.buffers

import java.nio._

/**
 * Created by Kneelawk on 4/3/19.
 */
trait WritableBufferObject extends BufferObject {
  def set(offset: Long, buf: ByteBuffer)
  def set(offset: Long, value: Byte)
  def set(offset: Long, buf: ShortBuffer)
  def set(offset: Long, value: Short)
  def set(offset: Long, buf: IntBuffer)
  def set(offset: Long, value: Int)
  def set(offset: Long, buf: LongBuffer)
  def set(offset: Long, value: Long)
  def set(offset: Long, buf: FloatBuffer)
  def set(offset: Long, value: Float)
  def set(offset: Long, buf: DoubleBuffer)
  def set(offset: Long, value: Double)

  def append(buf: ByteBuffer)
  def append(value: Byte)
  def append(buf: ShortBuffer)
  def append(value: Short)
  def append(buf: IntBuffer)
  def append(value: Int)
  def append(buf: LongBuffer)
  def append(value: Long)
  def append(buf: FloatBuffer)
  def append(value: Float)
  def append(buf: DoubleBuffer)
  def append(value: Double)

  def insert(offset: Long, buf: ByteBuffer)
  def insert(offset: Long, value: Byte)
  def insert(offset: Long, buf: ShortBuffer)
  def insert(offset: Long, value: Short)
  def insert(offset: Long, buf: IntBuffer)
  def insert(offset: Long, value: Int)
  def insert(offset: Long, buf: LongBuffer)
  def insert(offset: Long, value: Long)
  def insert(offset: Long, buf: FloatBuffer)
  def insert(offset: Long, value: Float)
  def insert(offset: Long, buf: DoubleBuffer)
  def insert(offset: Long, value: Double)

  def replace(offset: Long, chunkLen: Long, buf: ByteBuffer)
  def replace(offset: Long, chunkLen: Long, value: Byte)
  def replace(offset: Long, chunkLen: Long, buf: ShortBuffer)
  def replace(offset: Long, chunkLen: Long, value: Short)
  def replace(offset: Long, chunkLen: Long, buf: IntBuffer)
  def replace(offset: Long, chunkLen: Long, value: Int)
  def replace(offset: Long, chunkLen: Long, buf: LongBuffer)
  def replace(offset: Long, chunkLen: Long, value: Long)
  def replace(offset: Long, chunkLen: Long, buf: FloatBuffer)
  def replace(offset: Long, chunkLen: Long, value: Float)
  def replace(offset: Long, chunkLen: Long, buf: DoubleBuffer)
  def replace(offset: Long, chunkLen: Long, value: Double)

  def replaceAfter(offset: Long, buf: ByteBuffer)
  def replaceAfter(offset: Long, value: Byte)
  def replaceAfter(offset: Long, buf: ShortBuffer)
  def replaceAfter(offset: Long, value: Short)
  def replaceAfter(offset: Long, buf: IntBuffer)
  def replaceAfter(offset: Long, value: Int)
  def replaceAfter(offset: Long, buf: LongBuffer)
  def replaceAfter(offset: Long, value: Long)
  def replaceAfter(offset: Long, buf: FloatBuffer)
  def replaceAfter(offset: Long, value: Float)
  def replaceAfter(offset: Long, buf: DoubleBuffer)
  def replaceAfter(offset: Long, value: Double)

  def replaceBefore(cutoff: Long, buf: ByteBuffer)
  def replaceBefore(cutoff: Long, value: Byte)
  def replaceBefore(cutoff: Long, buf: ShortBuffer)
  def replaceBefore(cutoff: Long, value: Short)
  def replaceBefore(cutoff: Long, buf: IntBuffer)
  def replaceBefore(cutoff: Long, value: Int)
  def replaceBefore(cutoff: Long, buf: LongBuffer)
  def replaceBefore(cutoff: Long, value: Long)
  def replaceBefore(cutoff: Long, buf: FloatBuffer)
  def replaceBefore(cutoff: Long, value: Float)
  def replaceBefore(cutoff: Long, buf: DoubleBuffer)
  def replaceBefore(cutoff: Long, value: Double)

  def remove(offset: Long, chunkLen: Long)

  def removeAfter(offset: Long)

  def removeBefore(cutoff: Long)

  def clear()
}
