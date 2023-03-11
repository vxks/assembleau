import java.io.{File, FileWriter}
import java.net.URI
import java.nio.file.{Files, Path}

val program: DSL = {
  import DSL.*
  import Directive.*
  import Register.*
  val str        = "Hello World from Scala!\\n"
  val strLen     = str.length - 1
  val helloworld = Label("helloworld", ascii(str))

  val _main = Label(
    "_main",
    mov(X0, 1) |>
      adr(X1, helloworld) |>
      mov(X2, strLen) |>
      mov(X16, 4) |>
      svc(0) |>
      mov(X0, 0) |>
      mov(X16, 4) |>
      svc(0)
  )

  global(_main) |>
    align(2) |>
    define(_main) |>
    define(helloworld)
}

@main def main(): Unit =
  Files.createDirectories(Path.of("out/"))
  val outFile = new File("out/main.asm")
  val fw      = new FileWriter(outFile)
  try
    fw.write(program.generateCode(0))
    fw.flush()
  finally fw.close()
