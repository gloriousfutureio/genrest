package io.gloriousfuture.genrpc

trait SpecTransformer {

  def transform(spec: String): RpcPackage
}
