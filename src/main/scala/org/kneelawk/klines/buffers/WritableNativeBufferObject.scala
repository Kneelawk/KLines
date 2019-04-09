package org.kneelawk.klines.buffers

import java.nio._

import org.kneelawk.klines.util.TryUtil.tryWith
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.memAddress

/**
 * Created by Kneelawk on 4/6/19.
 */
trait WritableNativeBufferObject extends WritableBufferObject {

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + len > getSize.
   * Memory length and address version.
   */
  def setNative(offset: Long, len: Long, address: Long)

  /**
   * Append data to the end of this buffer.
   * Memory length and address version.
   */
  def appendNative(len: Long, address: Long)

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * Memory length and address version.
   */
  def insertNative(offset: Long, len: Long, address: Long)

  /**
   * Replaces the chunk at offset of size chunkLen with the data represented by len and address.
   * Memory length and address version.
   */
  def replaceNative(offset: Long, chunkLen: Long, len: Long, address: Long)

  /**
   * Replaces everything at and after offset with the data represented by len and address.
   * Memory length and address version.
   */
  def replaceAfterNative(offset: Long, len: Long, address: Long)

  /**
   * Replaces everything before cutoff with the data represented by len and address.
   * Memory length and address version.
   */
  def replaceBeforeNative(cutoff: Long, len: Long, address: Long)

  /**
   * Replaces the entirety of the contents of this buffer with the data represented by len and address.
   *
   * @param len     the length of the buffer in bytes.
   * @param address the pointer address of the buffer.
   */
  def replaceAllNative(len: Long, address: Long)


  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + buf.remaining() > getSize.
   * ByteBuffer version.
   */
  override def set(offset: Long, buf: ByteBuffer): Unit = {
    setNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Set a single byte within this buffer.
   *
   * @param offset the offset of the byte to set.
   * @param value  the new value of the byte to set.
   */
  override def set(offset: Long, value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      set(offset, stack.bytes(value))
    }
  }

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + buf.remaining() > getSize.
   * ShortBuffer version.
   */
  override def set(offset: Long, buf: ShortBuffer): Unit = {
    setNative(offset, buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Set a single short within this buffer.
   *
   * @param offset the offset in bytes of the short to set.
   * @param value  the new value of the short to set.
   */
  override def set(offset: Long, value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      set(offset, stack.shorts(value))
    }
  }

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + buf.remaining() > getSize.
   * IntBuffer version.
   */
  override def set(offset: Long, buf: IntBuffer): Unit = {
    setNative(offset, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Set a single int within this buffer.
   *
   * @param offset the offset in bytes of the int to set.
   * @param value  the new value of the int to set.
   */
  override def set(offset: Long, value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      set(offset, stack.ints(value))
    }
  }

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + buf.remaining() > getSize.
   * LongBuffer version.
   */
  override def set(offset: Long, buf: LongBuffer): Unit = {
    setNative(offset, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Set a single long within this buffer.
   *
   * @param offset the offset in bytes of the long to set.
   * @param value  the new value of the long to set.
   */
  override def set(offset: Long, value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      set(offset, stack.longs(value))
    }
  }

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + buf.remaining() > getSize.
   * FloatBuffer version.
   */
  override def set(offset: Long, buf: FloatBuffer): Unit = {
    setNative(offset, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Set a single float within this buffer.
   *
   * @param offset the offset in bytes of the float to set.
   * @param value  the new value of the float to set.
   */
  override def set(offset: Long, value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      set(offset, stack.floats(value))
    }
  }

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + buf.remaining() > getSize.
   * DoubleBuffer version.
   */
  override def set(offset: Long, buf: DoubleBuffer): Unit = {
    setNative(offset, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Set a single double within this buffer.
   *
   * @param offset the offset in bytes of the double to set.
   * @param value  the new value of the double to set.
   */
  override def set(offset: Long, value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      set(offset, stack.doubles(value))
    }
  }

  /**
   * Append data to the end of this buffer.
   * ByteBuffer version.
   */
  override def append(buf: ByteBuffer): Unit = {
    appendNative(buf.remaining(), memAddress(buf))
  }

  /**
   * Append a single byte to the end of this buffer.
   *
   * @param value the value of the byte to append.
   */
  override def append(value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      append(stack.bytes(value))
    }
  }

  /**
   * Append data to the end of this buffer.
   * ShortBuffer version.
   */
  override def append(buf: ShortBuffer): Unit = {
    appendNative(buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Append a single short to the end of this buffer.
   *
   * @param value the value of the short to append.
   */
  override def append(value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      append(stack.shorts(value))
    }
  }

  /**
   * Append data to the end of this buffer.
   * IntBuffer version.
   */
  override def append(buf: IntBuffer): Unit = {
    appendNative(buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Append a single int to the end of this buffer.
   *
   * @param value the value of the int to append.
   */
  override def append(value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      append(stack.ints(value))
    }
  }

  /**
   * Append data to the end of this buffer.
   * LongBuffer version.
   */
  override def append(buf: LongBuffer): Unit = {
    appendNative(buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Append a single long to the end of this buffer.
   *
   * @param value the value of the long to append.
   */
  override def append(value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      append(stack.longs(value))
    }
  }

  /**
   * Append data to the end of this buffer.
   * FloatBuffer version.
   */
  override def append(buf: FloatBuffer): Unit = {
    appendNative(buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Append a single float to the end of this buffer.
   *
   * @param value the value of the float to append.
   */
  override def append(value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      append(stack.floats(value))
    }
  }

  /**
   * Append data to the end of this buffer.
   * DoubleBuffer version.
   */
  override def append(buf: DoubleBuffer): Unit = {
    appendNative(buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Append a single double to the end of this buffer.
   *
   * @param value the value of the double to append.
   */
  override def append(value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      append(stack.doubles(value))
    }
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * ByteBuffer version.
   */
  override def insert(offset: Long, buf: ByteBuffer): Unit = {
    insertNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Inserts a single byte into this buffer at offset, moving the data currently after
   * offset to the end of where this byte is inserted.
   *
   * @param offset the offset of the byte to be inserted.
   * @param value  the value of the byte to be inserted.
   */
  override def insert(offset: Long, value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      insert(offset, stack.bytes(value))
    }
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * ShortBuffer version.
   */
  override def insert(offset: Long, buf: ShortBuffer): Unit = {
    insertNative(offset, buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Inserts a single short into this buffer at offset, moving the data currently after
   * offset to the end of where this short is inserted.
   *
   * @param offset the offset in byte of the short to be inserted.
   * @param value  the value of the short to be inserted.
   */
  override def insert(offset: Long, value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      insert(offset, stack.shorts(value))
    }
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * IntBuffer version.
   */
  override def insert(offset: Long, buf: IntBuffer): Unit = {
    insertNative(offset, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Inserts a single int into this buffer at offset, moving the data currently after
   * offset to the end of where this int is inserted.
   *
   * @param offset the offset in bytes of the int to be inserted.
   * @param value  the value of the int to be inserted.
   */
  override def insert(offset: Long, value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      insert(offset, stack.ints(value))
    }
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * LongBuffer version.
   */
  override def insert(offset: Long, buf: LongBuffer): Unit = {
    insertNative(offset, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Inserts a single long into this buffer at offset, moving the data currently after
   * offset to the end of where this long is inserted.
   *
   * @param offset the offset in bytes of the long to be inserted.
   * @param value  the value of the long to be inserted.
   */
  override def insert(offset: Long, value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      insert(offset, stack.longs(value))
    }
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * FloatBuffer version.
   */
  override def insert(offset: Long, buf: FloatBuffer): Unit = {
    insertNative(offset, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Inserts a single float into this buffer at offset, moving the data currently after
   * offset to the end of where this float is inserted.
   *
   * @param offset the offset in bytes of the float to be inserted.
   * @param value  the value of the float to be inserted.
   */
  override def insert(offset: Long, value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      insert(offset, stack.floats(value))
    }
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * DoubleBuffer version.
   */
  override def insert(offset: Long, buf: DoubleBuffer): Unit = {
    insertNative(offset, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Inserts a single double into this buffer at offset, moving the data currently after
   * offset to the end of where this double is inserted.
   *
   * @param offset the offset in bytes of the double to be inserted.
   * @param value  the value of the double to be inserted.
   */
  override def insert(offset: Long, value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      insert(offset, stack.doubles(value))
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data in buf.
   * ByteBuffer version.
   */
  override def replace(offset: Long, chunkLen: Long, buf: ByteBuffer): Unit = {
    replaceNative(offset, chunkLen, buf.remaining(), memAddress(buf))
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the single byte value.
   *
   * @param offset   the offset in bytes of the chunk to be replaced.
   * @param chunkLen the length in bytes of the chunk to be replaced.
   * @param value    the value of the single byte to replace the chunk with.
   */
  override def replace(offset: Long, chunkLen: Long, value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      replace(offset, chunkLen, stack.bytes(value))
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data in buf.
   * ShortBuffer version.
   */
  override def replace(offset: Long, chunkLen: Long, buf: ShortBuffer): Unit = {
    replaceNative(offset, chunkLen, buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the single short value.
   *
   * @param offset   the offset in bytes of the chunk to be replaced.
   * @param chunkLen the length in bytes of the chunk to be replaced.
   * @param value    the value of the single short to replace the chunk with.
   */
  override def replace(offset: Long, chunkLen: Long, value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      replace(offset, chunkLen, stack.shorts(value))
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data in buf.
   * IntBuffer version.
   */
  override def replace(offset: Long, chunkLen: Long, buf: IntBuffer): Unit = {
    replaceNative(offset, chunkLen, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the single int value.
   *
   * @param offset   the offset in bytes of the chunk to be replaced.
   * @param chunkLen the length in bytes of the chunk to be replaced.
   * @param value    the value of the single int to replace the chunk with.
   */
  override def replace(offset: Long, chunkLen: Long, value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      replace(offset, chunkLen, stack.ints(value))
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data in buf.
   * LongBuffer version.
   */
  override def replace(offset: Long, chunkLen: Long, buf: LongBuffer): Unit = {
    replaceNative(offset, chunkLen, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the single long value.
   *
   * @param offset   the offset in bytes of the chunk to be replaced.
   * @param chunkLen the length in bytes of the chunk to be replaced.
   * @param value    the value of the single long to replace the chunk with.
   */
  override def replace(offset: Long, chunkLen: Long, value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      replace(offset, chunkLen, stack.longs(value))
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data in buf.
   * FloatBuffer version.
   */
  override def replace(offset: Long, chunkLen: Long, buf: FloatBuffer): Unit = {
    replaceNative(offset, chunkLen, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the single float value.
   *
   * @param offset   the offset in bytes of the chunk to be replaced.
   * @param chunkLen the length in bytes of the chunk to be replaced.
   * @param value    the value of the single float to replace the chunk with.
   */
  override def replace(offset: Long, chunkLen: Long, value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      replace(offset, chunkLen, stack.floats(value))
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data in buf.
   * DoubleBuffer version.
   */
  override def replace(offset: Long, chunkLen: Long, buf: DoubleBuffer): Unit = {
    replaceNative(offset, chunkLen, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the single double value.
   *
   * @param offset   the offset in bytes of the chunk to be replaced.
   * @param chunkLen the length in bytes of the chunk to be replaced.
   * @param value    the value of the single double to replace the chunk with.
   */
  override def replace(offset: Long, chunkLen: Long, value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      replace(offset, chunkLen, stack.doubles(value))
    }
  }

  /**
   * Replaces everything at and after offset with the data in buf.
   * ByteBuffer version.
   */
  override def replaceAfter(offset: Long, buf: ByteBuffer): Unit = {
    replaceAfterNative(offset, buf.remaining(), memAddress(buf))
  }

  /**
   * Replaces everything at and after offset with the single byte value.
   *
   * @param offset the offset in bytes of the beginning of the chunk to be replaced.
   * @param value  the single byte to replace the chunk with.
   */
  override def replaceAfter(offset: Long, value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAfter(offset, stack.bytes(value))
    }
  }

  /**
   * Replaces everything at and after offset with the data in buf.
   * ShortBuffer version.
   */
  override def replaceAfter(offset: Long, buf: ShortBuffer): Unit = {
    replaceAfterNative(offset, buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Replaces everything at and after offset with the single short value.
   *
   * @param offset the offset in bytes of the beginning of the chunk to be replaced.
   * @param value  the single short to replace the chunk with.
   */
  override def replaceAfter(offset: Long, value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAfter(offset, stack.shorts(value))
    }
  }

  /**
   * Replaces everything at and after offset with the data in buf.
   * IntBuffer version.
   */
  override def replaceAfter(offset: Long, buf: IntBuffer): Unit = {
    replaceAfterNative(offset, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces everything at and after offset with the single int value.
   *
   * @param offset the offset in bytes of the beginning of the chunk to be replaced.
   * @param value  the single int to replace the chunk with.
   */
  override def replaceAfter(offset: Long, value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAfter(offset, stack.ints(value))
    }
  }

  /**
   * Replaces everything at and after offset with the data in buf.
   * LongBuffer version.
   */
  override def replaceAfter(offset: Long, buf: LongBuffer): Unit = {
    replaceAfterNative(offset, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces everything at and after offset with the single long value.
   *
   * @param offset the offset in bytes of the beginning of the chunk to be replaced.
   * @param value  the single long to replace the chunk with.
   */
  override def replaceAfter(offset: Long, value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAfter(offset, stack.longs(value))
    }
  }

  /**
   * Replaces everything at and after offset with the data in buf.
   * FloatBuffer version.
   */
  override def replaceAfter(offset: Long, buf: FloatBuffer): Unit = {
    replaceAfterNative(offset, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces everything at and after offset with the single float value.
   *
   * @param offset the offset in bytes of the beginning of the chunk to be replaced.
   * @param value  the single float to replace the chunk with.
   */
  override def replaceAfter(offset: Long, value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAfter(offset, stack.floats(value))
    }
  }

  /**
   * Replaces everything at and after offset with the data in buf.
   * DoubleBuffer version.
   */
  override def replaceAfter(offset: Long, buf: DoubleBuffer): Unit = {
    replaceAfterNative(offset, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces everything at and after offset with the single double value.
   *
   * @param offset the offset in bytes of the beginning of the chunk to be replaced.
   * @param value  the single double to replace the chunk with.
   */
  override def replaceAfter(offset: Long, value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAfter(offset, stack.doubles(value))
    }
  }

  /**
   * Replaces everything before cutoff with the data in buf.
   * ByteBuffer version.
   */
  override def replaceBefore(cutoff: Long, buf: ByteBuffer): Unit = {
    replaceBeforeNative(cutoff, buf.remaining(), memAddress(buf))
  }

  /**
   * Replaces everything before cutoff with the single byte value.
   *
   * @param cutoff the point in bytes before which is replaced.
   * @param value  the single byte to replace the chunk with.
   */
  override def replaceBefore(cutoff: Long, value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      replaceBefore(cutoff, stack.bytes(value))
    }
  }

  /**
   * Replaces everything before cutoff with the data in buf.
   * ShortBuffer version.
   */
  override def replaceBefore(cutoff: Long, buf: ShortBuffer): Unit = {
    replaceBeforeNative(cutoff, buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Replaces everything before cutoff with the single short value.
   *
   * @param cutoff the point in bytes before which is replaced.
   * @param value  the single short to replace the chunk with.
   */
  override def replaceBefore(cutoff: Long, value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      replaceBefore(cutoff, stack.shorts(value))
    }
  }

  /**
   * Replaces everything before cutoff with the data in buf.
   * IntBuffer version.
   */
  override def replaceBefore(cutoff: Long, buf: IntBuffer): Unit = {
    replaceBeforeNative(cutoff, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces everything before cutoff with the single int value.
   *
   * @param cutoff the point in bytes before which is replaced.
   * @param value  the single int to replace the chunk with.
   */
  override def replaceBefore(cutoff: Long, value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      replaceBefore(cutoff, stack.ints(value))
    }
  }

  /**
   * Replaces everything before cutoff with the data in buf.
   * LongBuffer version.
   */
  override def replaceBefore(cutoff: Long, buf: LongBuffer): Unit = {
    replaceBeforeNative(cutoff, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces everything before cutoff with the single long value.
   *
   * @param cutoff the point in bytes before which is replaced.
   * @param value  the single long to replace the chunk with.
   */
  override def replaceBefore(cutoff: Long, value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      replaceBefore(cutoff, stack.longs(value))
    }
  }

  /**
   * Replaces everything before cutoff with the data in buf.
   * FloatBuffer version.
   */
  override def replaceBefore(cutoff: Long, buf: FloatBuffer): Unit = {
    replaceBeforeNative(cutoff, buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces everything before cutoff with the single float value.
   *
   * @param cutoff the point in bytes before which is replaced.
   * @param value  the single float to replace the chunk with.
   */
  override def replaceBefore(cutoff: Long, value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      replaceBefore(cutoff, stack.floats(value))
    }
  }

  /**
   * Replaces everything before cutoff with the data in buf.
   * DoubleBuffer version.
   */
  override def replaceBefore(cutoff: Long, buf: DoubleBuffer): Unit = {
    replaceBeforeNative(cutoff, buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces everything before cutoff with the single double value.
   *
   * @param cutoff the point in bytes before which is replaced.
   * @param value  the double byte to replace the chunk with.
   */
  override def replaceBefore(cutoff: Long, value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      replaceBefore(cutoff, stack.doubles(value))
    }
  }

  /**
   * Replaces the entirety of the contents of this buffer with the data in buf.
   *
   * @param buf the buffer to replace all of this buffer's contents with.
   */
  override def replaceAll(buf: ByteBuffer): Unit = {
    replaceAllNative(buf.remaining(), memAddress(buf))
  }

  /**
   * Replaces the entirety of the contents of this buffer with the single byte value.
   *
   * @param value the single byte to replace all of this buffer's contents with.
   */
  override def replaceAll(value: Byte): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAll(stack.bytes(value))
    }
  }

  /**
   * Replaces the entirety of the contents of this buffer with the data in buf.
   *
   * @param buf the buffer to replace all of this buffer's contents with.
   */
  override def replaceAll(buf: ShortBuffer): Unit = {
    replaceAllNative(buf.remaining() << 1, memAddress(buf))
  }

  /**
   * Replaces the entirety of the contents of this buffer with the single short value.
   *
   * @param value the single short to replace all of this buffer's contents with.
   */
  override def replaceAll(value: Short): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAll(stack.shorts(value))
    }
  }

  /**
   * Replaces the entirety of the contents of this buffer with the data in buf.
   *
   * @param buf the buffer to replace all of this buffer's contents with.
   */
  override def replaceAll(buf: IntBuffer): Unit = {
    replaceAllNative(buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces the entirety of the contents of this buffer with the single int value.
   *
   * @param value the single int to replace all of this buffer's contents with.
   */
  override def replaceAll(value: Int): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAll(stack.ints(value))
    }
  }

  /**
   * Replaces the entirety of the contents of this buffer with the data in buf.
   *
   * @param buf the buffer to replace all of this buffer's contents with.
   */
  override def replaceAll(buf: LongBuffer): Unit = {
    replaceAllNative(buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces the entirety of the contents of this buffer with the single long value.
   *
   * @param value the single long to replace all of this buffer's contents with.
   */
  override def replaceAll(value: Long): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAll(stack.longs(value))
    }
  }

  /**
   * Replaces the entirety of the contents of this buffer with the data in buf.
   *
   * @param buf the buffer to replace all of this buffer's contents with.
   */
  override def replaceAll(buf: FloatBuffer): Unit = {
    replaceAllNative(buf.remaining() << 2, memAddress(buf))
  }

  /**
   * Replaces the entirety of the contents of this buffer with the single float value.
   *
   * @param value the single float to replace all of this buffer's contents with.
   */
  override def replaceAll(value: Float): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAll(stack.floats(value))
    }
  }

  /**
   * Replaces the entirety of the contents of this buffer with the data in buf.
   *
   * @param buf the buffer to replace all of this buffer's contents with.
   */
  override def replaceAll(buf: DoubleBuffer): Unit = {
    replaceAllNative(buf.remaining() << 3, memAddress(buf))
  }

  /**
   * Replaces the entirety of the contents of this buffer with the single float value.
   *
   * @param value the single float to replace all of this buffer's contents with.
   */
  override def replaceAll(value: Double): Unit = {
    tryWith(stackPush()) { stack =>
      replaceAll(stack.doubles(value))
    }
  }
}
