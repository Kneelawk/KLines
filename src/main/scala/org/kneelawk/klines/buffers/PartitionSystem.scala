package org.kneelawk.klines.buffers

import java.nio._

import scala.collection.mutable.ListBuffer

/**
 * This is a system designed for managing an object's access to a buffer.
 */
class PartitionSystem(buffer: WritableBufferObject) {

  private val partitions: ListBuffer[PartitionImpl] = new ListBuffer[PartitionImpl]

  def getBuffer: WritableBufferObject = buffer

  def createPartition(): Partition = {
    val part = new PartitionImpl(buffer.getSize, partitions.size)

    partitions += part

    part
  }

  def removePartition(part: Partition): Unit = {
    part.clear()

    partitions.remove(part.getIndex)
  }

  private class PartitionImpl(var offset: Long, var index: Int) extends Partition {
    override def getOffset: Long = offset

    override def getIndex: Int = index

    override def set(offset: Long, buf: ByteBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + buf.remaining())

      buffer.set(this.offset + offset, buf)
    }

    override def set(offset: Long, value: Byte): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + 1)

      buffer.set(this.offset + offset, value)
    }

    override def set(offset: Long, buf: ShortBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + (buf.remaining() << 1))

      buffer.set(this.offset + offset, buf)
    }

    override def set(offset: Long, value: Short): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + 2)

      buffer.set(this.offset + offset, value)
    }

    override def set(offset: Long, buf: IntBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + (buf.remaining() << 2))

      buffer.set(this.offset + offset, buf)
    }

    override def set(offset: Long, value: Int): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + 4)

      buffer.set(this.offset + offset, value)
    }

    override def set(offset: Long, buf: LongBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + (buf.remaining() << 3))

      buffer.set(this.offset + offset, buf)
    }

    override def set(offset: Long, value: Long): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + 8)

      buffer.set(this.offset + offset, value)
    }

    override def set(offset: Long, buf: FloatBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + (buf.remaining() << 2))

      buffer.set(this.offset + offset, buf)
    }

    override def set(offset: Long, value: Float): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + 4)

      buffer.set(this.offset + offset, value)
    }

    override def set(offset: Long, buf: DoubleBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + (buf.remaining() << 3))

      buffer.set(this.offset + offset, buf)
    }

    override def set(offset: Long, value: Double): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot set at a negative offset")

      extendToPoint(offset + 8)

      buffer.set(this.offset + offset, value)
    }

    override def append(buf: ByteBuffer): Unit = {
      buffer.insert(offset + getSize, buf)

      offsetLaterPartitions(buf.remaining())
    }

    override def append(value: Byte): Unit = {
      buffer.insert(offset + getSize, value)

      offsetLaterPartitions(1)
    }

    override def append(buf: ShortBuffer): Unit = {
      buffer.insert(offset + getSize, buf)

      offsetLaterPartitions(buf.remaining() << 1)
    }

    override def append(value: Short): Unit = {
      buffer.insert(offset + getSize, value)

      offsetLaterPartitions(2)
    }

    override def append(buf: IntBuffer): Unit = {
      buffer.insert(offset + getSize, buf)

      offsetLaterPartitions(buf.remaining() << 2)
    }

    override def append(value: Int): Unit = {
      buffer.insert(offset + getSize, value)

      offsetLaterPartitions(4)
    }

    override def append(buf: LongBuffer): Unit = {
      buffer.insert(offset + getSize, buf)

      offsetLaterPartitions(buf.remaining() << 3)
    }

    override def append(value: Long): Unit = {
      buffer.insert(offset + getSize, value)

      offsetLaterPartitions(8)
    }

    override def append(buf: FloatBuffer): Unit = {
      buffer.insert(offset + getSize, buf)

      offsetLaterPartitions(buf.remaining() << 2)
    }

    override def append(value: Float): Unit = {
      buffer.insert(offset + getSize, value)

      offsetLaterPartitions(4)
    }

    override def append(buf: DoubleBuffer): Unit = {
      buffer.insert(offset + getSize, buf)

      offsetLaterPartitions(buf.remaining() << 3)
    }

    override def append(value: Double): Unit = {
      buffer.insert(offset + getSize, value)

      offsetLaterPartitions(8)
    }

    override def appendBlank(len: Long): Unit = {
      if (len < 0)
        throw new IllegalArgumentException("Cannot append a chunk with a negative length")

      buffer.insertBlank(offset + getSize, len)

      offsetLaterPartitions(len)
    }

    override def insert(offset: Long, buf: ByteBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + buf.remaining() - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, buf)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, buf)

        offsetLaterPartitions(buf.remaining())
      }
    }

    override def insert(offset: Long, value: Byte): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + 1 - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, value)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, value)

        offsetLaterPartitions(1)
      }
    }

    override def insert(offset: Long, buf: ShortBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + (buf.remaining() << 1) - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, buf)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, buf)

        offsetLaterPartitions(buf.remaining() << 1)
      }
    }

    override def insert(offset: Long, value: Short): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + 2 - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, value)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, value)

        offsetLaterPartitions(2)
      }
    }

    override def insert(offset: Long, buf: IntBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + (buf.remaining() << 2) - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, buf)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, buf)

        offsetLaterPartitions(buf.remaining() << 2)
      }
    }

    override def insert(offset: Long, value: Int): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + 4 - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, value)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, value)

        offsetLaterPartitions(4)
      }
    }

    override def insert(offset: Long, buf: LongBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + (buf.remaining() << 3) - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, buf)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, buf)

        offsetLaterPartitions(buf.remaining() << 3)
      }
    }

    override def insert(offset: Long, value: Long): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + 8 - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, value)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, value)

        offsetLaterPartitions(8)
      }
    }

    override def insert(offset: Long, buf: FloatBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + (buf.remaining() << 2) - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, buf)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, buf)

        offsetLaterPartitions(buf.remaining() << 2)
      }
    }

    override def insert(offset: Long, value: Float): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + 4 - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, value)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, value)

        offsetLaterPartitions(4)
      }
    }

    override def insert(offset: Long, buf: DoubleBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + (buf.remaining() << 3) - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, buf)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, buf)

        offsetLaterPartitions(buf.remaining() << 3)
      }
    }

    override def insert(offset: Long, value: Double): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + 8 - size

        buffer.insertBlank(this.offset + size, lenDiff)
        buffer.set(this.offset + offset, value)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insert(this.offset + offset, value)

        offsetLaterPartitions(8)
      }
    }

    override def insertBlank(offset: Long, len: Long): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot insert at a negative offset")

      if (len < 0)
        throw new IllegalArgumentException("Cannot insert a chunk with a negative length")

      val size = getSize
      if (offset >= size) {
        val lenDiff = offset + len - size

        buffer.insertBlank(this.offset + size, lenDiff)

        offsetLaterPartitions(lenDiff)
      } else {
        buffer.insertBlank(this.offset + offset, len)

        offsetLaterPartitions(len)
      }
    }

    override def replace(offset: Long, chunkLen: Long, buf: ByteBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val bufLen = buf.remaining()

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, buf)

        offsetLaterPartitions(bufLen - chunkLen)
      } else {
        val sizeDiff = offset + bufLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + bufLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, buf)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, value: Byte): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val valLen = 1

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, value)

        offsetLaterPartitions(valLen - chunkLen)
      } else {
        val sizeDiff = offset + valLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + valLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, value)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, buf: ShortBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val bufLen = buf.remaining() << 1

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, buf)

        offsetLaterPartitions(bufLen - chunkLen)
      } else {
        val sizeDiff = offset + bufLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + bufLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, buf)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, value: Short): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val valLen = 2

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, value)

        offsetLaterPartitions(valLen - chunkLen)
      } else {
        val sizeDiff = offset + valLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + valLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, value)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, buf: IntBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val bufLen = buf.remaining() << 2

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, buf)

        offsetLaterPartitions(bufLen - chunkLen)
      } else {
        val sizeDiff = offset + bufLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + bufLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, buf)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, value: Int): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val valLen = 4

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, value)

        offsetLaterPartitions(valLen - chunkLen)
      } else {
        val sizeDiff = offset + valLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + valLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, value)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, buf: LongBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val bufLen = buf.remaining() << 3

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, buf)

        offsetLaterPartitions(bufLen - chunkLen)
      } else {
        val sizeDiff = offset + bufLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + bufLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, buf)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, value: Long): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val valLen = 8

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, value)

        offsetLaterPartitions(valLen - chunkLen)
      } else {
        val sizeDiff = offset + valLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + valLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, value)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, buf: FloatBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val bufLen = buf.remaining() << 2

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, buf)

        offsetLaterPartitions(bufLen - chunkLen)
      } else {
        val sizeDiff = offset + bufLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + bufLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, buf)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, value: Float): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val valLen = 4

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, value)

        offsetLaterPartitions(valLen - chunkLen)
      } else {
        val sizeDiff = offset + valLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + valLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, value)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, buf: DoubleBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val bufLen = buf.remaining() << 3

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, buf)

        offsetLaterPartitions(bufLen - chunkLen)
      } else {
        val sizeDiff = offset + bufLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + bufLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, buf)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replace(offset: Long, chunkLen: Long, value: Double): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot replace a chunk with a negative length")

      val size = getSize
      val valLen = 8

      if (offset + chunkLen <= size) {
        buffer.replace(this.offset + offset, chunkLen, value)

        offsetLaterPartitions(valLen - chunkLen)
      } else {
        val sizeDiff = offset + valLen - size
        if (sizeDiff > 0) {
          buffer.insertBlank(this.offset + size, sizeDiff)
        } else if (sizeDiff < 0) {
          buffer.remove(this.offset + offset + valLen, -sizeDiff)
        }

        buffer.set(this.offset + offset, value)

        if (sizeDiff != 0) {
          offsetLaterPartitions(sizeDiff)
        }
      }
    }

    override def replaceAfter(offset: Long, buf: ByteBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val bufLen = buf.remaining()
      val sizeDiff = offset + bufLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + bufLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, buf)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, value: Byte): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val valLen = 1
      val sizeDiff = offset + valLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + valLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, value)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, buf: ShortBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val bufLen = buf.remaining() << 1
      val sizeDiff = offset + bufLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + bufLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, buf)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, value: Short): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val valLen = 2
      val sizeDiff = offset + valLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + valLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, value)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, buf: IntBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val bufLen = buf.remaining() << 2
      val sizeDiff = offset + bufLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + bufLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, buf)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, value: Int): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val valLen = 4
      val sizeDiff = offset + valLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + valLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, value)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, buf: LongBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val bufLen = buf.remaining() << 3
      val sizeDiff = offset + bufLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + bufLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, buf)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, value: Long): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val valLen = 8
      val sizeDiff = offset + valLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + valLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, value)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, buf: FloatBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val bufLen = buf.remaining() << 2
      val sizeDiff = offset + bufLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + bufLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, buf)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, value: Float): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val valLen = 4
      val sizeDiff = offset + valLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + valLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, value)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, buf: DoubleBuffer): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val bufLen = buf.remaining() << 3
      val sizeDiff = offset + bufLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + bufLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, buf)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceAfter(offset: Long, value: Double): Unit = {
      if (offset < 0)
        throw new IllegalArgumentException("Cannot replace at a negative offset")

      val size = getSize
      val valLen = 8
      val sizeDiff = offset + valLen - size

      if (sizeDiff > 0) {
        buffer.insertBlank(this.offset + size, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(this.offset + offset + valLen, -sizeDiff)
      }

      buffer.set(this.offset + offset, value)

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    override def replaceBefore(cutoff: Long, buf: ByteBuffer): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, buf)

      offsetLaterPartitions(buf.remaining() - chunkLen)
    }

    override def replaceBefore(cutoff: Long, value: Byte): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, value)

      offsetLaterPartitions(1 - chunkLen)
    }

    override def replaceBefore(cutoff: Long, buf: ShortBuffer): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, buf)

      offsetLaterPartitions((buf.remaining() << 1) - chunkLen)
    }

    override def replaceBefore(cutoff: Long, value: Short): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, value)

      offsetLaterPartitions(2 - chunkLen)
    }

    override def replaceBefore(cutoff: Long, buf: IntBuffer): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, buf)

      offsetLaterPartitions((buf.remaining() << 2) - chunkLen)
    }

    override def replaceBefore(cutoff: Long, value: Int): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, value)

      offsetLaterPartitions(4 - chunkLen)
    }

    override def replaceBefore(cutoff: Long, buf: LongBuffer): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, buf)

      offsetLaterPartitions((buf.remaining() << 3) - chunkLen)
    }

    override def replaceBefore(cutoff: Long, value: Long): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, value)

      offsetLaterPartitions(8 - chunkLen)
    }

    override def replaceBefore(cutoff: Long, buf: FloatBuffer): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, buf)

      offsetLaterPartitions((buf.remaining() << 2) - chunkLen)
    }

    override def replaceBefore(cutoff: Long, value: Float): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, value)

      offsetLaterPartitions(4 - chunkLen)
    }

    override def replaceBefore(cutoff: Long, buf: DoubleBuffer): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, buf)

      offsetLaterPartitions((buf.remaining() << 3) - chunkLen)
    }

    override def replaceBefore(cutoff: Long, value: Double): Unit = {
      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot replace before a negative cutoff")

      val size = getSize
      val chunkLen = if (cutoff < size) cutoff else size

      buffer.replace(offset, chunkLen, value)

      offsetLaterPartitions(8 - chunkLen)
    }

    override def replaceAll(buf: ByteBuffer): Unit = {
      val size = getSize

      buffer.replace(offset, size, buf)

      offsetLaterPartitions(buf.remaining() - size)
    }

    override def replaceAll(value: Byte): Unit = {
      val size = getSize

      buffer.replace(offset, size, value)

      offsetLaterPartitions(1 - size)
    }

    override def replaceAll(buf: ShortBuffer): Unit = {
      val size = getSize

      buffer.replace(offset, size, buf)

      offsetLaterPartitions((buf.remaining() << 1) - size)
    }

    override def replaceAll(value: Short): Unit = {
      val size = getSize

      buffer.replace(offset, size, value)

      offsetLaterPartitions(2 - size)
    }

    override def replaceAll(buf: IntBuffer): Unit = {
      val size = getSize

      buffer.replace(offset, size, buf)

      offsetLaterPartitions((buf.remaining() << 2) - size)
    }

    override def replaceAll(value: Int): Unit = {
      val size = getSize

      buffer.replace(offset, size, value)

      offsetLaterPartitions(4 - size)
    }

    override def replaceAll(buf: LongBuffer): Unit = {
      val size = getSize

      buffer.replace(offset, size, buf)

      offsetLaterPartitions((buf.remaining() << 3) - size)
    }

    override def replaceAll(value: Long): Unit = {
      val size = getSize

      buffer.replace(offset, size, value)

      offsetLaterPartitions(8 - size)
    }

    override def replaceAll(buf: FloatBuffer): Unit = {
      val size = getSize

      buffer.replace(offset, size, buf)

      offsetLaterPartitions((buf.remaining() << 2) - size)
    }

    override def replaceAll(value: Float): Unit = {
      val size = getSize

      buffer.replace(offset, size, value)

      offsetLaterPartitions(4 - size)
    }

    override def replaceAll(buf: DoubleBuffer): Unit = {
      val size = getSize

      buffer.replace(offset, size, buf)

      offsetLaterPartitions((buf.remaining() << 3) - size)
    }

    override def replaceAll(value: Double): Unit = {
      val size = getSize

      buffer.replace(offset, size, value)

      offsetLaterPartitions(8 - size)
    }

    override def remove(offset: Long, chunkLen: Long): Unit = {
      val size = getSize

      if (offset < 0)
        throw new IllegalArgumentException("Cannot remove a chunk before the start of this buffer")

      if (offset > size)
        throw new IndexOutOfBoundsException(s"Cannot remove chunk at $offset (beyond size $size)")

      if (chunkLen < 0)
        throw new IllegalArgumentException("Cannot remove a chunk with a negative size")

      if (offset + chunkLen < size) {
        buffer.remove(this.offset + offset, chunkLen)

        offsetLaterPartitions(-chunkLen)
      } else {
        val toBeRemoved = size - offset

        buffer.remove(this.offset + offset, toBeRemoved)

        offsetLaterPartitions(-toBeRemoved)
      }
    }

    override def removeAfter(offset: Long): Unit = {
      val size = getSize

      if (offset < 0)
        throw new IllegalArgumentException("Cannot remove a chunk before the start of this buffer")

      if (offset > size)
        throw new IndexOutOfBoundsException(s"Cannot remove chunk at $offset (beyond size $size")

      val toBeRemoved = size - offset
      if (toBeRemoved > 0) {
        buffer.remove(this.offset + offset, toBeRemoved)

        offsetLaterPartitions(-toBeRemoved)
      }
    }

    override def removeBefore(cutoff: Long): Unit = {
      val size = getSize

      if (cutoff < 0)
        throw new IllegalArgumentException("Cannot remove a chunk before the start of this buffer")

      val toBeRemoved = if (size < cutoff) size else cutoff

      buffer.remove(offset, toBeRemoved)

      offsetLaterPartitions(-toBeRemoved)
    }

    override def clear(): Unit = {
      val size = getSize
      buffer.remove(offset, size)

      offsetLaterPartitions(-size)
    }

    /**
     * Gets the current size of the buffer.
     *
     * @return the current size of the buffer in bytes.
     */
    override def getSize: Long = {
      if (partitions.size > index + 1) {
        partitions(index + 1).offset - offset
      } else {
        buffer.getSize - offset
      }
    }

    /**
     * Sets the current size of the buffer.
     *
     * Changing the size of a buffer may or may not destroy the data within
     * the buffer depending on implementation.
     *
     * @param size the new size of the buffer in bytes.
     */
    override def setSize(size: Long): Unit = {
      if (size < 0)
        throw new IllegalArgumentException("Cannot set the size to be negative")

      val currentSize = getSize
      val sizeDiff = size - currentSize
      if (sizeDiff > 0) {
        buffer.insertBlank(offset + currentSize, sizeDiff)
      } else if (sizeDiff < 0) {
        buffer.remove(offset + size, -sizeDiff)
      }

      if (sizeDiff != 0) {
        offsetLaterPartitions(sizeDiff)
      }
    }

    private def extendToPoint(point: Long): Unit = {
      val size = getSize

      if (point > size) {
        buffer.insertBlank(offset + size, point - size)

        offsetLaterPartitions(point - size)
      }
    }

    private def offsetLaterPartitions(offset: Long): Unit = {
      partitions.slice(index + 1, partitions.size).foreach(_.offset += offset)
    }
  }

}

/**
 * This serves as the object's interface to the underlying buffer.
 */
trait Partition extends WritableBufferObject {
  def getOffset: Long

  def getIndex: Int
}
