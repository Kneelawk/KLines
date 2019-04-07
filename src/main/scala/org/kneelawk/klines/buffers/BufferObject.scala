package org.kneelawk.klines.buffers

/**
 * Created by Kneelawk on 4/6/19.
 */
trait BufferObject {
  /**
   * Gets the current size of the buffer.
   *
   * @return the current size of the buffer in bytes.
   */
  def getSize: Long

  /**
   * Sets the current size of the buffer.
   *
   * Changing the size of a buffer may or may not destroy the data within
   * the buffer depending on implementation.
   *
   * @param size the new size of the buffer in bytes.
   */
  def setSize(size: Long)
}
