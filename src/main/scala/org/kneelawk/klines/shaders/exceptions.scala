package org.kneelawk.klines.shaders

import java.io.IOException

class ShaderException(msg: String) extends IOException(msg)

class FileCompileException(msg: String) extends ShaderException(msg)

class ProgramLinkException(msg: String) extends ShaderException(msg)