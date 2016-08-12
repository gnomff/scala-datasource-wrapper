package org.blinkmob

import java.sql.Connection
import javax.sql.DataSource
import resource._


trait CxProvider{
  val ds:DataSource

  def cx[A](block: Connection => A): A = managed(ds.getConnection()).acquireAndGet(block(_))
}
