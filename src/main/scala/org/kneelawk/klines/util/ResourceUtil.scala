package org.kneelawk.klines.util

import java.io.InputStream

import scala.io.Source

object ResourceUtil {
  def readString(is: InputStream): String = {
    Source.fromInputStream(is).getLines().mkString("\n")
  }
}
