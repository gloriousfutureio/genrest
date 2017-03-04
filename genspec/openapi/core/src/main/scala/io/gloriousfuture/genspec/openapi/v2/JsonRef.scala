package io.gloriousfuture.genspec.openapi.v2

import scala.util.Try

/**
  * References another value within a JSON document.
  *
  * @see Json Reference https://tools.ietf.org/html/draft-pbryan-zyp-json-ref-02
  * @see Json Pointer https://tools.ietf.org/html/rfc6901
  *
  * @note if the [[domain]] is an empty string, then the [[path]] is referencing the current document.
  *
  * @param path a URI path from the root of the document
  * @param domain the domain that hosts the document
  */
case class JsonRef(path: String, domain: String) {
  def fullPath: String = s"$domain#$path"
}

object JsonRef {

  def parse(fullPath: String): Try[JsonRef] = Try {
    val parts = fullPath.split('#')
    require(parts.length > 1, s"${classOf[JsonRef].getSimpleName} path missing '#' seperator: '$fullPath'")
    val domain = parts(0)
    val uri = parts(1)
    // TODO Validate domain and uri (https://tools.ietf.org/html/rfc6901)
    JsonRef(uri, domain)
  }

  def fromPath(path: String, domain: String = ""): JsonRef = {
    JsonRef(path.split('.').mkString("/"), domain)
  }
}
