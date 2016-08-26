package org.blinkmob

import javax.sql.DataSource

/**
  * hasDataSource provides a data source to CxProvider. Example use:
  * <pre>
  * <code>
  * trait myDSCx extends CxProvider with hasDataSource{
  *    val ds = new HikariDataSource()
  * }
  * </code>
  * </pre>
  */
trait hasDataSource{
  val ds:DataSource
}
