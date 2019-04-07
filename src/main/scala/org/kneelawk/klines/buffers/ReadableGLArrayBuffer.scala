package org.kneelawk.klines.buffers

import org.lwjgl.opengl.GL15._

/**
 * This class represents an OpenGL buffer intended for reading data from the GPU to the CPU.
 */
class ReadableGLArrayBuffer(initialSize: Long) extends ReadableNativeBufferObject with GLArrayBufferObject {
  private val bufferName = glGenBuffers()

  /**
   * The current length of this buffer.
   */
  private var size: Long = initialSize

  // bind the buffer to GL_ARRAY_BUFFER
  glBindBuffer(GL_ARRAY_BUFFER, bufferName)
  // initialize space within the buffer
  glBufferData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_READ)

  /**
   * Gets this buffer's OpenGL buffer name.
   *
   * @return this buffer's OpenGL buffer name.
   */
  override def getId: Int = bufferName

  /**
   * Reads a chunk of data starting at offset and with a length of len into the
   * buffer represented by len and address.
   *
   * @param offset  the offset in bytes of the data within this buffer to read.
   * @param len     the length of the data to read.
   * @param address the address of the buffer to write to.
   */
  override def readToNative(offset: Long, len: Long, address: Long): Unit = {
    if (len < 0)
      throw new IllegalArgumentException("The length of the buffer cannot be negative")

    if (offset + len > size)
      throw new ArrayIndexOutOfBoundsException("The requested end of read chunk ("
        + (offset + len) + ") is greater than this buffer's size (" + size + ")")

    // bind the buffer to GL_ARRAY_BUFFER
    glBindBuffer(GL_ARRAY_BUFFER, bufferName)

    // copy the data from our buffer into the specified buffer
    nglGetBufferSubData(GL_ARRAY_BUFFER, offset, len, address)
  }

  /**
   * Gets the current size of the buffer.
   *
   * Changing the size of this buffer WILL destroy its data.
   *
   * @return the current size of the buffer in bytes.
   */
  override def getSize: Long = size

  /**
   * Sets the current size of the buffer.
   *
   * @param size the new size of the buffer in bytes.
   */
  override def setSize(size: Long): Unit = {
    if (size < 0)
      throw new IllegalArgumentException("The size of this buffer cannot be negative")

    // don't bother resizing if it's not needed
    if (size == this.size)
      return

    // bind the buffer to GL_ARRAY_BUFFER
    glBindBuffer(GL_ARRAY_BUFFER, bufferName)
    // initialize space within the buffer
    glBufferData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_READ)

    // set this buffer's size
    this.size = size
  }
}
