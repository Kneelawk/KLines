package org.kneelawk.klines.shaders

import java.io.InputStream

import org.kneelawk.klines.util.ResourceUtil
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL20._

class ShaderComponentSource(source: String, name: String, tpe: ShaderType) {
  def this(is: InputStream, name: String, tpe: ShaderType) = {
    this(ResourceUtil.readString(is), name, tpe)
  }

  @throws[FileCompileException]("if there is an error while compiling this shader file")
  def compile(): ShaderComponent = {
    val id = glCreateShader(tpe.getTypeId)

    val res = BufferUtils.createIntBuffer(1)

    glShaderSource(id, source)

    println(s"Compiling shader: $name")
    glCompileShader(id)

    glGetShaderiv(id, GL_COMPILE_STATUS, res)
    val log = glGetShaderInfoLog(id)
    if (log != null && log != "") {
      println(log)
    }
    val status = res.get
    if (status != GL_TRUE) {
      glDeleteShader(id)
      Console.err.println(s"Error compiling shader: $name")
      throw new FileCompileException(s"Error compiling shader: $name, log: $log, status: $status")
    }

    ShaderComponent(id, name, tpe)
  }
}

case class ShaderComponent(id: Int, name: String, tpe: ShaderType) {
  private var deleted = false

  def delete(): Unit = if (!deleted) {
    glDeleteShader(id)
    deleted = true
  }

  override protected def finalize(): Unit = delete()
}
