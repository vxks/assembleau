def pad(padding: Int): String = (1 to padding).map(_ => " ").mkString

sealed trait DSL:
  self =>
  import DSL.*

  def generateCode(padding: Int): String =
    self match
      case define(label) =>
        pad(padding) + s"${label.name}:\n" + s"${label.instructions.generateCode(padding + 4)}"

      case mov(to, from) =>
        val fromStr = from match
          case i: Int   => s"#$i"
          case l: Label => l.name
        pad(padding) + s"mov ${to.toString}, $fromStr"

      case adr(to, from) =>
        val fromStr = from match
          case i: Int   => i.toString
          case l: Label => l.name
        pad(padding) + s"adr ${to.toString}, $fromStr"

      case svc(syscall) =>
        pad(padding) + s"svc $syscall"

      case AndThen(first, andThen) =>
        s"${first.generateCode(padding)}\n${andThen.generateCode(padding)}"

      case directive: Directive =>
        directive match
          case Directive.global(label) => pad(padding) + s".global ${label.name}"
          case Directive.ascii(ascii)  => pad(padding) + s".ascii \"$ascii\""
          case Directive.align(number) => pad(padding) + s".align $number"

object DSL:
  case class define(label: Label)                 extends DSL
  case class mov(to: Register, from: Int | Label) extends DSL
  case class adr(to: Register, from: Int | Label) extends DSL
  case class svc(syscall: Int)                    extends DSL

  case class AndThen(first: DSL, andThen: DSL) extends DSL

  sealed trait Directive extends DSL
  object Directive:
    case class global(label: Label) extends Directive
    case class ascii(ascii: String) extends Directive
    case class align(number: Int)   extends Directive

  extension (dsl: DSL) def |>(that: DSL): DSL = DSL.AndThen(dsl, that)
