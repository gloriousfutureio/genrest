package io.gloriousfuture.genrpc

import scala.annotation.tailrec
import scala.language.implicitConversions

case class RpcPackage(packagePath: ModulePath, files: Seq[ModelFile])

case class ModelFile(packagePath: ModulePath, imports: Imports, models: Seq[Model])

case class Imports(imports: Set[ModulePath])
object Imports {
  val empty: Imports = Imports(Set.empty)

  implicit def fromSet(set: Set[ModulePath]): Imports = Imports(set)
  implicit def asSet(imports: Imports): Set[ModulePath] = imports.imports
}

case class Field(name: Identifier, typeName: Identifier)

case class Definition(field: Field, value: CodeBlock)

object Model {

  /**
    * Returns all ancestors in linearized order without duplicates.
    */
  def linearizeAncestors(model: Model): Stream[SealedTrait] = {
    @tailrec def linearize(all: Seq[SealedTrait], inOrder: Stream[SealedTrait]): Stream[SealedTrait] = {
      if (all.isEmpty) inOrder.distinct
      else linearize(all.flatMap(_.superclasses), inOrder ++ all.toStream.reverse)
    }
    linearize(model.superclasses, Stream.empty)
  }
}
sealed trait Model {

  def name: Identifier

  def superclasses: Seq[SealedTrait]
}

sealed trait ConcreteModel extends Model {

  def methods: Seq[Definition]
}

case class SealedTrait(name: Identifier, superclasses: Seq[SealedTrait], methods: Seq[Field])
  extends Model

case class CaseObject(name: Identifier, superclasses: Seq[SealedTrait], methods: Seq[Definition])
  extends ConcreteModel

case class CaseClass(name: Identifier, superclasses: Seq[SealedTrait], fields: Seq[Field], methods: Seq[Definition])
  extends ConcreteModel
