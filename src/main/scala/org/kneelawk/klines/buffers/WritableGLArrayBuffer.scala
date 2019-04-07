package org.kneelawk.klines.buffers

import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL31._

/**
 * This class represents an OpenGL buffer intended for writing data from the CPU to the GPU.
 */
class WritableGLArrayBuffer(initialAllocation: Long) extends WritableNativeBufferObject with GLArrayBufferObject {

  /**
   * Array of both buffers for safe keeping.
   */
  private val bufArray = new Array[Int](2)
  // Create both buffer names
  glGenBuffers(bufArray)

  /**
   * Default buffer in which all the data is kept.
   */
  private val defaultBuf = bufArray(0)

  /**
   * Temporary buffer used for copy operations.
   */
  private val tempBuf = bufArray(1)

  /**
   * Current length of the buffer and offset for new data.
   */
  private var size: Long = 0

  /**
   * Current length of allocated space for the buffer.
   */
  private var maxSize: Long = initialAllocation

  /**
   * Current length of allocated space for the temp buffer.
   */
  private var tmpSize: Long = initialAllocation

  /**
   * Constructs a GLArrayBuffer with an initial allocation of 1024 bytes.
   */
  def this() = this(1024)

  // bind default buffer to GL_ARRAY_BUFFER
  glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
  // initialize the space within the default buffer
  glBufferData(GL_ARRAY_BUFFER, maxSize, GL_DYNAMIC_DRAW)

  // bind the temp buffer to GL_ARRAY_BUFFER
  glBindBuffer(GL_ARRAY_BUFFER, tempBuf)
  // initialize the space within the temp buffer
  glBufferData(GL_ARRAY_BUFFER, tmpSize, GL_DYNAMIC_COPY)

  /**
   * This buffer's id.
   */
  override def getId: Int = defaultBuf

  /**
   * The current length of this buffer.
   */
  override def getSize: Long = size

  /**
   * Sets the current size of the buffer.
   *
   * Changing the size of this buffer will NOT destroy its data.
   *
   * @param size the new size of the buffer in bytes.
   */
  override def setSize(size: Long): Unit = {
    extendToPoint(size)
    this.size = size
  }

  /**
   * Set data within this buffer.
   * This can extend the length of this buffer if offset + len > getSize.
   * Memory length and address version.
   */
  override def setNative(offset: Long, len: Long, address: Long): Unit = {
    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    val chunkEnd = offset + len

    extendToPoint(chunkEnd)
    glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
    nglBufferSubData(GL_ARRAY_BUFFER, offset, len, address)

    if (chunkEnd > size) {
      size = chunkEnd
    }
  }

  /**
   * Append data to the end of this buffer.
   * Memory length and address version.
   */
  override def appendNative(len: Long, address: Long): Unit = {
    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    extendFromEnd(len)
    glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
    nglBufferSubData(GL_ARRAY_BUFFER, size, len, address)

    size += len
  }

  /**
   * Inserts a chunk of data into this buffer at offset, moving the data currently after
   * offset to the end of where this chunk is inserted.
   * Memory length and address version.
   */
  override def insertNative(offset: Long, len: Long, address: Long): Unit = {
    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    if (offset >= size) {
      setNative(offset, len, address)
    } else {
      copyChunk(offset, offset + len, size - offset)
      glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
      nglBufferSubData(GL_ARRAY_BUFFER, offset, len, address)

      size += len
    }
  }

  /**
   * Replaces the chunk at offset of size chunkLen with the data represented by len and data.
   * Memory length and address version.
   */
  override def replaceNative(offset: Long, chunkLen: Long, len: Long, address: Long): Unit = {
    if (chunkLen < 0)
      throw new IllegalArgumentException("The length of the chunk to be replaced cannot be negative")

    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    val lenDif = len - chunkLen

    if (offset + chunkLen < size) {
      if (lenDif != 0) {
        copyChunk(offset + chunkLen, offset + len, size - (offset + chunkLen))
      }

      glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
      nglBufferSubData(GL_ARRAY_BUFFER, offset, len, address)

      size += lenDif
    } else {
      extendToPoint(offset + len)
      glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
      nglBufferSubData(GL_ARRAY_BUFFER, offset, len, address)

      size = offset + len
    }
  }

  /**
   * Replaces everything at and after offset with the data represented by len and data.
   * Memory length and address version.
   */
  override def replaceAfterNative(offset: Long, len: Long, address: Long): Unit = {
    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    if (offset > size)
      throw new IndexOutOfBoundsException(s"Cannot replace a chunk at $offset (beyond size $size)")

    extendToPoint(offset + len)
    glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
    nglBufferSubData(GL_ARRAY_BUFFER, offset, len, address)

    size = offset + len
  }

  /**
   * Replaces everything before cutoff with the data by len and data.
   * Memory length and address version.
   */
  override def replaceBeforeNative(cutoff: Long, len: Long, address: Long): Unit = {
    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    if (cutoff > size)
      throw new IndexOutOfBoundsException(s"Cannot replace everything before $cutoff (beyond size $size)")

    val lenDif = len - cutoff

    if (lenDif != 0) {
      copyChunk(cutoff, len, size - cutoff)
    }

    glBindBuffer(GL_ARRAY_BUFFER, defaultBuf)
    nglBufferSubData(GL_ARRAY_BUFFER, 0, len, address)

    size += lenDif
  }

  /**
   * Removes the chunk at offset of size chunkLen from this buffer.
   */
  override def remove(offset: Long, chunkLen: Long): Unit = {
    if (offset > size)
      throw new IndexOutOfBoundsException(s"Cannot remove a chunk at $offset (beyond size $size)")

    if (offset + chunkLen < size) {
      copyChunk(offset + chunkLen, offset, size - (offset + chunkLen))

      size -= chunkLen
    } else {
      size = offset
    }
  }

  /**
   * Removes everything at and after cutoff from this buffer.
   */
  override def removeAfter(cutoff: Long): Unit = {
    if (cutoff > size)
      throw new IndexOutOfBoundsException(s"Cannot remove chunk at $cutoff (beyond size $size)")

    size = cutoff
  }

  /**
   * Removes everything before cutoff from this buffer.
   */
  override def removeBefore(cutoff: Long): Unit = {
    if (cutoff > size)
      throw new IndexOutOfBoundsException(s"Cannot remove everything before $cutoff (beyond size $size)")

    copyChunk(cutoff, 0, size - cutoff)

    size -= cutoff
  }

  /**
   * Sets the buffers current size to 0, effectively clearing it.
   */
  override def clear(): Unit = {
    size = 0
  }

  /**
   * Deletes the buffers held by this object, invalidating it.
   */
  def destroy(): Unit = {
    glDeleteBuffers(bufArray)
  }

  /**
   * Copies a chunk of data around within the default buffer, resizing if necessary.
   */
  private def copyChunk(sourceOffset: Long, destOffset: Long, chunkLen: Long) {
    // where is the end of the destination chunk?
    val chunkEnd = destOffset + chunkLen

    // bind default buffer to GL_COPY_WRITE_BUFFER
    glBindBuffer(GL_COPY_WRITE_BUFFER, defaultBuf)
    // bind temp buffer to GL_COPY_READ_BUFFER
    glBindBuffer(GL_COPY_READ_BUFFER, tempBuf)

    // resize the temp buffer to be able to hold all the data in the default buffer if needed
    if (tmpSize < size) {

      // keep growing until we're big enough
      while (tmpSize < size)
        tmpSize *= 2

      // allocate new space for the temp buffer
      glBufferData(GL_COPY_READ_BUFFER, size, GL_DYNAMIC_COPY)
    }

    // copy the data from the default buffer to the temp buffer
    glCopyBufferSubData(GL_COPY_WRITE_BUFFER, GL_COPY_READ_BUFFER, 0, 0, size)

    // if a resize is needed then resize and copy all the data back from the temp buffer
    if (maxSize < chunkEnd) {

      // keep growing until we're big enough
      while (maxSize < chunkEnd)
        maxSize *= 2

      // allocate new space for the default buffer
      glBufferData(GL_COPY_WRITE_BUFFER, maxSize, GL_DYNAMIC_DRAW)
      // copy the old data back from the temp buffer
      glCopyBufferSubData(GL_COPY_READ_BUFFER, GL_COPY_WRITE_BUFFER, 0, 0, size)
    }

    // copy the chunk from the source location in the temp buffer to the dest location in the default buffer
    glCopyBufferSubData(GL_COPY_READ_BUFFER, GL_COPY_WRITE_BUFFER, sourceOffset, destOffset, chunkLen)
  }

  /**
   * Doubles the length of allocated space for this buffer.
   */
  private def resize(newSize: Long) {
    // bind default buffer to GL_COPY_WRITE_BUFFER
    glBindBuffer(GL_COPY_WRITE_BUFFER, defaultBuf)
    // bind temp buffer to GL_COPY_READ_BUFFER
    glBindBuffer(GL_COPY_READ_BUFFER, tempBuf)

    // resize the temp buffer to be able to hold all the data in the default buffer if needed
    if (tmpSize < size) {

      // keep growing until we're big enough
      while (tmpSize < size)
        tmpSize *= 2

      // allocate new space for the temp buffer
      glBufferData(GL_COPY_READ_BUFFER, size, GL_DYNAMIC_COPY)
    }

    // copy the data from the default buffer to the temp buffer
    glCopyBufferSubData(GL_COPY_WRITE_BUFFER, GL_COPY_READ_BUFFER, 0, 0, size)

    while (maxSize < newSize)
      maxSize *= 2

    // allocate new space for the default buffer with the new maxSize
    glBufferData(GL_COPY_WRITE_BUFFER, maxSize, GL_DYNAMIC_DRAW)
    // copy the old data back from the temp buffer
    glCopyBufferSubData(GL_COPY_READ_BUFFER, GL_COPY_WRITE_BUFFER, 0, 0, size)
  }

  /**
   * Resizes if needed.
   */
  private def extendFromEnd(extraSize: Long) {
    if (extraSize + size > maxSize) {
      resize(extraSize + size)
    }
  }

  /**
   * Resizes if needed.
   */
  private def extendToPoint(point: Long) {
    if (point > maxSize) {
      resize(point)
    }
  }
}
