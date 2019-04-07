package org.kneelawk.klines.buffers

/**
 * Created by Kneelawk on 4/6/19.
 */
trait GLArrayBufferObject {
  /**
   * Gets this buffer's OpenGL buffer name.
   *
   * @return this buffer's OpenGL buffer name.
   */
  def getId: Int

  /**
   * Gets this buffer's size.
   * @return this buffer's size in bytes.
   */
  def getSize: Long
}
