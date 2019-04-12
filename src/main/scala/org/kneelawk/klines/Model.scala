package org.kneelawk.klines

import org.joml.Matrix4f
import org.kneelawk.klines.buffers.PartitionSystem

trait Model {
  def createPartitions(system: PartitionSystem)

  def update()

  def updatePartitions(transform: Matrix4f)
}